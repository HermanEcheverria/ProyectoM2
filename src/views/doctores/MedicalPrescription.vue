<template>
  <div class="receta-container">
    <h2>Generar Receta Médica</h2>
    <form @submit.prevent="guardarReceta">
      <!-- Datos del paciente -->
      <div class="form-group">
        <label>Nombre del Paciente</label>
        <input type="text" v-model="receta.paciente" required />
      </div>
      <div class="form-group">
        <label>Número de Identificación</label>
        <input type="text" v-model="receta.identificacion" required />
      </div>
      <div class="form-group">
        <label>Código de Seguro (Opcional)</label>
        <input type="text" v-model="receta.codigoSeguro" />
      </div>

      <!-- Datos del doctor -->
      <div class="form-group">
        <label>Nombre del Doctor</label>
        <input type="text" v-model="receta.doctor" required />
      </div>
      <div class="form-group">
        <label>Número de Colegiado</label>
        <input type="text" v-model="receta.colegiado" required />
      </div>
      <div class="form-group">
        <label>Especialidad</label>
        <input type="text" v-model="receta.especialidad" required />
      </div>

      <!-- Medicamentos -->
      <div class="medicamentos">
        <h3>Medicamentos</h3>
        <div v-for="(medicamento, index) in receta.medicamentos" :key="index" class="medicamento-item">
          <input type="text" v-model="medicamento.nombre" placeholder="Principio Activo" required />
          <input type="text" v-model="medicamento.concentracion" placeholder="Concentración" required />
          <input type="text" v-model="medicamento.presentacion" placeholder="Presentación" required />
          <input type="text" v-model="medicamento.dosis" placeholder="Dosis" required />
          <input type="text" v-model="medicamento.frecuencia" placeholder="Frecuencia" required />
          <input type="text" v-model="medicamento.duracion" placeholder="Duración" required />
          <button type="button" @click="eliminarMedicamento(index)">❌</button>
        </div>
        <button type="button" @click="agregarMedicamento">Agregar Medicamento</button>
      </div>

      <!-- Notas adicionales -->
      <div class="form-group">
        <label>Notas Adicionales</label>
        <textarea v-model="receta.notas"></textarea>
      </div>

      <!-- Acciones -->
      <button type="submit">Guardar Receta</button>
      <button type="button" @click="descargarPDF">Descargar PDF</button>
    </form>
  </div>
</template>

<script setup lang="js">
import { reactive } from 'vue';

const receta = reactive({
  paciente: "",
  identificacion: "",
  codigoSeguro: "",
  doctor: "",
  colegiado: "",
  especialidad: "",
  medicamentos: [],
  notas: "",
});

const agregarMedicamento = () => {
  receta.medicamentos.push({ nombre: "", concentracion: "", presentacion: "", dosis: "", frecuencia: "", duracion: "" });
};

const eliminarMedicamento = (index) => {
  receta.medicamentos.splice(index, 1);
};

const guardarReceta = () => {
  console.log("Receta guardada:", receta);
};

const descargarPDF = () => {
  console.log("Descargando receta en PDF...");
};
</script>


<style scoped>
.receta-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
.form-group {
  margin-bottom: 15px;
}
input, textarea {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ddd;
}
button {
  margin-top: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
.medicamentos {
  margin-top: 20px;
}
.medicamento-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}
</style>
