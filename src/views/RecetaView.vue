<template>
  <div class="recetas-container">
    <h1>Gestión de Recetas</h1>

    <!-- Formulario para crear receta -->
    <div v-if="!recetaGenerada" class="form-receta">
      <h3>Nueva Receta</h3>

      <input v-model="nuevaReceta.codigoReceta" placeholder="Código de Receta" required />

      <select v-model="nuevaReceta.idCita" @change="cargarDatosDeCita">
        <option value="" disabled>Seleccione Cita</option>
        <option v-for="cita in citas" :key="cita.idCita" :value="cita.idCita">
          Cita ID: {{ cita.idCita }} - Paciente ID: {{ cita.idPaciente }}
        </option>
      </select>

      <p>📌 Paciente Asignado (ID): {{ nuevaReceta.idPaciente || 'No asignado' }}</p>
      <p>📌 Doctor Asignado (ID): {{ nuevaReceta.idDoctor || 'No asignado' }}</p>

      <textarea v-model="nuevaReceta.anotaciones" placeholder="Anotaciones"></textarea>
      <textarea v-model="nuevaReceta.notasEspeciales" placeholder="Notas Especiales"></textarea>

      <button @click="generarReceta" class="btn-guardar">Generar Receta</button>
    </div>

    <!-- Sección para agregar medicamentos (solo si la receta ya fue creada) -->
    <div v-if="recetaGenerada" class="form-medicamentos">
      <h3>Agregar Medicamentos</h3>
      <p>📌 Receta ID: {{ nuevaReceta.idReceta }}</p>

      <select v-model="medicamentoSeleccionado">
        <option value="" disabled>Seleccione Medicamento</option>
        <option v-for="medicamento in medicamentos" :key="medicamento.idMedicamento" :value="medicamento.idMedicamento">
          {{ medicamento.principioActivo }} ({{ medicamento.concentracion }} - {{ medicamento.formaFarmaceutica }})
        </option>
      </select>

      <input v-model="dosis" placeholder="Dosis" required />
      <input v-model="frecuencia" placeholder="Frecuencia" required />
      <input v-model="duracion" placeholder="Duración" required />
      <input v-model="diagnostico" placeholder="Diagnóstico" required />

      <button @click="agregarMedicamento" class="btn-secundario">Añadir Medicamento</button>

      <ul v-if="nuevaReceta.medicamentos.length > 0">
        <li v-for="(med, index) in nuevaReceta.medicamentos" :key="index">
          Medicamento ID: {{ med.idMedicamento }} | Dosis: {{ med.dosis }}, Frecuencia: {{ med.frecuencia }}, Duración: {{ med.duracion }}
          <button @click="eliminarMedicamento(index)" class="btn-eliminar">❌</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import recetaService from "@/services/recetaService.js";
import { obtenerCitas, obtenerMedicamentos } from "@/services/selectService.js";

const recetas = ref([]);
const recetaGenerada = ref(false); // ✅ Controla si la receta ya fue creada
const nuevaReceta = ref({
  idReceta: null, // ✅ Guardará el ID de la receta creada
  codigoReceta: "",
  idCita: "",
  idPaciente: "",
  idDoctor: "",
  anotaciones: "",
  notasEspeciales: "",
  medicamentos: [],
});

const citas = ref([]);
const medicamentos = ref([]);

const medicamentoSeleccionado = ref("");
const dosis = ref("");
const frecuencia = ref("");
const duracion = ref("");
const diagnostico = ref("");

// 📌 Cargar datos iniciales
onMounted(async () => {
  citas.value = await obtenerCitas();
  medicamentos.value = await obtenerMedicamentos();
});

// 📌 Cargar datos de la cita seleccionada
const cargarDatosDeCita = () => {
  const citaSeleccionada = citas.value.find(cita => cita.idCita === nuevaReceta.value.idCita);
  if (citaSeleccionada) {
    nuevaReceta.value.idPaciente = citaSeleccionada.idPaciente;
    nuevaReceta.value.idDoctor = citaSeleccionada.idDoctor;
    console.log("📌 ID Paciente asignado:", nuevaReceta.value.idPaciente);
    console.log("📌 ID Doctor asignado:", nuevaReceta.value.idDoctor);
  }
};

// 📌 Paso 1: Crear la Receta
const generarReceta = async () => {
  try {
    console.log("📌 Enviando datos para crear receta:", JSON.stringify(nuevaReceta.value, null, 2));
    const respuesta = await recetaService.crearReceta(nuevaReceta.value);

    // ✅ Guardar el ID de la receta
    nuevaReceta.value.idReceta = respuesta.idReceta;
    recetaGenerada.value = true; // ✅ Ahora podemos agregar medicamentos

    console.log("✅ Receta creada con ID:", nuevaReceta.value.idReceta);
  } catch (error) {
    console.error("❌ Error generando receta:", error);
  }
};

// 📌 Paso 2: Agregar Medicamentos
const agregarMedicamento = async () => {
  if (!medicamentoSeleccionado.value) {
    alert("Seleccione un medicamento.");
    return;
  }

  const nuevoMedicamento = {
    idReceta: nuevaReceta.value.idReceta, // ✅ Asigna el ID de la receta
    idMedicamento: medicamentoSeleccionado.value,
    dosis: dosis.value,
    frecuencia: frecuencia.value,
    duracion: duracion.value,
    diagnostico: diagnostico.value,
  };

  try {
    console.log("📌 Enviando medicamento:", JSON.stringify(nuevoMedicamento, null, 2));
    await recetaService.agregarMedicamento(nuevoMedicamento);
    nuevaReceta.value.medicamentos.push(nuevoMedicamento);

    alert("✅ Medicamento agregado con éxito.");

    // Limpiar campos
    medicamentoSeleccionado.value = "";
    dosis.value = "";
    frecuencia.value = "";
    duracion.value = "";
    diagnostico.value = "";
  } catch (error) {
    console.error("❌ Error agregando medicamento:", error);
    alert("Error al agregar medicamento.");
  }
};

// 📌 Eliminar medicamento de la lista antes de guardar
const eliminarMedicamento = (index) => {
  nuevaReceta.value.medicamentos.splice(index, 1);
};
</script>

<style scoped>
.recetas-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}
h1, h3 {
  text-align: center;
}
.btn-guardar, .btn-secundario {
  display: block;
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
.btn-guardar:hover, .btn-secundario:hover {
  background-color: #0056b3;
}
.btn-eliminar {
  background-color: red;
  color: white;
  border: none;
  cursor: pointer;
}
input, select, textarea {
  margin: 5px 0;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.lista-medicamentos ul {
  list-style-type: none;
  padding: 0;
}
.lista-medicamentos li {
  display: flex;
  justify-content: space-between;
  padding: 5px;
  border-bottom: 1px solid #ccc;
}
</style>
