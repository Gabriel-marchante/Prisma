<script setup lang="ts">
interface Category {
  id: number
  name?: string
}

interface Series {
  id: number
  name?: string
}

interface Study {
  id: number
  name?: string
}

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
  resolucio?: string | null
  codec?: string | null
  pes?: number | null
}

const props = defineProps<{ video: Video }>()

const emit = defineEmits<{
  (e: 'edit', videoId: number): void
}>()

const handleEdit = () => emit('edit', props.video.id)

const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const formatSize = (kb: number | null | undefined) => {
  if (kb == null) return 'N/A'
  if (kb < 1024) return `${kb} KB`
  return `${(kb / 1024).toFixed(1)} MB`
}

const thumbnailUrl = `http://localhost:3000/static/${props.video.id}/thumbnail.jpg`
</script>

<template>
  <div class="video-card">
    <!-- HEADER -->
    <div class="card-header">
      <h3 class="title">{{ props.video.title }}</h3>
      <button class="edit-btn" @click="handleEdit">✏️</button>
    </div>

    <div class="divider"></div>

    <!-- IMAGE + DESCRIPTION -->
    <div class="top-content">
      <div class="thumbnail">
        <img
          :src="thumbnailUrl"
          alt="thumbnail"
          @error="($event.target as HTMLImageElement).style.display = 'none'"
        />
      </div>

      <div class="description">
        {{ props.video.description }}
      </div>
    </div>

    <!-- INFO BLOCKS -->
    <div class="meta-grid">
      <!-- IZQUIERDA -->
      <div class="meta-box">
        <div><strong>Temporada:</strong> {{ props.video.season }}</div>
        <div><strong>Capítol:</strong> {{ props.video.chapter }}</div>
        <div><strong>Valoració:</strong> {{ props.video.rating }}</div>

        <div class="categories">
          <strong>Categories:</strong>
          <span
            v-for="c in props.video.category"
            :key="c.id"
            class="chip"
          >
            {{ c.name || `#${c.id}` }}
          </span>
        </div>

        <div><strong>Sèrie:</strong> {{ props.video.series.name || `#${props.video.series.id}` }}</div>
        <div><strong>Estudi:</strong> {{ props.video.study.name || `#${props.video.study.id}` }}</div>
      </div>

      <!-- DERECHA -->
      <div class="meta-box">
        <div><strong>ID:</strong> {{ props.video.id }}</div>
        <div><strong>Duració:</strong> {{ formatDuration(props.video.duration) }}</div>
        <div><strong>Còdec:</strong> {{ props.video.codec ?? 'N/A' }}</div>
        <div><strong>Resolució:</strong> {{ props.video.resolucio ?? 'N/A' }}</div>
        <div><strong>Pes:</strong> {{ formatSize(props.video.pes) }}</div>
      </div>
    </div>
  </div>
</template>

<style src="../assets/css/VideoCard.css">

</style>
