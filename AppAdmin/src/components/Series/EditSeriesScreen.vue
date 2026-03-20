<template>
  <div class="upload-container">
    <!-- Left Section -->
    <div class="left-section">
      <div class="preview">
        <p><strong>Editando serie ID:</strong> {{ serieId }}</p>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="divider"></div>

    <!-- Right Section -->
    <div class="right-section">

      <!-- Nombre -->
      <div class="form-group">
        <label class="form-label">Nom de la sèrie</label>
        <input type="text" class="form-input" v-model="form.name" />
      </div>

      <!-- Clasificación -->
      <div class="form-group">
        <label class="form-label">Classificació</label>
        <input
          type="number"
          class="form-input"
          v-model.number="form.classification"
          min="0"
        />
      </div>

      <!-- Actions -->
      <div class="bottom-actions">
        <button class="delete-btn" @click="goBack">
          Cancelar
        </button>

        <button class="save-btn" :disabled="uploading" @click="handleSave">
          Guardar canvis
        </button>
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
  name: "",
  classification: 0
})

const serieId = ref<number | null>(null)
const uploading = ref(false)
const error = ref("")

// ───────────── Navegación ─────────────
function goBack() {
  router.back()
}

// ───────────── Cargar datos serie ─────────────
async function loadSerieData(id: number) {
  try {
    const res = await api.get(`/Serie/${id}`)

    form.value = {
      name: res.data.name || "",
      classification: res.data.classification || ""
    }

    serieId.value = res.data.id
  } catch (err) {
    console.error("Error cargando serie", err)
    error.value = "No s’ha pogut carregar la sèrie"
  }
}

// ───────────── Guardar cambios ─────────────
async function handleSave() {
  if (!serieId.value) {
    error.value = "ID de sèrie obligatori"
    return
  }

  uploading.value = true
  error.value = ""

  try {
    const payload = {
      name: form.value.name,
      classification: form.value.classification
    }

    await api.put(`/Serie/${serieId.value}`, payload)

    toast.success("Sèrie actualitzada correctament", {
      position: POSITION.TOP_CENTER,
      timeout: 4000
    })

  } catch (err: any) {
    console.error("Error actualizando serie", err)
    error.value = err.response?.data?.message || "Error actualitzant sèrie"
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
      await loadSerieData(id)
    }
  }
})
</script>

<style src="../../assets/css/edit.css"></style>
