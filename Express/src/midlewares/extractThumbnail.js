import { publicPath } from '../../index.js';
import fs from 'fs/promises';
import path from 'path';
import { spawn } from 'child_process';

export async function extractThumbnail(req, res, next) {
    const { clientId, videoId } = req;
    const WS = req.app.get('uploadScreenSocket');
    try {
        WS.sendProgress(clientId, 40, 'Starting thumbnail generation...');
        if (!req.file || !req.file.buffer) {
            return res.status(400).json({ error: 'No file uploaded or buffer missing' });
        }

        const videoId = req.videoId;
        if (!videoId) {
            throw new Error('videoId missing on request');
        }

        const outputDir = path.join(publicPath, videoId);
        await fs.mkdir(outputDir, { recursive: true });

        const thumbnailPath = path.join(outputDir, 'thumbnail.jpg');

        const file = req.file.buffer;
        const name = req.file.originalname || 'video';
        const type = req.file.mimetype || 'video/mp4'        // fallback mimetype

        // ── Generate thumbnail directly from buffer ─────────────────────────────
        const thumbnailBuffer = await generateThumbnailFromBuffer(file, name, type);
        WS.sendProgress(clientId, 70, 'Thumbnail generated successfully', {
            thumbnailUrl: `/public/${videoId}_thumbnail.png`
        });
        // Write the resulting JPEG buffer to disk (this is the only file write)
        await fs.writeFile(thumbnailPath, thumbnailBuffer);

        req.outputDir = outputDir;

        next();
    } catch (error) {
        WS.sendError(clientId, 'Thumbnail generation failed: ' + error.message);
        return res.status(500).json({ error: error.message });
    }
}

/**
 * Generates a single JPEG thumbnail from video buffer in memory.
 * @param {Buffer} videoBuffer - The raw video data from Multer memory storage
 * @returns {Promise<Buffer>} - The thumbnail JPEG as Buffer
 */
async function generateThumbnailFromBuffer(videoBuffer, originalName, mimeType) {
    return new Promise((resolve, reject) => {
        let inputFormat = 'mp4';
        const ext = path.extname(originalName || '').toLowerCase();
        if (ext === '.webm') inputFormat = 'webm';
        else if (ext === '.mov' || ext === '.qt') inputFormat = 'mov';
        else if (ext === '.avi') inputFormat = 'avi';
        if (mimeType.includes('webm')) inputFormat = 'webm';
        if (mimeType.includes('quicktime')) inputFormat = 'mov';

        const ffmpegArgs = [
            '-ss',         '3',                    // seek early
            '-f',          inputFormat,
            '-i',          'pipe:0',
            '-frames:v',   '1',
            '-vf',         'scale=640:360:force_original_aspect_ratio=decrease,pad=640:360:(ow-iw)/2:(oh-ih)/2',
            '-q:v',        '2',
            '-f',          'image2',
            '-vcodec',     'mjpeg',
            '-update',     '1',
            'pipe:1'
        ];

        console.log('Running ffmpeg with args:', ffmpegArgs.join(' '));

        const ffmpegProcess = spawn('ffmpeg', ffmpegArgs);

        let thumbnailChunks = [];
        let stderrLines = [];

        ffmpegProcess.stdout.on('data', (chunk) => {
            thumbnailChunks.push(chunk);
        });

        ffmpegProcess.stderr.on('data', (chunk) => {
            const line = chunk.toString().trim();
            if (line) stderrLines.push(line);
            console.log(`ffmpeg: ${line}`);
        });

        ffmpegProcess.on('error', (err) => {
            reject(new Error(`Failed to spawn ffmpeg: ${err.message}`));
        });

        ffmpegProcess.on('close', (code, signal) => {
            const stderr = stderrLines.join('\n');
            const thumbnail = Buffer.concat(thumbnailChunks);

            if (thumbnail.length >= 5000) {  // reasonable minimum for a 640x360 jpeg
                console.log(`Thumbnail success: ${thumbnail.length} bytes`);
                resolve(thumbnail);
            } else if (code === 0 && thumbnail.length > 0) {
                console.log(`Small thumbnail but code 0: ${thumbnail.length} bytes`);
                resolve(thumbnail);
            } else {
                reject(new Error(
                    `ffmpeg closed with code ${code || 'unknown'} (signal ${signal || 'none'})\n` +
                    `Thumbnail size: ${thumbnail.length} bytes\n` +
                    `Stderr:\n${stderr || '(no stderr)'}\n`
                ));
            }
        });

        // ── Critical: Ignore EPIPE on stdin ────────────────────────────────
        // This is the main fix — we treat EPIPE as non-fatal when we have output
        ffmpegProcess.stdin.on('error', (err) => {
            if (err.code === 'EPIPE') {
                console.log('EPIPE on stdin ignored (ffmpeg finished reading early)');
                // Do NOT reject here — we wait for 'close' to check if we have thumbnail
            } else {
                console.error('Unexpected stdin error:', err);
                reject(err);
            }
        });

        // Write the buffer (ffmpeg will stop reading early — that's OK)
        ffmpegProcess.stdin.write(videoBuffer);
        ffmpegProcess.stdin.end();
    });
}