<template>
  <div class="upload-container">
    <!-- Left Section -->
    <div class="left-section">
      <div class="preview">
        <p><strong>Editando vídeo ID:</strong> {{ videoId }}</p>
        <p><strong>Duración:</strong> {{ form.duration }} s</p>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="divider"></div>

    <!-- Right Section -->
    <div class="right-section">
      <!-- Title -->
      <div class="form-group">
        <label class="form-label">Títol</label>
        <input type="text" class="form-input" v-model="form.title" />
      </div>

      <!-- Description -->
      <div class="form-group">
        <label class="form-label">Descripción</label>
        <textarea class="form-textarea" v-model="form.description" rows="3" />
      </div>

      <!-- Categories -->
      <div class="form-group">
        <label class="form-label">Categoría 1</label>
        <select class="form-input" v-model="category1">
          <option value="">Selecciona una categoría</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">
            {{ c.name }}
          </option>
        </select>

        <label class="form-label">Categoría 2</label>
        <select class="form-input" v-model="category2">
          <option value="">Selecciona una categoría</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">
            {{ c.name }}
          </option>
        </select>
      </div>

      <!-- Studio -->
      <div class="form-group">
        <label class="form-label">Estudio</label>
        <select class="form-input" v-model="estudioId">
          <option value="">Selecciona un estudio</option>
          <option v-for="e in estudios" :key="e.id" :value="e.id">
            {{ e.name }}
          </option>
        </select>
      </div>

      <!-- Series -->
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">Serie</label>
          <select class="form-input" v-model="seriesId">
            <option value="">Selecciona una serie</option>
            <option v-for="s in series" :key="s.id" :value="s.id">
              {{ s.name }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Temporada</label>
          <input type="number" class="form-input" v-model.number="form.season" />
        </div>

        <div class="form-group">
          <label class="form-label">Capítulo</label>
          <input type="number" class="form-input" v-model.number="form.chapter" />
        </div>
      </div>

      <!-- Actions -->
      <div class="bottom-actions">
        <button class="delete-btn" @click="goBack">Cancelar</button>
        <button class="save-btn" :disabled="uploading" @click="handleSave">
          Guardar cambios
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '../services/api' // Axios amb interceptor que posa el token
import { useToast, POSITION } from 'vue-toastification'
import { useRouter, useRoute } from 'vue-router'

const toast = useToast()
const router = useRouter()
const route = useRoute()

// ── Form & Data ─────────────────────────────
const form = ref({
  id: 0,
  title: '',
  description: '',
  category: [] as Array<{ id: number }>,
  study: { id: 0 },
  series: { id: 0 },
  season: 1,
  chapter: 1,
  duration: 0,
  rating: 0,
  thumbnail: ''
})

const category1 = ref<number | null>(null)
const category2 = ref<number | null>(null)
const estudioId = ref<number | null>(null)
const seriesId = ref<number | null>(null)

const categories = ref<Array<{ id: number; name: string }>>([])
const estudios = ref<Array<{ id: number; name: string }>>([])
const series = ref<Array<{ id: number; name: string }>>([])

const uploading = ref(false)
const error = ref('')

// ── ID del vídeo que volem editar ─────────
const videoId = ref<number | null>(null)

// ── Helpers ────────────────────────────────
function goBack() {
  router.back()
}

// ── Càrrega dades de referència (categories, estudios, series) ─────────
async function loadReferenceData() {
  try {
    const [catRes, estRes, serRes] = await Promise.all([
      api.get('/Category'),
      api.get('/Estudi'),
      api.get('/Serie')
    ])
    categories.value = catRes.data
    estudios.value = estRes.data
    series.value = serRes.data
  } catch (err) {
    console.error('Error carregant referències', err)
  }
}

// ── Càrrega dades del vídeo ─────────────────
async function loadVideoData(id: number) {
  try {
    const res = await api.get(`/Cataleg/${id}`)
    const data = res.data

    form.value = {
      id: data.id,
      title: data.title,
      description: data.description,
      category: data.category || [],
      study: data.study || { id: 0 },
      series: data.series || { id: 0 },
      season: data.season || 1,
      chapter: data.chapter || 1,
      duration: data.duration || 0,
      rating: data.rating || 0,
      thumbnail: data.thumbnail || ''
    }

    // Assignem categories i altres camps per als selects
    if (form.value.category.length > 0) category1.value = form.value.category[0].id
    if (form.value.category.length > 1) category2.value = form.value.category[1].id
    estudioId.value = form.value.study.id
    seriesId.value = form.value.series.id
    videoId.value = data.id
  } catch (err) {
    console.error('Error carregant dades del vídeo', err)
    error.value = 'No s’ha pogut carregar el vídeo'
  }
}

// ── Actualitzar vídeo (PUT) ─────────────────
async function handleSave() {
  if (!videoId.value) {
    error.value = 'ID del vídeo obligatori'
    return
  }

  uploading.value = true
  error.value = ''

  try {
    const payload = {
      id: videoId.value,
      title: form.value.title,
      description: form.value.description,
      duration: form.value.duration,
      chapter: form.value.chapter || 1,
      season: form.value.season || 1,
      rating: form.value.rating || 0,
      thumbnail: form.value.thumbnail || '',
      category: [] as Array<{ id: number }>,
      study: estudioId.value ? { id: estudioId.value } : { id: 0 },
      series: seriesId.value ? { id: seriesId.value } : { id: 0 }
    }

    if (category1.value) payload.category.push({ id: category1.value })
    if (category2.value) payload.category.push({ id: category2.value })

    await api.put(`/Cataleg/${videoId.value}`, payload)

    toast.success('Vídeo actualitzat correctament', { position: POSITION.TOP_CENTER, timeout: 4000 })
  } catch (err: any) {
    console.error('Error al actualitzar vídeo', err)
    error.value = err.response?.data?.message || 'Error al actualitzar vídeo'
  } finally {
    uploading.value = false
  }
}

// ── Lifecycle ─────────────────────────────
onMounted(async () => {
  await loadReferenceData()

  const idParam = route.params.id
  if (idParam) {
    const id = Number(idParam)
    if (!isNaN(id)) await loadVideoData(id)
  }
})
</script>



<style src="../assets/css/edit.css">

</style>