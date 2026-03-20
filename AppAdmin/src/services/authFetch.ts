// src/services/authFetch.ts
import { refreshAccessToken } from "./token"
import { getTokenFromCookie } from "../utils/auth"

export async function authFetch(
  input: RequestInfo,
  init: RequestInit = {}
) {
  const token = getTokenFromCookie()

  let response = await fetch(input, {
    ...init,
    credentials: "include",
    headers: {
      ...init.headers,
      ...(token ? { Authorization: `Bearer ${token}` } : {})
    }
  })

  if (response.status !== 401) return response

  // Intentem refresh
  const newToken = await refreshAccessToken()
  if (!newToken) throw new Error("Sessió expirada")

  // Repetim la request amb token nou
  return fetch(input, {
    ...init,
    credentials: "include",
    headers: {
      ...init.headers,
      Authorization: `Bearer ${newToken}`
    }
  })
}
