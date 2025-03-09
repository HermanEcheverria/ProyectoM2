<template>
  <div class="container">
    <!-- Título -->
    <div class="header">ADMINISTRACIÓN DE FICHAS TÉCNICAS</div>

    <!-- Acciones -->
    <div class="actions">
      <button @click="crearFicha">Crear Nueva Ficha</button>
      <div class="search-container">
        <input v-model="buscarPaciente" placeholder="🔍 Buscar Paciente">
        <button @click="buscarFicha">Buscar</button>
      </div>
    </div>

    <!-- Listado de Pacientes con Fichas Técnicas -->
    <div class="section">
      <h3>LISTADO DE PACIENTES CON FICHAS TÉCNICAS</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>DPI</th>
            <th>Afiliación de seguro</th>
            <th>Código de seguro</th>
            <th># de carnet de seguro</th>
            <th>Fecha Creada</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
  <tr v-if="loading">
    <td colspan="8">⏳ Cargando datos...</td>
  </tr>
  <tr v-else-if="error">
    <td colspan="8" style="color: red;">⚠️ {{ error }}</td>
  </tr>
  <tr v-else-if="fichasFiltradas.length === 0">
    <td colspan="8">No hay fichas técnicas registradas.</td>
  </tr>
  <tr v-for="ficha in fichasFiltradas" :key="ficha.id">
    <td>{{ ficha.id }}</td>
    <td>{{ ficha.nombre }}</td>
    <td>{{ ficha.dpi }}</td>
    <td>{{ ficha.afiliacion }}</td>
    <td>{{ ficha.cod }}</td>
    <td>{{ ficha.carnet }}</td>
    <td>{{ ficha.fechaCreada }}</td>
    <td>
      <button @click="editarFicha(ficha.id)">Editar</button>
      <button @click="eliminarFicha(ficha.id)">Borrar</button>
      <button @click="verFicha(ficha.id)">Ver</button>
    </td>
  </tr>
</tbody>

      </table>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import fichasApi from "@/services/fichasApi.js"; // Importamos la API de Fichas Técnicas

export default {
  setup() {
    const router = useRouter();
    const buscarPaciente = ref("");
    const fichas = ref([]);
    const loading = ref(true);
    const error = ref(null);

    // 🔹 Obtener fichas técnicas desde el backend
    const cargarFichas = async () => {
      try {
        const data = await fichasApi.getAllFichas();
        fichas.value = data.map(ficha => ({
          id: ficha.paciente.idPaciente, // ID del paciente
          nombre: ficha.paciente.usuario.nombreUsuario || "N/A", // Nombre
          dpi: ficha.paciente.documento || "N/A", // DPI
          afiliacion: ficha.numeroAfiliacion || "N/A", // Afiliación de seguro
          cod: ficha.codigoSeguro || "N/A", // Código del seguro
          carnet: ficha.carnetSeguro || "N/A", // Carnet de seguro
          fechaCreada: ficha.fechaCreacion || "N/A", // Fecha de creación
          estado: "Activa", // Estado por defecto
        }));
        loading.value = false;
      } catch (err) {
        error.value = "Error al obtener las fichas. Revisa la conexión.";
        loading.value = false;
      }
    };

    // 🔹 Ejecutar la carga de fichas al montar el componente
    onMounted(() => {
      cargarFichas();
    });

    // 🔹 Filtrar fichas en tiempo real
    const fichasFiltradas = computed(() => {
      if (!buscarPaciente.value) return fichas.value;
      return fichas.value.filter(ficha =>
        ficha.nombre.toLowerCase().includes(buscarPaciente.value.toLowerCase()) ||
        ficha.id.toString().includes(buscarPaciente.value)
      );
    });

    // 🔹 Crear una nueva ficha
    const crearFicha = () => {
      router.push("/admin/crear-ficha-tecnica");
    };

    // 🔹 Editar ficha (Ejemplo, implementar modal en el futuro)
    const editarFicha = async (id, datos) => {
      try {
        await fichasApi.updateFicha(id, datos);
        alert(`Ficha ID: ${id} actualizada correctamente.`);
        cargarFichas();
      } catch (err) {
        alert("Error al actualizar la ficha.");
      }
    };

    // 🔹 Eliminar ficha técnica
    const eliminarFicha = async (id) => {
      if (confirm(`¿Seguro que deseas eliminar la ficha ID: ${id}?`)) {
        try {
          await fichasApi.deleteFicha(id);
          fichas.value = fichas.value.filter(ficha => ficha.id !== id);
          alert("Ficha eliminada exitosamente.");
        } catch (err) {
          alert("Error al eliminar la ficha.");
        }
      }
    };

    // 🔹 Ver una ficha técnica
    const verFicha = (id) => {
      alert(`Ver ficha ID: ${id}`);
    };

    return {
      buscarPaciente,
      fichasFiltradas,
      crearFicha,
      editarFicha,
      eliminarFicha,
      verFicha,
      loading,
      error,
    };
  },
};
</script>


<style scoped>
/* Estilos de Contenedor */
.container {
  max-width: 95%; /* Extender a casi todo el ancho de la pantalla */
  margin: auto;
  font-family: Arial, sans-serif;
  background: #f9f9f9;
  color: #e0e1dd;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

/* Estilo del Título */
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

/* Estilo de Botones */
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.search-container {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* Alinea el buscador a la derecha */
  gap: 10px; /* Espaciado entre el input y el botón */
  width: 100%;
}
input {
  padding: 7px;
  width: 250px;
  background: #B2F2BB;
  border-radius: 5px;
  border: none;
  text-align: center;
}

button {
  background: #B2F2BB;
  color: #012030;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background: #B2F2BB;
}

/* Sección de Tablas */
.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

/* Ajuste de la tabla para mayor espacio */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
  background: #13678A;
  color: white;
  table-layout: fixed; /* Mantiene el ancho fijo y evita distorsiones */
}

th, td {
  border: 1px solid #DAFDBA;
  padding: 12px;
  text-align: center;
  min-width: 100px; /* Evita que las columnas se achiquen demasiado */
  white-space: nowrap; /* Evita que el texto se divida en varias líneas */
}

th {
  background: #01324b;
}

/* Ajuste de los botones dentro de la celda de Acciones */
td .actions {
  display: flex;
  justify-content: center;
  gap: 8px; /* Espacio entre botones */
}

td .actions button {
  padding: 6px 10px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

/* Estado de Ficha Activa/Inactiva */
.activa {
  color: #DAFDBA;
  font-weight: bold;
}

.inactiva {
  color: #ff8a7d;
  font-weight: bold;
}




</style>
