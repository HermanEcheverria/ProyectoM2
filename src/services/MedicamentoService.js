import axios from "axios";

const API_URL = "http://localhost:8080/medicamentos";

export default {
  listarTodos() {
    return axios.get(API_URL);
  }
};
