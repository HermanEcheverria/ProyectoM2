<template>
  <div class="admin-servicios">
    <h2>Administración de Servicios</h2>

    <div class="form">
      <input v-model="form.nombre" type="text" placeholder="Nombre del Servicio" class="input" />
      <input v-model.number="form.costo" type="number" placeholder="Costo" class="input" />

      <label class="checkbox-container">
        <input v-model="form.cubiertoSeguro" type="checkbox" />
        <span>¿Cubierto por Seguro?</span>
      </label>

      <label>Servicio Padre (Opcional):</label>
      <select v-model="form.parentId" class="input">
        <option :value="null">Ninguno (Categoría principal)</option>
        <option v-for="s in servicios" :value="s.id" :key="s.id">
          {{ s.nombre }}
        </option>
      </select>

      <button @click="registrarServicio" class="btn btn-primary">Agregar Servicio</button>
    </div>

    <ul class="servicios-list">
      <li v-for="servicio in servicios" :key="servicio.id" class="card">
        <div class="card-header">
          <h3>{{ servicio.nombre }}</h3>
          <span class="precio">${{ servicio.costo }}</span>
        </div>

        <div class="card-body">
          <p>
            <strong>Cubierto Seguro:</strong>
            <span :class="{'text-green': servicio.cubiertoSeguro, 'text-red': !servicio.cubiertoSeguro}">
              {{ servicio.cubiertoSeguro ? "Sí" : "No" }}
            </span>
          </p>
        </div>

        <div class="acciones">
          <button @click="removeServicio(servicio.id)" class="btn btn-danger">Eliminar</button>
        </div>

        <!-- Subservicios visibles directamente -->
        <ul v-if="servicio.subServicios.length > 0" class="subservicio-list">
          <li v-for="sub in servicio.subServicios" :key="sub.id" class="subservicio-item">
            <div class="subservicio-nombre">
              <span class="subservicio-icon">↳</span> {{ sub.nombre }}
            </div>
            <button @click="eliminarRelacion(servicio.id, sub.id)" class="btn btn-danger btn-small">Eliminar</button>
          </li>
        </ul>

        <!-- Formulario para agregar subservicio -->
        <div class="subservicio-form">
          <select v-model="subservicioSeleccionadoPorServicio[servicio.id]" class="input">
            <option :value="null">Selecciona un servicio</option>
            <option v-for="s in servicios" :value="s.id" :key="s.id">{{ s.nombre }}</option>
          </select>
          <button @click="agregarRelacion(servicio.id)" class="btn btn-secondary">Agregar Subservicio</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import {
  getServicios,
  addServicio as agregarServicio,
  deleteServicio,
  addSubServicio,
  deleteSubServicio
} from "../../services/servicioService";

const servicios = ref([]);
const form = ref({
  nombre: "",
  costo: "",
  cubiertoSeguro: false,
  parentId: null,
});

// 🔹 Objeto reactivo para almacenar subservicios seleccionados por servicio padre
const subservicioSeleccionadoPorServicio = reactive({});

// Cargar servicios al montar el componente
const cargarServicios = async () => {
  try {
    servicios.value = await getServicios();
  } catch (error) {
    console.error("Error al cargar los servicios:", error);
  }
};

onMounted(cargarServicios);

// Agregar un servicio con jerarquía
const registrarServicio = async () => {
  if (!form.value.nombre || !form.value.costo) {
    alert("Todos los campos son obligatorios.");
    return;
  }

  try {
    await agregarServicio(form.value, form.value.parentId);
    form.value = { nombre: "", costo: "", cubiertoSeguro: false, parentId: null };
    await cargarServicios();
  } catch (error) {
    console.error("Error al agregar servicio:", error);
  }
};

// Eliminar un servicio
const removeServicio = async (id) => {
  try {
    await deleteServicio(id);
    await cargarServicios();
  } catch (error) {
    console.error("Error al eliminar servicio:", error);
  }
};

// Agregar un subservicio correctamente
const agregarRelacion = async (id) => {
  const subServicioId = subservicioSeleccionadoPorServicio[id];

  if (!subServicioId) {
    alert("Selecciona un servicio para agregar como subservicio.");
    return;
  }

  // 🔹 Buscar el objeto del servicio seleccionado
  const subServicio = servicios.value.find(s => s.id === subServicioId);
  if (!subServicio) {
    alert("Error: No se encontró el servicio seleccionado.");
    return;
  }

  try {
    // 🔹 Enviar el objeto completo con nombre, costo y cubiertoSeguro
    await addSubServicio(id, {
      nombre: subServicio.nombre,
      costo: subServicio.costo,
      cubiertoSeguro: subServicio.cubiertoSeguro
    });

    subservicioSeleccionadoPorServicio[id] = null;
    await cargarServicios();
  } catch (error) {
    console.error("Error al agregar subservicio:", error);
  }
};


// Eliminar relación de subservicio
const eliminarRelacion = async (id, subServicioId) => {
  try {
    await deleteSubServicio(id, subServicioId);
    await cargarServicios();
  } catch (error) {
    console.error("Error al eliminar subservicio:", error);
  }
};
</script>

<style scoped>
/* General */
.admin-servicios {
  max-width: 750px;
  margin: auto;
  padding: 20px;
  font-family: "Arial", sans-serif;
  color: #fff;
}

/* Formulario */
.form {
  background: #1e1e1e;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.15);
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.input {
  display: block;
  padding: 12px;
  width: 100%;
  border: 1px solid #666;
  border-radius: 5px;
  font-size: 16px;
  background: #2a2a2a;
  color: #fff;
}

/* Tarjetas de Servicios */
.card {
  background: #2a2a2a;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.12);
  margin-bottom: 15px;
  border-left: 5px solid #007bff;
  transition: all 0.3s ease;
  color: #fff !important;
}

.card:hover {
  transform: translateY(-3px);
}

/* Subservicios */
.subservicio-list {
  padding-left: 25px;
  margin-top: 8px;
  border-left: 3px solid #007bff;
}

.subservicio-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #3a3a3a;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 5px;
  border-left: 4px solid #6c757d;
  padding-left: 10px;
}

.subservicio-nombre {
  display: flex;
  align-items: center;
  font-weight: bold;
  color: #fff;
}

.subservicio-icon {
  color: #007bff;
  margin-right: 5px;
}
</style>
