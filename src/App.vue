<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import { userRole, isLoggedIn, logout } from "@/stores/authStore";
import { onMounted } from "vue";

const router = useRouter();

// Recuperar datos del usuario en el inicio
onMounted(() => {
  const storedRole = localStorage.getItem("userRole");
  const storedUser = localStorage.getItem("userId");

  if (storedRole && storedUser) {
    userRole.value = parseInt(storedRole);
    isLoggedIn.value = true;
  }
});

// Determinar ruta de "Mi Cuenta" según el rol del usuario
const myAccountRoute = () => {
  switch (userRole.value) {
    case 1:
      return "/my-account-admin";
    case 2:
      return "/my-account-doctor";
    case 3:
      return "/my-account-empleado";
    case 4:
      return "/my-account-paciente";
    default:
      return "/login"; // Redirigir al login si no hay rol asignado
  }
};
</script>

<template>
  <!--  Barra de navegación fija -->
  <header class="navbar">
    <div class="navbar-container">
      <div class="brand">
        <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="50" height="50" />
        <h1>Hospitals</h1>
      </div>

      <nav class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>

        <RouterLink to="/contact">Contact Us</RouterLink>
        <RouterLink to="/faq">FAQ</RouterLink>

        <!--  Mostrar "Mi Cuenta" solo si el usuario está autenticado -->
        <RouterLink v-if="isLoggedIn" :to="myAccountRoute()">Mi Cuenta</RouterLink>

        <!--  Mostrar "Gestionar" solo si el usuario es Admin -->
        <RouterLink v-if="userRole === 1" to="/admin-portal">Gestionar</RouterLink>
        <RouterLink v-if="userRole === 1" to="/servicios-medicos">Servicios Médicos</RouterLink>

        <!--  Mostrar "Log in" solo si NO está autenticado -->
        <RouterLink v-if="!isLoggedIn" to="/login">Log in</RouterLink>

        <!--  Botón "Cerrar sesión" si está autenticado -->
        <button v-if="isLoggedIn" @click="logout(router)" class="logout-btn">Cerrar sesión</button>
      </nav>
    </div>
  </header>

  <!--  Contenedor principal de la aplicación -->
  <main class="app-main">
    <RouterView />
  </main>

  <!--  Footer -->
  <footer class="footer">
    <div class="footer-container">
      <p>&copy; {{ new Date().getFullYear() }} Hospitals. Todos los derechos reservados.</p>
    </div>
  </footer>
</template>

<style scoped>
/* Barra de navegación */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  z-index: 999;
}

/*  Contenedor de la navbar */
.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem 1rem;
}

/*  Logo y título */
.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.brand .logo {
  display: block;
}

/*  Enlaces de navegación */
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

/*  Botón de logout */
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

/*  Contenedor principal del contenido */
.app-main {
  margin-top: 80px; /* Espacio para la navbar fija */
  min-height: calc(100vh - 140px); /* Ajustado para dejar espacio al footer */
  background-color: #181818;
  padding: 2rem;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

/* Footer */
.footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 1rem 0;
  width: 100%;
}

.footer-container {
  max-width: 1200px;
  margin: auto;
}
</style>
