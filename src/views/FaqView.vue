<template>
    <div class="faq-page">
      <h1>Preguntas Frecuentes (FAQ)</h1>
      <!-- 
        Sección que recorre la lista de preguntas y respuestas (faqs) mediante v-for.
        'item' representa cada objeto con la pregunta y la respuesta.
        'index' es el índice del elemento en el array.
        :key="index" ayuda a Vue a identificar cada sección de forma única.
      -->
      <section
        v-for="(item, index) in faqs"
        :key="index"
        class="faq-item"
      >
        <!-- 
          Al dar clic sobre este encabezado (h2), se ejecuta la función toggleActive(index), 
          que controla qué pregunta se encuentra activa (o abierta).
        -->
        <h2 @click="toggleActive(index)">
          <!-- Este span representa la flecha "▶". Rotated gira la flecha.-->
          <span :class="{ 'rotated': activeIndex === index }">▶</span>
          {{ item.question }}
        </h2>
  
        <!-- Solo se muestra la respuesta si 'activeIndex === index', si esta pregunta está seleccionada como activa.-->
        <p v-if="activeIndex === index">
          {{ item.answer }}
        </p>
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  /**
   * Importamos 'ref' de 'vue' para crear variables reactivas 
   * que puedan ser detectadas por el sistema de reactividad de Vue.
   */
  import { ref } from 'vue'
  
  /**
   * Interfaz TypeScript que define la forma (tipo) de cada FAQ.
   * - question: el texto de la pregunta.
   * - answer: el texto de la respuesta correspondiente.
   */
  interface FaqItem {
    question: string
    answer: string
  }
  
  /*
   * faqs: un array reactivo (ref) de objetos que cumplen la interfaz FaqItem.
   * Aquí definimos la lista de preguntas y respuestas que se mostrarán en la página.
   */
  const faqs = ref<FaqItem[]>([
    {
      question: '¿Cómo me registro en el sistema del hospital?',
      answer: 'Para registrarte, visita la sección "Registro" en la página principal y completa el formulario.'
    },
    {
      question: '¿Cuáles son los horarios de atención?',
      answer: 'El hospital atiende consultas de lunes a viernes de 8:00 a 18:00 y sábados de 9:00 a 14:00.'
    },
    {
      question: '¿Se requiere cita previa para recibir atención?',
      answer: 'Para la mayoría de los servicios, sí. Te recomendamos agendar tu cita en línea o llamar al teléfono de contacto.'
    },
    {
      question: '¿Aceptan seguros médicos?',
      answer: 'Sí, trabajamos con la mayoría de las aseguradoras. Consulta en recepción o en la sección de convenios vigentes para más información.'
    }
  ])
  
  /**
   * activeIndex: variable reactiva que indica el índice 
   * de la pregunta actualmente seleccionada (o activa).
   * Si su valor es 'null', significa que no hay ninguna pregunta abierta.
   */
  const activeIndex = ref<number | null>(null)
  
  /**
   * toggleActive:
   * Función que recibe el índice de la pregunta clicada (index).
   * - Si ya está activa (activeIndex === index), se cierra poniendo null.
   * - Si es otra pregunta, se actualiza activeIndex con el nuevo índice 
   *   para mostrar la respuesta correspondiente.
   */
  function toggleActive(index: number) {
    if (activeIndex.value === index) {
      // Si la misma pregunta ya estaba activa, la cerramos.
      activeIndex.value = null
    } else {
      // Si era otra pregunta o no había ninguna activa, mostramos la nueva.
      activeIndex.value = index
    }
  }
  </script>
  
  <style scoped>

  .faq-page {
    max-width: 800px;
    margin: 0 auto;
    padding-top: 3rem;
  }

  h1 {
    margin-bottom: 2rem;
    text-align: center;
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
  </style>
  