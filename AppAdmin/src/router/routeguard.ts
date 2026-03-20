// src/router/routeGuard.ts
import { Router } from 'vue-router'
import { getTokenFromCookie } from '../utils/auth'
import jwt_decode from 'jwt-decode'

export function setupRouteGuard(router: Router) {
  router.beforeEach((to, _from, next) => {
    const token = getTokenFromCookie()

    if (to.path === '/login') { next(); return }

    if (!token) { next('/login'); return }

    try {
      const payload: any = jwt_decode(token)
      if (!payload.is_admin) { next('/login'); return }
      next()
    } catch {
      next('/login')
    }
  })
}
