<template>
  <div class="container">
    <h2>📦 Control de Stock de Medicamentos</h2>
    <table class="stock-table">
      <thead>
        <tr>
          <th>Medicamento</th>
          <th>Cantidad</th>
          <th>Entradas</th>
          <th>Salidas</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="medicamento in medicamentos" :key="medicamento.id">
          <td>{{ medicamento.nombre }}</td>
          <td :class="{'low-stock': medicamento.cantidad <= 10}">{{ medicamento.cantidad }}</td>
          <td>{{ medicamento.entradas }}</td>
          <td>{{ medicamento.salidas }}</td>
          <td>
            <span :class="{'status-low': medicamento.estado === 'Bajo Stock', 'status-sufficient': medicamento.estado === 'Suficiente'}">
              {{ medicamento.estado }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>
    <button class="btn-update" @click="actualizarInventario">🔄 Actualizar Inventario</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      medicamentos: [
        { id: 1, nombre: "Paracetamol", cantidad: 8, entradas: 10, salidas: 15, estado: "Bajo Stock" },
        { id: 2, nombre: "Ibuprofeno", cantidad: 20, entradas: 5, salidas: 9, estado: "Suficiente" },
        { id: 3, nombre: "Amoxicilina", cantidad: 7, entradas: 7, salidas: 9, estado: "Bajo Stock" },
        { id: 4, nombre: "Omeprazol", cantidad: 25, entradas: 15, salidas: 5, estado: "Suficiente" }
      ]
    };
  },
  methods: {
    actualizarInventario() {
      alert("Inventario actualizado con éxito.");
      // Aquí podrías hacer una petición a la API para actualizar los datos reales.
    }
  }
};
</script>

<style scoped>
.container {
  width: 90%;
  margin: 20px auto;
  padding: 20px;
  background-color: #e8f5e9;
  border-radius: 8px;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h2 {
  color: #2e7d32;
  margin-bottom: 15px;
}

.stock-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

th, td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #1b5e20;
  color: white;
}

.low-stock {
  color: red;
  font-weight: bold;
}

.status-low {
  color: red;
  font-weight: bold;
}

.status-sufficient {
  color: green;
  font-weight: bold;
}

.btn-update {
  margin-top: 15px;
  padding: 10px 15px;
  background-color: #388e3c;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.btn-update:hover {
  background-color: #2e7d32;
}
</style>
