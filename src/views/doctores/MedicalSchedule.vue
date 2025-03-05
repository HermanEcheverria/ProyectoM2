<template>
  <div class="schedule-portal">
    <h2>Portal de Gestión de Citas</h2>

    <!-- Tabla de citas -->
    <div class="schedule-list">
      <table>
        <thead>
          <tr>
            <!-- Columnas adicionales para Admin -->
            <th v-if="isAdmin">ID Cita</th>
            <th>Paciente</th>
            <th v-if="isAdmin">Doctor</th>
            <th>Fecha</th>
            <th>Hora Inicio</th>
            <th>Hora Fin</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.idCita">
            <td v-if="isAdmin">{{ cita.idCita }}</td>
            <td>{{ cita.paciente ? cita.paciente.nombre + ' ' + cita.paciente.apellido : 'Sin asignar' }}</td>
            <td v-if="isAdmin">
              {{ cita.doctor ? cita.doctor.nombre + ' ' + cita.doctor.apellido : 'Sin asignar' }}
            </td>
            <td>{{ cita.fecha }}</td>
            <td>{{ cita.horaInicio }}</td>
            <td>{{ cita.horaFin }}</td>
            <td>{{ cita.estado }}</td>
            <td>
              <!-- Solo si la cita está pendiente se pueden realizar acciones -->
              <button v-if="cita.estado === 'PENDIENTE'" @click="abrirModalProcesar(cita)">Procesar</button>
              <button v-if="cita.estado === 'PENDIENTE'" @click="cancelarCita(cita)">Cancelar</button>
              <!-- La reasignación solo la puede hacer el Admin -->
              <button v-if="isAdmin && cita.estado === 'PENDIENTE'" @click="abrirModalReasignar(cita)">Reasignar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal para procesar cita (finzalizar) -->
    <div v-if="mostrarModalProcesar" class="modal">
      <div class="modal-content">
        <h3>Procesar Cita</h3>
        <form @submit.prevent="guardarProcesamiento">
          <div class="form-group">
            <label for="diagnostico">Diagnóstico:</label>
            <textarea id="diagnostico" v-model="citaSeleccionada.diagnostico" required></textarea>
          </div>
          <div class="form-group">
            <label for="resultados">Resultados de Exámenes:</label>
            <textarea id="resultados" v-model="citaSeleccionada.resultados"></textarea>
          </div>
          <button type="submit">Guardar</button>
          <button type="button" @click="cerrarModalProcesar">Cancelar</button>
        </form>
      </div>
    </div>

    <!-- Modal para reasignar doctor (solo Admin) -->
    <div v-if="mostrarModalReasignar" class="modal">
      <div class="modal-content">
        <h3>Reasignar Doctor</h3>
        <form @submit.prevent="guardarReasignacion">
          <div class="form-group">
            <label for="doctorSelect">Nuevo Doctor:</label>
            <select id="doctorSelect" v-model="doctorSeleccionado" required>
              <option v-for="doc in listaDoctores" :key="doc.idDoctor" :value="doc.idDoctor">
                {{ doc.nombre + ' ' + doc.apellido }} ({{ doc.especialidad }})
              </option>
            </select>
          </div>
          <button type="submit">Guardar Reasignación</button>
          <button type="button" @click="cerrarModalReasignar">Cancelar</button>
        </form>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';
import API_URL from '@/config';
// Importa los refs de tu authStore.ts
import { userRole, userId } from '@/stores/authStore';

export default {
  name: "MedicalSchedule",
  data() {
    return {
      citas: [],
      mostrarModalProcesar: false,
      mostrarModalReasignar: false,
      citaSeleccionada: {},
      doctorSeleccionado: null,
      listaDoctores: []
    };
  },
  computed: {
    isAdmin() {
      return userRole.value === 1;
    },
    isDoctor() {
      return userRole.value === 2;
    },
    idDoctor() {
      return this.isDoctor ? userId.value : null;
    }
  },
  mounted() {
    if (!this.isAdmin && !this.isDoctor) {
      alert("Acceso restringido: el usuario no tiene permisos para ver esta página.");
      return;
    }
    this.obtenerCitas();
    if (this.isAdmin) {
      this.obtenerDoctores();
    }
  },
  methods: {
    // Obtiene las citas del backend según el rol
    async obtenerCitas() {
      try {
        let response;
        if (this.isDoctor) {
          response = await axios.get(`${API_URL}/citas/doctor/${this.idDoctor}`);
        } else if (this.isAdmin) {
          response = await axios.get(`${API_URL}/citas`);
        }
        this.citas = response.data;
      } catch (error) {
        console.error("Error al obtener las citas:", error);
        alert("No se pudieron cargar las citas");
      }
    },
    // Obtiene la lista de doctores (solo para Admin)
    async obtenerDoctores() {
      try {
        const response = await axios.get(`${API_URL}/doctor`);
        this.listaDoctores = response.data;
      } catch (error) {
        console.error("Error al obtener los doctores:", error);
        alert("No se pudo cargar la lista de doctores");
      }
    },
    // Abre el modal para procesar (finalizar) una cita
    abrirModalProcesar(cita) {
      this.citaSeleccionada = { ...cita };
      this.mostrarModalProcesar = true;
    },
    cerrarModalProcesar() {
      this.mostrarModalProcesar = false;
      this.citaSeleccionada = {};
    },
    // Guarda el procesamiento de la cita (cambia su estado a FINALIZADA)
    async guardarProcesamiento() {
      try {
        this.citaSeleccionada.estado = "FINALIZADA";
        await axios.put(`${API_URL}/citas/${this.citaSeleccionada.idCita}`, this.citaSeleccionada);
        alert("Cita procesada con éxito");
        this.cerrarModalProcesar();
        this.obtenerCitas();
      } catch (error) {
        console.error("Error al procesar la cita:", error);
        alert("Error al procesar la cita");
      }
    },
    // Actualiza el estado de la cita a CANCELADA
    async cancelarCita(cita) {
      if (confirm("¿Desea cancelar esta cita?")) {
        try {
          let updatedCita = { ...cita, estado: "CANCELADA" };
          await axios.put(`${API_URL}/citas/${cita.idCita}`, updatedCita);
          alert("Cita cancelada con éxito");
          this.obtenerCitas();
        } catch (error) {
          console.error("Error al cancelar la cita:", error);
          alert("Error al cancelar la cita");
        }
      }
    },
    // Abre el modal para reasignar el doctor (solo Admin)
    abrirModalReasignar(cita) {
      this.citaSeleccionada = { ...cita };
      this.doctorSeleccionado = null;
      this.mostrarModalReasignar = true;
    },
    cerrarModalReasignar() {
      this.mostrarModalReasignar = false;
      this.citaSeleccionada = {};
    },
    // Guarda la reasignación del doctor
    async guardarReasignacion() {
      try {
        this.citaSeleccionada.idDoctor = this.doctorSeleccionado;
        await axios.put(`${API_URL}/citas/${this.citaSeleccionada.idCita}`, this.citaSeleccionada);
        alert("Cita reasignada con éxito");
        this.cerrarModalReasignar();
        this.obtenerCitas();
      } catch (error) {
        console.error("Error al reasignar el doctor:", error);
        alert("Error al reasignar el doctor");
      }
    }
  }
};
</script>

<style>
/* =========================
   Variables globales
========================= */
:root {
  --color-verde-claro:  #DAFDBA;
  --color-menta:        #B2F2BB;
  --color-teal:         #45C4B0;
  --color-teal-oscuro:  #13678A;
  --color-azul-noche:   #012030;

  --color-texto-claro:  #FFFFFF;
  --color-texto-oscuro: #1C1C1C;
}

/* =========================
   Estilo general del body
========================= */
body {
  margin: 0;
  padding: 0;
  background-color: var(--color-azul-noche); /* Fondo total de la página */
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: var(--color-texto-claro);
}

/* =========================
   Contenedor principal
   Extendemos ancho y damos
   un look tipo "card"
========================= */
.schedule-portal {
  margin: 2rem auto;
  padding: 2rem;
  max-width: 90%; /* Ajusta si quieres más (100%) o menos (por ejemplo 80%) */
  background: linear-gradient(to bottom, var(--color-teal-oscuro), var(--color-azul-noche));
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.4);
  color: var(--color-texto-claro);
}

/* Título centrado con algo de estilo */
.schedule-portal h2 {
  margin-top: 0;
  margin-bottom: 1rem;
  text-align: center;
  font-size: 2rem;
  color: var(--color-menta);
  text-shadow: 1px 1px 2px rgba(0,0,0,0.5);
}

/* =========================
   Tabla de citas
========================= */
.schedule-list {
  overflow-x: auto; /* por si se desborda en pantallas pequeñas */
}

/* Contenedor de la tabla */
.schedule-list table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 8px; /* esquinas redondeadas */
  overflow: hidden;   /* para que el thead respete el radio */
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

/* Encabezado */
.schedule-list thead {
  background-color: var(--color-teal);
}
.schedule-list thead th {
  padding: 1rem;
  font-weight: 600;
  text-align: left;
  color: var(--color-texto-claro);
  font-size: 1rem;
  border-bottom: 2px solid var(--color-teal-oscuro);
}

/* Cuerpo */
.schedule-list tbody tr {
  background-color: var(--color-azul-noche);
  transition: background-color 0.2s;
}
.schedule-list td {
  padding: 0.75rem 1rem;
  font-size: 0.95rem;
  color: var(--color-verde-claro);
  border-bottom: 1px solid var(--color-teal-oscuro);
}

/* Efecto hover en filas */
.schedule-list tbody tr:hover {
  background-color: var(--color-teal-oscuro);
}

/* =========================
   BOTONES
========================= */

/* Quita estilos básicos */
button {
  border: none;
  border-radius: 4px;
  padding: 0.5rem 0.75rem;
  margin-right: 5px;
  cursor: pointer;
  font-weight: 600;
  transition: filter 0.2s, background-color 0.2s;
}

/* Botón para "Procesar" */
.btn-procesar {
  background-color: #2ecc71; /* verde clarito (puedes cambiar) */
  color: #fff;
}
.btn-procesar:hover {
  filter: brightness(110%);
}

/* Botón para "Cancelar" */
.btn-cancelar {
  background-color: #e74c3c; /* rojo */
  color: #fff;
}
.btn-cancelar:hover {
  filter: brightness(110%);
}

/* Botón para "Reasignar" */
.btn-reasignar {
  background-color: #f1c40f; /* amarillo/mostaza */
  color: #1C1C1C;
}
.btn-reasignar:hover {
  filter: brightness(110%);
}

/* =========================
   MODALES (Procesar / Reasignar)
========================= */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background: var(--color-verde-claro);
  color: var(--color-texto-oscuro);
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
}
input, textarea, select {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid var(--color-teal-oscuro);
  font-size: 1rem;
}


</style>
