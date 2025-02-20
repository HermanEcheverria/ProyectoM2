import axios from "axios";
import API_URL from "@/config";
import emailjs from "emailjs-com";

// Configuración de EmailJS
const SERVICE_ID = "service_f70s6q3";
const TEMPLATE_SIGNUP_ID = "template_tf3o0fd";  // Template de registro
const TEMPLATE_ACTIVATION_ID = "template_5cq4vng"; // Template de activación
const PUBLIC_KEY = "SFAQ9kOAKVFMBgkSC";

// 🔹 Función para enviar el correo de confirmación de registro
const sendWelcomeEmail = async (userEmail, userName) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_SIGNUP_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de bienvenida enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo de bienvenida:", error);
  }
};

// Función para enviar el correo de activación
const sendActivationEmail = async (userEmail, userName, userRole) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
      message: `Your account has been created and activated successfully. Your assigned role is ${userRole}`,
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_ACTIVATION_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de activación enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo de activación:", error);
  }
};

// 🔹 Registro de usuario
export const registerUser = async (nombreUsuario, correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/registro`, {
      nombreUsuario,
      correo,
      contrasena,
      rol: null, // Se registra como NULL hasta que un admin lo asigne
    });

    if (response.data) {
      // Enviar correo de bienvenida solo si el registro fue exitoso
      await sendWelcomeEmail(correo, nombreUsuario);
    }

    return response.data;
  } catch (error) {
    console.error("Error registrando usuario:", error.response?.data || error.message);
    throw error;
  }
};

// 🔹 Función para activar un usuario y enviar correo de activación
export const activateUser = async (usuarioId, userEmail, userName, userRole) => {
  try {
    await axios.put(`${API_URL}/usuarios/${usuarioId}/activar`, { rol: userRole });

    // Enviar correo de activación después de activar la cuenta
    await sendActivationEmail(userEmail, userName, userRole);
  } catch (error) {
    console.error("Error activando usuario:", error.response?.data || error.message);
    throw error;
  }
};

// 🔹 Inicio de sesión
export const loginUser = async (correo, contrasena) => {
  try {
    const response = await axios.post(`${API_URL}/usuarios/login`, { correo, contrasena });

    const { id, rol, estado } = response.data;

    if (estado === 0) {
      throw new Error("Tu cuenta está inactiva. Espera a que un administrador la active.");
    }

    localStorage.setItem("userRole", rol?.id || null);
    localStorage.setItem("userId", id);

    return response.data;
  } catch (error) {
    console.error("Error iniciando sesión:", error.response?.data || error.message);
    throw error;
  }
};

// 🔹 Función para cerrar sesión
export const logoutUser = () => {
  localStorage.removeItem("userRole");
  localStorage.removeItem("userId");
};
