import axios from "axios";
import  API_URL from "@/config.js";

export interface Pregunta {
  id: number;
  pregunta: string;
  respuesta?: string;
  autor?: string;
  editadoPor?: string;
}

// ✅ Crear una nueva pregunta
export const enviarPregunta = async (pregunta: string, autor: string): Promise<Pregunta> => {
  try {
    const response = await axios.post(`${API_URL}/faq/crear`, { pregunta, autor, respuesta: null });
    return response.data;
  } catch (error) {
    console.error("Error al enviar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Obtener todas las preguntas y respuestas
export const obtenerPreguntas = async (): Promise<Pregunta[]> => {
  try {
    const response = await axios.get(`${API_URL}/faq`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener preguntas:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Responder una pregunta (para administradores)
export const responderPregunta = async (id: number, respuesta: string): Promise<void> => {
  try {
    await axios.put(`${API_URL}/faq/responder/${id}`, { respuesta });
  } catch (error) {
    console.error("Error al responder la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Editar una pregunta
export const editarPregunta = async (id: number, pregunta: string, respuesta: string, editadoPor: string): Promise<Pregunta> => {
  try {
    const response = await axios.put(`${API_URL}/faq/editar/${id}`, { pregunta, respuesta, editadoPor });
    return response.data;
  } catch (error) {
    console.error("Error al editar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Eliminar una pregunta
export const eliminarPregunta = async (id: number): Promise<void> => {
  try {
    await axios.delete(`${API_URL}/faq/eliminar/${id}`);
  } catch (error) {
    console.error("Error al eliminar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};
