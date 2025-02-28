import axios from "axios";

const API_URL = "http://localhost:8080/api/doctor-images";

export default {
  async getDoctorImage(idDoctor) {
    return await axios.get(`${API_URL}/${idDoctor}`);
  },

  async updateDoctorImage(idDoctor, file, fileTitle) {
    const formData = new FormData();
    formData.append("fotografia", file);
    formData.append("fotoTitulo", fileTitle);

    return await axios.put(`${API_URL}/${idDoctor}/update`, formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });
  },

  async deleteDoctorImage(idDoctor) {
    return await axios.delete(`${API_URL}/${idDoctor}/delete`);
  }
};
