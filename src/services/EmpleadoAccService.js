import axios from "axios";

const API_URL = "http://localhost:8080/empleados";

export default {
  getEmpleadoById(userId) {
    return axios.get(`${API_URL}/${userId}`);
  },

  updateEmpleado(userId, empleadoData) {
    return axios.put(`${API_URL}/${userId}`, empleadoData);
  }
};
