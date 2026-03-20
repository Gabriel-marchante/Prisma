<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import api from '../../services/api'
import { useRouter } from 'vue-router'

import SeriesCard from './SeriesCard.vue'

interface Serie {
  id: number
  name: string
  classification: number
}

const router = useRouter()

const props = defineProps<{
  searchQuery: string
  searchType: number
  refreshInterval?: number
}>()

const series = ref<Serie[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

const token = localStorage.getItem('token')

function goEdit(id: number) {
  router.push(`/series/edit/${id}`)
}

async function fetchSeries() {
  try {
    loading.value = true
    error.value = null

    const res = await api.get('http://localhost:8090/Serie', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    series.value = res.data
  } catch (e) {
    console.error(e)
    error.value = 'No s’han pogut carregar les series'
  } finally {
    loading.value = false
  }
}

const filteredSeries = computed(() => {
  if (!props.searchQuery.trim()) return series.value

  const q = props.searchQuery.toLowerCase()

  if (props.searchType === 1) {
    return series.value.filter(c =>
      c.id.toString().includes(q)
    )
  }

  if (props.searchType === 2) {
    return series.value.filter(c =>
      c.classification.toString().includes(q)
    )
  }

  return series.value.filter(c =>
    c.name.toLowerCase().includes(q)
  )
})

onMounted(fetchSeries)
</script>

<template>
  <div class="categories-list-container">
    <div v-if="loading" class="categories-status">Carregant...</div>

    <div v-else-if="error" class="categories-status categories-error">
      {{ error }}
    </div>

    <div v-else-if="filteredSeries.length === 0" class="categories-status">
      No hi han series
    </div>

    <div v-else class="categories-list">
      <SeriesCard
        v-for="cat in filteredSeries"
        :key="cat.id"
        :serie="cat"
        @edit="goEdit"  
      />
    </div>
  </div>
</template>

<style src="../../assets/css/Lists.css"></style>
