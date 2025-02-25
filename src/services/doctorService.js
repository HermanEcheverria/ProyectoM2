import axios from "axios";

const API_URL = "http://localhost:8080/doctor";

export default {
  async getAllDoctors() {
    try {
      const response = await axios.get(API_URL);
      return response.data; // Extraer los datos correctamente
    } catch (error) {
      console.error("Error al obtener la lista de doctores:", error);
      throw error;
    }
  },

  async registrarDoctor(doctor) {
    return await axios.post(API_URL, {
      usuario: {
        nombreUsuario: doctor.nombreUsuario,
        correo: doctor.correo,
        contrasena: doctor.password,
        rol: { id: 2 }, // ROL_ID = 2 para Doctores
      },
      apellido: doctor.apellido,
      documento: doctor.documento,
      fechaNacimiento: doctor.fechaNacimiento,
      genero: doctor.genero,
      telefono: doctor.telefono,
      especialidad: doctor.especialidad,
      numeroColegiado: doctor.numeroColegiado,
      horarioAtencion: doctor.horarioAtencion,
      fechaGraduacion: doctor.fechaGraduacion,
      universidadGraduacion: doctor.universidadGraduacion,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  async updateDoctor(id, doctor) {
    return axios.put(`${API_URL}/${id}`, {
      usuario: {
        nombreUsuario: doctor.usuario.nombreUsuario,
        correo: doctor.usuario.correo,
        contrasena: doctor.usuario.contrasena,
        rol: { id: 2 }
      },
      apellido: doctor.apellido,
      documento: doctor.documento,
      fechaNacimiento: doctor.fechaNacimiento,
      genero: doctor.genero,
      telefono: doctor.telefono,
      especialidad: doctor.especialidad,
      numeroColegiado: doctor.numeroColegiado,
      horarioAtencion: doctor.horarioAtencion,
      fechaGraduacion: doctor.fechaGraduacion,
      universidadGraduacion: doctor.universidadGraduacion,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  async deleteDoctor(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
