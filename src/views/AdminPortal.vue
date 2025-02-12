<template>
  <div class="admin-portal">
    <h1>Panel de Administración</h1>
    <p>Bienvenido al portal de gestión.</p>
    <!-- Se integra el componente para administrar usuarios inactivos -->
    <AdminUsuarios />
  </div>
</template>

<script lang="ts" setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import AdminUsuarios from "@/components/AdminUsuarios.vue"; // Asegúrate que la ruta es correcta

const router = useRouter();

// Validación de seguridad: redirige a Home si el usuario no es Admin (rol "1")
onMounted(() => {
  const userRole = localStorage.getItem("userRole");
  if (userRole !== "1") {
    router.push("/");
  }
});
</script>

<style scoped>
.admin-portal {
  padding: 20px;
}
</style>
