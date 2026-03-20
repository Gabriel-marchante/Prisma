<template>
  <div class="upload-container">
    <!-- Left Section -->
    <div class="left-section">
      <div class="preview">
        <p><strong>Editant estudi ID:</strong> {{ estudiId }}</p>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="divider"></div>

    <!-- Right Section -->
    <div class="right-section">

      <!-- Nom -->
      <div class="form-group">
        <label class="form-label">Nom de l'estudi</label>
        <input type="text" class="form-input" v-model="form.name" />
      </div>

      <!-- Actions -->
      <div class="bottom-actions">
        <button class="delete-btn" @click="goBack">Cancelar</button>
        <button class="save-btn" :disabled="uploading" @click="handleSave">Guardar canvis</button>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue"
import api from "../../services/api"
import { useToast, POSITION } from "vue-toastification"
import { useRouter, useRoute } from "vue-router"

const toast = useToast()
const router = useRouter()
const route = useRoute()

// ───────────── Form ─────────────
const form = ref({
  name: ""
})

const estudiId = ref<number | null>(null)
const uploading = ref(false)
const error = ref("")

// ───────────── Navegació ─────────────
function goBack() {
  router.back()
}

// ───────────── Càrrega dades estudi ─────────────
async function loadEstudiData(id: number) {
  try {
    const res = await api.get(`/Estudi/${id}`)

    form.value = {
      name: res.data.name || ""
    }

    estudiId.value = res.data.id
  } catch (err) {
    console.error("Error carregant estudi", err)
    error.value = "No s’ha pogut carregar l’estudi"
  }
}

// ───────────── Guardar canvis ─────────────
async function handleSave() {
  if (!estudiId.value) {
    error.value = "ID de l'estudi obligatori"
    return
  }

  uploading.value = true
  error.value = ""

  try {
    const payload = {
      name: form.value.name
    }

    await api.put(`/Estudi/${estudiId.value}`, payload)

    toast.success("Estudi actualitzat correctament", {
      position: POSITION.TOP_CENTER,
      timeout: 4000
    })
  } catch (err: any) {
    console.error("Error actualitzant estudi", err)
    error.value = err.response?.data?.message || "Error actualitzant estudi"
  } finally {
    uploading.value = false
  }
}

// ───────────── Lifecycle ─────────────
onMounted(async () => {
  const idParam = route.params.id
  if (idParam) {
    const id = Number(idParam)
    if (!isNaN(id)) {
      await loadEstudiData(id)
    }
  }
})
</script>

<style src="../../assets/css/edit.css"></style>
