// src/services/historialService.js
import axios from "axios";

const ASEGURADORA_API = `${window.location.protocol}//${window.location.hostname}:5001/api`;

export const enviarHistorialAseguradora = async (clienteId, data) => {
  try {
    const response = await axios.post(`${ASEGURADORA_API}/clientes/${clienteId}/historial`, data);
    return response.data;
  } catch (error) {
    console.error(" Error al enviar historial a la aseguradora:", error);
    throw error;
  }
};
