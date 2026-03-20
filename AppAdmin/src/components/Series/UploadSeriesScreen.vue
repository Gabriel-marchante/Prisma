<!-- src/components/UploadSeriesScreen.vue -->
<template>
  <div class="upload-container">
    <div class="left-section">
      <h2>Afegir nova sèrie</h2>

      <!-- Error -->
      <p v-if="error" class="error">{{ error }}</p>

      <!-- Form -->
      <div class="form-group">
        <label class="form-label">Nom de la sèrie</label>
        <input type="text" class="form-input" v-model="form.name" placeholder="Nom de la sèrie" />
      </div>

      <div class="form-group">
        <label class="form-label">Classificació</label>
        <input type="text" class="form-input" v-model="form.classification" placeholder="p. ex. 7, 12, 16, 18..." />
      </div>

      <!-- Bottom Actions -->
      <div class="bottom-actions">
        <button class="delete-btn" @click="$emit('close')" title="Cancelar">
          Cancel·lar
        </button>

        <button class="save-btn" :disabled="uploading || !form.name || !form.classification" @click="handleSave">
          {{ uploading ? 'Guardant...' : 'Guardar' }}
        </button>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import api from '../../services/api' 
import { useToast, POSITION } from 'vue-toastification'

const toast = useToast()

// ── Emissions ──────────────────────────
const emit = defineEmits<{
  (e: 'close'): void
}>()

// ── Form & Status ───────────────────────
const form = ref({
  name: '',
  classification: ''
})

const uploading = ref(false)
const error = ref('')

// ── Save function ──────────────────────
async function handleSave() {
  if (!form.value.name || !form.value.classification) return

  uploading.value = true
  error.value = ''

  try {
    const token = localStorage.getItem('token')
    await api.post('http://localhost:8090/Serie', form.value, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    toast.success('Sèrie afegida correctament', { position: POSITION.TOP_CENTER, timeout: 3000 })
    resetForm()
    emit('close')
  } catch (err: any) {
    console.error('Error al pujar la sèrie:', err)
    error.value = err.response?.data?.message || 'Error al guardar la sèrie'
  } finally {
    uploading.value = false
  }
}

// ── Reset form ─────────────────────────
function resetForm() {
  form.value = {
    name: '',
    classification: ''
  }
}
</script>

<style src="../../assets/css/edit.css"></style>
