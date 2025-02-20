<template>
  <div class="paciente-container">
    <h1>Gestión de Pacientes</h1>

    <!-- Botón para agregar un paciente -->
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

          <!--Nombre editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.usuario.nombreUsuario" />
            <span v-else>{{ paciente.usuario.nombreUsuario }}</span>
          </td>

          <!-- Apellido editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.apellido" />
            <span v-else>{{ paciente.apellido }}</span>
          </td>

          <!-- Documento editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.documento" />
            <span v-else>{{ paciente.documento }}</span>
          </td>

          <!-- Fecha editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.fechaNacimiento" type="date" />
            <span v-else>{{ paciente.fechaNacimiento }}</span>
          </td>

          <!-- Género editable -->
          <td>
            <select v-if="paciente.editando" v-model="paciente.genero">
              <option value="Masculino">Masculino</option>
              <option value="Femenino">Femenino</option>
            </select>
            <span v-else>{{ paciente.genero }}</span>
          </td>

          <!-- Teléfono editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.telefono" />
            <span v-else>{{ paciente.telefono }}</span>
          </td>

          <!-- Correo editable -->
          <td>
            <input v-if="paciente.editando" v-model="paciente.usuario.correo" />
            <span v-else>{{ paciente.usuario.correo }}</span>
          </td>

          <!-- Contraseña editable  -->
          <td>
            <input v-if="paciente.editando" type="password" v-model="paciente.usuario.contrasena" />
            <span v-else>{{ paciente.usuario.contrasena}}</span>

          </td>

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

    <!-- ✅ Modal para Agregar Paciente -->
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

export default {
  data() {
    return {
      pacienteList: [],
      mostrarFormulario: false,
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
        alert("⚠️ Error al cargar los pacientes.");
      }
    },

    async updatePaciente(paciente) {
      try {
        await pacienteService.updatePaciente(paciente.idPaciente, paciente);
        paciente.editando = false;
        alert("✅ Paciente actualizado correctamente.");
      } catch (error) {
        console.error("Error al actualizar paciente:", error);
        alert("⚠️ Error al actualizar el paciente.");
      }
    },

    async deletePaciente(id) {
      if (!confirm("¿Estás seguro de eliminar este paciente?")) return;

      try {
        await pacienteService.deletePaciente(id);
        this.pacienteList = this.pacienteList.filter(p => p.idPaciente !== id);
        alert("✅ Paciente eliminado correctamente.");
      } catch (error) {
        console.error("Error al eliminar paciente:", error);
        alert("⚠️ Error al eliminar el paciente.");
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
        this.cerrarFormulario();
        this.getAllPaciente();
        alert("✅ Paciente registrado correctamente.");
      } catch (error) {
        console.error("Error al registrar paciente:", error);
        alert("⚠️ Error al registrar el paciente.");
      }
    },
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
