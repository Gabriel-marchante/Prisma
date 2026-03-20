<script setup lang="ts">
interface Category {
  id: number
  name: string
}

const props = defineProps<{
  category: Category
}>()

const emit = defineEmits<{
  (e: "edit", categoryId: number): void
}>()

const handleEdit = () => {
  emit("edit", props.category.id)
}
</script>

<template>
  <div v-if="category" class="category-card">

    <!-- HEADER -->
    <div class="card-header">
      <strong>{{ category.name }}</strong>
      <button class="edit-btn" @click="handleEdit">✏️</button>
    </div>

    <span>id: {{ category.id }}</span>

  </div>
</template>

<style src="../../assets/css/cards.css"></style>
