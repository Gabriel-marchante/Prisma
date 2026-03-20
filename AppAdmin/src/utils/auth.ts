// // src/services/auth.ts
// import jwt_decode, { JwtPayload } from 'jwt-decode';

// /* Interfaces per tipatge */
// interface LoginResponse {
//   jsonrpc: string
//   id: number
//   result: {
//     access_token?: string
//     refresh_token?: string // no s’usa ara, però pot vindre
//     error?: string
//   }
// }

// interface TokenPayload extends JwtPayload {
//   user_id: number
//   is_admin: boolean
// }

// interface LoginResult {
//   success: boolean
//   token?: string
//   error?: string
// }

// /**
//  * loginUser: Fa POST al servidor i retorna informació de l'usuari
//  * @param email Email de l'usuari
//  * @param password Contrasenya de l'usuari
//  * @returns LoginResult
//  */
// export async function loginUser(
//   email: string,
//   password: string
// ): Promise<LoginResult> {
//   try {
//     const response = await fetch("/api/authenticate", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       credentials: "include",
//       body: JSON.stringify({
//         jsonrpc: "2.0",
//         method: "call",
//         params: {
//           db: "Padalustro",
//           login: email,
//           password: password
//         },
//         id: 1
//       })
//     })

//     const data: LoginResponse = await response.json()

//     // Error del servidor
//     if (data.result?.error)
//       return { success: false, error: data.result.error }

//     // Access token no retornat
//     if (!data.result?.access_token)
//       return { success: false, error: "Access token no retornat" }

//     // Decodificar JWT per verificar admin
//     const payload = jwt_decode<TokenPayload>(data.result.access_token)

//     if (!payload.is_admin)
//       return { success: false, error: "No tens permisos d'admin" }

//     // Guardar cookie (1 hora per defecte)
//     document.cookie = `token=${data.result.access_token}; path=/; max-age=3600`

//     return {
//       success: true,
//       token: data.result.access_token
//     }

//   } catch (err) {
//     console.error(err)
//     return { success: false, error: "Error de connexió" }
//   }
// }

// /**
//  * getTokenFromCookie: Retorna el token guardat a les cookies
//  */
// export function getTokenFromCookie(): string | null {
//   const match = document.cookie.match(/(^| )token=([^;]+)/)
//   return match ? match[2] : null
// }


// export function getRefreshToken(): string | null {
//   const match = document.cookie.match(/(^| )refresh_token=([^;]+)/)
//   return match ? match[2] : null
// }


// /**
//  * logoutUser: Elimina la cookie de token
//  */
// export function logoutUser() {
//   document.cookie = 'token=; path=/; max-age=0'
// }


// src/services/auth.ts
import jwt_decode, { JwtPayload } from "jwt-decode";

/* Interfaces per tipatge */
interface LoginResponse {
  jsonrpc: string;
  id: number;
  result: {
    access_token?: string;
    refresh_token?: string; // no s’usa ara, però pot vindre
    error?: string;
  };
}

interface TokenPayload extends JwtPayload {
  user_id: number;
  is_admin: boolean;
}

interface LoginResult {
  success: boolean;
  token?: string;
  error?: string;
}

/**
 * Base de la API segons entorn
 * - DEV  → proxy Vite (/api)
 * - PROD → Odoo directe (NW.js / build)
 */
const API_BASE =
  import.meta.env.DEV
    ? "/api"
    : "http://localhost:8069";

/**
 * loginUser: Fa POST al servidor i retorna informació de l'usuari
 * @param email Email de l'usuari
 * @param password Contrasenya de l'usuari
 * @returns LoginResult
 */
export async function loginUser(
  email: string,
  password: string
): Promise<LoginResult> {
  try {
    const response = await fetch(
      `${API_BASE}/authenticate`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          jsonrpc: "2.0",
          method: "call",
          params: {
            db: "Padalustro",
            login: email,
            password: password,
          },
          id: 1,
        }),
      }
    );

    const data: LoginResponse = await response.json();

    // Error del servidor
    if (data.result?.error)
      return { success: false, error: data.result.error };

    // Access token no retornat
    if (!data.result?.access_token)
      return { success: false, error: "Access token no retornat" };

    // Decodificar JWT per verificar admin
    const payload = jwt_decode<TokenPayload>(data.result.access_token);

    if (!payload.is_admin)
      return { success: false, error: "No tens permisos d'admin" };

    // Guardar cookie (1 hora per defecte)
    document.cookie = `token=${data.result.access_token}; path=/; max-age=3600`;

    return {
      success: true,
      token: data.result.access_token,
    };

  } catch (err) {
    console.error(err);
    return { success: false, error: "Error de connexió" };
  }
}

/**
 * getTokenFromCookie: Retorna el token guardat a les cookies
 */
export function getTokenFromCookie(): string | null {
  const match = document.cookie.match(/(^| )token=([^;]+)/);
  return match ? match[2] : null;
}

export function getRefreshToken(): string | null {
  const match = document.cookie.match(/(^| )refresh_token=([^;]+)/);
  return match ? match[2] : null;
}

/**
 * logoutUser: Elimina la cookie de token
 * Tambe tindriem que fer un post a /api/revoke/token: Revokes a refresh token.
 * Input: {}
 * Headers (mobile): {'refreshToken': xxxx, ...default_headers}
 * Output: { 'status': 'done' }
 * Perque si ens furten el refreshToken podrien accedir
 */
export function logoutUser() {
  document.cookie = "token=; path=/; max-age=0";
  document.cookie = "refresh_token=; path=/; max-age=0";
}

