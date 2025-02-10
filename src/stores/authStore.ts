import { ref } from "vue";

export const userRole = ref<number | null>(null);
export const isLoggedIn = ref<boolean>(false);

// Función para establecer el usuario después del login
export const setUser = (roleId: number) => {
  userRole.value = roleId;
  isLoggedIn.value = true;
  localStorage.setItem("userRole", roleId.toString());
};

// 🔹 Función para cerrar sesión y redirigir a Home
export const logout = (router) => {
  userRole.value = null;
  isLoggedIn.value = false;
  localStorage.clear();  // 🔥 Borra todo en localStorage
  router.push("/"); // 🔄 Redirige al Home
};
