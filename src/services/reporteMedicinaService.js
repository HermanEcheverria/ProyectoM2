import axios from 'axios';

const API_URL = 'http://localhost:8080';

export async function obtenerReporteMedicinas(fechaInicio, fechaFin, limite = 10) {
  try {
    const response = await axios.get(`${API_URL}/reporte-medicinas`, {
      params: {
        inicio: fechaInicio,
        fin: fechaFin,
        limite: limite
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error obteniendo reporte:', error);
    throw error;
  }
}
