<template>
  <div class="upload-container">
    <!-- Left Section -->
    <div class="left-section">
      <div class="preview">
        <p><strong>Editant categoria ID:</strong> {{ categoryId }}</p>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="divider"></div>

    <!-- Right Section -->
    <div class="right-section">

      <!-- Nom -->
      <div class="form-group">
        <label class="form-label">Nom de la categoria</label>
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

const categoryId = ref<number | null>(null)
const uploading = ref(false)
const error = ref("")

// ───────────── Navegació ─────────────
function goBack() {
  router.back()
}

// ───────────── Càrrega dades categoria ─────────────
async function loadCategoryData(id: number) {
  try {
    const res = await api.get(`/Category/${id}`)

    form.value = {
      name: res.data.name || ""
    }

    categoryId.value = res.data.id
  } catch (err) {
    console.error("Error carregant categoria", err)
    error.value = "No s’ha pogut carregar la categoria"
  }
}

// ───────────── Guardar canvis ─────────────
async function handleSave() {
  if (!categoryId.value) {
    error.value = "ID de la categoria obligatori"
    return
  }

  uploading.value = true
  error.value = ""

  try {
    const payload = {
      name: form.value.name
    }

    await api.put(`/Category/${categoryId.value}`, payload)

    toast.success("Categoria actualitzada correctament", {
      position: POSITION.TOP_CENTER,
      timeout: 4000
    })
  } catch (err: any) {
    console.error("Error actualitzant categoria", err)
    error.value = err.response?.data?.message || "Error actualitzant categoria"
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
      await loadCategoryData(id)
    }
  }
})
</script>

<style src="../../assets/css/edit.css"></style>
