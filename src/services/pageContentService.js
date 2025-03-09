import axios from "axios";
import API_URL from "@/config";

// 🔹 Obtener contenido publicado por nombre de página
export const getPublishedContent = async (pageName) => {
  try {
    const response = await axios.get(`${API_URL}/api/page-content/${pageName}`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener contenido publicado:", error);
    throw error;
  }
};

// 🔹 Obtener contenido en estado "PROCESO" (borradores)
export const getDraftContent = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/page-content/drafts`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener borradores:", error);
    throw error;
  }
};

// 🔹 Crear nuevo contenido
export const createContent = async (contentData) => {
  try {
    const response = await axios.post(`${API_URL}/api/page-content`, contentData);
    return response.data;
  } catch (error) {
    console.error("Error al crear contenido:", error);
    throw error;
  }
};

// 🔹 Actualizar contenido existente por ID
export const updateContent = async (contentId, contentData) => {
  try {
    const response = await axios.put(`${API_URL}/api/page-content/${contentId}`, contentData);
    return response.data;
  } catch (error) {
    console.error("Error al actualizar contenido:", error);
    throw error;
  }
};

// 🔹 Publicar contenido (cambiar estado a PUBLICADO)
export const publishContent = async (contentId) => {
  try {
    const response = await axios.put(`${API_URL}/api/page-content/${contentId}/publish`);
    return response.data;
  } catch (error) {
    console.error("Error al publicar contenido:", error);
    throw error;
  }
};

// 🔹 Eliminar contenido por ID
export const deleteContent = async (contentId) => {
  try {
    await axios.delete(`${API_URL}/api/page-content/${contentId}`);
  } catch (error) {
    console.error("Error al eliminar contenido:", error);
    throw error;
  }
};
