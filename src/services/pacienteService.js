import axios from "axios";

const API_URL = "http://localhost:8080/paciente";

export default {
  getAllPaciente() {
    return axios.get(API_URL);
  },

  async registrarPaciente(paciente) {
    return await axios.post(API_URL, {
      usuario: {
        nombreUsuario: paciente.nombreUsuario,
        correo: paciente.correo,
        contrasena: paciente.password,
        rol: { id: 4 }
      },
      apellido: paciente.apellido,
      documento: paciente.documento,
      fechaNacimiento: paciente.fechaNacimiento,
      genero: paciente.genero,
      telefono: paciente.telefono,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  updatePaciente(id, paciente) {
    return axios.put(`${API_URL}/${id}`, paciente);
  },

  deletePaciente(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
