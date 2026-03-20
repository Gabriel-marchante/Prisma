<script setup lang="ts">
interface Estudi {
  id: number
  name: string
}

const props = defineProps<{
  estudi: Estudi
}>()

const emit = defineEmits<{
  (e: "edit", estudiId: number): void
}>()

const handleEdit = () => {
  emit("edit", props.estudi.id)
}
</script>

<template>  
  <div v-if="estudi" class="category-card">

    <!-- HEADER -->
    <div class="card-header">
      <strong>{{ estudi.name }}</strong>
      <button class="edit-btn" @click="handleEdit">✏️</button>
    </div>

    <span>id: {{ estudi.id }}</span>

  </div>
</template>

<style src="../../assets/css/cards.css"></style>
