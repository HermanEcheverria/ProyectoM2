<template>
  <div class="formulario-receta">
    <h2>{{ modoEdicion ? 'Editar' : 'Crear' }} Receta Médica</h2>
    <form @submit.prevent="enviarReceta">
      <!-- Datos generales -->
      <div class="form-group">
        <label>ID del Paciente</label>
        <input v-model="receta.idPaciente" placeholder="ID del Paciente" required />
      </div>

      <div class="form-group">
        <label>ID del Doctor</label>
        <input v-model="receta.idDoctor" placeholder="ID del Doctor" required />
      </div>

      <div class="form-group">
        <label>Diagnóstico</label>
        <input v-model="receta.diagnostico" placeholder="Diagnóstico" required />
      </div>

      <div class="form-group">
        <label>Observaciones</label>
        <textarea v-model="receta.observaciones" placeholder="Observaciones" rows="3"></textarea>
      </div>

      <!-- Estado de la receta -->
      <div class="form-group">
        <label>Estado</label>
        <select v-model="receta.estado" required>
          <option value="activa">Activa</option>
          <option value="completada">Completada</option>
          <option value="cancelada">Cancelada</option>
        </select>
      </div>

      <!-- Lista de medicamentos -->
      <h3>Medicamentos</h3>
      <div v-for="(med, index) in receta.detalleMedicamentos" :key="index" class="medicamento-item">
        <input v-model="med.principioActivo" placeholder="Principio Activo" required />
        <input v-model="med.concentracion" placeholder="Concentración (mg)" required />
        <input v-model="med.presentacion" placeholder="Presentación" required />
        <input v-model="med.dosis" placeholder="Dosis" required />
        <input v-model="med.frecuencia" placeholder="Frecuencia (cada X horas)" required />
        <input v-model.number="med.duracion" placeholder="Duración (días)" required />
        <button @click.prevent="eliminarMedicamento(index)" class="btn-eliminar">❌</button>
      </div>

      <!-- Botones de acción -->
      <button @click.prevent="agregarMedicamento" class="btn-secundario">➕ Agregar Medicamento</button>
      <button type="submit" class="btn-primario">💾 {{ modoEdicion ? 'Actualizar' : 'Crear' }} Receta</button>
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
      receta: {
        idPaciente: '',
        idDoctor: '',
        diagnostico: '',
        observaciones: '',
        estado: 'activa',
        detalleMedicamentos: [
          { principioActivo: '', concentracion: '', presentacion: '', dosis: '', frecuencia: '', duracion: 0 }
        ]
      }
    };
  },
  created() {
    if (this.recetaInicial) {
      this.receta = { ...this.recetaInicial };
    }
  },
  methods: {
    agregarMedicamento() {
      this.receta.detalleMedicamentos.push({
        principioActivo: '',
        concentracion: '',
        presentacion: '',
        dosis: '',
        frecuencia: '',
        duracion: 0
      });
    },
    eliminarMedicamento(index) {
      this.receta.detalleMedicamentos.splice(index, 1);
    },
    async enviarReceta() {
      try {
        // Convertimos 'duracion' a entero
        this.receta.detalleMedicamentos = this.receta.detalleMedicamentos.map(med => ({
          ...med,
          duracion: parseInt(med.duracion) || 0
        }));

        const response = await fetch('/api/recetas', {
          method: this.modoEdicion ? 'PUT' : 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.receta)
        });

        const resultado = await response.json();

        if (response.ok) {
          alert(`Receta ${this.modoEdicion ? 'actualizada' : 'creada'} exitosamente.`);
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
      this.receta = {
        idPaciente: '',
        idDoctor: '',
        diagnostico: '',
        observaciones: '',
        estado: 'activa',
        detalleMedicamentos: [
          { principioActivo: '', concentracion: '', presentacion: '', dosis: '', frecuencia: '', duracion: 0 }
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

input, select, textarea {
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

