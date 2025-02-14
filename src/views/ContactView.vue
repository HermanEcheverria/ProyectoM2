<script lang="js" setup>
import { ref } from "vue";
import { enviarPregunta } from "@/services/faqService.js";

// ✅ Variables reactivas
const nombre = ref("");
const correo = ref("");
const mensaje = ref("");
const mensajeExito = ref("");

// ✅ Función para enviar el formulario
const enviarFormulario = async () => {
  if (!nombre.value.trim() || !correo.value.trim() || !mensaje.value.trim()) {
    mensajeExito.value = "⚠ Todos los campos son obligatorios.";
    return;
  }

  try {
    await enviarPregunta(nombre.value, correo.value, mensaje.value);
    mensajeExito.value = "✅ ¡Pregunta enviada con éxito!";

    // Limpiar los campos después de enviar
    nombre.value = "";
    correo.value = "";
    mensaje.value = "";
  } catch (error) {
    console.error("❌ Error al enviar la pregunta:", error);
    mensajeExito.value = "❌ Hubo un error al enviar tu pregunta. Intenta nuevamente.";
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

    <p v-if="mensajeExito" class="mensaje">{{ mensajeExito }}</p>
  </div>
</template>

<style scoped>
.contact-page {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;
}

.form-group {
  margin-bottom: 1.25rem;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

input, textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
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

button:hover {
  background-color: #0056b3;
}

.mensaje {
  margin-top: 1rem;
  font-weight: bold;
}
</style>
