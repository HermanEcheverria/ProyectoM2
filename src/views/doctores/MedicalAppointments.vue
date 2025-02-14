<template>
  <div class="cita-container">
    <h2>Agendar Cita Médica</h2>
    <form @submit.prevent="guardarCita">
      <!-- Datos del paciente -->
      <div class="form-group">
        <label>Nombre del Paciente</label>
        <input type="text" v-model="cita.paciente" required />
      </div>
      <div class="form-group">
        <label>Número de Identificación</label>
        <input type="text" v-model="cita.identificacion" required />
      </div>

      <!-- Datos del doctor -->
      <div class="form-group">
        <label>Seleccionar Doctor</label>
        <select v-model="cita.doctor" required>
          <option v-for="doctor in doctores" :key="doctor.id" :value="doctor.nombre">
            {{ doctor.nombre }} - {{ doctor.especialidad }}
          </option>
        </select>
      </div>

      <!-- Fecha y hora -->
      <div class="form-group">
        <label>Fecha de la Cita</label>
        <input type="date" v-model="cita.fecha" required />
      </div>
      <div class="form-group">
        <label>Hora de la Cita</label>
        <input type="time" v-model="cita.hora" required />
      </div>

      <!-- Notas adicionales -->
      <div class="form-group">
        <label>Notas Adicionales</label>
        <textarea v-model="cita.notas"></textarea>
      </div>

      <!-- Acciones -->
      <button type="submit">Agendar Cita</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cita: {
        paciente: "",
        identificacion: "",
        doctor: "",
        fecha: "",
        hora: "",
        notas: "",
      },
      doctores: [
        { id: 1, nombre: "Dr. Juan Pérez", especialidad: "Cardiología" },
        { id: 2, nombre: "Dra. Ana Gómez", especialidad: "Pediatría" },
        { id: 3, nombre: "Dr. Luis Ramírez", especialidad: "Neurología" },
      ],
    };
  },
  methods: {
    guardarCita() {
      console.log("Cita agendada:", this.cita);
      alert("Cita agendada con éxito");
    },
  },
};
</script>

<style scoped>
.cita-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
.form-group {
  margin-bottom: 15px;
}
input, select, textarea {
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
</style>
