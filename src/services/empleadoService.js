import axios from "axios";

const API_URL = "http://localhost:8080/empleado";

export default {
  getAllEmpleados() {
    return axios.get(API_URL);
  },

  async registrarEmpleado(empleado) {
    return await axios.post(API_URL, {
      usuario: {
        nombreUsuario: empleado.nombreUsuario,
        correo: empleado.correo,
        contrasena: empleado.password,
        rol: { id: 2 } // ROL_ID = 2 para empleados
      },
      apellido: empleado.apellido,
      documento: empleado.documento,
      fechaNacimiento: empleado.fechaNacimiento,
      genero: empleado.genero,
      telefono: empleado.telefono,
      puesto: empleado.puesto,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  updateEmpleado(id, empleado) {
    return axios.put(`${API_URL}/${id}`, empleado);
  },

  deleteEmpleado(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
