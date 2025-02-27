import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

export default {
  async getCatalogoDoctores() {
    try {
      // Obtener lista de doctores
      const doctorResponse = await axios.get(`${API_BASE_URL}/doctor`);
      const doctorList = doctorResponse.data;

      // Obtener nombres de usuario para cada doctor
      const promises = doctorList.map(async (doctor) => {
        try {
          const userResponse = await axios.get(`${API_BASE_URL}/usuarios/${doctor.idUsuario}`);
          return {
            ...doctor,
            nombreUsuario: userResponse.data.nombreUsuario || "Sin Nombre"
          };
        } catch (error) {
          console.error(`Error obteniendo usuario para ID ${doctor.idUsuario}:`, error);
          return { ...doctor, nombreUsuario: "Desconocido" };
        }
      });

      return await Promise.all(promises);
    } catch (error) {
      console.error("Error obteniendo el catálogo de doctores:", error);
      return [];
    }
  }
};
