<script setup lang="js">
import { ref, onMounted } from "vue";
import { obtenerPreguntas } from "@/services/faqService.js";


// Variables reactivas
const preguntas = ref([]);
const activeIndex = ref(null);
const errorCargando = ref(false);

// Función para cargar preguntas desde la API
const cargarPreguntas = async () => {
  try {
    errorCargando.value = false;
    const data = await obtenerPreguntas();
    if (Array.isArray(data)) {
      preguntas.value = data;
    } else {
      console.error("🚨 Error: La API no devolvió un array de preguntas.");
      errorCargando.value = true;
    }
  } catch (error) {
    console.error("🚨 Error cargando preguntas:", error);
    errorCargando.value = true;
  }
};

// Alternar visibilidad de respuestas
const toggleActive = (index) => {
  activeIndex.value = activeIndex.value === index ? null : index;
};

// Cargar preguntas al montar el componente
onMounted(() => {
  cargarPreguntas();
});
</script>

<template>
  <div class="faq-page">
    <div class="header">Preguntas Frecuentes (FAQ)</div>

    <div class="section">
    <!-- Mostrar mensaje si hay error al cargar preguntas -->
    <p v-if="errorCargando" class="error-message">
      ⚠ No se pudieron cargar las preguntas. Intenta de nuevo más tarde.
    </p>

    <!-- Sección dinámica con preguntas -->
    <section v-for="(pregunta, index) in preguntas" :key="pregunta.id" class="faq-item">
      <h2 @click="toggleActive(index)">
        <span :class="{ 'rotated': activeIndex === index }">▶</span>
        {{ pregunta.pregunta }}
      </h2>

      <p v-if="activeIndex === index">
        {{ pregunta.respuesta?.trim() || "🔹 Esta pregunta aún no tiene respuesta." }}
      </p>
    </section>
  </div>
  </div>
</template>

<style scoped>
.faq-page {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
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


.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}


.faq-item {
  margin-bottom: 1rem;
  border-bottom: 1px solid #ccc;
  padding-bottom: 1rem;
}

.faq-item h2 {
  cursor: pointer;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  user-select: none;
}

.faq-item h2 span {
  display: inline-block;
  margin-right: 0.5rem;
  transition: transform 0.2s ease;
}

.faq-item h2 span.rotated {
  transform: rotate(90deg);
}

.faq-item p {
  margin-top: 0.5rem;
  line-height: 1.4;
}

.error-message {
  color: red;
  text-align: center;
  font-size: 1.2rem;
  font-weight: bold;
}
</style>
