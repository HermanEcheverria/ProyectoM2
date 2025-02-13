<template>
  <div class="admin-usuarios">
    <h1>Administración de Usuarios</h1>

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Correo</th>
          <th>Estado</th>
          <th>Rol a asignar</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="usuario in usuarios" :key="usuario.id">
          <td>{{ usuario.id }}</td>
          <td>{{ usuario.nombreUsuario }}</td>
          <td>{{ usuario.correo }}</td>
          <td>
            <span :class="usuario.estado === 1 ? 'activo' : 'inactivo'">
              {{ usuario.estado === 1 ? 'Activo' : 'Inactivo' }}
            </span>
          </td>
          <td>
            <select v-model="usuario.rolSeleccionado" :disabled="usuario.estado === 1">
              <option disabled value="">Seleccione un rol</option>
              <option v-for="rol in roles" :key="rol.id" :value="rol.id">
                {{ rol.roleName }}
              </option>
            </select>
          </td>
          <td>
            <button v-if="usuario.estado === 0" @click="activarUsuario(usuario)">
              Activar
            </button>
            <button v-else class="desactivar" @click="desactivarUsuario(usuario)">
              Desactivar
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import axios from "axios";

/**
 * Interfaz que representa un Usuario en la aplicación.
 */
interface Usuario {
  id: number;
  nombreUsuario: string;
  correo: string;
  estado: number; // 1 = Activo, 0 = Inactivo
  rolSeleccionado?: number;
}

/**
 * Interfaz que representa un Rol en la aplicación.
 */
interface Rol {
  id: number;
  roleName: string;
}

const usuarios = ref<Usuario[]>([]);
const roles = ref<Rol[]>([]);

/**
 * Obtiene la lista de usuarios desde el backend.
 */
async function fetchUsuarios() {
  try {
    const response = await axios.get<Usuario[]>("/usuarios");
    usuarios.value = response.data.map((usuario) => ({
      ...usuario,
      rolSeleccionado: undefined, // Agregamos rolSeleccionado para control en el frontend
    }));
  } catch (error) {
    console.error("Error al cargar usuarios:", error);
  }
}

/**
 * Obtiene la lista de roles desde el backend.
 */
async function fetchRoles() {
  try {
    const response = await axios.get<Rol[]>("/usuarios/roles");
    roles.value = response.data;
  } catch (error) {
    console.error("Error al cargar roles:", error);
  }
}

/**
 * Activa un usuario seleccionando un rol y cambiando su estado a activo.
 * @param usuario Usuario a activar.
 */
async function activarUsuario(usuario: Usuario) {
  if (!usuario.rolSeleccionado) {
    alert("Seleccione un rol para activar el usuario.");
    return;
  }
  try {
    await axios.put(`/usuarios/${usuario.id}/activar`, {
      rolId: usuario.rolSeleccionado,
    });
    usuario.estado = 1; // Actualizar estado en frontend
    alert("Usuario activado correctamente.");
  } catch (error) {
    console.error("Error al activar usuario:", error);
    alert("Ocurrió un error al activar el usuario.");
  }
}

/**
 * Desactiva un usuario cambiando su estado a inactivo.
 * @param usuario Usuario a desactivar.
 */
async function desactivarUsuario(usuario: Usuario) {
  try {
    await axios.put(`/usuarios/${usuario.id}/desactivar`);
    usuario.estado = 0; // Actualizar estado en frontend
    alert("Usuario desactivado correctamente.");
  } catch (error) {
    console.error("Error al desactivar usuario:", error);
    alert("Ocurrió un error al desactivar el usuario.");
  }
}

// Cargar datos cuando el componente se monta
onMounted(() => {
  fetchUsuarios();
  fetchRoles();
});
</script>

<style scoped>
.admin-usuarios {
  padding: 20px;
  background: #181818;
  color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 12px;
  border: 1px solid #444;
  text-align: left;
}

th {
  background-color: #333;
  color: white;
}

select {
  padding: 5px;
  background: #222;
  color: white;
  border: 1px solid #444;
  border-radius: 5px;
}

button {
  padding: 8px 15px;
  background-color: #28a745;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

button.desactivar {
  background-color: #dc3545;
}

button:hover {
  opacity: 0.8;
}

.activo {
  color: #28a745;
  font-weight: bold;
}

.inactivo {
  color: #dc3545;
  font-weight: bold;
}
</style>
