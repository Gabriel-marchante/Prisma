<script setup lang="ts">
import { useRouter } from 'vue-router'
import { logoutUser } from '../utils/auth'

const router = useRouter()

function handleLogout() {
  logoutUser()
  router.push('/login')
}
</script>

<template>
  <button class="logout-btn" @click="handleLogout" title="Tancar sessió">
    ⏻
  </button>
</template>

<style scoped>
.logout-btn {
  background: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 8px;
  color: #000000;
}

.logout-btn:hover {
  color: #ff6b6b;
}
</style>
