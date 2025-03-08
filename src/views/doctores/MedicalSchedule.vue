<template>
  <div class="schedule-portal">
    <h2>Portal de Gestión de Citas</h2>

    <!-- Tabla de citas -->
    <div class="schedule-list">
      <table>
        <thead>
          <tr>
            <th v-if="isAdmin">ID Cita</th>
            <th>Paciente</th>
            <th v-if="isAdmin">Doctor</th>
            <th>Fecha</th>
            <th>Hora Inicio</th>
            <th>Hora Fin</th>
            <th>Estado</th>
            <th>Acciones</th>
            <th>Receta</th>
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
              <button v-if="cita.estado === 'PENDIENTE'" @click="abrirModalProcesar(cita)">Procesar</button>
              <button v-if="cita.estado === 'PENDIENTE'" @click="cancelarCita(cita)">Cancelar</button>
              <button v-if="isAdmin && cita.estado === 'PENDIENTE'" @click="abrirModalReasignar(cita)">Reasignar</button>
            </td>
            <td>
              <button @click="gestionarReceta(cita)">Gestionar Receta</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal para gestionar receta -->
    <div v-if="mostrarModalReceta" class="modal">
      <div class="modal-content">
        <h3>Gestión de Receta</h3>
        <p><strong>Cita ID:</strong> {{ citaSeleccionada.idCita }}</p>

        <div v-if="recetaExistente">
          <p><strong>Receta ID:</strong> {{ receta.idReceta }}</p>
          <p><strong>Código Receta:</strong> {{ receta.codigoReceta }}</p>
          <p><strong>Notas:</strong> {{ receta.notasEspeciales }}</p>

          <h4>Medicamentos:</h4>
          <ul v-if="receta.medicamentos.length > 0">
            <li v-for="med in receta.medicamentos" :key="med.idRecetaMedicamento">
              {{ med.medicamento.principioActivo }} - {{ med.dosis }} ({{ med.frecuencia }})
            </li>
          </ul>
          <p v-else>Sin medicamentos registrados.</p>

          <button @click="mostrarAgregarMedicamento = true">Agregar Medicamento</button>
        </div>

        <div v-else>
          <h3>Nueva Receta</h3>
          <input v-model="nuevaReceta.codigoReceta" placeholder="Código de Receta" required />

          <p style="color: gray;">Paciente: {{ citaSeleccionada.paciente?.nombre || 'No asignado' }} {{ citaSeleccionada.paciente?.apellido || '' }}</p>
          <p style="color: gray;">Doctor: {{ citaSeleccionada.doctor?.nombre || 'No asignado' }} {{ citaSeleccionada.doctor?.apellido || '' }}</p>

          <textarea v-model="nuevaReceta.anotaciones" placeholder="Anotaciones"></textarea>
          <textarea v-model="nuevaReceta.notasEspeciales" placeholder="Notas Especiales"></textarea>

          <button @click="crearRecetaDesdeCita">Generar Receta</button>
        </div>

        <div v-if="mostrarAgregarMedicamento">
          <h3>Agregar Medicamento</h3>
          <select v-model="medicamentoSeleccionado">
            <option value="" disabled>Seleccione Medicamento</option>
            <option v-for="medicamento in medicamentos" :key="medicamento.idMedicamento" :value="medicamento.idMedicamento">
              {{ medicamento.principioActivo }} ({{ medicamento.concentracion }} - {{ medicamento.formaFarmaceutica }})
            </option>
          </select>

          <input v-model="dosis" placeholder="Dosis" required />
          <input v-model="frecuencia" placeholder="Frecuencia" required />
          <input v-model="duracion" placeholder="Duración" required />
          <input v-model="diagnostico" placeholder="Diagnóstico" />

          <button @click="agregarMedicamento">Añadir Medicamento</button>
        </div>
        <button @click="editarReceta">Editar Receta</button>


        <button @click="cerrarModalReceta">Cerrar</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import API_URL from '@/config';
import recetaService from "@/services/RecetaService.js";
import { userRole, userId } from '@/stores/authStore';

export default {
  data() {
    return {
      citas: [],
      mostrarModalReceta: false,
      mostrarAgregarMedicamento: false,
      recetaExistente: false,
      citaSeleccionada: {},
      receta: {},
      nuevaReceta: {
        codigoReceta: "",
        idCita: null,
        idPaciente: null,
        idDoctor: null,
        anotaciones: "",
        notasEspeciales: "",
      },
      medicamentoSeleccionado: "",
      dosis: "",
      frecuencia: "",
      duracion: "",
      diagnostico: "",
      medicamentos: [],
    };
  },
  computed: {
    isAdmin() {
      return userRole.value === 1;
    },
    isDoctor() {
      return userRole.value === 2;
    }
  },
  mounted() {
    console.log("📌 mounted() ejecutado.");
    this.obtenerCitas();
    this.obtenerMedicamentos();
  },
  methods: {

    cerrarModalReceta() {
  console.log("🔴 Cerrando modal de receta...");
  this.mostrarModalReceta = false;
  this.mostrarAgregarMedicamento = false;
  this.recetaExistente = false;
  this.receta = {};
  this.nuevaReceta = {
    codigoReceta: "",
    idCita: null,
    idPaciente: null,
    idDoctor: null,
    anotaciones: "",
    notasEspeciales: "",
  };
},

    async obtenerCitas() {
      try {
        console.log("⏳ Llamando a obtenerCitas()...");
        let response = await axios.get(`${API_URL}/citas`);
        this.citas = response.data;
        console.log("✅ Citas obtenidas:", this.citas);
      } catch (error) {
        console.error("❌ Error al obtener las citas:", error);
        alert("No se pudieron cargar las citas.");
      }
    },

    async obtenerMedicamentos() {
  try {
    console.log("⏳ Cargando medicamentos...");
    const response = await axios.get(`${API_URL}/medicamentos`);
    this.medicamentos = response.data;
    console.log("✅ Medicamentos cargados:", this.medicamentos);
  } catch (error) {
    console.error("❌ Error al obtener los medicamentos:", error);
    alert("No se pudieron cargar los medicamentos.");
  }
},

async agregarMedicamento() {
  // 🔥 Validar que todos los campos tengan datos
  if (!this.medicamentoSeleccionado || !this.dosis || !this.frecuencia || !this.duracion) {
    alert("❌ Todos los campos son obligatorios.");
    return;
  }

  // 🔥 Validar que la receta tenga un ID válido
  if (!this.receta.idReceta) {
    alert("❌ Error: No hay una receta seleccionada.");
    console.error("❌ No hay ID de receta al agregar medicamento.");
    return;
  }

  // 🔥 Validar que el medicamento tenga un ID válido
  if (!this.medicamentoSeleccionado) {
    alert("❌ Error: No se ha seleccionado un medicamento.");
    console.error("❌ ID de medicamento es null.");
    return;
  }

  // 📌 Estructura correcta que el backend espera recibir
  const medicamentoData = {
    receta: { idReceta: this.receta.idReceta },
    medicamento: { idMedicamento: this.medicamentoSeleccionado },
    dosis: this.dosis,
    frecuencia: this.frecuencia,
    duracion: this.duracion,
    diagnostico: this.diagnostico || null,
  };

  // 🔍 Mostrar los datos antes de enviarlos
  console.log("📌 Enviando medicamento al backend:", JSON.stringify(medicamentoData, null, 2));

  try {
    const response = await axios.post(`${API_URL}/recetas/medicamentos`, medicamentoData);
    console.log("✅ Medicamento agregado correctamente:", response.data);

    alert("✅ Medicamento agregado correctamente.");

    // 🔄 Cargar medicamentos después de agregar uno nuevo
    await this.cargarMedicamentosPorReceta();

    // 🔄 Resetear los campos después de agregar el medicamento
    this.medicamentoSeleccionado = "";
    this.dosis = "";
    this.frecuencia = "";
    this.duracion = "";
    this.diagnostico = "";

  } catch (error) {
    console.error("❌ Error al agregar medicamento:", error);

    if (error.response) {
      console.error("🛑 Respuesta del servidor:", error.response.data);
      alert(`Error al agregar el medicamento: ${error.response.data}`);
    } else {
      alert("Error al agregar el medicamento.");
    }
  }
},

async cargarMedicamentosPorReceta() {
  if (!this.receta.idReceta) {
    console.error("❌ No se puede cargar medicamentos: idReceta es null.");
    return;
  }

  try {
    console.log("🔄 Cargando medicamentos de la receta con ID:", this.receta.idReceta);
    const response = await axios.get(`${API_URL}/recetas/${this.receta.idReceta}/medicamentos`);
    this.receta.medicamentos = response.data || [];
    console.log("✅ Medicamentos actualizados:", this.receta.medicamentos);
  } catch (error) {
    console.error("❌ Error al cargar medicamentos:", error);
  }
},




    async gestionarReceta(cita) {
      console.log("📌 Gestionando receta para cita:", cita);

      this.citaSeleccionada = { ...cita };
      this.nuevaReceta.idCita = cita.idCita;
      this.nuevaReceta.idPaciente = cita.paciente?.idPaciente || null;
      this.nuevaReceta.idDoctor = cita.doctor?.idDoctor || null;

      if (!this.nuevaReceta.idPaciente || !this.nuevaReceta.idDoctor) {
        console.error("❌ Error: idPaciente o idDoctor son null.");
        alert("Error: La cita no tiene paciente o doctor asignado.");
        return;
      }

      try {
        console.log("⏳ Buscando receta existente...");
        const response = await axios.get(`${API_URL}/recetas/cita/${cita.idCita}`);
        this.receta = response.data || {};
        this.recetaExistente = Boolean(response.data);
        console.log("✅ Receta encontrada:", this.receta);
      } catch (error) {
        console.warn("⚠️ No hay receta registrada para esta cita.");
        this.recetaExistente = false;
      }

      this.mostrarModalReceta = true;
    },

    async crearRecetaDesdeCita() {
  console.log("📌 Creando receta con:", JSON.stringify(this.nuevaReceta, null, 2));

  try {
    const respuesta = await recetaService.crearReceta(this.nuevaReceta);

    if (!respuesta.idReceta) {
      console.error("❌ Error: El backend no devolvió un ID de receta válido.");
      alert("Error al crear la receta. Inténtalo de nuevo.");
      return;
    }

    this.receta = { ...respuesta, medicamentos: respuesta.medicamentos || [] };
    this.recetaExistente = true;
    this.mostrarAgregarMedicamento = true;

    console.log("✅ Receta creada con éxito:", this.receta);
  } catch (error) {
    console.error("❌ Error al generar la receta:", error);
    alert("Error al generar la receta.");
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
