<!-- src/components/CategoriesList.vue -->
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import api from '../../services/api'
import { useRouter } from 'vue-router'

import EstudiCard from './EstudiCard.vue'

interface Estudi {
  id: number
  name: string
}

const router = useRouter()

const props = defineProps<{
  searchQuery: string
  searchType: number
  refreshInterval?: number
}>()

const estudis = ref<Estudi[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

const token = localStorage.getItem('token')

function goEdit(id: number) {
  router.push(`/estudis/edit/${id}`)
}

async function fetchEstudis() {
  try {
    loading.value = true
    error.value = null

    const res = await api.get('http://localhost:8090/Estudi', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    estudis.value = res.data
  } catch (e) {
    console.error(e)
    error.value = 'No s’han pogut carregar els studis'
  } finally {
    loading.value = false
  }
}

const filteredEstudis = computed(() => {
  if (!props.searchQuery.trim()) return estudis.value

  const q = props.searchQuery.toLowerCase()

  if (props.searchType === 1) {
    return estudis.value.filter(c =>
      c.id.toString().includes(q)
    )
  }

  return estudis.value.filter(c =>
    c.name.toLowerCase().includes(q)
  )
})

onMounted(fetchEstudis)
</script>

<template>
  <div class="categories-list-container">
    <div v-if="loading" class="categories-status">Carregant...</div>

    <div v-else-if="error" class="categories-status categories-error">
      {{ error }}
    </div>

    <div v-else-if="filteredEstudis.length === 0" class="categories-status">
      No hi han estudis
    </div>

    <div v-else class="categories-list">
      <EstudiCard
        v-for="cat in filteredEstudis"
        :key="cat.id"
        :estudi="cat"
        @edit="goEdit"  
      />
    </div>
  </div>
</template>


<style src="../../assets/css/Lists.css"></style>
