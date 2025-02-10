import axios from "axios";
import { API_URL } from "@/config.js";

export const registerUser = async (nombreUsuario, correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/registro`, {
      nombreUsuario,
      correo,
      contrasena,
      rol: null, // Se registra como NULL hasta que un admin lo asigne
    });
    return response.data;
  } catch (error) {
    console.error("Error registrando usuario:", error.response?.data || error.message);
    throw error;
  }
};

export const loginUser = async (correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/login`, { correo, contrasena });

    const { id, rol } = response.data;

    localStorage.setItem("userRole", rol?.id || null);
    localStorage.setItem("userId", id);

    return response.data;
  } catch (error) {
    console.error("Error iniciando sesión:", error.response?.data || error.message);
    throw error;
  }
};

// Función para cerrar sesión
export const logoutUser = () => {
  localStorage.removeItem("userRole");
  localStorage.removeItem("userId");
};
