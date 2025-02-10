<template>
  <div>
    <h1>Panel de Administración</h1>
    <p>Bienvenido al portal de gestión.</p>
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
</script>
