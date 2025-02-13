<script setup>
import { ref } from "vue";
import { enviarPregunta } from "@/services/faqService";

// Definir variables reactivas
const nombre = ref("");
const correo = ref("");
const mensaje = ref("");
const mensajeExito = ref("");

// Función para enviar el formulario
const enviarFormulario = async () => {
  try {
    await enviarPregunta(nombre.value, correo.value, mensaje.value);
    mensajeExito.value = "¡Pregunta enviada con éxito!";

    // Limpiar los campos después de enviar
    nombre.value = "";
    correo.value = "";
    mensaje.value = "";
  } catch (error) {
    mensajeExito.value = "Error enviando la pregunta.";
  }
};
</script>

<template>
  <div class="contact-page">
    <h2>Contáctanos</h2>
    <p>¿Tienes preguntas? Completa el formulario y te responderemos.</p>

    <div class="form-group">
      <label for="nombre">Nombre</label>
      <input v-model="nombre" placeholder="Nombre" required />
    </div>

    <div class="form-group">
      <label for="correo">Correo electrónico</label>
      <input v-model="correo" type="email" placeholder="Correo electrónico" required />
    </div>

    <div class="form-group">
      <label for="mensaje">Mensaje</label>
      <textarea v-model="mensaje" placeholder="Mensaje" required></textarea>
    </div>

    <button @click="enviarFormulario">Enviar</button>

    <p v-if="mensajeExito">{{ mensajeExito }}</p>
  </div>
</template>

<style scoped>
.contact-page {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 1.25rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

input, textarea {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
}

button {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
}
</style>
