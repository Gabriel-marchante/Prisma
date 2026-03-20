// src/services/api.ts
import axios, { AxiosError, InternalAxiosRequestConfig, AxiosHeaders } from 'axios'
import { refreshAccessToken } from './TokenUserID'
import { getTokenFromCookie } from '../utils/auth'

const api = axios.create({
  baseURL: 'http://localhost:8090'
})

// Funció auxiliar per afegir el token al config
function setAuthorizationHeader(
  config: InternalAxiosRequestConfig,
  token: string
) {
  // Assegurem que headers existeixen i són AxiosHeaders
  const headers = config.headers instanceof AxiosHeaders
    ? config.headers
    : new AxiosHeaders(config.headers)

  headers.set('Authorization', `Bearer ${token}`)
  config.headers = headers
}

// Interceptor per afegir el token automàticament
api.interceptors.request.use(async (config: InternalAxiosRequestConfig) => {
  let token = getTokenFromCookie()

  if (!token) {
    token = await refreshAccessToken()
  }

  if (token) {
    setAuthorizationHeader(config, token)
  }

  return config
})

// Interceptor de resposta per gestionar 401
api.interceptors.response.use(
  response => response,
  async (error: AxiosError) => {
    if (error.response?.status === 401 && error.config) {
      console.warn("Token invàlid o caducat, intentant refrescar...")

      const newToken = await refreshAccessToken()
      if (newToken) {
        setAuthorizationHeader(error.config as InternalAxiosRequestConfig, newToken)
        return api(error.config)
      } else {
        document.cookie = 'token=; path=/; max-age=0'
        window.location.href = '/login'
        return Promise.reject(error)
      }
    }
    return Promise.reject(error)
  }
)

export default api
