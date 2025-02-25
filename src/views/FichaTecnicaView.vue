<template>
  <div class="container">
    <!-- Título -->
    <div class="header">ADMINISTRACIÓN DE FICHAS TÉCNICAS</div>

    <!-- Opciones de Gestión -->
    <div class="actions">
      <button @click="crearFicha">🆕 Crear Nueva Ficha</button>
      <button @click="editarFicha">✏️ Editar Ficha</button>
      <button @click="eliminarFicha">🗑️ Eliminar Ficha</button>
      <button @click="verFicha">📄 Ver Ficha</button>
      <input v-model="buscarId" placeholder="🔍 Buscar Ficha por ID">
      <button @click="buscarFicha">🔎 Buscar</button>
    </div>

    <!-- Listado de Fichas Técnicas -->
    <div class="section">
      <h3>📋 LISTADO DE FICHAS TÉCNICAS</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Fecha Creada</th>
            <th>Última Modif.</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ficha in fichas" :key="ficha.id">
            <td>{{ ficha.id }}</td>
            <td>{{ ficha.nombre }}</td>
            <td>{{ ficha.fechaCreada }}</td>
            <td>{{ ficha.ultimaModif }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Datos del Paciente -->
    <div class="section">
      <h3>🧑‍⚕️ DATOS DEL PACIENTE</h3>
      <div class="form-group">
        <label>Nombre:</label>
        <input v-model="paciente.nombre">
        <label>ID:</label>
        <input v-model="paciente.id">
      </div>
      <div class="form-group">
        <label>Fecha de Nacimiento:</label>
        <input type="date" v-model="paciente.fechaNacimiento">
      </div>
      <div class="form-group">
        <label>Fotografía:</label>
        <input type="file">
      </div>
      <div class="form-group">
        <label>Documento de Identificación:</label>
        <input v-model="paciente.documentoIdentidad">
      </div>
      <div class="form-group">
        <label>Número de Afiliación al Seguro:</label>
        <input v-model="paciente.afiliacionSeguro">
        <label>Código de Seguro:</label>
        <input v-model="paciente.codigoSeguro">
      </div>
      <div class="form-group">
        <label>Número de Carnet de Seguro:</label>
        <input v-model="paciente.afiliacionSeguro">
        <label>Código de Seguro:</label>
        <input v-model="paciente.codigoSeguro">
      </div>
    </div>

    <!-- Historial de Servicios Médicos -->
    <div class="section">
      <h3>🏥 HISTORIAL DE SERVICIOS MÉDICOS</h3>
      <table>
        <thead>
          <tr>
            <th>Fecha</th>
            <th>Servicio</th>
            <th>Médico</th>
            <th>Costo</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="servicio in historial" :key="servicio.fecha">
            <td>{{ servicio.fecha }}</td>
            <td>{{ servicio.servicio }}</td>
            <td>{{ servicio.medico }}</td>
            <td>Q{{ servicio.costo }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Comentarios Médicos -->
    <div class="section">
      <h3>💬 COMENTARIOS MÉDICOS Y SEGUIMIENTO</h3>
      <div v-for="comentario in comentarios" :key="comentario.id" class="comentario">
        <b>{{ comentario.autor }}</b> ({{ comentario.fecha }}): {{ comentario.texto }}
      </div>
      <button @click="agregarComentario">💬 Agregar Comentario</button>
    </div>

    <!-- Botones de Acción -->
    <div class="actions">
      <button @click="guardarFicha">💾 Guardar</button>
      <button @click="editarFicha">🖊️ Editar</button>
      <button @click="eliminarFicha">🗑️ Eliminar</button>
      <button @click="exportarPDF">📄 Exportar PDF</button>
      <button @click="volver">⬅️ Volver</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      buscarId: "",
      fichas: [
        { id: "001", nombre: "Juan Pérez", fechaCreada: "12/02/2025", ultimaModif: "18/02/2025" },
        { id: "002", nombre: "María López", fechaCreada: "14/02/2025", ultimaModif: "17/02/2025" },
        { id: "003", nombre: "Carlos Gómez", fechaCreada: "16/02/2025", ultimaModif: "20/02/2025" },
      ],
      paciente: {
        nombre: "Juan Pérez",
        id: "001",
        fechaNacimiento: "1990-01-15",
        documentoIdentidad: "DPI-123456789",
        afiliacionSeguro: "987654",
        codigoSeguro: "A1B2C3",
      },
      historial: [
        { fecha: "10/02/2025", servicio: "Consulta General", medico: "Dr. Pérez", costo: "200.00" },
        { fecha: "15/02/2025", servicio: "Examen de Sangre", medico: "Dr. Gómez", costo: "150.00" },
        { fecha: "20/02/2025", servicio: "Cirugía", medico: "Dr. López", costo: "5000.00" },
      ],
      comentarios: [
        { id: 1, autor: "Dr. Pérez", fecha: "10/02/2025", texto: "El paciente muestra mejoría." },
        { id: 2, autor: "Enfermera López", fecha: "11/02/2025", texto: "Se han ajustado los medicamentos según lo indicado." },
      ],
    };
  },
  methods: {
    crearFicha() { alert("Crear nueva ficha en desarrollo."); },
    editarFicha() { alert("Editar ficha en desarrollo."); },
    eliminarFicha() { alert("Eliminar ficha en desarrollo."); },
    verFicha() { alert("Ver ficha en desarrollo."); },
    buscarFicha() { alert(`Buscando ficha con ID: ${this.buscarId}`); },
    agregarComentario() { alert("Agregar comentario en desarrollo."); },
    guardarFicha() { alert("Guardar ficha en desarrollo."); },
    exportarPDF() { alert("Exportar a PDF en desarrollo."); },
    volver() { alert("Volviendo a la lista."); },
  }
};
</script>

<style scoped>
/* Estilos generales */
.container {
    max-width: 900px;
    margin: auto;
    font-family: Arial, sans-serif;
    background: #0D1B2A;  /* Fondo oscuro */
    color: #E0E1DD;  /* Texto claro */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.2);
}

/* Cabecera */
.header {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    background: #1B263B;  /* Azul oscuro */
    color: #E0E1DD;
    padding: 15px;
    border-radius: 5px;
}

/* Secciones */
.section {
    background: #1B263B;
    padding: 15px;
    margin: 15px 0;
    border-radius: 5px;
    border: 1px solid #415A77;
}

/* Botones */
.actions {
    display: flex;
    justify-content: space-around;
    padding: 10px;
}

button {
    background: #00A86B; /* Verde resplandeciente */
    color: white;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: 0.3s;
}

button:hover {
    background: #009f5c;
}

/* Botón de eliminar */
button.eliminar {
    background: #D72638; /* Rojo */
}

button.eliminar:hover {
    background: #C21010;
}

/* Tablas */
table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
    background: #0D1B2A;
}

th, td {
    border: 1px solid #415A77;
    padding: 8px;
    text-align: center;
    color: #E0E1DD;
}

th {
    background: #415A77;
    font-weight: bold;
}

/* Inputs y formularios */
input, select, textarea {
    width: 100%;
    padding: 8px;
    margin-top: 5px;
    border: 1px solid #415A77;
    background: #0D1B2A;
    color: #E0E1DD;
    border-radius: 5px;
}

/* Comentarios */
.comentario {
    background: #1B263B;
    padding: 10px;
    margin: 5px 0;
    border-left: 4px solid #00A86B;
    border-radius: 5px;
}

</style>
