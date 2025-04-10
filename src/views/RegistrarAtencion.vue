<template>
  <div class="container mt-5">
    <h2>Registrar Atención Hospitalaria</h2>
    <form @submit.prevent="registrarAtencion">
      <div class="mb-3">
        <label class="form-label">ID del Cliente Asegurado</label>
        <input v-model="form.afiliado" type="text" class="form-control" required />
      </div>

      <div class="mb-3">
        <label class="form-label">ID del Servicio</label>
        <input v-model="form.servicio" type="text" class="form-control" required />
      </div>

      <div class="mb-3">
        <label class="form-label">Monto</label>
        <input v-model="form.monto" type="number" class="form-control" required />
      </div>

      <div class="mb-3">
        <label class="form-label">Aseguradora</label>
        <select v-model="form.aseguradoraId" class="form-select" required>
          <option disabled value="">Seleccione aseguradora</option>
          <option v-for="aseguradora in aseguradoras" :key="aseguradora._id" :value="aseguradora._id">
            {{ aseguradora.nombre }}
          </option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">Enviar Solicitud</button>
    </form>

    <div v-if="mensaje" class="alert alert-info mt-3">
      {{ mensaje }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { enviarSolicitudHospital } from '../services/solicitudesService'

const form = ref({
  afiliado: '',
  servicio: '',
  monto: 0,
  aseguradoraId: ''
})

const aseguradoras = ref([])
const mensaje = ref('')

const cargarAseguradoras = async () => {
  try {
    const res = await fetch("http://localhost:5001/api/seguros")
    if (res.ok) {
      aseguradoras.value = await res.json()
    }
  } catch (err) {
    console.error("❌ Error cargando aseguradoras:", err)
  }
}

const registrarAtencion = async () => {
  try {
    const hospitalId = localStorage.getItem("hospitalId") || "67f7cba9eb65224cb0ff978e"
    const response = await enviarSolicitudHospital({
      afiliado: form.value.afiliado,
      servicio: form.value.servicio,
      monto: form.value.monto,
      hospital: hospitalId,
      aseguradoraId: form.value.aseguradoraId
    })

    mensaje.value = "✅ Solicitud enviada correctamente."
    form.value = { afiliado: '', servicio: '', monto: 0, aseguradoraId: '' }
    console.log("Respuesta del servidor:", response)
  } catch (err) {
    mensaje.value = "❌ Error al enviar la solicitud."
    console.error(err)
  }
}

onMounted(() => {
  cargarAseguradoras()
})
</script>
