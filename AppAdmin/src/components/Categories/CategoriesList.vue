<!-- src/components/CategoriesList.vue -->
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import api from '../../services/api'

import CategoriesCard from './CategoriesCard.vue'
import router from '../../router'

interface Category {
  id: number
  name: string
}

const props = defineProps<{
  searchQuery: string
  searchType: number
  refreshInterval?: number
}>()

const categories = ref<Category[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

const token = localStorage.getItem('token')

async function fetchCategories() {
  try {
    loading.value = true
    error.value = null

    const res = await api.get('http://localhost:8090/Category', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    categories.value = res.data
  } catch (e) {
    console.error(e)
    error.value = 'No s’han pogut carregar les categories'
  } finally {
    loading.value = false
  }
}

const filteredCategories = computed(() => {
  if (!props.searchQuery.trim()) return categories.value

  const q = props.searchQuery.toLowerCase()

  if (props.searchType === 1) {
    return categories.value.filter(c =>
      c.id.toString().includes(q)
    )
  }

  return categories.value.filter(c =>
    c.name.toLowerCase().includes(q)
  )
})

onMounted(fetchCategories)

function goToEditCategory(id: number) {
  router.push(`/categories/edit/${id}`)
}


</script>

<template>
  <div class="categories-list-container">
    <div v-if="loading" class="categories-status">Carregant...</div>

    <div v-else-if="error" class="categories-status categories-error">
      {{ error }}
    </div>

    <div v-else-if="filteredCategories.length === 0" class="categories-status">
      No hi ha categories
    </div>

    <div v-else class="categories-list">
      <CategoriesCard
        v-for="cat in filteredCategories"
        :key="cat.id"
        :category="cat"
        @edit="goToEditCategory"
      />
    </div>
  </div>
</template>


<style src="../../assets/css/Lists.css"></style>
