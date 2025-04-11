import axios from "axios";

const API_URL = "http://localhost:5001/api/clientes";

/**
 * Buscar un cliente por su número de documento (DPI) y aseguradora.
 * @param {string} documento - El número de DPI del cliente
 * @param {string} aseguradoraId - El ID de la aseguradora
 * @returns {Promise<Object>} Cliente con historial de servicios
 */
export const buscarClientePorDpiYAseguradora = async (documento, aseguradoraId) => {
  try {
    const response = await axios.get(`${API_URL}/historial/buscar`, {
      params: {
        documento,
        aseguradoraId,
      },
    });

    if (response.status === 200) {
      return response.data.cliente; // 👈 solo retornamos el cliente
    } else {
      throw new Error("Cliente no encontrado");
    }
  } catch (error) {
    console.error("❌ Error al buscar cliente:", error.response?.data || error.message);
    throw error;
  }
};
