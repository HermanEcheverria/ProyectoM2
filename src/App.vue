<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import { userRole, isLoggedIn, logout } from "@/stores/authStore";
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
  <!-- Barra de navegación fija -->
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
        <RouterLink to="/">Sub Home 1</RouterLink>
        <RouterLink to="/">Sub Home 2</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/servicios-medicos">Servicios Médicos</RouterLink>
        <RouterLink to="/contact">Contact Us</RouterLink>
        <RouterLink to="/faq">FAQ</RouterLink>

        <!-- Mostrar "Gestionar" SOLO si el usuario es Admin -->
        <RouterLink v-if="userRole === 1" to="/admin-portal">Gestionar</RouterLink>

        <!-- Mostrar "Log in" solo si NO está autenticado -->
        <RouterLink v-if="!isLoggedIn" to="/login">Log in</RouterLink>

        <!-- Botón "Cerrar sesión" si está autenticado -->
        <button v-if="isLoggedIn" @click="logout(router)" class="logout-btn">
          Cerrar sesión
        </button>
      </nav>
    </div>
  </header>

  <!-- 🔹 Contenedor principal de la aplicación -->
  <main class="app-main">
    <!-- 🔥 Eliminamos cualquier otro contenedor restrictivo, dejando solo el RouterView -->
    <RouterView />
  </main>
</template>

<style scoped>
/* 🔹 Barra de navegación */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  z-index: 999;
}

/* 🔹 Contenedor de la navbar */
.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem 1rem;
}

/* 🔹 Logo y título */
.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.brand .logo {
  display: block;
}

/* 🔹 Enlaces de navegación */
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

/* 🔹 Botón de logout */
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

/* 🔹 Contenedor principal del contenido */
/* 🔹 Ajustar el contenedor principal */
.app-main {
  margin-top: 80px; /* Espacio para la navbar fija */
  min-height: calc(100vh - 80px);
  background-color: #181818;
  padding: 2rem;
  width: 100%;
  display: flex;
  justify-content: center; /* Centra el contenido horizontalmente */
  align-items: flex-start; /* Asegura que el contenido no quede pegado arriba */
}

</style>
