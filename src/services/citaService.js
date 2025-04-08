import axios from "axios";
import API_URL from "@/config";

const CITA_API = `${API_URL}/citas`;

const citaService = {
  async obtenerCitas() {
    try {
      const response = await axios.get(CITA_API);
      return response.data;
    } catch (error) {
      console.error("Error al obtener citas:", error);
      throw error;
    }
  },
  async agendarCita(cita) {
    try {
      console.log("📌 Enviando cita al backend:", JSON.stringify(cita, null, 2)); // 🔍 DEBUG
      await axios.post(CITA_API, cita);
    } catch (error) {
      console.error("❌ Error al agendar cita:", error);
      throw error;
    }
  }
  ,
  async cancelarCita(id) {
    try {
      await axios.delete(`${CITA_API}/${id}`);
    } catch (error) {
      console.error("Error al cancelar cita:", error);
      throw error;
    }
  },

};

export default citaService;
