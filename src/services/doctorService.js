import axios from "axios";

const API_URL = "http://localhost:8080/doctor";

export default {
  getAllDoctors() {
    return axios.get(API_URL);
  },

  async registrarDoctor(doctor) {
    return await axios.post(API_URL, {
      usuario: {
        nombreUsuario: doctor.nombreUsuario,
        correo: doctor.correo,
        contrasena: doctor.password,
        rol: { id: 2 } // ROL_ID = 2 para Doctores
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

  updateDoctor(id, doctor) {
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

  deleteDoctor(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
