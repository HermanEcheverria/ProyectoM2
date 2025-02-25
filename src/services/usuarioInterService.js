import axios from "axios";

const API_URL = "http://localhost:8080/usuariointer";

export default {
  obtenerUsuarioInter() {
    return axios.get(API_URL);
  },

  async registrarUsuarioInter(usuarioInter) {
    return await axios.post(API_URL, {
      usuario: {
        nombreUsuario: usuarioInter.nombreUsuario,
        correo: usuarioInter.correo,
        contrasena: usuarioInter.password,
        rol: { id: 5 }
      },
      apellido: usuarioInter.apellido,
      documento: usuarioInter.documento,
      fechaNacimiento: usuarioInter.fechaNacimiento,
      genero: usuarioInter.genero,
      telefono: usuarioInter.telefono,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },



  actualizarUsuario(id, usuarioInter) {
    return axios.put(`${API_URL}/${id}`, usuarioInter);
  },

  eliminarUsuario(id) {
    return axios.delete(`${API_URL}/${id}`);  // Asegúrate de que el ID se está pasando correctamente aquí
  },


};






