<template>
  <div class="container mt-5">
    <h2>Registrar Atención Hospitalaria</h2>

    <form @submit.prevent="registrarAtencion">

      <!-- Aseguradora -->
      <div class="mb-3">
        <label class="form-label">Aseguradora</label>
        <select v-model="form.aseguradoraUrl" class="form-select" required @change="cargarClientes">
          <option disabled value="">Seleccione aseguradora</option>
          <option v-for="aseguradora in aseguradoras" :key="aseguradora._id" :value="aseguradora.url">
            {{ aseguradora.nombre }}
          </option>
        </select>
      </div>

      <!-- Buscar por DPI -->
      <div class="mb-3">
        <label class="form-label">Buscar por DPI</label>
        <div class="input-group">
          <input v-model="dpiBusqueda" class="form-control" placeholder="Ingrese DPI del cliente" />
          <button @click.prevent="buscarPorDPI" class="btn btn-secondary">Buscar y Verificar Póliza</button>
        </div>
      </div>

      <!-- Cliente asegurado -->
      <div class="mb-3">
        <label class="form-label">Cliente Asegurado</label>
        <select v-model="form.afiliado" class="form-select" required>
          <option disabled value="">Seleccione cliente</option>
          <option v-for="cliente in clientes" :key="cliente._id" :value="cliente._id">
            {{ cliente.nombre }} {{ cliente.apellido }}
          </option>
        </select>
      </div>

      <!-- Servicio -->
      <div class="mb-3">
        <label class="form-label">Servicio</label>
        <select v-model="form.servicio" class="form-select" required>
          <option disabled value="">Seleccione servicio</option>
          <option v-for="serv in servicios" :key="serv._id" :value="serv._id">
            {{ serv.nombre }} - Q{{ serv.precioAseguradora }}
          </option>
        </select>
      </div>

      <!-- Monto -->
      <div class="mb-3">
        <label class="form-label">Monto</label>
        <input v-model="form.monto" type="number" class="form-control" required />
      </div>

      <button type="submit" class="btn btn-primary">Enviar Solicitud</button>
    </form>

    <!-- Mensaje -->
    <div v-if="mensaje" class="alert alert-info mt-3">{{ mensaje }}</div>

    <!-- Detalles del cliente -->
    <div v-if="datosCliente" class="card mt-4 p-3 bg-light border">
      <h5>Detalles del Cliente</h5>
      <p><strong>Nombre:</strong> {{ datosCliente.nombre }} {{ datosCliente.apellido }}</p>
      <p><strong>Número de Afiliación:</strong> {{ datosCliente.numeroAfiliacion }}</p>
      <p><strong>Póliza:</strong> {{ datosCliente.polizaNombre }}</p>
      <p>
        <strong>Estado de Pago:</strong>
        <span :class="datosCliente.estadoPago ? 'text-success' : 'text-danger'">
          {{ datosCliente.estadoPago ? 'Al día' : 'Pendiente' }}
        </span>
      </p>

      <div v-if="autorizacionId" class="mt-3">
        <strong>ID de Autorización:</strong> {{ autorizacionId }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

//  Aquí declaramos todos los backends aseguradoras (original, dos, tres...)
const EXPRESS_URLS = [
  "http://localhost:5001",
  "http://localhost:5022",

];

const form = ref({
  afiliado: '',
  servicio: '',
  monto: 0,
  aseguradoraUrl: ''
});

const aseguradoras = ref([]);
const clientes = ref([]);
const servicios = ref([]);
const mensaje = ref('');
const dpiBusqueda = ref('');
const datosCliente = ref(null);
const autorizacionId = ref(null);
const hospitalId = localStorage.getItem("hospitalId") || "67f88b2f87f154c62d78d4e2";

// ✅ Cargar aseguradoras desde TODAS las aseguradoras
const cargarAseguradoras = async () => {
  aseguradoras.value = [];
  for (const url of EXPRESS_URLS) {
    try {
      const res = await fetch(`${url}/api/seguros`);
      if (res.ok) {
        const data = await res.json();
        data.forEach(seguro => seguro.url = url);
        aseguradoras.value.push(...data);
      }
    } catch (err) {
      console.error(`Error cargando aseguradoras desde ${url}`, err);
    }
  }
};

const cargarClientes = async () => {
  if (!form.value.aseguradoraUrl) return;
  try {
    const res = await fetch(`${form.value.aseguradoraUrl}/api/clientes/hospital/${hospitalId}/aseguradora`);
    if (res.ok) {
      clientes.value = await res.json();
    }
  } catch (err) {
    console.error("Error cargando clientes:", err);
  }
};

const buscarPorDPI = async () => {
  if (!dpiBusqueda.value || !form.value.aseguradoraUrl) {
    mensaje.value = "Selecciona aseguradora y escribe DPI";
    return;
  }

  try {
    const res = await fetch(`${form.value.aseguradoraUrl}/api/clientes/buscar-por-documento/${dpiBusqueda.value}`);
    if (!res.ok) throw new Error("No encontrado");

    const cliente = await res.json();
    form.value.afiliado = cliente._id;
    clientes.value = [cliente];
    datosCliente.value = cliente;
    mensaje.value = "Cliente encontrado.";
    autorizacionId.value = null;
  } catch (err) {
    mensaje.value = "Cliente no encontrado.";
    clientes.value = [];
    datosCliente.value = null;
    autorizacionId.value = null;
  }
};

const cargarServicios = async () => {
  servicios.value = [];

  for (const url of EXPRESS_URLS) {
    try {
      const res = await fetch(`${url}/api/servicios/hospital/${hospitalId}`);
      if (res.ok) {
        const data = await res.json();
        servicios.value.push(...data);
      }
    } catch (err) {
      console.error(`Error cargando servicios desde ${url}`, err);
    }
  }
};

const registrarAtencion = async () => {
  if (!form.value.aseguradoraUrl) {
    mensaje.value = "Selecciona una aseguradora para enviar.";
    return;
  }

  try {
    const payload = {
      afiliado: form.value.afiliado,
      servicio: form.value.servicio,
      monto: form.value.monto,
      hospital: hospitalId,
      aseguradora: form.value.aseguradoraUrl
    };

    const res = await fetch(`${form.value.aseguradoraUrl}/api/solicitudes-atencion`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (res.ok) {
      const data = await res.json();
      mensaje.value = data.message || "Solicitud enviada correctamente.";
      autorizacionId.value = data.numeroAutorizacion;
      form.value.servicio = '';
      form.value.monto = 0;
    } else {
      mensaje.value = "Error al enviar la solicitud.";
      autorizacionId.value = null;
    }

  } catch (err) {
    console.error(err);
    mensaje.value = "Error de conexión.";
    autorizacionId.value = null;
  }
};

onMounted(() => {
  cargarAseguradoras();
  cargarServicios();
});
</script>
