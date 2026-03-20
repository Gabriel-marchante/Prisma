<!-- src/screens/LoginScreen.vue -->
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '../utils/auth' // ← ruta correcta segons estructura

// Dades del formulari
const email = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

const router = useRouter()

async function handleLogin() {
  errorMsg.value = ''
  loading.value = true

  const result = await loginUser(email.value, password.value)

  if (!result.success) {
    errorMsg.value = result.error || "Error desconegut"
    loading.value = false
    return
  }

  // Login correcte → redirigir a home
  router.push('/home')
  loading.value = false
}

</script>

<template>
  <div class="login-container">
    <h2>Login Administrador</h2>

    <input
      v-model="email"
      type="email"
      placeholder="Email"
      required
    />

    <input
      v-model="password"
      type="password"
      placeholder="Contraseña"
      required
    />

    <button @click="handleLogin" :disabled="loading">
      {{ loading ? 'Entrando...' : 'Entrar' }}
    </button>

    <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  text-align: center;
  background: #fff;
}

input {
  text-align: center;
  display: block;
  width: 100%;
  margin: 12px 0;
  padding: 12px 0;
  border-radius: 80px;
  border: 1px solid #ccc;
  font-size: 16px;
}

button {
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  background-color: #667eea;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: 0.2s;
}

button:disabled {
  background-color: #a0a0ff;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #5563c1;
}

.error-msg {
  color: red;
  margin-top: 12px;
}
</style>
