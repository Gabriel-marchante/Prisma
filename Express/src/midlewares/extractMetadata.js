import { spawn } from 'child_process';
import { UploadScreenSocket } from '../websockets/uploadScreenSocket.js';

const FFPROBE = 'ffprobe'; // or static path if needed
2
export async function metadata(req, res, next) {
    // Get clientId from request body or headers
    const clientId = req.headers['x-client-id'];
    
    if (!clientId) {
        return res.status(400).json({
            error: 'clientId is required in body or X-Client-Id header'
        });
    }

    // Get the WebSocket instance (should be initialized in server.js)
    const WS = req.app.get('uploadScreenSocket');
    
    if (!WS || !WS.isClientConnected(clientId)) {
        return res.status(400).json({
            error: 'Client not connected to WebSocket'
        });
    }

    if (!req.file || !req.file.buffer) {
        return res.status(400).json({
            error: 'No video file received. Send multipart/form-data with field "video".'
        });
    }

    console.log('File received → size:', req.file.size, 'bytes');

    try {
        // Generate video ID
        const videoId = Date.now().toString();
        
        // Start processing workflow
        WS.startProcessing(clientId, videoId, req.file.originalname);
        WS.sendProgress(clientId, 0, 'Starting metadata extraction...');

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Extract Duration (0% -> 7%)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        WS.sendProgress(clientId, 2, 'Extracting video duration...');

        const durationArgs = [
            '-v', 'error',
            '-show_entries', 'format=duration',
            '-of', 'default=noprint_wrappers=1:nokey=1',
            '-'
        ];

        const durationStr = await runFfprobe(req.file.buffer, durationArgs);
        const duration = Number(durationStr);

        if (isNaN(duration) || duration <= 0) {
            WS.sendError(clientId, `Invalid duration: "${durationStr}"`);
            throw new Error(`Invalid duration: "${durationStr}"`);
        }

        WS.sendProgress(clientId, 7, 'Duration extracted', { duration });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Extract Codec (7% -> 14%)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        WS.sendProgress(clientId, 8, 'Detecting video codec...');

        const codecArgs = [
            '-v', 'error',
            '-select_streams', 'v:0',
            '-show_entries', 'stream=codec_name',
            '-of', 'default=noprint_wrappers=1:nokey=1',
            '-'
        ];

        const codec = await runFfprobe(req.file.buffer, codecArgs);

        if (!codec || codec.trim() === '') {
            WS.sendError(clientId, 'Could not detect video codec');
            throw new Error('Could not detect video codec');
        }

        WS.sendProgress(clientId, 14, 'Codec detected', { codec: codec.trim() });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Extract Resolution (14% -> 20%)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        WS.sendProgress(clientId, 15, 'Detecting video resolution...');

        const resolutionArgs = [
            '-v', 'error',
            '-select_streams', 'v:0',
            '-show_entries', 'stream=width,height',
            '-of', 'csv=p=0:s=x',
            '-'
        ];

        const resolution = await runFfprobe(req.file.buffer, resolutionArgs);

        if (!resolution || !resolution.includes('x')) {
            WS.sendError(clientId, 'Could not detect video resolution');
            throw new Error('Could not detect video resolution');
        }

        WS.sendProgress(clientId, 20, 'Metadata extraction completed', {
            duration,
            codec: codec.trim(),
            resolution: resolution.trim(),
            size: req.file.size,
            filename: req.file.originalname
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Attach metadata to request for next middleware
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        req.clientId = clientId;
        req.videoId = videoId;
        req.videoDuration = duration;
        req.videoCodec = codec.trim();
        req.videoResolution = resolution.trim();
        req.videoName = req.file.originalname.split('.').slice(0, -1).join('.');
        // req.file.size -> size available
        // req.file.originalname -> original filename

        WS.sendMessage(clientId, {
            clientId: clientId,
            type: 'metadata',
            videoId: videoId,
            duration: duration,
            codec: req.videoCodec,
            resolution: req.videoResolution,
            size: req.file.size,
            filename: req.videoName
        });

        console.log(`Extracted: duration=${duration}s, codec=${req.videoCodec}, size=${req.file.size}, name=${req.videoName}, resolution=${req.videoResolution}`);

        next();

    } catch (err) {
        console.error('Metadata extraction failed:', err.message);
        WS.sendError(clientId, 'Metadata extraction failed: ' + err.message);
        return res.status(500).json({
            error: 'Failed to extract video metadata',
            details: err.message
        });
    }
}

async function runFfprobe(buffer, args) {
    return new Promise((resolve, reject) => {
        const child = spawn(FFPROBE, args, { encoding: 'utf8' });

        let output = '';
        let errorOutput = '';

        child.stdout.on('data', (data) => { output += data; });
        child.stderr.on('data', (data) => { errorOutput += data; });
        child.stdin.on('error', (err) => {
            if (err.code === 'EPIPE' || err.code === 'ECONNRESET') {
                return;
            }
            reject(err);
        });

        child.on('close', (code) => {
            if (code === 0) {
                resolve(output.trim());
            } else {
                reject(new Error(`ffprobe failed (code ${code}): ${errorOutput.trim() || 'no output'}`));
            }
        });

        child.on('error', reject);

        child.stdin.write(buffer);
        child.stdin.end();
    });
}