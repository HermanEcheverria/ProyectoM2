<template>
  <div class="page-container">
        <div class="section">
    <div v-for="section in pageContent" :key="section.idContent" class="section">
    <div class="header">{{ section.contentTitle }}</div>
      <div v-html="section.contentBody"></div>

      <!-- ✅ Mostrar la imagen correctamente -->
      <img v-if="section.image" :src="`data:image/jpeg;base64,${section.image}`" class="page-image"/>
    </div>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getPublishedContent } from '../services/pageContentService';

const route = useRoute();
const pageName = ref(route.params.pageName);
const pageContent = ref([]);

const loadContent = async () => {
  try {
    const data = await getPublishedContent(pageName.value);

    // ✅ Convertir imagen BLOB en Base64 al formato correcto
    pageContent.value = data.map(section => ({
      ...section,
      image: section.image ? `data:image/jpeg;base64,${section.image}` : null
    }));

  } catch (error) {
    console.error("Error cargando contenido de página:", error);
  }
};

onMounted(loadContent);
watch(() => route.params.pageName, (newName) => {
  pageName.value = newName;
  loadContent();
});
</script>

<style scoped>
.page-image {
  max-width: 100%;
  height: auto;
  margin-top: 10px;
}
.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.page-container {
  background: #f9f9f9;
  color: #e0e1dd;          /* Texto verde/menta */
  min-height: 100vh;
  padding: 2rem;
  border-radius: 10px;
  max-width: 900px;
  margin: 0 auto;
}
.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}
</style>

