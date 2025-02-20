<template>
  <div class="admin-servicios">
    <h2>Administración de Servicios</h2>
    <div class="form">
      <input v-model="form.nombre" type="text" placeholder="Nombre del Servicio" />
      <input v-model="form.categoria" type="text" placeholder="Categoría" />
      <input v-model="form.subcategoria" type="text" placeholder="Subcategoría" />
      <input v-model.number="form.costo" type="number" placeholder="Costo" />
      <label>
        <input v-model="form.cubiertoSeguro" type="checkbox" /> Cubierto por Seguro
      </label>
      <button @click="addServicio">Agregar Servicio</button>
    </div>
    <ul class="servicios-list">
      <li v-for="servicio in servicios" :key="servicio.id">
        {{ servicio.nombre }} - {{ servicio.categoria }} - {{ servicio.subcategoria }} - ${{ servicio.costo }} -
        {{ servicio.cubiertoSeguro ? "Sí" : "No" }}
        <button @click="removeServicio(servicio.id)">Eliminar</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from "vue";

const servicios = ref([
  { id: 1, nombre: "Consulta General", categoria: "Consulta", subcategoria: "General", costo: 50, cubiertoSeguro: false },
  { id: 2, nombre: "Examen de Sangre", categoria: "Laboratorio", subcategoria: "Hematología", costo: 30, cubiertoSeguro: true },
]);

const form = ref({
  nombre: "",
  categoria: "",
  subcategoria: "",
  costo: "",
  cubiertoSeguro: false,
});

const addServicio = () => {
  if (!form.value.nombre || !form.value.categoria || !form.value.subcategoria || !form.value.costo) return;
  servicios.value.push({ id: servicios.value.length + 1, ...form.value });
  form.value = { nombre: "", categoria: "", subcategoria: "", costo: "", cubiertoSeguro: false };
};

const removeServicio = (id) => {
  servicios.value = servicios.value.filter(servicio => servicio.id !== id);
};
</script>

<style scoped>
.admin-servicios {
  max-width: 600px;
  margin: auto;
  padding: 20px;
}
.form input {
  display: block;
  margin-bottom: 10px;
  padding: 5px;
}
.servicios-list li {
  margin: 10px 0;
  list-style: none;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
button {
  margin-left: 10px;
  padding: 5px 10px;
}
</style>
