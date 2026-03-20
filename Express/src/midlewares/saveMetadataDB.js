import connect from "../../connectionDB.js";

const db = await connect();

export const saveDB = async (req, res, next) => {
    const { clientId, videoId, videoDuration, videoCodec, videoResolution, videoName } = req;
    const WS = req.app.get('uploadScreenSocket');
    try {
        WS.sendProgress(clientId, 25, 'Saving video information to database...');

        const size = Math.round(req.file.size / 1024)

        const result = db.query(
            'INSERT INTO video (id, titol, duracio, codec, resolucio, pes) VALUES(?, ?, ?, ?, ?, ?)',
            [videoId, videoName, videoDuration, videoCodec, videoResolution, size]
        )
        console.log(result);
        WS.sendProgress(clientId, 40, 'Video information saved successfully', {
            videoId,
            saved: true
        });
        next();
    } catch (error) {
        WS.sendError(clientId, 'Database save failed: ' + error.message);
        return res.status(500).json({ error: error.message });
    }

}