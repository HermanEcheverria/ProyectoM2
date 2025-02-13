<template>
  <div>
    <h1>Panel de Administración</h1>
    <p>Bienvenido al portal de gestión.</p>

    <!-- Botón para acceder a la gestión de FAQ -->
    <button @click="irAGestionFaq" class="faq-button">❓ Gestionar Preguntas Frecuentes</button>

    <!-- Vista dinámica según la ruta -->
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// Seguridad extra: si el usuario no es Admin, redirigir a Home
onMounted(() => {
  const userRole = localStorage.getItem("userRole");
  if (userRole !== "1") {
    router.push("/");
  }
});

// Redirigir a la página de gestión de FAQ
const irAGestionFaq = () => {
  router.push("/admin/faq");
};
</script>

<style scoped>
.faq-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 15px;
}

.faq-button:hover {
  background-color: #0056b3;
}
</style>


