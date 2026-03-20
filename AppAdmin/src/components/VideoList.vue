<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import axios from 'axios'
import VideoCard from './VideoCard.vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const router = useRouter()

function goToEdit(videoId: number) {
  router.push(`/videos/edit/${videoId}`)
}

// --- Interfaces ---
interface Category { id: number; name: string }
interface Series { id: number; name: string }
interface Study { id: number; name: string }

interface Video {
  id: number
  title: string
  description: string
  duration: number
  season: number
  chapter: number
  rating: number
  category: Category[]
  series: Series
  study: Study
}

// --- Props ---
const props = defineProps<{
  searchQuery: string
  searchType: number
  refreshInterval?: number
}>()

// --- State ---
const videos = ref<Video[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const lastRefreshTime = ref<Date | null>(null)
let refreshTimer: ReturnType<typeof setInterval> | null = null

// --- Datos de referencia ---
const categoriesList = ref<Category[]>([])
const seriesList = ref<Series[]>([])
const studyList = ref<Study[]>([])

// --- Función de fetch ---
async function fetchAllData() {
  try {
    loading.value = true
    error.value = null

    // Fetch videos
    const videosRes = await api.get('http://localhost:8090/Cataleg')
    const videosData = videosRes.data

    // Fetch categorías, series y estudios
    const [catRes, serRes, estRes] = await Promise.all([
      api.get('http://localhost:8090/Category'),
      api.get('http://localhost:8090/Serie'),
      api.get('http://localhost:8090/Estudi')
    ])
    categoriesList.value = catRes.data
    seriesList.value = serRes.data
    studyList.value = estRes.data

    // Mapear IDs a nombres
    videos.value = videosData.map((v: any) => ({
      id: v.id,
      title: v.title || v.titol || 'Sin título',
      description: v.description || '',
      duration: v.duration || v.duracio || 0,
      season: v.season || 1,
      chapter: v.chapter || 1,
      rating: v.rating || 0,
      category: v.category?.map((c: any) => {
        const cat = categoriesList.value.find(cat => cat.id === c.id)
        return { id: c.id, name: cat?.name || `#${c.id}` }
      }) || [],
      series: (() => {
        const s = seriesList.value.find(s => s.id === v.series?.id)
        return { id: v.series?.id || 0, name: s?.name || `#${v.series?.id}` }
      })(),
      study: (() => {
        const s = studyList.value.find(s => s.id === v.study?.id)
        return { id: v.study?.id || 0, name: s?.name || `#${v.study?.id}` }
      })(),
       codec: v.codec || v.code || 'N/A',
        resolucio: v.resolucio || v.resolution || 'N/A',
        pes: v.pes || v.size || 0
    }))

    lastRefreshTime.value = new Date()

  } catch (err) {
    console.error(err)
    error.value = "No s'han pogut carregar els vídeos"
  } finally {
    loading.value = false
  }
}

// --- Auto-refresh ---
function setupAutoRefresh() {
  const interval = props.refreshInterval || 30000
  refreshTimer = setInterval(fetchAllData, interval)
}

// --- Filtrado ---
const filteredVideos = computed(() => {
  if (!props.searchQuery.trim()) return videos.value
  const query = props.searchQuery.toLowerCase().trim()
  if (props.searchType === 1) {
    return videos.value.filter(v => v.id.toString().includes(query))
  } else {
    return videos.value.filter(v => v.title.toLowerCase().includes(query))
  }
})

// --- Última actualización ---
const formattedRefreshTime = computed(() => {
  if (!lastRefreshTime.value) return ''
  const now = new Date()
  const diff = Math.floor((now.getTime() - lastRefreshTime.value.getTime()) / 1000)
  if (diff < 60) return `hace ${diff}s`
  if (diff < 3600) return `hace ${Math.floor(diff / 60)}m`
  return lastRefreshTime.value.toLocaleTimeString()
})

// --- Lifecycle ---
onMounted(() => {
  fetchAllData()
  setupAutoRefresh()
})

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer)
})

// --- Expose ---
defineExpose({ fetchAllData })
</script>

<template>
  <div class="video-list-container">
    <!-- Refresh indicator -->
    <div v-if="lastRefreshTime" class="refresh-indicator">
      <span class="refresh-dot"></span>
      Última actualización: {{ formattedRefreshTime }}
    </div>

    <!-- Loading / Error / Empty -->
    <div v-if="loading" class="status">
      <div class="spinner"></div>
      <p>Carregant...</p>
    </div>
    <div v-else-if="error" class="status error">{{ error }}</div>
    <div v-else-if="filteredVideos.length === 0" class="status">
      <span v-if="searchQuery">No s'ha trobat cap vídeo amb "{{ searchQuery }}"</span>
      <span v-else>No hi ha vídeos</span>
    </div>

    <!-- Video list -->
    <div v-else class="video-list">
      <VideoCard
        v-for="video in filteredVideos"
        :key="video.id"
        :video="video"
        @edit="goToEdit"
      />
    </div>
  </div>
</template>

<style src="../assets/css/VideoList.css">

</style>
