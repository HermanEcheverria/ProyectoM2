import axios from "axios";

const API_URL = "http://localhost:8080/usuariosinter";

export default {
  getUsuarioInterById(userId) {
    return axios.get(`${API_URL}/${userId}`);
  },

  updateUsuarioInter(userId, usuarioInterData) {
    return axios.put(`${API_URL}/${userId}`, usuarioInterData);
  }
};
