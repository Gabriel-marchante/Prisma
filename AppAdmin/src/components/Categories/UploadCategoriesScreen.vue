<template>
  <div class="upload-container">
    <div class="left-section">
      <h2>Afegir nova categoria</h2>

      <!-- Error -->
      <p v-if="error" class="error">{{ error }}</p>

      <!-- Form -->
      <div class="form-group">
        <label class="form-label">Nova categoria</label>
        <input 
          type="text" 
          class="form-input" 
          v-model="form.name" 
          placeholder="Nom de la categoria" 
        />
      </div>

      <!-- Bottom Actions -->
      <div class="bottom-actions">
        <button class="delete-btn" @click="emit('close')" title="Cancelar">
          Cancel·lar
        </button>

        <button 
          class="save-btn" 
          :disabled="uploading || !form.name"
          @click="handleSave"
        >
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
})

const uploading = ref(false)
const error = ref('')

// ── Save function ──────────────────────
async function handleSave() {
  if (!form.value.name) return

  uploading.value = true
  error.value = ''

  try {
    const token = localStorage.getItem('token')
    await api.post('http://localhost:8090/Category', form.value, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    toast.success('Categoria afegida correctament', { position: POSITION.TOP_CENTER, timeout: 3000 })
    resetForm()
    emit('close')
  } catch (err: any) {
    console.error('Error al pujar la categoria:', err)
    error.value = err.response?.data?.message || 'Error al guardar la categoria'
  } finally {
    uploading.value = false
  }
}

// ── Reset form ─────────────────────────
function resetForm() {
  form.value = {
    name: '',
  }
}
</script>

<style src="../../assets/css/edit.css"></style>
