<template>
  <div class="admin-portal">
    <h1>Panel de Administración</h1>
    <p>Bienvenido al portal de gestión.</p>

    <!-- Menú de navegación con botones -->
    <div class="gestion-menu">
      <button @click="router.push('/admin-usuarios')" class="gestion-button">Usuarios</button>
      <button @click="router.push('/admin/doctores-recetas')" class="gestion-button">Recetas médicas</button>
      <button @click="router.push('/admin/doctores-citas')" class="gestion-button">Citas médicas</button>
      <button @click="router.push('/admin/doctores-agenda')" class="gestion-button">Agenda médica</button>
      <button @click="router.push('/admin/ficha-tecnica')" class="gestion-button">Ficha técnica</button>
      <button @click="router.push('/admin/faq')" class="faq-button">FAQ</button>
      <button @click="router.push('/admin/historia')" class="gestion-button">Historia</button>

    </div>

    <!-- Vista dinámica según la ruta -->
    <router-view />

    <!-- Mostrar el formulario de AdminUsuarios si la ruta es /admin-usuarios -->
    <AdminUsuarios v-if="route.path === '/admin-usuarios'" />
  </div>
</template>

<script lang="ts" setup>
import { onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import AdminUsuarios from "@/components/AdminUsuarios.vue";

const router = useRouter();
const route = useRoute();

// Validación de seguridad: redirige a Home si el usuario no es Admin (rol "1")
onMounted(() => {
  const userRole = localStorage.getItem("userRole");
  if (userRole !== "1") {
    router.push("/");
  }
});
</script>

<style scoped>
.faq-button, .gestion-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  display: block;
  width: 100%;
  text-align: center;
}

.faq-button:hover, .gestion-button:hover {
  background-color: #0056b3;
}

.admin-portal {
  padding: 20px;
}

.gestion-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 300px;
  margin-bottom: 20px;
}
</style>
