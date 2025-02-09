import axios from "axios";
import { API_URL } from "@/config.js";

// 🔹 Registro de usuario
export const registerUser = async (nombreUsuario, correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/registro`, {
      nombreUsuario,
      correo,
      contrasena,
      rol: null, // Se registra como NULL si el rol no es obligatorio
    });
    return response.data;
  } catch (error) {
    console.error(" Error registrando usuario:", error.response?.data || error.message);
    throw error;
  }
};

// 🔹 Login de usuario
export const loginUser = async (correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/login`, { correo, contrasena });
    return response.data;
  } catch (error) {
    console.error(" Error iniciando sesión:", error.response?.data || error.message);
    throw error;
  }
};
