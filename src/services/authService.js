import axios from "axios";
import { API_URL } from "@/config.js";
import emailjs from "emailjs-com";

// Configuración de EmailJS
const SERVICE_ID = "service_f70s6q3";
const TEMPLATE_ID = "template_tf3o0fd";
const PUBLIC_KEY = "SFAQ9kOAKVFMBgkSC";

//  Función para enviar el correo de confirmación
const sendWelcomeEmail = async (userEmail, userName) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de bienvenida enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo:", error);
  }
};

//  Registro de usuario
export const registerUser = async (nombreUsuario, correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/registro`, {
      nombreUsuario,
      correo,
      contrasena,
      rol: null, // Se registra como NULL hasta que un admin lo asigne
    });

    //  Enviar correo de bienvenida después del registro
    await sendWelcomeEmail(correo, nombreUsuario);

    return response.data;
  } catch (error) {
    console.error("Error registrando usuario:", error.response?.data || error.message);
    throw error;
  }
};

//  Inicio de sesión
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

//  Función para cerrar sesión
export const logoutUser = () => {
  localStorage.removeItem("userRole");
  localStorage.removeItem("userId");
};
