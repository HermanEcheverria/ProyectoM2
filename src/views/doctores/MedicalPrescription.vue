<template>
  <div class="formulario-receta">
    <h2>{{ modoEdicion ? 'Editar' : 'Crear' }} Receta Médica</h2>

    <form @submit.prevent="enviarReceta">
      <!-- Código de Hospital (Solo lectura) -->
      <div class="form-group">
        <label>Código de Hospital</label>
        <input
          v-model="receta.codigoHospital"
          placeholder="Código del Hospital"
          readonly
        />
      </div>

      <!-- Checkbox para indicar si tiene seguro -->
      <div class="form-group">
        <label>¿Tiene seguro?</label>
        <input type="checkbox" v-model="tieneSeguro" />
      </div>

      <!-- Código de Seguro (solo si tieneSeguro = true) -->
      <div class="form-group" v-if="tieneSeguro">
        <label>Código de Seguro</label>
        <input
          v-model="receta.codigoSeguro"
          placeholder="Código del Seguro"
        />
      </div>

      <!-- DPI del paciente (id único) -->
      <div class="form-group">
        <label>Número de DPI</label>
        <input
          v-model="receta.dpi"
          placeholder="DPI del Paciente"
          required
        />
      </div>

      <!-- Código de Receta (readonly y se calcula automáticamente) -->
      <div class="form-group">
        <label>Código de la Receta</label>
        <input
          :value="codigoReceta"
          placeholder="Se genera automáticamente"
          readonly
        />
      </div>

      <!-- Fecha de la receta -->
      <div class="form-group">
        <label>Fecha</label>
        <input
          v-model="receta.fecha"
          type="date"
          :min="minDate"
          required
        />
      </div>

      <!-- Paciente (nombre) -->
      <div class="form-group">
        <label>Nombre del Paciente</label>
        <input
          v-model="receta.paciente"
          placeholder="Nombre del Paciente"
          required
        />
      </div>

      <!-- Médico -->
      <div class="form-group">
        <label>Nombre del Médico</label>
        <input
          v-model="receta.nombreMedico"
          placeholder="Nombre del Médico"
          required
        />
      </div>

      <div class="form-group">
        <label>Número de Colegiado</label>
        <input
          v-model="receta.numeroColegiado"
          placeholder="No. de Colegiado"
          required
        />
      </div>

      <div class="form-group">
        <label>Especialidad</label>
        <input
          v-model="receta.especialidad"
          placeholder="Especialidad"
          required
        />
      </div>

      <!-- Anotaciones / Notas especiales -->
      <div class="form-group">
        <label>Anotaciones</label>
        <textarea
          v-model="receta.anotaciones"
          placeholder="Anotaciones"
          rows="2"
        ></textarea>
      </div>

      <div class="form-group">
        <label>Notas Especiales</label>
        <textarea
          v-model="receta.notasEspeciales"
          placeholder="Notas Especiales"
          rows="2"
        ></textarea>
      </div>

      <!-- Lista de medicamentos -->
      <h3>Medicamentos</h3>
      <div
        v-for="(med, index) in receta.detalleMedicamentos"
        :key="index"
        class="medicamento-item"
      >
        <input
          v-model="med.principioActivo"
          placeholder="Principio Activo"
          required
        />
        <input
          v-model="med.concentracion"
          placeholder="Concentración (mg)"
          required
        />
        <input
          v-model="med.presentacion"
          placeholder="Presentación"
          required
        />
        <input
          v-model="med.formaFarmaceutica"
          placeholder="Forma Farmacéutica"
          required
        />
        <input
          v-model="med.dosis"
          placeholder="Dosis"
          required
        />
        <input
          v-model="med.frecuencia"
          placeholder="Frecuencia (cada X horas)"
          required
        />
        <input
          v-model.number="med.duracion"
          placeholder="Duración (días)"
          required
        />
        <input
          v-model="med.diagnostico"
          placeholder="Diagnóstico"
        />
        <button
          @click.prevent="eliminarMedicamento(index)"
          class="btn-eliminar"
        >
          ❌
        </button>
      </div>

      <!-- Botones -->
      <button
        @click.prevent="agregarMedicamento"
        class="btn-secundario"
      >
        ➕ Agregar Medicamento
      </button>

      <button type="submit" class="btn-primario">
        💾 {{ modoEdicion ? 'Actualizar' : 'Crear' }} Receta
      </button>
    </form>
  </div>
</template>

<script>
export default {
  props: {
    modoEdicion: { type: Boolean, default: false },
    recetaInicial: { type: Object, default: null }
  },
  data() {
    return {
      tieneSeguro: false,
      minDate: new Date().toISOString().slice(0, 10),
      cita: {
        fecha: ''
      },
      // Datos iniciales de la receta
      receta: {
        // Por ejemplo, el código de hospital podría venir
        // fijo desde tu configuración o inyectarse por props.
        codigoHospital: '',
        codigoSeguro: '', // se llenará solo si tieneSeguro=true
        dpi: '',
        codigoReceta: '', // se calculará automáticamente
        fecha: new Date().toISOString().slice(0, 10), // YYYY-MM-DD
        paciente: '',
        nombreMedico: '',
        numeroColegiado: '',
        especialidad: '',
        anotaciones: '',
        notasEspeciales: '',
        detalleMedicamentos: [
          {
            principioActivo: '',
            concentracion: '',
            presentacion: '',
            formaFarmaceutica: '',
            dosis: '',
            frecuencia: '',
            duracion: '',
            diagnostico: ''
          }
        ]
      }
    };
  },
  computed: {
    /**
     * Genera automáticamente el código de receta
     * hospital - seguro - DPI
     * Si no hay seguro, omite la parte del seguro.
     */
    codigoReceta() {
      const { codigoHospital, codigoSeguro, dpi } = this.receta;

      // Si no hay DPI o no hay códigoHospital, aún no se puede formar
      if (!dpi || !codigoHospital) {
        return '';
      }

      if (this.tieneSeguro && codigoSeguro) {
        return `${codigoHospital}-${codigoSeguro}-${dpi}`;
      } else {
        return `${codigoHospital}-${dpi}`;
      }
    }
  },
  created() {
    // Si recibimos una receta inicial (para editar), la seteamos
    if (this.recetaInicial) {
      this.receta = { ...this.recetaInicial };

      // Ajustamos checkbox de seguro según si hay o no códigoSeguro
      if (this.receta.codigoSeguro) {
        this.tieneSeguro = true;
      }
    }
  },
  methods: {
    agregarMedicamento() {
      this.receta.detalleMedicamentos.push({
        principioActivo: '',
        concentracion: '',
        presentacion: '',
        formaFarmaceutica: '',
        dosis: '',
        frecuencia: '',
        duracion: '',
        diagnostico: ''
      });
    },
    eliminarMedicamento(index) {
      this.receta.detalleMedicamentos.splice(index, 1);
    },
    async enviarReceta() {
      try {
        // Asignamos el valor calculado de codigoReceta a la data antes de enviar
        this.receta.codigoReceta = this.codigoReceta;

        // Convertimos duraciones a entero por si acaso
        this.receta.detalleMedicamentos = this.receta.detalleMedicamentos.map(med => ({
          ...med,
          duracion: parseInt(med.duracion) || 0
        }));

        const metodo = this.modoEdicion ? 'PUT' : 'POST';
        const response = await fetch('/api/recetas', {
          method: metodo,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.receta)
        });

        const resultado = await response.json();

        if (response.ok) {
          alert(`Receta ${this.modoEdicion ? 'actualizada' : 'creada'} exitosamente.`);
          // Opcional: Lógica de envío PDF
          // await fetch(`/api/recetas/${resultado.id}/enviarPDF`, { method: 'POST' });

          this.limpiarFormulario();
        } else {
          alert(`Error: ${resultado.mensaje || 'No se pudo completar la operación.'}`);
        }
      } catch (error) {
        console.error('Error al enviar receta:', error);
        alert('Error al conectar con el servidor.');
      }
    },
    limpiarFormulario() {
      this.tieneSeguro = false;
      this.receta = {
        codigoHospital: '',
        codigoSeguro: '',
        dpi: '',
        codigoReceta: '',
        fecha: new Date().toISOString().slice(0, 10),
        paciente: '',
        nombreMedico: '',
        numeroColegiado: '',
        especialidad: '',
        anotaciones: '',
        notasEspeciales: '',
        detalleMedicamentos: [
          {
            principioActivo: '',
            concentracion: '',
            presentacion: '',
            formaFarmaceutica: '',
            dosis: '',
            frecuencia: '',
            duracion: '',
            diagnostico: ''
          }
        ]
      };
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
