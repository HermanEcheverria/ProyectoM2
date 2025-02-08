<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router';
</script>

<template>
  <!-- Navbar fijo -->
  <header class="navbar">
    <div class="navbar-container">
      <!-- Logo y nombre -->
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

      <!-- rutas a archivos -->
      <nav class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/contact">Contact Us</RouterLink>
        <RouterLink to="/faq">FAQ</RouterLink>

        <!-- 🔹 Se mantiene solo el botón de Login en el navbar -->
        <RouterLink to="/login">Log in</RouterLink>
      </nav>
    </div>
  </header>

  <!-- Contenido principal -->
  <main>
    <RouterView />
  </main>
</template>

<style scoped>
/* css navbar */
.navbar {
  position: fixed;       /* Se queda "pegada" al top */
  top: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  z-index: 999;         /* Asegura que quede encima de otros elementos */
}

/* centrar contenido */
.navbar-container {
  max-width: 1200px;    /* Anchura máxima deseada (ajústalo a tu gusto) */
  margin: 0 auto;       /* Centra horizontalmente */
  display: flex;
  align-items: center;  /* Centra verticalmente */
  justify-content: space-between; /* Espacio entre logo y menú */
  padding: 0.5rem 1rem; /* Espacio interno del navbar */
}

/* Sección para el logo y texto */
.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem; /* Espacio entre logo y título */
}

.brand .logo {
  display: block;
}

/* Estilos del nav */
.nav-links a {
  margin-left: 1rem;
  text-decoration: none;
  color: #333;
  font-weight: 500;
}

main {
  margin-top: 80px; /* Ajustar según la altura del navbar */
  padding: 1rem;
}
</style>
