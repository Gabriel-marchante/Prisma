import multer from 'multer';
import ffmpeg from 'ffmpeg-static';
import { spawn } from 'child_process';
import path from 'path';
import { publicPath } from '../../index.js ';
import connect from '../../connectionDB.js';

let db = null;

export async function initDb() {
    if (!db) {
        db = await connect();
        console.log("DB pool initialized");
    }
    return db;
}

export const getVideos = async (req, res) => {
    try {
        if (!db) {
            throw new Error("Database pool is null or undefined — connection never initialized?");
        }

        const [rows] = await db.query('SELECT * FROM video');
        res.status(200).json(rows);
    } catch (err) {
        res.status(500).json({
            error: "Failed to fetch videos",
            details: err.message || "Unknown database error",
            code: err.code || "UNKNOWN"
        });
    }
};
export const getVideoById = async (req, res) => {
    try {
        if (!db) {
            throw new Error("DB not initialized");
        }

        const { id } = req.params;
        const userId = req.user.user_id; // para la peticion autenticada

        if (!id) {
            return res.status(400).json({ error: "Falta id del video" });
        }

        console.log(`Usuario ${userId} pide el vídeo ${id}`);

        const [rows] = await db.query(
            'SELECT url FROM video WHERE id = ?',
            [id]
        );

        if (rows.length === 0) {
            return res.status(404).json({ error: "Video no encontrado" });
        }
        const playlistPath = path.join(process.cwd(), rows[0].url);

            if (!fs.existsSync(playlistPath)) {
                return res.status(404).json({ error: "Archivo no encontrado" });
            }
            res.sendFile(playlistPath);
    } catch (err) {
        res.status(500).json({
            error: "Failed to fetch video",
            details: err.message
        });
    }
};

const memoryUpload = multer({
    storage: multer.memoryStorage(),
    limits: { fileSize: 500 * 1024 * 1024 },
});

export const uploadVideo = memoryUpload;

export const processVideo = (req, res) => {
    const { clientId, videoId } = req;
    const WS = req.app.get('uploadScreenSocket');

    try {
        WS.sendProgress(clientId, 70, 'Starting video processing...');

        // Comprovar si s'esta pujant un fitxer
        if (!req.file || !req.file.buffer) {
            return res.status(400).json({ error: 'No se recibió ningún vídeo o está vacío' });
        }

        const videoId = req.videoId;
        const outputPlaylist = path.join(req.outputDir, 'index.m3u8');



        const args = [
            '-i', 'pipe:0',
            '-profile:v', 'baseline',
            '-level', '3.0',
            '-start_number', '0',
            '-hls_time', '10',
            '-hls_list_size', '0',
            '-f', 'hls',
            outputPlaylist
        ];

        console.log('Executant FFmpeg:', ffmpeg, args.join(' '));

        const ffmpegProcess = spawn(ffmpeg || 'ffmpeg', args);

        ffmpegProcess.stderr.on('data', (data) => {
            console.log(`FFmpeg: ${data}`);
        });

        setImmediate(() => {
            ffmpegProcess.stdin.write(req.file.buffer);
            ffmpegProcess.stdin.end();
        });

        ffmpegProcess.on('close', (code) => {
            if (code === 0) {

                WS.sendProgress(clientId, 100, 'Video processing completed');
                req.processedVideoPath = `${videoId}/index.m3u8`;
                WS.completeProcessing(clientId, {
                    videoId,
                    videoUrl: req.processedVideoPath,
                    thumbnailUrl: req.thumbnailPath,
                    metadata: {
                        duration: req.videoDuration,
                        codec: req.videoCodec,
                        resolution: req.videoResolution,
                        size: req.file.size
                    }
                });

                // Send HTTP response
                res.json({
                    success: true,
                    message: 'Video processed successfully',
                    data: {
                        videoId,
                        videoUrl: req.processedVideoPath,
                        thumbnailUrl: req.thumbnailPath
                    }
                });
            } else {
                console.error(`Error: ${code}`);
                res.status(500).json({ error: 'Error al processar el video' });
            }
        });

        ffmpegProcess.on('error', (err) => {
            console.error('Error al iniciar FFmpeg:', err);
            res.status(500).json({ error: 'Error al iniciar FFmpeg', details: err.message });
        });


        setTimeout(() => {
            if (ffmpegProcess.pid && !ffmpegProcess.killed) {
                console.error('Timeout: kill FFmpeg per timeout');
                ffmpegProcess.kill('SIGKILL');
            }
        }, 60000);

        // ffmpeg -i /home/disnaking/Downloads/videoplayback.mp4 -profile:v baseline -level 3.0 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls ./public/index.m3u8       
    } catch (error) {
        WS.sendError(clientId, 'Video processing failed: ' + error.message);
        return res.status(500).json({ error: error.message });
    }
}