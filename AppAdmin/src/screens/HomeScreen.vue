<!-- src/screens/HomeScreen.vue -->
<script setup lang="ts">
import { ref } from 'vue'
import VideoList from '../components/VideoList.vue'
import CategoriesList from '../components/Categories/CategoriesList.vue'
import SeriesList from '../components/Series/SeriesList.vue'
import EstudisList from '../components/Estudis/EstudiList.vue'
import UploadScreen from '../screens/UploadScreen.vue'
import UploadSeriesScreen from '../components/Series/UploadSeriesScreen.vue'
import UploadEstudiScreen from '../components/Estudis/UploadEstudisScreen.vue'
import UploadCategoriesScreen from '../components/Categories/UploadCategoriesScreen.vue'
import TopBar from '../components/TopBar.vue'
import LogoutButton from '../components/logoutButton.vue'


// ================== Secció activa ==================
const activeSection = ref<'videos' | 'series' | 'categories' | 'studios'>('videos')

// ================== Modals Upload ==================
const showUploadVideoScreen = ref(false)
const showUploadSeriesScreen = ref(false)
const showUploadEstudiScreen = ref(false)
const showUploadCategoriesScreen = ref(false)

// ================== Buscador ==================
const tipoBuscador = [
  { id: 1, name: 'ID' },
  { id: 2, name: 'Nom' }
]

const searchQuery = ref('')
const selected = ref(2)
const refreshInterval = 30000

// ================== Funcions ==================
function handleEditVideo(videoId: number) {
  alert(`Editing video ID: ${videoId}`)
}
</script>

<template>
  <!-- ================= MODALS ================= -->
  <UploadScreen
    v-if="showUploadVideoScreen"
    @close="showUploadVideoScreen = false"
  />

  <UploadSeriesScreen
    v-if="showUploadSeriesScreen"
    @close="showUploadSeriesScreen = false"
  />

  <UploadEstudiScreen
    v-if="showUploadEstudiScreen"
    @close="showUploadEstudiScreen = false"
  />

  <UploadCategoriesScreen
    v-if="showUploadCategoriesScreen"
    @close="showUploadCategoriesScreen = false"
  />

  <!-- ================= CONTINGUT PRINCIPAL ================= -->
  <div v-else class="container">
    <TopBar v-model:activeSection="activeSection" />

    <!-- ================= VIDEOS ================= -->
    <div v-if="activeSection === 'videos'">
      <div class="filters-wrapper">
        <LogoutButton />
        <select v-model="selected">
          <option v-for="type in tipoBuscador" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
        </select>

        <div class="search-container">
          <input v-model="searchQuery" type="text" placeholder="Buscar vídeo..." class="search-input" />
          <span v-if="searchQuery" class="clear-btn" @click="searchQuery = ''">×</span>
        </div>

        <button class="upload-btn" @click="showUploadVideoScreen = true">
          Pujar vídeo
        </button>
      </div>

      <VideoList
        :search-query="searchQuery"
        :search-type="selected"
        :refresh-interval="refreshInterval"
        @edit-video="handleEditVideo"
      />
    </div>

    <!-- ================= CATEGORIES ================= -->
    <div v-else-if="activeSection === 'categories'">
      <div class="filters-wrapper">
        <LogoutButton />
        <select v-model="selected">
          <option v-for="type in tipoBuscador" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
        </select>

        <div class="search-container">
          <input v-model="searchQuery" type="text" placeholder="Buscar categoria..." class="search-input" />
          <span v-if="searchQuery" class="clear-btn" @click="searchQuery = ''">×</span>
        </div>
        <button class="upload-btn" @click="showUploadCategoriesScreen = true">
          Afegir categoria
        </button>
      </div>

      <CategoriesList
        :search-query="searchQuery"
        :search-type="selected"
        :refresh-interval="refreshInterval"
      />
    </div>

    <!-- ================= SERIES ================= -->
    <div v-else-if="activeSection === 'series'">
      <div class="filters-wrapper">
        <LogoutButton />
        <select v-model="selected">
          <option v-for="type in tipoBuscador" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
        </select>

        <div class="search-container">
          <input v-model="searchQuery" type="text" placeholder="Buscar Series..." class="search-input" />
          <span v-if="searchQuery" class="clear-btn" @click="searchQuery = ''">×</span>
        </div>

        <button class="upload-btn" @click="showUploadSeriesScreen = true">
          Afegir sèrie
        </button>
      </div>

      <SeriesList
        :search-query="searchQuery"
        :search-type="selected"
        :refresh-interval="refreshInterval"
      />
    </div>

    <!-- ================= STUDIOS ================= -->
    <div v-else-if="activeSection === 'studios'">
      <div class="filters-wrapper">
        <LogoutButton />
        <select v-model="selected">
          <option v-for="type in tipoBuscador" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
        </select>

        <div class="search-container">
          <input v-model="searchQuery" type="text" placeholder="Buscar estudi..." class="search-input" />
          <span v-if="searchQuery" class="clear-btn" @click="searchQuery = ''">×</span>
        </div>

        <button class="upload-btn" @click="showUploadEstudiScreen = true">
          Afegir estudi
        </button>
      </div>

      <EstudisList
        :search-query="searchQuery"
        :search-type="selected"
        :refresh-interval="refreshInterval"
      />
    </div>
  </div>
</template>

<style src="../assets/css/home.css"></style>
