<template>
  <div class="recetas-paciente">
    <h2>Mis Recetas Médicas</h2>
    <div v-for="receta in recetas" :key="receta.idReceta" class="receta">
      <h3>{{ receta.diagnostico }}</h3>
      <p><strong>Fecha:</strong> {{ formatFecha(receta.fecha) }}</p>
      <p><strong>Médico:</strong> {{ receta.doctorNombre || 'Desconocido' }}</p>
      <p><strong>Observaciones:</strong> {{ receta.observaciones }}</p>
      <p><strong>Estado:</strong>
        <span :class="estadoClase(receta.estado)">
          {{ receta.estado }}
        </span>
      </p>
      <h4>Medicamentos</h4>
      <ul>
        <li v-for="(med, index) in receta.detalleMedicamentos" :key="index">
          {{ med.principioActivo }} - {{ med.dosis }} cada {{ med.frecuencia }} por {{ med.duracion }} días
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recetas: []
    };
  },
  async created() {
    try {
      const idPaciente = this.$store?.state?.user?.idPaciente;
      if (!idPaciente) {
        throw new Error('ID del paciente no disponible.');
      }

      const response = await fetch(`/api/recetas/paciente/${idPaciente}`);
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.mensaje || 'Error al cargar recetas');
      }

      this.recetas = await response.json();
    } catch (error) {
      console.error(error);
      alert('Error al cargar recetas. Por favor, inténtelo de nuevo.');
    }
  },
  methods: {
    // Formatear fecha para que sea más legible
    formatFecha(fecha) {
      if (!fecha) return 'Fecha no disponible';
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },
    // Asignar una clase según el estado para mejorar la visualización
    estadoClase(estado) {
      switch (estado) {
        case 'activa':
          return 'estado-activa';
        case 'completada':
          return 'estado-completada';
        case 'cancelada':
          return 'estado-cancelada';
        default:
          return '';
      }
    }
  }
};
</script>

<style scoped>
.recetas-paciente {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #eaf6ff;
}

.receta {
  margin-bottom: 15px;
  border: 1px solid #bbb;
  padding: 15px;
  border-radius: 10px;
  background-color: #fff;
}

.estado-activa {
  color: green;
  font-weight: bold;
}

.estado-completada {
  color: blue;
  font-weight: bold;
}

.estado-cancelada {
  color: red;
  font-weight: bold;
}
</style>
