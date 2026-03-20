// src/main.ts
import { createApp } from "vue"
import App from "./App.vue"
import Toast from 'vue-toastification'
import "vue-toastification/dist/index.css"
import router from './router'
import { getTokenFromCookie } from './utils/auth'
import jwt_decode from 'jwt-decode'

const app = createApp(App)

// Guard global d'administrador
router.beforeEach((to, _from, next) => {
  // Rutes que no necessiten login
  const publicPaths = ['/login']

  if (publicPaths.includes(to.path)) {
    next() // si és login, deixar passar
    return
  }

  const token = getTokenFromCookie()
  if (!token) {
    router.push('/login')
    return
  }

  try {
    const payload: { is_admin: boolean } = jwt_decode(token)
    if (!payload.is_admin) {
      router.push('/login')
      return
    }
    next() // és admin → deixar passar
  } catch (err) {
    console.error("Token invàlid", err)
    router.push('/login')
  }
})

app.use(router)
app.use(Toast, {
  position: "top-right",
  timeout: 4000,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 0.6,
  hideProgressBar: false,
  newestOnTop: true
})

app.mount('#app')
