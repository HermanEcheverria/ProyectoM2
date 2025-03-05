<template>
  <div class="container">
    <h2>Historial de pagos y facturación</h2>
    <table class="payment-table">
      <thead>
        <tr>
          <th>ID Pago</th>
          <th>Fecha</th>
          <th>Paciente</th>
          <th>Monto</th>
          <th>Detalles del Servicio</th>
          <th>Método de Pago</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pago in pagos" :key="pago.id">
          <td>{{ pago.id }}</td>
          <td>{{ pago.fecha }}</td>
          <td>{{ pago.paciente }}</td>
          <td>Q {{ pago.monto.toFixed(2) }}</td>
          <td>{{ pago.servicio }}</td>
          <td>{{ pago.metodoPago }}</td>
          <td>
            <span :class="{'status-paid': pago.estado === 'Pagado', 'status-pending': pago.estado === 'Pendiente'}">
              {{ pago.estado }}
            </span>
          </td>
          <td>
            <button class="btn-detail">Detalle</button>
            <button class="btn-invoice">Generar Factura</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pagos: [
        { id: 1, fecha: "20/02/2025", paciente: "Juan Pérez López", monto: 500, servicio: "Consulta General", metodoPago: "Tarjeta", estado: "Pagado" },
        { id: 2, fecha: "15/02/2025", paciente: "María Gómez Ruiz", monto: 1200, servicio: "Cirugía de Úlcera", metodoPago: "Seguro", estado: "Pagado" },
        { id: 3, fecha: "10/02/2025", paciente: "Carlos Méndez Díaz", monto: 800, servicio: "Tratamiento de Fractura", metodoPago: "Efectivo", estado: "Pendiente" },
        { id: 4, fecha: "05/02/2025", paciente: "Ana López Herrera", monto: 350, servicio: "Chequeo General", metodoPago: "Tarjeta", estado: "Pagado" },
        { id: 5, fecha: "01/02/2025", paciente: "Pedro Sánchez Jiménez", monto: 700, servicio: "Terapia Respiratoria", metodoPago: "Seguro", estado: "Pagado" }
      ]
    };
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
}

h2 {
  text-align: center;
  margin-bottom: 15px;
  color: #2e7d32;
}

.payment-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #1b5e20;
  color: white;
}

.status-paid {
  color: green;
  font-weight: bold;
}

.status-pending {
  color: red;
  font-weight: bold;
}

.btn-detail, .btn-invoice {
  padding: 5px 10px;
  margin: 2px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.btn-detail {
  background-color: #1976d2;
  color: white;
}

.btn-invoice {
  background-color: #388e3c;
  color: white;
}

.btn-detail:hover {
  background-color: #1565c0;
}

.btn-invoice:hover {
  background-color: #2e7d32;
}
</style>
