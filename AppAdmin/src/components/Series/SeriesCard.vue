<script setup lang="ts">
interface Serie {
  id: number
  name: string
  classification: number
}

const props = defineProps<{
  serie: Serie
}>()

const emit = defineEmits<{
  (e: "edit", serieId: number): void
}>()

const handleEdit = () => {
  emit("edit", props.serie.id)
}
</script>

<template>  
  <div v-if="serie" class="category-card">

    <!-- HEADER -->
    <div class="card-header">
      <strong>{{ serie.name }}</strong>
      <button class="edit-btn" @click="handleEdit">✏️</button>
    </div>

    <span>Clasificació: {{ serie.classification }}</span>
    <span>id: {{ serie.id }}</span>

  </div>
</template>

<style scoped>
.category-card {
  position: relative; /* important per al botó absolut */
  background: white;
  border-radius: 12px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  padding: 15px 6px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 4px;
  width: 90%;
  margin: 0 auto;
  height: 70px;
  overflow: hidden;
  transition: transform 0.1s, box-shadow 0.1s;
}

.category-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(0,0,0,0.1);
}

/* Header per al títol i botó */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Botó edit taronja circular */
.edit-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  min-width: 32px;
  min-height: 32px;
  max-width: 32px;
  max-height: 32px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f39c12;
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  z-index: 10;
  font-size: 16px;
  transition: background 0.2s;
}

.edit-btn:hover {
  background: #e67e22;
}

/* Text compacte */
strong, span {
  font-size: 0.75rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-left: 15px;
}
</style>
