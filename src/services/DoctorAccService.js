import axios from "axios";

const API_URL = "http://localhost:8080/doctores";

export default {
  getDoctorById(userId) {
    return axios.get(`${API_URL}/${userId}`);
  },

  updateDoctor(userId, doctorData) {
    return axios.put(`${API_URL}/${userId}`, doctorData);
  }
};
