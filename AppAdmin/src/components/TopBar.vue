<!-- src/components/TopBar.vue -->
<template>
  <div class="top-buttons">
    <!-- Esquerra -->
    <div class="left-buttons">
      <button 
        :class="{ active: active === 'videos' }" 
        @click="changeSection('videos')"
      >
        Videos
      </button>
      <button 
        :class="{ active: active === 'series' }" 
        @click="changeSection('series')"
      >
        Series
      </button>
    </div>

    <!-- Centre -->
    <div class="title">
      Padalustro
    </div>

    <!-- Dreta -->
    <div class="right-buttons">
      <button 
        :class="{ active: active === 'categories' }" 
        @click="changeSection('categories')"
      >
        Categorías
      </button>
      <button 
        :class="{ active: active === 'studios' }" 
        @click="changeSection('studios')"
      >
        Estudios
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  activeSection?: 'videos' | 'series' | 'categories' | 'studios'
}>()

const emit = defineEmits<{
  (e: 'update:activeSection', section: 'videos' | 'series' | 'categories' | 'studios'): void
}>()

const active = ref(props.activeSection || 'videos')

function changeSection(section: typeof active.value) {
  active.value = section
  emit('update:activeSection', section)
}
</script>

<style scoped>
.top-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  /* Sense fons ni ombra */
}

.left-buttons,
.right-buttons {
  display: flex;
  gap: 8px;
}

button {
  background: transparent;
  border: 1px solid #999;
  color: #333;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
}

button.active {
  background: #333;
  color: white;
  border-color: #333;
}

button:hover {
  background: rgba(0,0,0,0.1);
}

.title {
  text-align: center;
  font-weight: 700;
  font-size: 1.5rem;
  flex: 1;
}
</style>
