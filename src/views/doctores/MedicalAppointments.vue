<template>
  <div class="cita-container">
    <h2>Gestión de Citas Médicas</h2>

    <!-- Buscador de pacientes -->
    <div class="form-group">
      <label>Buscar Paciente</label>
      <input
        type="text"
        v-model="filtroPaciente"
        placeholder="Ingrese nombre o identificación"
        @input="buscarPaciente"
      />
      <ul v-if="pacientes.length">
        <li v-for="paciente in pacientes" :key="paciente.idPaciente" @click="seleccionarPaciente(paciente)">
          {{ paciente.usuario?.nombreUsuario || "Desconocido" }} - {{ paciente.documento }}
        </li>
      </ul>
    </div>

    <!-- Formulario para agendar citas -->
    <form @submit.prevent="guardarCita">
      <div class="form-group">
        <label>Nombre del Paciente</label>
        <input type="text" v-model="cita.pacienteNombre" readonly />
      </div>
      <div class="form-group">
        <label>Número de Identificación</label>
        <input type="text" v-model="cita.identificacion" readonly />
      </div>
      <div class="form-group">
        <label>Seleccionar Doctor</label>
        <select v-model="cita.idDoctor" required>
          <option value="" disabled>Seleccione un doctor</option>
          <option v-for="doctor in doctores" :key="doctor.idDoctor" :value="doctor.idDoctor">
            {{ doctor.usuario?.nombreUsuario || "Desconocido" }} - {{ doctor.especialidad || "Sin especialidad" }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label>Fecha de la Cita</label>
        <input type="date" v-model="cita.fecha" required />
      </div>
      <div class="form-group">
        <label>Hora de Inicio</label>
        <select v-model="cita.horaInicio" required>
          <option v-for="hora in horasDisponibles" :key="hora" :value="hora">{{ hora }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>Hora de Fin</label>
        <select v-model="cita.horaFin" required>
          <option v-for="hora in horasDisponibles" :key="hora" :value="hora">{{ hora }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>Notas Adicionales</label>
        <textarea v-model="cita.notas"></textarea>
      </div>

      <button type="submit">Agendar Cita</button>
    </form>

    <!-- Calendario para visualizar las citas -->
    <FullCalendar :options="calendarOptions" />
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import citaService from "@/services/citaService.js";
import doctorService from "@/services/doctorService.js";
import pacienteService from "@/services/pacienteService.js";

export default {
  components: {
    FullCalendar,
  },
  setup() {
    const filtroPaciente = ref("");
    const pacientes = ref([]);
    const cita = ref({
      pacienteNombre: "",
      identificacion: "",
      idPaciente: "",
      idDoctor: "",
      fecha: "",
      horaInicio: "",
      horaFin: "",
      notas: "",
    });

    const doctores = ref([]);
    const citasExistentes = ref([]);
    const horasDisponibles = ref(generarHoras());
    const calendarOptions = ref({
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      initialView: "dayGridMonth",
      editable: false,
      events: [],
    });

    async function cargarDoctores() {
  try {
    const response = await doctorService.getAllDoctors();
    console.log("📌 Respuesta de la API de doctores:", response); // ✅ DEBUG

    if (Array.isArray(response) && response.length > 0) {
      doctores.value = response;
      console.log("✅ Lista de doctores cargada en el frontend:", doctores.value);
    } else {
      console.warn("⚠️ No se encontraron doctores en la API.");
      doctores.value = [];
    }
  } catch (error) {
    console.error("❌ Error al obtener doctores:", error);
    doctores.value = [];
  }
}


    async function cargarCitas() {
      try {
        const response = await citaService.obtenerCitas();
        if (Array.isArray(response.data)) {
          citasExistentes.value = response.data;
          calendarOptions.value.events = citasExistentes.value.map((cita) => ({
            title: `Cita con ${cita.paciente.usuario.nombreUsuario}`,
            start: `${cita.fecha}T${cita.horaInicio}`,
            end: `${cita.fecha}T${cita.horaFin}`,
          }));
        }
      } catch (error) {
        console.error("❌ Error al obtener citas:", error);
      }
    }

    async function buscarPaciente() {
  if (filtroPaciente.value.length >= 3) {
    try {
      const response = await pacienteService.getAllPaciente();
      console.log("📌 Respuesta de la API de pacientes:", response); // 🔍 DEBUG

      let pacientesData = response.data;

      // ⬇️ Verificar si `response.data` es un string y si tiene un formato válido
      if (typeof pacientesData === "string") {
        try {
          pacientesData = JSON.parse(pacientesData);
        } catch (error) {
          console.error("❌ Error al parsear JSON de pacientes:", error, "⚠️ Datos recibidos:", response.data);
          pacientes.value = [];
          return;
        }
      }

      // ⬇️ Verificar si `pacientesData` es un array antes de aplicar `filter()`
      if (Array.isArray(pacientesData)) {
        pacientes.value = pacientesData.filter(
          p =>
            p.usuario?.rol?.roleName === "Paciente" && (
              p.documento.includes(filtroPaciente.value) ||
              p.usuario?.nombreUsuario?.toLowerCase().includes(filtroPaciente.value.toLowerCase())
            )
        );
      } else {
        console.warn("⚠️ La API no devolvió un array de pacientes:", pacientesData);
        pacientes.value = [];
      }
    } catch (error) {
      console.error("❌ Error al buscar pacientes:", error);
    }
  } else {
    pacientes.value = [];
  }
}




    function seleccionarPaciente(paciente) {
      cita.value.pacienteNombre = paciente.usuario?.nombreUsuario || "Desconocido";
      cita.value.identificacion = paciente.documento;
      cita.value.idPaciente = paciente.idPaciente;
      pacientes.value = [];
      filtroPaciente.value = "";
    }

    async function guardarCita() {
  try {
    console.log("📌 Enviando cita:", cita.value);

    const { fecha, idDoctor, horaInicio, horaFin, idPaciente, notas } = { ...cita.value };
    const fechaHoy = new Date().toISOString().split("T")[0];

    // Validaciones previas
    if (fecha < fechaHoy) {
      alert("❌ No puedes agendar una cita en el pasado.");
      return;
    }

    if (!horasDisponibles.value.includes(horaInicio) || !horasDisponibles.value.includes(horaFin)) {
      alert("❌ Horario no permitido. Seleccione una hora válida.");
      return;
    }

    if (horaInicio >= horaFin) {
      alert("❌ La hora de fin debe ser posterior a la hora de inicio.");
      return;
    }

    const citaExistente = citasExistentes.value.some(
      (c) => c.fecha === fecha && c.idDoctor === idDoctor && c.horaInicio === horaInicio
    );

    if (citaExistente) {
      alert("❌ Ya existe una cita con este doctor en esta fecha y hora.");
      return;
    }

    // 📌 Enviar cita con `estado`
    await citaService.agendarCita({
      fecha,
      idDoctor,
      horaInicio,
      horaFin,
      idPaciente,
      motivo: notas,
      idHospital: 22,  // Cambia si es necesario
      estado: "PENDIENTE",
    });

    alert("✅ Cita agendada con éxito!");

    // Refrescar datos
    limpiarFormulario();
    cargarCitas();
  } catch (error) {
    console.error("❌ Error al agendar cita:", error);
    alert("Hubo un error al agendar la cita.");
  }
}




    function limpiarFormulario() {
      cita.value = {
        pacienteNombre: "",
        identificacion: "",
        idPaciente: "",
        idDoctor: "",
        fecha: "",
        horaInicio: "",
        horaFin: "",
        notas: "",
      };
    }

    function generarHoras() {
      const horas = [];
      let hora = 8;
      let minutos = 0;

      while (hora < 16 || (hora === 16 && minutos === 0)) {
        horas.push(`${hora.toString().padStart(2, "0")}:${minutos.toString().padStart(2, "0")}`);
        minutos += 30;
        if (minutos === 60) {
          minutos = 0;
          hora++;
        }
      }
      return horas;
    }

    onMounted(() => {
      cargarDoctores();
      cargarCitas();
    });

    return {
      filtroPaciente,
      pacientes,
      cita,
      doctores,
      horasDisponibles,
      guardarCita,
      buscarPaciente,
      seleccionarPaciente,
      calendarOptions,
    };
  },
};
</script>



<style scoped>
.cita-container {
  max-width: 800px;
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
