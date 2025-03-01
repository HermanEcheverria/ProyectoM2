<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "../config"; // Importamos API_URL desde config.ts

const userId = localStorage.getItem("userId");
const user = ref(null);
const doctor = ref(null);
const loading = ref(true);
const errorMensaje = ref("");
const isEditing = ref(false);

// Mapeo de roles por ID
const rolesMap = {
  1: "Administrador",
  2: "Doctor",
  3: "Empleado",
  4: "Paciente",
  5: "Usuario Interconexión",
};

// Mapeo de estados
const estadoMap = {
  1: "Activo",
  0: "Inactivo",
};

// Cargar datos del usuario y del doctor
onMounted(async () => {
  if (!userId) {
    errorMensaje.value = "Usuario no encontrado. Intenta iniciar sesión nuevamente.";
    loading.value = false;
    return;
  }

  try {
    const userResponse = await axios.get(`${API_URL}/usuarios/${userId}`);
    const doctorResponse = await axios.get(`${API_URL}/doctores/${userId}`);

    if (userResponse.data && doctorResponse.data) {
      user.value = userResponse.data;
      doctor.value = doctorResponse.data;
    } else {
      errorMensaje.value = "No se encontraron datos del usuario o doctor.";
    }
  } catch (error) {
    console.error("Error cargando datos:", error);
    errorMensaje.value = "Error al cargar el perfil. Inténtalo más tarde.";
  } finally {
    loading.value = false;
  }
});

// Alternar modo edición
const toggleEdit = () => {
  isEditing.value = !isEditing.value;
};

// Actualizar perfil
const updateProfile = async () => {
  if (!user.value || !doctor.value) return;

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, {
      nombreUsuario: user.value.nombreUsuario,
      correo: user.value.correo,
      contrasena: user.value.contrasena,
    });

    await axios.put(`${API_URL}/doctores/${userId}`, {
      apellido: doctor.value.apellido,
      documento: doctor.value.documento,
      fechaNacimiento: doctor.value.fechaNacimiento,
      genero: doctor.value.genero,
      telefono: doctor.value.telefono,
      especialidad: doctor.value.especialidad,
      numeroColegiado: doctor.value.numeroColegiado,
      horarioAtencion: doctor.value.horarioAtencion,
      fechaGraduacion: doctor.value.fechaGraduacion,
      universidadGraduacion: doctor.value.universidadGraduacion,
    });

    alert("Perfil actualizado correctamente.");
    isEditing.value = false; // Salir del modo edición
  } catch (error) {
    console.error("Error actualizando perfil:", error);
    alert("Hubo un error al actualizar el perfil.");
  }
};
</script>


<template>
  <div class="account-container">
    <h2>Mi Cuenta (Doctor)</h2>

    <!-- Mostrar mensaje de carga -->
    <p v-if="loading">Cargando datos...</p>

    <!-- Mostrar mensaje de error si hay problemas -->
    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>

    <!-- Mostrar el formulario si los datos están disponibles -->
    <form v-if="user && doctor" @submit.prevent="updateProfile">
      <label><b>Nombre:</b></label>
      <input v-model="user.nombreUsuario" :disabled="!isEditing" required />

      <label><b>Correo:</b></label>
      <input v-model="user.correo" :disabled="!isEditing" required />

      <label><b>Contraseña:</b></label>
      <input type="password" v-model="user.contrasena" :disabled="!isEditing" required />

      <label><b>Rol:</b></label>
      <input :value="rolesMap[user.rolId]" disabled class="readonly-field" />

      <label><b>Estado:</b></label>
      <input :value="estadoMap[user.estado]" disabled class="readonly-field" />

      <label><b>Fecha de Creación:</b></label>
      <input v-model="user.fechaCreacion" disabled class="readonly-field" />

      <label><b>Apellido:</b></label>
      <input v-model="doctor.apellido" :disabled="!isEditing" required />

      <label><b>Documento:</b></label>
      <input v-model="doctor.documento" :disabled="!isEditing" required />

      <label><b>Fecha de Nacimiento:</b></label>
      <input type="date" v-model="doctor.fechaNacimiento" :disabled="!isEditing" required />

      <label><b>Género:</b></label>
      <input v-model="doctor.genero" :disabled="!isEditing" required />

      <label><b>Teléfono:</b></label>
      <input v-model="doctor.telefono" :disabled="!isEditing" required />

      <label><b>Especialidad:</b></label>
      <input v-model="doctor.especialidad" :disabled="!isEditing" required />

      <label><b>Número de Colegiado:</b></label>
      <input v-model="doctor.numeroColegiado" :disabled="!isEditing" required />

      <label><b>Horario de Atención:</b></label>
      <input v-model="doctor.horarioAtencion" :disabled="!isEditing" required />

      <label><b>Fecha de Graduación:</b></label>
      <input type="date" v-model="doctor.fechaGraduacion" :disabled="!isEditing" required />

      <label><b>Universidad de Graduación:</b></label>
      <input v-model="doctor.universidadGraduacion" :disabled="!isEditing" required />

      <button v-if="isEditing" type="submit">Guardar Cambios</button>
      <button v-else type="button" @click="toggleEdit">Editar</button>
    </form>
  </div>
</template>

<style scoped>
/* Estilos del contenedor */
.account-container {
  max-width: 500px;
  margin: auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

/* Estilo de los inputs */
input {
  width: 100%;
  padding: 10px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Estilo para campos no editables */
.readonly-field {
  background-color: #e0e0e0;
  color: #555;
}

/* Estilo de etiquetas */
label {
  display: block;
  margin-top: 10px;
  font-size: 14px;
  color: #333;
  font-weight: bold; /* Ahora los títulos están en negrita */
}

/* Botón de edición */
button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  margin-top: 10px;
}

button:hover {
  background-color: #0056b3;
}

/* Mensaje de error */
.error {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}
</style>
