// src/services/TokenUserID.ts
import jwt_decode from "jwt-decode";
import { getRefreshToken } from '../utils/auth';

/* Interfaces */
interface TokenPayload {
  user_id: number;
  exp: number;
  [key: string]: any;
}

interface RefreshResponse {
  jsonrpc: string;
  id: number | null;
  result: {
    access_token: string;
  };
}

/**
 * getUserIdFromToken: Extreu el user_id d'un JWT
 */
function getUserIdFromToken(token: string): number | null {
  try {
    const decoded = jwt_decode<TokenPayload>(token);
    return decoded.user_id || null;
  } catch (e) {
    console.error("Error decoding token:", e);
    return null;
  }
}

/**
 * getUserIdFromRefreshToken: Retorna el user_id del refresh token guardat
 */
export function getUserIdFromRefreshToken(): number | null {
  const refreshToken = getRefreshToken();
  if (!refreshToken) return null;
  return getUserIdFromToken(refreshToken);
}

/**
 * refreshAccessToken: Fa POST a Odoo per obtenir un nou access token
 */
export async function refreshAccessToken(): Promise<string | null> {
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    console.error("No hi ha refresh token");
    return null;
  }

  const userId = getUserIdFromToken(refreshToken);
  if (!userId) {
    console.error("No s'ha pogut obtenir user_id del token");
    return null;
  }

  try {
    const response = await fetch("http://localhost:8069/api/update/access-token", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        params: {
          user_id: userId,
          refreshToken: refreshToken
        }
      })
    });

    if (!response.ok) {
      console.error("Error al refrescar el token:", response.status, response.statusText);
      return null;
    }

    const data: RefreshResponse = await response.json();
    if (!data.result?.access_token) {
      console.error("No s'ha rebut access token del servidor", data);
      return null;
    }

    const newAccessToken = data.result.access_token;

    // Guardar el nou access token a les cookies (1 hora)
    document.cookie = `token=${newAccessToken}; path=/; max-age=3600`;

    return newAccessToken;

  } catch (err) {
    console.error("Error de connexió al refrescar token:", err);
    return null;
  }
}

/**
 * getValidAccessToken: Retorna un access token vàlid,
 * refrescant-lo automàticament si ha caducat.
 */
export async function getValidAccessToken(): Promise<string | null> {
  const match = document.cookie.match(/(^| )token=([^;]+)/);
  let token = match ? match[2] : null;

  if (!token) {
    console.log("No hi ha token, provem de refrescar...");
    return await refreshAccessToken();
  }

  try {
    const payload = jwt_decode<TokenPayload>(token);
    const now = Math.floor(Date.now() / 1000);

    // Si el token ha caducat o caduca en menys de 1 minut
    if (payload.exp && payload.exp - now < 60) {
      console.log("Token caducat o pròxim a caducar, refrescant...");
      return await refreshAccessToken();
    }

    return token;
  } catch {
    console.log("Token invàlid, refrescant...");
    return await refreshAccessToken();
  }
}
