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
        <select v-model="form.aseguradora" class="form-select" required @change="cargarClientesDesdeAseguradora">
          <option disabled value="">Seleccione un seguro</option>
          <option v-for="aseg in seguros" :key="aseg.nombre" :value="aseg">
            {{ aseg.nombre }}
          </option>
        </select>
      </div>

      <!-- Lista de clientes de la aseguradora seleccionada -->
      <div v-if="clientes.length" class="mt-4">
        <h5>Clientes de esta Aseguradora</h5>
        <ul class="list-group">
          <li v-for="cliente in clientes" :key="cliente._id" class="list-group-item">
            {{ cliente.nombre }} {{ cliente.apellido }} - {{ cliente.numeroAfiliacion }}
          </li>
        </ul>
      </div>

      <button class="btn btn-primary" type="submit">Enviar Solicitud</button>
    </form>

    <div v-if="mensaje" class="alert alert-info mt-3">{{ mensaje }}</div>

    <div v-if="estadoSolicitud" class="card mt-4 p-3">
      <h5>Estado de la solicitud enviada</h5>
      <p><strong>Aseguradora:</strong> {{ estadoSolicitud.aseguradora }}</p>
      <p><strong>Estado:</strong>
        <span :class="estadoSolicitud.estado === 'aprobado' ? 'text-success' : estadoSolicitud.estado === 'rechazado' ? 'text-danger' : 'text-warning'">
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
        <tr v-for="(solicitud, index) in historialSolicitudes" :key="solicitud._id || index">
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

    <hr class="my-5" />
    <h4>Registrar nueva Aseguradora</h4>
    <form @submit.prevent="registrarAseguradora">
      <div class="mb-3">
        <label>Nombre de la Aseguradora</label>
        <input v-model="nuevaAseguradora.nombre" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>URL base de conexión</label>
        <input v-model="nuevaAseguradora.url" class="form-control" required />
      </div>

      <button class="btn btn-success">Registrar Aseguradora</button>
    </form>

    <div v-if="mensajeRegistro" class="alert alert-info mt-3">{{ mensajeRegistro }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import API_URL from "@/config";

const form = ref({
  nombre: "",
  direccion: "",
  telefono: "",
  aseguradora: null
});

const mensaje = ref("");
const estadoSolicitud = ref<any>(null);
const seguros = ref<any[]>([]);
const historialSolicitudes = ref<any[]>([]);
const nuevaAseguradora = ref({ nombre: "", url: "" });
const mensajeRegistro = ref("");
const clientes = ref([]);

const registrarAseguradora = async () => {
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras/registrar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevaAseguradora.value)
    });
    if (res.ok) {
      mensajeRegistro.value = "Aseguradora registrada con éxito.";
      nuevaAseguradora.value = { nombre: "", url: "" };
      await cargarSeguros();
      await cargarHistorial();
    } else if (res.status === 409) {
      mensajeRegistro.value = "La aseguradora ya existe.";
    } else {
      mensajeRegistro.value = "Error al registrar aseguradora.";
    }
  } catch (err) {
    console.error(err);
    mensajeRegistro.value = "Error de conexión.";
  }
};

const cargarSeguros = async () => {
  seguros.value = [];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras`);
    if (!res.ok) {
      console.error("No se pudo obtener la lista de aseguradoras");
      return;
    }
    const aseguradoras = await res.json();
    seguros.value = aseguradoras.map((a: any) => ({
      nombre: a.nombre,
      url: a.urlBase
    }));
  } catch (err) {
    console.error("Error al cargar aseguradoras registradas:", err);
  }
};

const cargarClientesDesdeAseguradora = async () => {
  clientes.value = [];
  if (!form.value.aseguradora?.url) return;

  const hospitalId = localStorage.getItem("hospitalId") || "67f88b2f87f154c62d78d4e2";

  try {
    const res = await fetch(`${form.value.aseguradora.url}/api/clientes/hospital/${hospitalId}/aseguradora`);
    if (res.ok) {
      clientes.value = await res.json();
    } else {
      console.warn("No se pudieron cargar los clientes desde la aseguradora.");
    }
  } catch (err) {
    console.error("Error al conectar con aseguradora:", err);
  }
};

const cargarHistorial = async () => {
  historialSolicitudes.value = [];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras`);
    const aseguradoras = await res.json();
    const resultados: any[] = [];
    for (const a of aseguradoras) {
      try {
        const resHist = await fetch(`${a.urlBase}/solicitudes-atencion`);
        if (resHist.ok) {
          const data = await resHist.json();
          resultados.push(...data);
        }
      } catch (e) {
        console.error("Error cargando historial desde:", a.urlBase);
      }
    }
    historialSolicitudes.value = resultados;
  } catch (err) {
    console.error("Error al cargar historial:", err);
  }
};

const enviar = async () => {
  const aseguradoraSeleccionada = form.value.aseguradora;

  if (!aseguradoraSeleccionada || !aseguradoraSeleccionada.url) {
    mensaje.value = "No se encontró la URL de la aseguradora seleccionada.";
    return;
  }

  const yaAprobada = historialSolicitudes.value.some((s) =>
    s.estado === "aprobado" && s.aseguradora === aseguradoraSeleccionada.nombre
  );

  if (yaAprobada) {
    mensaje.value = "Ya existe una solicitud aprobada. No se puede enviar otra.";
    return;
  }

  const urlDestino = `${aseguradoraSeleccionada.url}/solicitudes/hospital`;

  try {
    const res = await fetch(urlDestino, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: form.value.nombre,
        direccion: form.value.direccion,
        telefono: form.value.telefono,
        aseguradora: aseguradoraSeleccionada.nombre,
        estado: "pendiente",
        origen: "hospital"
      })
    });

    if (res.ok) {
      const data = await res.json();
      mensaje.value = "Solicitud enviada a la aseguradora.";
      estadoSolicitud.value = data;
      form.value = { nombre: "", direccion: "", telefono: "", aseguradora: null };
      await cargarHistorial();
    } else {
      mensaje.value = "Error al enviar solicitud.";
    }
  } catch (err) {
    console.error(err);
    mensaje.value = "Error de conexión.";
  }
};

onMounted(() => {
  cargarSeguros();
  cargarHistorial();
});
</script>
