import axios from "axios";

const API_URL = "http://localhost:8080/pacientes";

export default {
  getPacienteById(userId) {
    return axios.get(`${API_URL}/${userId}`);
  },

  updatePaciente(userId, pacienteData) {
    return axios.put(`${API_URL}/${userId}`, pacienteData);
  }
};
