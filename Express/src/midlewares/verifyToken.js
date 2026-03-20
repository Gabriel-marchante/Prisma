import jwt from 'jsonwebtoken';
import fs from 'fs';
import path from 'path';

// Ruta ABSOLUTA a la clave pública
const publicKey = fs.readFileSync(
  path.resolve('keys/public.pem'),
  'utf8'
);

export default function verifyToken(req, res, next) {
  const authHeader = req.headers['authorization'];

  if (!authHeader) {
    return res.status(401).json({ message: 'Token no proporcionado' });
  }

  const token = authHeader.split(' ')[1];

  jwt.verify(
    token,
    publicKey,
    { algorithms: ['RS256'] },
    (err, decoded) => {
        if (err) {
        return res.status(403).json({ message: 'Token inválido' });
    }
    // comprobar suscripción
    if (!decoded.has_subscription && !decoded.is_admin) {
      return res.status(403).json({
        message: 'Existe usuario pero no tiene suscripción',
      });
    }


      // para usarlo en el controlador de la peticion de video.
    req.user = decoded;

      next();
    }
  );
}
