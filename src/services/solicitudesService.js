import axios from "axios";
import API_URL from "../config"; // Importa tu API dinámico centralizado

// Función para enviar solicitud hospitalaria a la aseguradora
export const enviarSolicitudHospital = async ({ afiliado, servicio, monto, hospital, aseguradoraId }) => {
  try {
    const payload = {
      afiliado,
      servicio,
      hospital,
      monto,
      aseguradora: aseguradoraId
    };

    //  Se usa axios, no fetch
    const response = await axios.post(`${API_URL}/solicitudes-atencion`, payload);

    return response.data;

  } catch (error) {
    console.error("Error en enviarSolicitudHospital:", error.response?.data || error.message);
    throw error;
  }
};
