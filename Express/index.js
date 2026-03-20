import express from 'express';
import http from 'http';
import path from 'path';
import verifyToken from './src/midlewares/verifyToken.js'; // note: typo? "midlewares" → "middlewares"?
import { fileURLToPath } from 'url';
import cors from 'cors';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const port = 3000;

// CORS (looks good, but consider allowing your Flutter app origin dynamically in production)
app.use(cors({
  origin: ['http://localhost:1420'], // add your production Flutter web/mobile origins if needed
  methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'X-Client-Id'],
  credentials: false,
}));

// JSON parsing
app.use(express.json());

// WebSocket setup
import { UploadScreenSocket } from "./src/websockets/uploadScreenSocket.js";
const server = http.createServer(app);
const uploadScreenSocket = new UploadScreenSocket();
uploadScreenSocket.initialize(server);
app.set('uploadScreenSocket', uploadScreenSocket);

// Init DB (good that it's awaited)
import { initDb } from './src/controllers/VideoController.js';
await initDb();

// Define public path
export const publicPath = path.join(__dirname, 'public');
console.log('Public path:', publicPath);

// IMPORTANT: Protect /static BEFORE serving static files
app.use('/static', (req, res, next) => {
  // Skip auth for images/thumbnails if you want them public
  if (req.path.match(/\.(jpg|jpeg|png|gif|webp)$/i)) {
    return next();
  }

  // For .m3u8 playlists and .ts segments → require JWT
  verifyToken(req, res, next);
});

// NOW serve the static files (after auth check)
app.use('/static', express.static(publicPath));

// Your API routes (should come last)
import Routes from './src/routes/routes.js';
app.use(Routes);

// Start server
server.listen(port, '0.0.0.0', () => {
  console.log(`Server running on http://localhost:${port}`); // fixed misleading HTTPS
  console.log(`Static files (HLS) available at http://localhost:${port}/static/...`);
});