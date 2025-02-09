<script setup>
import { ref } from "vue";
import { loginUser } from "@/services/authService.js";
import { useRouter } from "vue-router";

const router = useRouter();
const correo = ref("");
const contrasena = ref("");
const mensaje = ref("");

const login = async () => {
  try {
    const user = await loginUser({
      correo: correo.value,
      contrasena: contrasena.value,
    });
    localStorage.setItem("user", JSON.stringify(user));
    mensaje.value = "Inicio de sesión exitoso. Redirigiendo...";
    setTimeout(() => {
      router.push("/");
    }, 2000);
  } catch (error) {
    mensaje.value = "Credenciales incorrectas.";
  }
};
</script>

<template>
  <form @submit.prevent="login">
    <h2>Iniciar Sesión</h2>
    <input v-model="correo" type="email" placeholder="Correo" required />
    <input v-model="contrasena" type="password" placeholder="Contraseña" required />
    <button type="submit">Ingresar</button>

    <p v-if="mensaje">{{ mensaje }}</p>

    <p>
      ¿No tienes cuenta?
      <RouterLink to="/signup">Regístrate aquí</RouterLink>
    </p>
  </form>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  text-align: center;
}

input, button {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  background-color: #007BFF;
  color: white;
  cursor: pointer;
}
</style>
