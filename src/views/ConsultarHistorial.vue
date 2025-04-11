<template>
  <div class="container mt-4 text-white-custom">
    <h2>Consultar Historial del Paciente</h2>

    <!-- Campo para ingresar el DPI -->
    <div class="mb-3">
      <label>DPI del paciente:</label>
      <input v-model="dpi" class="form-control" placeholder="Ingrese DPI" />
    </div>

    <!-- Seleccionar aseguradora -->
    <div class="mb-3">
      <label>Aseguradora:</label>
      <select v-model="aseguradoraId" class="form-select">
        <option disabled value="">Seleccione aseguradora</option>
        <option v-for="aseg in aseguradoras" :key="aseg._id" :value="aseg._id">
          {{ aseg.nombre }}
        </option>
      </select>
    </div>

    <!-- Botón de búsqueda -->
    <button @click="buscarHistorial" class="btn">Buscar</button>

    <!-- Mostrar resultados si se encuentra el cliente -->
    <div v-if="cliente" class="mt-4">
      <h5>Cliente encontrado: {{ cliente.nombre }} {{ cliente.apellido }}</h5>
      <p><strong>Afiliación:</strong> {{ cliente.numeroAfiliacion }}</p>
      <p><strong>Póliza:</strong> {{ cliente.polizaNombre }}</p>

      <table class="tabla-historial mt-3">
        <thead>
          <tr>
            <th>HOSPITAL</th>
            <th>SERVICIO</th>
            <th>FECHA</th>
            <th>COSTO (HOSPITAL)</th>
            <th>COSTO (ASEGURADORA)</th>
            <th>COPAGO</th>
            <th>ESTADO</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in historial" :key="item._id">
            <td>{{ item.hospital?.nombre || "Desconocido" }}</td>
            <td>{{ item.servicio?.nombre || "Desconocido" }}</td>
            <td>{{ new Date(item.fechaServicio).toLocaleDateString() }}</td>
            <td>Q{{ item.costo }}</td>
            <td>Q{{ item.servicio?.precioAseguradora || "N/A" }}</td>
            <td>Q{{ item.copago }}</td>
            <td>
              <span :class="item.estadoCopago === 'pagado' ? 'estado-pagado' : 'estado-pendiente'">
                {{ item.estadoCopago }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Si ya se buscó y no se encontró cliente -->
    <div v-else-if="buscado">
      <p class="text-danger">Cliente no encontrado.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { buscarClientePorDpiYAseguradora } from "../services/clientesHospitalService";

const dpi = ref("");
const aseguradoraId = ref("");
const aseguradoras = ref([]);
const cliente = ref(null);
const historial = ref([]);
const buscado = ref(false);

const buscarHistorial = async () => {
  cliente.value = null;
  historial.value = [];
  buscado.value = false;

  if (!dpi.value || !aseguradoraId.value) return;

  try {
    const fetchedCliente = await buscarClientePorDpiYAseguradora(dpi.value, aseguradoraId.value);
    cliente.value = fetchedCliente;
    historial.value = fetchedCliente.historialServicios;
    buscado.value = true;
  } catch (error) {
    console.error("Error en la búsqueda:", error.message);
    cliente.value = null;
    historial.value = [];
    buscado.value = true;
  }
};

const cargarAseguradoras = async () => {
  try {
    const res = await fetch("http://localhost:5001/api/seguros");
    aseguradoras.value = await res.json();
  } catch (err) {
    console.error("Error al cargar aseguradoras", err);
  }
};

onMounted(() => {
  cargarAseguradoras();
});
</script>

<style scoped>
.text-white-custom {
  color: white;
}

.table,
.tabla-historial {
  width: 100%;
  border-collapse: collapse;
}

.tabla-historial th,
.tabla-historial td {
  padding: 8px;
  border: 1px solid #ccc;
  color: white;
}

.tabla-historial thead {
  background-color: #444;
}

.tabla-historial tbody tr:nth-child(even) {
  background-color: #222;
}

.tabla-historial tbody tr:nth-child(odd) {
  background-color: #111;
}

.estado-pagado {
  color: #4caf50;
  font-weight: bold;
}

.estado-pendiente {
  color: #f44336;
  font-weight: bold;
}

.btn {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
