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

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import emailjs from "emailjs-com";

const usuarios = ref([]);
const roles = ref([]);

// Configuración de EmailJS
const SERVICE_ID = "service_f70s6q3";
const TEMPLATE_ID = "template_5cq4vng";
const PUBLIC_KEY = "SFAQ9kOAKVFMBgkSC";

/**
 * Obtiene la lista de usuarios desde el backend.
 */
async function fetchUsuarios() {
  try {
    const response = await axios.get("/usuarios");
    usuarios.value = response.data.map(usuario => ({
      ...usuario,
      rolSeleccionado: null // Para permitir selección de rol antes de activar
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
    const response = await axios.get("/usuarios/roles");
    roles.value = response.data;
  } catch (error) {
    console.error("Error al cargar roles:", error);
  }
}

/**
 * Envía un correo de activación con el rol asignado.
 */
const sendActivationEmail = async (userEmail, userName, roleName) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
      message: `Your account has been activated. Your assigned role is: ${roleName}`
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de activación enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo de activación:", error);
  }
};

/**
 * Activa un usuario y le envía un correo de confirmación con su rol.
 */
async function activarUsuario(usuario) {
  if (!usuario.rolSeleccionado) {
    alert("Seleccione un rol para activar el usuario.");
    return;
  }
  try {
    // Obtener el nombre del rol seleccionado
    const roleName = roles.value.find(rol => rol.id === usuario.rolSeleccionado)?.roleName || "Unknown Role";

    // Activar usuario en el backend
    await axios.put(`/usuarios/${usuario.id}/activar`, {
      rolId: usuario.rolSeleccionado
    });

    // Cambiar el estado en el frontend
    usuario.estado = 1;

    // Enviar correo de activación
    await sendActivationEmail(usuario.correo, usuario.nombreUsuario, roleName);

    alert("Usuario activado correctamente y correo enviado.");
  } catch (error) {
    console.error("Error al activar usuario:", error);
    alert("Ocurrió un error al activar el usuario.");
  }
}

/**
 * Desactiva un usuario cambiando su estado a inactivo.
 */
async function desactivarUsuario(usuario) {
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
