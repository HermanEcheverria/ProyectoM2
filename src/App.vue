<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import { userRole, isLoggedIn, logout } from "@/stores/authStore"; // Estado global
import { onMounted } from "vue";

const router = useRouter();

onMounted(() => {
  const storedRole = localStorage.getItem("userRole");
  if (storedRole) {
    userRole.value = parseInt(storedRole);
    isLoggedIn.value = true;
  }
});
</script>

<template>
  <header class="navbar">
    <div class="navbar-container">
      <div class="brand">
        <img
          alt="Vue logo"
          class="logo"
          src="@/assets/logo.svg"
          width="50"
          height="50"
        />
        <h1>Hospitals</h1>
      </div>

      <nav class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/contact">Contact Us</RouterLink>
        <RouterLink to="/faq">FAQ</RouterLink>

        <!-- 🔹 Mostrar "Gestionar" SOLO si el usuario es Admin (rol_id === 1) -->
        <RouterLink v-if="userRole === 1" to="/admin-portal">Gestionar</RouterLink>

        <!-- 🔹 Mostrar "Log in" solo si NO está autenticado -->
        <RouterLink v-if="!isLoggedIn" to="/login">Log in</RouterLink>

        <!-- 🔹 Botón "Cerrar sesión" si está autenticado -->
        <button v-if="isLoggedIn" @click="logout(router)" class="logout-btn">Cerrar sesión</button>
      </nav>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
</template>


<style scoped>

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  z-index: 999;
}

/* centrar contenido */
.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem 1rem;
}

/* Sección para el logo y texto */
.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.brand .logo {
  display: block;
}

/* Estilos del nav */
.nav-links {
  display: flex;
  align-items: center;
}

.nav-links a {
  margin-left: 1rem;
  text-decoration: none;
  color: #333;
  font-weight: 500;
}

.logout-btn {
  margin-left: 1rem;
  background-color: #d9534f;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 5px;
}

.logout-btn:hover {
  background-color: #c9302c;
}

main {
  margin-top: 80px;
  padding: 1rem;
}
</style>
