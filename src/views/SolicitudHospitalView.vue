<template>
  <div class="container">
    <h2>Solicitar Convenio con Aseguradora</h2>

    <form @submit.prevent="enviar">
      <div class="mb-3">
        <label>Nombre del hospital</label>
        <input v-model="form.nombre" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Dirección</label>
        <input v-model="form.direccion" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Teléfono</label>
        <input v-model="form.telefono" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Aseguradora (Seguro)</label>
        <select v-model="form.aseguradora" class="form-select" required>
          <option disabled value="">Seleccione un seguro</option>
          <option v-for="seguro in seguros" :key="seguro._id" :value="seguro.nombre">
            {{ seguro.nombre }}
          </option>
        </select>
      </div>

      <button class="btn btn-primary" type="submit">Enviar Solicitud</button>
    </form>

    <div v-if="mensaje" class="alert alert-info mt-3">{{ mensaje }}</div>

    <div v-if="estadoSolicitud" class="card mt-4 p-3">
      <h5>Estado de la solicitud enviada</h5>
      <p><strong>Aseguradora:</strong> {{ estadoSolicitud.aseguradora }}</p>
      <p><strong>Estado:</strong>
        <span :class="estadoSolicitud.estado === 'aprobado' ? 'text-success' :
                      estadoSolicitud.estado === 'rechazado' ? 'text-danger' : 'text-warning'">
          {{ estadoSolicitud.estado }}
        </span>
      </p>
    </div>

    <hr class="my-4" />
    <h4>Historial de Solicitudes</h4>
    <table class="table table-dark table-bordered mt-3">
      <thead>
        <tr>
          <th>#</th>
          <th>Aseguradora</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(solicitud, index) in historialSolicitudes" :key="solicitud._id">
          <td>{{ index + 1 }}</td>
          <td>{{ solicitud.aseguradora }}</td>
          <td>
            <span :class="{
              'text-success': solicitud.estado === 'aprobado',
              'text-danger': solicitud.estado === 'rechazado',
              'text-warning': solicitud.estado === 'pendiente'
            }">
              {{ solicitud.estado }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import API_URL from "@/config"; // Backend de Quarkus

// ✅ ESTA ES LA FORMA CORRECTA: Array de URLs
const EXPRESS_URLS = [
  "http://localhost:5001",
  "http://localhost:5022"
];

const form = ref({
  nombre: "",
  direccion: "",
  telefono: "",
  aseguradora: ""
});

const mensaje = ref("");
const estadoSolicitud = ref<any>(null);
const seguros = ref<any[]>([]);
const historialSolicitudes = ref<any[]>([]);

// ✅ Cargar seguros desde TODAS las aseguradoras
const cargarSeguros = async () => {
  seguros.value = []; // Limpiar antes
  const resultados = [];

  for (const url of EXPRESS_URLS) {
    try {
      const res = await fetch(`${url}/api/seguros`);
      if (res.ok) {
        const data = await res.json();
        resultados.push(...data);
      }
    } catch (err) {
      console.error(`Error cargando seguros de ${url}`, err);
    }
  }

  seguros.value = resultados;
};

// ✅ Cargar historial desde TODAS las aseguradoras
const cargarHistorial = async () => {
  historialSolicitudes.value = []; // Limpiar antes
  const resultados = [];

  for (const url of EXPRESS_URLS) {
    try {
      const res = await fetch(`${url}/api/solicitudes`);
      if (res.ok) {
        const data = await res.json();
        resultados.push(...data);
      }
    } catch (err) {
      console.error(`Error cargando historial de ${url}`, err);
    }
  }

  historialSolicitudes.value = resultados;
};

// Enviar solicitud al backend Quarkus
const enviar = async () => {
  const yaAprobada = historialSolicitudes.value.some((s) =>
  s.estado === "aprobado" && s.aseguradora === form.value.aseguradora
);
  if (yaAprobada) {
    mensaje.value = "Ya existe una solicitud aprobada. No se puede enviar otra.";
    return;
  }

  try {
    const res = await fetch(`${API_URL}/hospital/solicitudes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form.value)
    });

    if (res.ok) {
      const data = await res.json();
      mensaje.value = "Solicitud enviada a la aseguradora.";
      estadoSolicitud.value = data;
      form.value = { nombre: "", direccion: "", telefono: "", aseguradora: "" };
      cargarHistorial(); // Refrescar
    } else {
      mensaje.value = "Error al enviar solicitud.";
    }
  } catch (err) {
    console.error(err);
    mensaje.value = "Error de conexión.";
  }
};

// Inicialización
onMounted(() => {
  cargarSeguros();
  cargarHistorial();
});
</script>
