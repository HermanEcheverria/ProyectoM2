<template>
  <div class="paciente-container">
    <h1>Gestión de Pacientes</h1>

    <!-- Botón par agregar un paciente -->
    <button class="add-button" @click="abrirFormulario">Agregar Paciente</button>

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
          <th>Correo</th>
          <th>Contraseña</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="paciente in pacienteList" :key="paciente.idPaciente">
          <td>{{ paciente.idPaciente }}</td>

          <!-- Nombre -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.usuario.nombreUsuario" />
            <span v-else>{{ paciente.usuario.nombreUsuario }}</span>
          </td>

          <!-- Apellido -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.apellido" />
            <span v-else>{{ paciente.apellido }}</span>
          </td>

          <!-- Documento -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.documento" />
            <span v-else>{{ paciente.documento }}</span>
          </td>

          <!-- Fecha Nacimiento -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.fechaNacimiento" type="date" />
            <span v-else>{{ paciente.fechaNacimiento }}</span>
          </td>

          <!-- Género -->
          <td>
            <select v-if="paciente.editando" v-model="paciente.genero">
              <option value="Masculino">Masculino</option>
              <option value="Femenino">Femenino</option>
            </select>
            <span v-else>{{ paciente.genero }}</span>
          </td>

          <!-- Teléfono -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.telefono" />
            <span v-else>{{ paciente.telefono }}</span>
          </td>

          <!-- Correo -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.usuario.correo" />
            <span v-else>{{ paciente.usuario.correo }}</span>
          </td>

          <!-- Contraseña con opción de mostrar -->
          <td>
            <input v-if="paciente.editando" :type="mostrarContrasena ? 'text' : 'password'" v-model="paciente.usuario.contrasena" />
            <span v-else>{{paciente.usuario.contrasena }}</span>

          </td>

          <!-- Botones de acciones -->
          <td>
            <button class="edit-button" @click="paciente.editando = !paciente.editando">
              {{ paciente.editando ? "Cancelar" : "Editar" }}
            </button>
            <button class="save-button" v-if="paciente.editando" @click="updatePaciente(paciente)">Guardar</button>
            <button class="delete-button" @click="deletePaciente(paciente.idPaciente)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal para Agregar Paciente -->
    <div v-if="mostrarFormulario" class="modal-overlay">
      <div class="modal-content">
        <h2>Agregar Nuevo Paciente</h2>
        <input v-model="nuevoPaciente.nombreUsuario" placeholder="Nombre" />
        <input v-model="nuevoPaciente.apellido" placeholder="Apellido" />
        <input v-model="nuevoPaciente.documento" placeholder="Documento" />
        <input v-model="nuevoPaciente.fechaNacimiento" type="date" />
        <select v-model="nuevoPaciente.genero">
          <option value="Masculino">Masculino</option>
          <option value="Femenino">Femenino</option>
        </select>
        <input v-model="nuevoPaciente.telefono" placeholder="Teléfono" />
        <input v-model="nuevoPaciente.correo" placeholder="Correo" />
        <input v-model="nuevoPaciente.password" type="password" placeholder="Contraseña" />

        <div class="modal-buttons">
          <button class="save-button" @click="registrarPaciente">Guardar</button>
          <button class="cancel-button" @click="cerrarFormulario">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import pacienteService from "@/services/pacienteService.js";
import emailjs from 'emailjs-com';

export default {
  data() {
    return {
      pacienteList: [],
      mostrarFormulario: false,
      mostrarContrasena: false,
      nuevoPaciente: {
        nombreUsuario: "",
        apellido: "",
        documento: "",
        fechaNacimiento: "",
        genero: "Masculino",
        telefono: "",
        correo: "",
        password: "",
      },
    };
  },
  methods: {
    async getAllPaciente() {
      try {
        const response = await pacienteService.getAllPaciente();
        this.pacienteList = response.data.map(p => ({ ...p, editando: false }));
      } catch (error) {
        console.error("Error obteniendo pacientes:", error);
        alert(" Error al cargar los pacientes.");
      }
    },

    async updatePaciente(paciente) {
      try {
        await pacienteService.updatePaciente(paciente.idPaciente, paciente);
        paciente.editando = false;
        alert(" Paciente actualizado correctamente.");
      } catch (error) {
        console.error("Error al actualizar paciente, recuerda que no se puede usar el mismo correo electronico:", error);
        alert(" Error al actualizar el paciente, recuerda que no se puede usar el mismo correo electronico.");
      }
    },

    async deletePaciente(id) {
      if (!confirm("¿Estás seguro de eliminar este paciente?")) return;

      try {
        await pacienteService.deletePaciente(id);
        this.pacienteList = this.pacienteList.filter(p => p.idPaciente !== id);
        alert(" Paciente eliminado correctamente.");
      } catch (error) {
        console.error("Error al eliminar paciente:", error);
        alert("Error al eliminar el paciente.");
      }
    },

    abrirFormulario() {
      this.mostrarFormulario = true;
    },

    cerrarFormulario() {
      this.mostrarFormulario = false;
    },

    async registrarPaciente() {
      try {
        await pacienteService.registrarPaciente(this.nuevoPaciente);
        this.enviarCorreo(this.nuevoPaciente);
        this.cerrarFormulario();
        this.getAllPaciente();
        alert("Paciente registrado correctamente.");
      } catch (error) {
        console.error("Error al registrar paciente:", error);
        if (error.response && error.response.status === 400) {
          alert("Error: El correo ya está registrado.");
        } else {
          alert(" Error al registrar el paciente.");
        }
      }
    },

    enviarCorreo(paciente) {
      emailjs.send("service_f70s6q3", "template_5cq4vng", {
        to_name: paciente.nombreUsuario,
        to_email: paciente.correo,
        message: `Tu cuenta ha sido creada con éxito.\nTu usuario es: ${paciente.correo}\nContraseña: ${paciente.password}, \nRol: Paciente `
      }, "SFAQ9kOAKVFMBgkSC")
      .then(() => {
        alert(" Correo de confirmación enviado.");
      }).catch((error) => {
        console.error("Error al enviar correo:", error);
        alert(" No se pudo enviar el correo.");
      });
    }
  },

  mounted() {
    this.getAllPaciente();
  }
};
</script>


<style scoped>
.paciente-container {
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
