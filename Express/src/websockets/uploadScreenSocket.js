import { WebSocketServer, WebSocket } from "ws";
import crypto from "crypto";

export class UploadScreenSocket {
  constructor() {
    this.wss = null;
    this.clients = new Map(); // Store connected clients with metadata
  }

  initialize(server) {
    // Create WebSocket server instance
    this.wss = new WebSocketServer({ noServer: true });

    // Handle HTTP upgrade requests to WebSocket
    server.on('upgrade', (request, socket, head) => {
      const url = new URL(request.url, `http://${request.headers.host}`);
      
      // Only handle requests to the screen upload endpoint
      if (url.pathname.startsWith('/vid')){
        this.wss.handleUpgrade(request, socket, head, (ws) => {
          this.wss.emit('connection', ws, request);
        });
      } else {
        socket.destroy(); // Reject connections to other paths
      }
    });

    // Handle new WebSocket connections
    this.wss.on('connection', (ws, request) => {
      const clientId = crypto.randomUUID();
      
      // Store client with metadata
      this.clients.set(ws, {
        id: clientId,
        connectedAt: new Date(),
        ip: request.socket.remoteAddress,
        currentTask: null,
        currentProgress: 0
      });

      console.log(`Client ${clientId} connected. Total clients: ${this.clients.size}`);

      // Send welcome message
      ws.send(JSON.stringify({
        type: 'connection',
        message: 'Connected to video processing server',
        clientId: clientId
      }));

      // Handle incoming messages
      ws.on('message', (data) => {
        this.handleMessage(ws, data);
      });

      // Handle client disconnect
      ws.on('close', () => {
        const client = this.clients.get(ws);
        console.log(`Client ${client?.id} disconnected`);
        this.clients.delete(ws);
      });

      // Handle errors
      ws.on('error', (error) => {
        console.error('WebSocket error:', error);
        this.clients.delete(ws);
      });
    });
  }

  handleMessage(ws, data) {
    try {
      const client = this.clients.get(ws);
      
      // Handle binary data (video upload)
      if (data instanceof Buffer) {
        console.log(`Received video data from ${client.id}: ${data.length} bytes`);
        
        // Broadcast to all other clients
        this.broadcast(data, ws);
        
        // Send acknowledgment
        ws.send(JSON.stringify({
          type: 'ack',
          message: 'Data received',
          size: data.length
        }));
      } 
      // Handle text messages (JSON)
      else {
        const message = JSON.parse(data.toString());
        console.log(`Message from ${client.id}:`, message);
        
        // Handle different message types
        switch (message.type) {
          case 'ping':
            ws.send(JSON.stringify({ type: 'pong' }));
            break;
          
          default:
            ws.send(JSON.stringify({ 
              type: 'error', 
              message: 'Unknown message type' 
            }));
        }
      }
    } catch (error) {
      console.error('Error handling message:', error);
      ws.send(JSON.stringify({ 
        type: 'error', 
        message: 'Failed to process message' 
      }));
    }
  }

  /**
   * Get client by ID
   * @param {string} clientId - The client UUID
   * @returns {WebSocket|null} - The WebSocket connection or null
   */
  getClientById(clientId) {
    for (const [ws, client] of this.clients.entries()) {
      if (client.id === clientId) {
        return ws;
      }
    }
    return null;
  }

  /**
   * Start a video processing workflow
   * @param {string} clientId - The client UUID
   * @param {string} videoId - Unique identifier for the video being processed
   * @param {string} videoName - Name of the video file
   */
  startProcessing(clientId, videoId, videoName) {
    const ws = this.getClientById(clientId);
    if (!ws) {
      console.error(`Client ${clientId} not found`);
      return false;
    }

    const client = this.clients.get(ws);
    client.currentTask = 'processing';
    client.currentProgress = 0;

    if (ws.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify({
        type: 'processing-start',
        videoId: videoId,
        videoName: videoName,
        message: 'Video processing started',
        timestamp: new Date().toISOString()
      }));
    }

    return true;
  }

  /**
   * Send progress update to client
   * @param {string} clientId - The client UUID
   * @param {number} progress - Progress percentage (0-100)
   * @param {string} message - Optional custom message
   * @param {object} data - Optional additional data to send
   */
  sendProgress(clientId, progress, message = null, data = null) {
    const ws = this.getClientById(clientId);
    if (!ws) {
      console.error(`Client ${clientId} not found`);
      return false;
    }

    const client = this.clients.get(ws);
    client.currentProgress = progress;

    // Clamp progress between 0 and 100
    const clampedProgress = Math.max(0, Math.min(100, progress));

    if (ws.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify({
        type: 'progress',
        clientId: clientId,
        progress: Math.round(clampedProgress * 100) / 100, // Round to 2 decimals
        message: message || `Processing: ${Math.round(clampedProgress)}%`,
        data: data,
        timestamp: new Date().toISOString()
      }));
    }

    return true;
  }

  /**
   * Complete video processing
   * @param {string} clientId - The client UUID
   * @param {object} result - Processing result data (video URL, metadata, etc.)
   */
  completeProcessing(clientId, result = {}) {
    const ws = this.getClientById(clientId);
    if (!ws) {
      console.error(`Client ${clientId} not found`);
      return false;
    }

    const client = this.clients.get(ws);
    client.currentTask = null;
    client.currentProgress = 100;

    if (ws.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify({
        type: 'processing-complete',
        progress: 100,
        message: 'Video processing completed successfully',
        result: result,
        timestamp: new Date().toISOString()
      }));
    }

    return true;
  }

  /**
   * Send error to client
   * @param {string} clientId - The client UUID
   * @param {string} errorMessage - Error description
   * @param {object} errorData - Optional error data
   */
  sendError(clientId, errorMessage, errorData = null) {
    const ws = this.getClientById(clientId);
    if (!ws) {
      console.error(`Client ${clientId} not found`);
      return false;
    }

    const client = this.clients.get(ws);
    client.currentTask = null;

    if (ws.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify({
        type: 'error',
        error: errorMessage,
        data: errorData,
        timestamp: new Date().toISOString()
      }));
    }

    return true;
  }

  /**
   * Send custom message to client
   * @param {string} clientId - The client UUID
   * @param {object} message - Custom message object
   */
  sendMessage(clientId, message) {
    const ws = this.getClientById(clientId);
    if (!ws || ws.readyState !== WebSocket.OPEN) {
      console.error(`Client ${clientId} not found or not connected`);
      return false;
    }

    ws.send(JSON.stringify({
      ...message,
      timestamp: new Date().toISOString()
    }));

    return true;
  }

  broadcast(data, sender) {
    // Send data to all connected clients except the sender
    this.clients.forEach((clientInfo, client) => {
      if (client !== sender && client.readyState === WebSocket.OPEN) {
        client.send(data);
      }
    });
  }


  /**
   * Get all connected client IDs
   * @returns {string[]} - Array of client UUIDs
   */
  getConnectedClients() {
    return Array.from(this.clients.values()).map(client => client.id);
  }

  /**
   * Check if client is connected
   * @param {string} clientId - The client UUID
   * @returns {boolean}
   */
  isClientConnected(clientId) {
    return this.getClientById(clientId) !== null;
  }

  /**
   * Get current progress for a client
   * @param {string} clientId - The client UUID
   * @returns {number|null} - Current progress or null if client not found
   */
  getCurrentProgress(clientId) {
    const ws = this.getClientById(clientId);
    if (!ws) return null;
    
    const client = this.clients.get(ws);
    return client.currentProgress;
  }

  // Clean up resources
  close() {
    this.clients.forEach((_, ws) => {
      ws.close();
    });
    this.wss?.close();
  }
}

export default UploadScreenSocket;