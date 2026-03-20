// src/services/token.ts

function getCookie(name: string): string | null {
  const match = document.cookie.match(new RegExp(`(^| )${name}=([^;]+)`))
  return match ? match[2] : null
}

export async function refreshAccessToken(): Promise<string | null> {
  const refreshToken = getCookie("refresh_token")
  if (!refreshToken) return null

  const response = await fetch(
    "http://localhost:8069/api/update/access-token",
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({
        jsonrpc: "2.0",
        method: "call",
        params: {
          refresh_token: refreshToken
        },
        id: 1
      })
    }
  )

  if (!response.ok) return null

  const data = await response.json()

  // segons el que m'has ensenyat abans
  const accessToken = data?.result?.access_token
  if (!accessToken) return null

  // Guardem nou access token
  document.cookie = `token=${accessToken}; path=/; max-age=3600`

  return accessToken
}
