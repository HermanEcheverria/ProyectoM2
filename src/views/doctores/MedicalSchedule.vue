<template>
  <div class="agenda-container">
    <h2>Gestión de Agenda de Doctores</h2>
    <form @submit.prevent="guardarAgenda">
      <!-- Selección del doctor -->
      <div class="form-group">
        <label>Seleccionar Doctor</label>
        <select v-model="agenda.doctor" required>
          <option v-for="doctor in doctores" :key="doctor.id" :value="doctor.nombre">
            {{ doctor.nombre }} - {{ doctor.especialidad }}
          </option>
        </select>
      </div>

      <!-- Días de atención -->
      <div class="form-group">
        <label>Días de Atención</label>
        <div v-for="(dia, index) in diasSemana" :key="index" class="checkbox-group">
          <input type="checkbox" :id="dia" :value="dia" v-model="agenda.diasAtencion" />
          <label :for="dia">{{ dia }}</label>
        </div>
      </div>

      <!-- Horarios de atención -->
      <div class="form-group">
        <label>Hora de Inicio</label>
        <input type="time" v-model="agenda.horaInicio" required />
      </div>
      <div class="form-group">
        <label>Hora de Fin</label>
        <input type="time" v-model="agenda.horaFin" required />
      </div>

      <!-- Notas adicionales -->
      <div class="form-group">
        <label>Notas Adicionales</label>
        <textarea v-model="agenda.notas"></textarea>
      </div>

      <!-- Acciones -->
      <button type="submit">Guardar Agenda</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      agenda: {
        doctor: "",
        diasAtencion: [],
        horaInicio: "",
        horaFin: "",
        notas: "",
      },
      doctores: [
        { id: 1, nombre: "Dr. Juan Pérez", especialidad: "Cardiología" },
        { id: 2, nombre: "Dra. Ana Gómez", especialidad: "Pediatría" },
        { id: 3, nombre: "Dr. Luis Ramírez", especialidad: "Neurología" },
      ],
      diasSemana: ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
    };
  },
  methods: {
    guardarAgenda() {
      console.log("Agenda guardada:", this.agenda);
      alert("Agenda guardada con éxito");
    },
  },
};
</script>

<style scoped>
.agenda-container {
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
.checkbox-group {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
