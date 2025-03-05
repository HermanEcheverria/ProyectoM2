<template>
  <div class="container">
    <div class="header">📦 Control de Stock de Medicamentos</div>

    <div class="section">
    <table>
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
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

.stock-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th, td {
  padding: 12px;
  border: 1px solid #DAFDBA;
  text-align: center; /* Alinear al centro */
  vertical-align: middle;
}

th {
  background: #01324b;
}

.low-stock {
  color: ff8a7d;
  font-weight: bold;
}

.status-low {
  color: ff8a7d;
  font-weight: bold;
}

.status-sufficient {
  color: #DAFDBA;
  font-weight: bold;

}

.btn-update {
  margin-top: 15px;
  padding: 10px 15px;
  background-color: #DAFDBA;
  color: #01324b;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.btn-update:hover {
  background-color: #2e7d32;
}
</style>
