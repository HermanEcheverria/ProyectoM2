<script setup lang="ts">
import { ref, onMounted } from "vue";
import { obtenerPreguntas, enviarPregunta, editarPregunta, eliminarPregunta } from "../../services/faqService";

// Definir la estructura de una pregunta
interface Pregunta {
  id: number;
  pregunta: string;
  respuesta?: string;
  autor?: string;
  editadoPor?: string;
}

// Variables reactivas
const preguntas = ref<Pregunta[]>([]);
const nuevaPregunta = ref(""); // Campo para agregar nueva pregunta
const preguntaEditada = ref<Pregunta>({ id: 0, pregunta: "", respuesta: "" }); // Siempre un objeto válido
const isEditing = ref(false);

// ✅ Cargar preguntas desde la API
const cargarPreguntas = async () => {
  try {
    const data = await obtenerPreguntas();
    console.log("📢 Datos de API:", data);
    if (Array.isArray(data)) {
      preguntas.value = data;
    } else {
      console.error("⚠️ Error: La API no devolvió un array.");
    }
  } catch (error) {
    console.error("❌ Error cargando preguntas:", error);
  }
};

// ✅ Agregar una nueva pregunta
const agregarPregunta = async () => {
  if (!nuevaPregunta.value.trim()) {
    alert("⚠️ La pregunta no puede estar vacía.");
    return;
  }
  try {
    console.log("📢 Agregando pregunta:", nuevaPregunta.value);
    await enviarPregunta(nuevaPregunta.value, "Admin");
    nuevaPregunta.value = "";
    cargarPreguntas(); // Recargar la lista
  } catch (error) {
    console.error("❌ Error agregando pregunta:", error);
  }
};

// ✅ Iniciar edición de una pregunta
const iniciarEdicion = (pregunta: Pregunta) => {
  preguntaEditada.value = {
    id: pregunta.id,
    pregunta: pregunta.pregunta || "",
    respuesta: pregunta.respuesta || "",
    autor: pregunta.autor || "",
    editadoPor: pregunta.editadoPor || "",
  };
  isEditing.value = true;
};

// ✅ Guardar cambios en la pregunta editada
const guardarEdicion = async () => {
  if (!preguntaEditada.value.id) {
    console.error("⚠️ No hay pregunta seleccionada para editar.");
    return;
  }
  if (!preguntaEditada.value.pregunta.trim()) {
    alert("⚠️ La pregunta no puede estar vacía.");
    return;
  }
  try {
    await editarPregunta(
      preguntaEditada.value.id,
      preguntaEditada.value.pregunta,
      preguntaEditada.value.respuesta || "",
      "Admin" // Se registra el usuario que editó
    );
    isEditing.value = false;
    cargarPreguntas();
  } catch (error) {
    console.error("❌ Error guardando edición:", error);
  }
};

// ✅ Eliminar pregunta
const borrarPregunta = async (id: number) => {
  if (confirm("❗ ¿Seguro que deseas eliminar esta pregunta?")) {
    try {
      await eliminarPregunta(id);
      cargarPreguntas();
    } catch (error) {
      console.error("❌ Error eliminando pregunta:", error);
    }
  }
};

// ✅ Cargar preguntas al montar el componente
onMounted(() => {
  cargarPreguntas();
});
</script>

<template>
  <div class="admin-faq">
    <h2>Gestión de Preguntas Frecuentes</h2>

    <!-- Formulario para agregar nuevas preguntas -->
    <div class="formulario">
      <input v-model="nuevaPregunta" placeholder="Escribe una nueva pregunta aquí..." />
      <button @click="agregarPregunta">➕ Agregar Pregunta</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>Pregunta</th>
          <th>Respuesta</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pregunta in preguntas" :key="pregunta.id">
          <td>{{ pregunta.pregunta }}</td>
          <td>{{ pregunta.respuesta || "No respondida aún" }}</td>
          <td>
            <button @click="iniciarEdicion(pregunta)">✏️ Editar</button>
            <button @click="borrarPregunta(pregunta.id)">🗑️ Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal para editar preguntas -->
    <div v-if="isEditing" class="modal">
      <h3>Editar Pregunta</h3>
      <label>Pregunta:</label>
      <input v-model="preguntaEditada.pregunta" type="text" />

      <label>Respuesta:</label>
      <textarea v-model="preguntaEditada.respuesta"></textarea>

      <button @click="guardarEdicion">Guardar</button>
      <button @click="isEditing = false">Cancelar</button>
    </div>
  </div>
</template>

<style scoped>
.admin-faq {
  max-width: 800px;
  margin: 0 auto;
}

.formulario {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
}

input {
  width: 60%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 8px 12px;
  cursor: pointer;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
}

button:hover {
  background: #0056b3;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 10px;
  border: 1px solid #ccc;
}

.modal {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0px 0px 10px gray;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
