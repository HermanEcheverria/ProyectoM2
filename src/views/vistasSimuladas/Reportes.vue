<template>
  <div class="container">
    <h2>Módulo de Reportes y Analítica</h2>
    <div class="filters">
      <input type="date" v-model="fechaInicio">
      <input type="date" v-model="fechaFin">
      <select v-model="doctor">
        <option value="">Todos los doctores</option>
        <option v-for="doc in doctores" :key="doc">{{ doc }}</option>
      </select>
      <select v-model="servicio">
        <option value="">Todos los servicios</option>
        <option v-for="srv in servicios" :key="srv">{{ srv }}</option>
      </select>
      <input type="text" v-model="paciente" placeholder="Nombre del paciente">
      <button class="btn-generate" @click="generarReporte">Generar Reporte</button>
      <button class="btn-download" @click="descargarPDF">Descargar PDF</button>
    </div>

    <table class="report-table">
      <thead>
        <tr>
          <th>Fecha</th>
          <th>Doctor</th>
          <th>Servicio</th>
          <th>Paciente</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reporte in reportesFiltrados" :key="reporte.id">
          <td>{{ reporte.fecha }}</td>
          <td>{{ reporte.doctor }}</td>
          <td>{{ reporte.servicio }}</td>
          <td>{{ reporte.paciente }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fechaInicio: "",
      fechaFin: "",
      doctor: "",
      servicio: "",
      paciente: "",
      doctores: ["Dr. Pérez", "Dra. Gómez", "Dr. Sánchez"],
      servicios: ["Consulta General", "Rayos X", "Cirugía"],
      reportes: [
        { id: 1, fecha: "2025-03-01", doctor: "Dr. Pérez", servicio: "Consulta General", paciente: "Juan López" },
        { id: 2, fecha: "2025-03-02", doctor: "Dra. Gómez", servicio: "Rayos X", paciente: "Ana Martínez" },
        { id: 3, fecha: "2025-03-03", doctor: "Dr. Pérez", servicio: "Rayos X", paciente: "Carlos Ramírez" }
      ]
    };
  },
  computed: {
    reportesFiltrados() {
      return this.reportes.filter(r =>
        (!this.fechaInicio || r.fecha >= this.fechaInicio) &&
        (!this.fechaFin || r.fecha <= this.fechaFin) &&
        (!this.doctor || r.doctor === this.doctor) &&
        (!this.servicio || r.servicio === this.servicio) &&
        (!this.paciente || r.paciente.toLowerCase().includes(this.paciente.toLowerCase()))
      );
    }
  },
  methods: {
    generarReporte() {
      alert("Generando Reporte...");
    },
    descargarPDF() {
      alert("Descargando Reporte en PDF...");
    }
  }
};
</script>

<style scoped>
.container {
  width: 90%;
  margin: auto;
  padding: 20px;
  background-color: #e3f2fd;
  border-radius: 8px;
}

h2 {
  text-align: center;
  margin-bottom: 15px;
  color: #1565c0;
}

.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

input, select {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.btn-generate, .btn-download {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.btn-generate {
  background-color: #1565c0;
  color: white;
}

.btn-download {
  background-color: #2e7d32;
  color: white;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #1976d2;
  color: white;
}
</style>
