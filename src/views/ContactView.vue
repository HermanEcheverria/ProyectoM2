<template>
    <div class="contact-page">
      <h1>Contáctanos</h1>
      <p>
        ¿Tienes preguntas o necesitas asistencia?  
        Completa el formulario y te responderemos a la brevedad.
      </p>
      
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="name">Nombre:</label>
          <input id="name" v-model="name" required />
        </div>
  
        <div class="form-group">
          <label for="email">Correo electrónico:</label>
          <input id="email" type="email" v-model="email" required />
        </div>
  
        <div class="form-group">
          <label for="message">Mensaje:</label>
          <textarea id="message" v-model="message" required></textarea>
        </div>
  
        <button type="submit">Enviar</button>
      </form>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue'
  
  const name = ref('')
  const email = ref('')
  const message = ref('')
  
  function submitForm() {
    // Ejemplo de manejo de datos: (por ahora solo imprimir en consola y limpiar)
    console.log(`Nombre: ${name.value}`)
    console.log(`Email: ${email.value}`)
    console.log(`Mensaje: ${message.value}`)
  
    alert('¡Gracias por tu mensaje! Pronto nos pondremos en contacto.')
    
    // Limpieza de campos
    name.value = ''
    email.value = ''
    message.value = ''
  }
  </script>
  
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
  }
  </style>
  