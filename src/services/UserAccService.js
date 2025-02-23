import axios from "axios";

const API_URL = "http://localhost:8080/usuarios";

export default {
  getUserById(userId) {
    return axios.get(`${API_URL}/${userId}`);
  },

  updateUser(userId, userData) {
    return axios.put(`${API_URL}/${userId}`, userData);
  }
};
