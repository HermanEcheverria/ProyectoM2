<template>
  <div class="formulario-receta">
    <h2>Ficha Técnica de Pacientes</h2>

    <!-- Campo: Nombre -->
    <div class="form-group">
      <label for="nombre">Nombre:</label>
      <input
        type="text"
        id="nombre"
        v-model="paciente.nombre"
        placeholder="Ingresa el nombre completo"
      />
    </div>

    <!-- Campo: DPI -->
    <div class="form-group">
      <label for="dpi">DPI:</label>
      <input
        type="text"
        id="dpi"
        v-model="paciente.dpi"
        placeholder="Ingresa el número de DPI"
      />
    </div>

    <!-- Campo: Fecha de nacimiento -->
    <div class="form-group">
      <label for="fechaNacimiento">Fecha de nacimiento:</label>
      <input
        type="date"
        id="fechaNacimiento"
        v-model="paciente.fechaNacimiento"
      />
    </div>

    <!-- Campo: Fotografía -->
    <div class="form-group">
      <label for="fotografia">Fotografía:</label>
      <input
        type="file"
        id="fotografia"
        accept="image/*"
        @change="onFileSelected"
      />
    </div>

    <!-- Campo: Número de afiliación -->
    <div class="form-group">
      <label for="numAfiliacion">Número de afiliación:</label>
      <input
        type="text"
        id="numAfiliacion"
        v-model="paciente.numeroAfiliacion"
        placeholder="Ingresa el número de afiliación"
      />
    </div>

    <!-- Campo: Código de compañía de seguro (opcional) -->
    <div class="form-group">
      <label for="codigoSeguro">Código de compañía del seguro:</label>
      <input
        type="text"
        id="codigoSeguro"
        v-model="paciente.codigoCompania"
        placeholder="Ingresa el código (si aplica)"
      />
    </div>

    <!-- Campo: Número de carné (opcional) -->
    <div class="form-group">
      <label for="numeroCarne">Número de carné:</label>
      <input
        type="text"
        id="numeroCarne"
        v-model="paciente.numeroCarne"
        placeholder="Ingresa el número de carné (si aplica)"
      />
    </div>

    <!-- Campo: Historial de servicios prestados -->
    <!-- Podría ser un textarea o una lista dinámica; aquí se usa un textarea sencillo. -->
    <div class="form-group">
      <label for="historialServicios">Historial de servicios prestados:</label>
      <textarea
        id="historialServicios"
        rows="4"
        v-model="paciente.historialServicios"
        placeholder="Describe brevemente los servicios prestados al paciente"
      ></textarea>
    </div>

    <!-- Botón para enviar/guardar el formulario -->
    <button class="btn-primario" @click="guardarFicha">Guardar Ficha</button>
  </div>
</template>

<script>
export default {
  name: 'FichaTecnicaPacientes',
  data() {
    return {
      paciente: {
        nombre: '',
        dpi: '',
        fechaNacimiento: '',
        fotografia: null,         // Se guardará el objeto File
        numeroAfiliacion: '',
        codigoCompania: '',
        numeroCarne: '',
        historialServicios: ''
      }
    };
  },
  methods: {
    /**
     * Maneja la selección de un archivo (imagen) para la fotografía,
     * guardando la información en el atributo fotografia.
     */
    onFileSelected(event) {
      const file = event.target.files[0];
      this.paciente.fotografia = file || null;
    },
    /**
     * Aquí iría la lógica para enviar los datos al backend (Quarkus con Hibernate)
     * o bien para validarlos antes de proceder.
     */
    guardarFicha() {
      // Lógica de envío de datos (fetch / axios / etc.)
      console.log('Datos del paciente:', this.paciente);
      alert('Ficha de paciente guardada (ejemplo).');
    }
  }
};
</script>

<style scoped>
.formulario-receta {
  max-width: 700px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 12px;
  background-color: #f4f4f4;
}

.form-group {
  margin-bottom: 15px;
}

input,
select,
textarea {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-sizing: border-box;
}

.medicamento-item {
  margin-bottom: 10px;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 6px;
  background-color: #eef5ff;
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.btn-primario {
  background-color: #28a745;
  color: white;
  padding: 10px;
  border: none;
  margin-top: 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primario:hover {
  background-color: #218838;
}

.btn-secundario {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border: none;
  margin-top: 15px;
  margin-right: 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-secundario:hover {
  background-color: #0056b3;
}

.btn-eliminar {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-eliminar:hover {
  background-color: #c82333;
}
</style>
