<template>
  <div class="doctor-container">
    <h1>Gestión de Doctores</h1>

    <!-- Botón para agregar un doctor -->
    <button class="add-button" @click="abrirFormulario">Agregar Doctor</button>

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Documento</th>
          <th>Fecha Nacimiento</th>
          <th>Género</th>
          <th>Teléfono</th>
          <th>Especialidad</th>
          <th>Número Colegiado</th>
          <th>Horario Atención</th>
          <th>Fecha Graduación</th>
          <th>Universidad</th>
          <th>Correo</th>
          <th>Contraseña</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="doctor in doctorList" :key="doctor.idDoctor">
          <td>{{ doctor.idDoctor }}</td>
          <td><input v-if="doctor.editando" v-model="doctor.usuario.nombreUsuario" /> <span v-else>{{ doctor.usuario.nombreUsuario }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.apellido" /> <span v-else>{{ doctor.apellido }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.documento" /> <span v-else>{{ doctor.documento }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.fechaNacimiento" type="date" /> <span v-else>{{ doctor.fechaNacimiento }}</span></td>
          <td>
            <select v-if="doctor.editando" v-model="doctor.genero">
              <option value="Masculino">Masculino</option>
              <option value="Femenino">Femenino</option>
            </select>
            <span v-else>{{ doctor.genero }}</span>
          </td>
          <td><input v-if="doctor.editando" v-model="doctor.telefono" /> <span v-else>{{ doctor.telefono }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.especialidad" /> <span v-else>{{ doctor.especialidad }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.numeroColegiado" /> <span v-else>{{ doctor.numeroColegiado }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.horarioAtencion" /> <span v-else>{{ doctor.horarioAtencion }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.fechaGraduacion" type="date" /> <span v-else>{{ doctor.fechaGraduacion }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.universidadGraduacion" /> <span v-else>{{ doctor.universidadGraduacion }}</span></td>
          <td><input v-if="doctor.editando" v-model="doctor.usuario.correo" /> <span v-else>{{ doctor.usuario.correo }}</span></td>
          <td><input v-if="doctor.editando" type="password" v-model="doctor.usuario.contrasena" /> <span v-else>********</span></td>
          <td>
            <button v-if="!doctor.editando" @click="doctor.editando = true">Editar</button>
            <button v-if="doctor.editando" @click="updateDoctor(doctor)">Guardar</button>
            <button @click="deleteDoctor(doctor.idDoctor)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal para Agregar Doctor -->
    <div v-if="mostrarFormulario" class="modal-overlay">
      <div class="modal-content">
        <h2>Agregar Nuevo Doctor</h2>
        <input v-model="nuevoDoctor.nombreUsuario" placeholder="Nombre" />
        <input v-model="nuevoDoctor.apellido" placeholder="Apellido" />
        <input v-model="nuevoDoctor.documento" placeholder="Documento" />
        <input v-model="nuevoDoctor.fechaNacimiento" type="date" />
        <select v-model="nuevoDoctor.genero">
          <option value="Masculino">Masculino</option>
          <option value="Femenino">Femenino</option>
        </select>
        <input v-model="nuevoDoctor.telefono" placeholder="Teléfono" />
        <input v-model="nuevoDoctor.especialidad" placeholder="Especialidad" />
        <input v-model="nuevoDoctor.numeroColegiado" placeholder="Número Colegiado" />
        <input v-model="nuevoDoctor.horarioAtencion" placeholder="Horario Atención" />
        <input v-model="nuevoDoctor.fechaGraduacion" type="date" />
        <input v-model="nuevoDoctor.universidadGraduacion" placeholder="Universidad" />
        <input v-model="nuevoDoctor.correo" placeholder="Correo" />
        <input v-model="nuevoDoctor.password" type="password" placeholder="Contraseña" />

        <div class="modal-buttons">
          <button class="save-button" @click="registrarDoctor">Guardar</button>
          <button class="cancel-button" @click="cerrarFormulario">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import doctorService from "@/services/doctorService.js";
import emailjs from "emailjs-com";

export default {
  data() {
    return {
      doctorList: [],
      mostrarFormulario: false,
      nuevoDoctor: {
        nombreUsuario: "",
        apellido: "",
        documento: "",
        fechaNacimiento: "",
        genero: "Masculino",
        telefono: "",
        especialidad: "",
        numeroColegiado: "",
        horarioAtencion: "",
        fechaGraduacion: "",
        universidadGraduacion: "",
        correo: "",
        password: "",
      },
    };
  },
  methods: {
    async getAllDoctors() {
      const response = await doctorService.getAllDoctors();
      this.doctorList = response.data.map(d => ({ ...d, editando: false }));
    },

    async registrarDoctor() {
      try {
        await doctorService.registrarDoctor(this.nuevoDoctor);
        this.enviarCorreo(this.nuevoDoctor);
        this.cerrarFormulario();
        this.getAllDoctors();
        alert("Doctor registrado correctamente.");
      } catch (error) {
        console.error("Error al registrar doctor:", error);
        alert("Error al registrar el doctor.");
      }
    },

    async updateDoctor(doctor) {
      await doctorService.updateDoctor(doctor.idDoctor, doctor);
      doctor.editando = false;
    },

    async deleteDoctor(id) {
      await doctorService.deleteDoctor(id);
      this.getAllDoctors();
    },

    abrirFormulario() {
      this.mostrarFormulario = true;
    },

    cerrarFormulario() {
      this.mostrarFormulario = false;
    },

    enviarCorreo(doctor) {
      emailjs
        .send(
          "service_f70s6q3",
          "template_5cq4vng",
          {
            to_name: doctor.nombreUsuario,
            to_email: doctor.correo,
            message: `Tu cuenta ha sido creada con éxito.\nTu usuario es: ${doctor.correo}\nContraseña: ${doctor.password}, \nRol: Doctor`,
          },
          "SFAQ9kOAKVFMBgkSC"
        )
        .then(() => {
          alert("Correo de confirmación enviado.");
        })
        .catch(error => {
          console.error("Error al enviar correo:", error);
          alert("No se pudo enviar el correo.");
        });
    },
  },

  mounted() {
    this.getAllDoctors();
  },
};
</script>

<style scoped>
.doctor-container {
  padding: 20px;
  max-width: 100%;
}

.add-button {
  background-color: #28a745;
  color: white;
  padding: 12px 18px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th, td {
  padding: 15px;
  border: 1px solid #444;
  text-align: center;
  font-size: 16px;
}

input, select {
  padding: 8px;
  font-size: 16px;
  width: 100%;
}

.save-button, .edit-button, .delete-button {
  padding: 10px;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button { background-color: #f0ad4e; color: white; }
.save-button { background-color: #007bff; color: white; }
.delete-button { background-color: #dc3545; color: white; }
</style>
