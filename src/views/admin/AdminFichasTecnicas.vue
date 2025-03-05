<template>
  <div class="container">
    <!-- Título -->
    <div class="header">ADMINISTRACIÓN DE FICHAS TÉCNICAS</div>

    <!-- Acciones -->
    <div class="actions">
      <button @click="crearFicha">🆕 Crear Nueva Ficha</button>
      <div class="search-container">
        <input v-model="buscarPaciente" placeholder="🔍 Buscar Paciente">
        <button @click="buscarFicha">🔎 Buscar</button>
      </div>
    </div>

    <!-- Listado de Pacientes con Fichas Técnicas -->
    <div class="section">
      <h3>📋 LISTADO DE PACIENTES CON FICHAS TÉCNICAS</h3>
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
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ficha in fichasFiltradas" :key="ficha.id">
            <td>{{ ficha.id }}</td>
            <td>{{ ficha.nombre }}</td>
            <td>{{ ficha.dpi }}</td>
            <td>{{ ficha.afiliacion }}</td>
            <td>{{ ficha.cod }}</td>
            <td>{{ ficha.carnet }}</td>
            <td>{{ ficha.fechaCreada }}</td>
            <td :class="{'activa': ficha.estado === 'Activa', 'inactiva': ficha.estado === 'Inactiva'}">
              {{ ficha.estado }}
            </td>
            <td>
              <button @click="editarFicha(ficha.id)">🖊️</button>
              <button @click="eliminarFicha(ficha.id)">🗑️</button>
              <button @click="verFicha(ficha.id)">👁️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

export default {
  setup() {
    const router = useRouter(); // Usamos Vue Router correctamente
    const buscarPaciente = ref("");
    const fichas = ref([
      { id: "001", nombre: "Juan Pérez", dpi: "21212121", afiliacion: "1212", cod: "111", carnet: "888888", fechaCreada: "12/02/2025", estado: "Activa" },
      { id: "002", nombre: "María López", dpi: "21212121", afiliacion: "1212", cod: "111", carnet: "888888", fechaCreada: "14/02/2025", estado: "Activa" },
      { id: "003", nombre: "Carlos Gómez", dpi: "21212121", afiliacion: "1212", cod: "111", carnet: "888888", fechaCreada: "16/02/2025", estado: "Inactiva" },
    ]);

    const fichasFiltradas = computed(() => {
      if (!buscarPaciente.value) return fichas.value;
      return fichas.value.filter(ficha =>
        ficha.nombre.toLowerCase().includes(buscarPaciente.value.toLowerCase()) ||
        ficha.id.includes(buscarPaciente.value)
      );
    });

    const crearFicha = () => {
      router.push("/admin/crear-ficha-tecnica"); // ✅ Solución correcta
    };

    const editarFicha = (id) => {
      alert(`Editar ficha ID: ${id}`);
    };

    const eliminarFicha = (id) => {
      if (confirm(`¿Seguro que deseas eliminar la ficha ID: ${id}?`)) {
        fichas.value = fichas.value.filter(ficha => ficha.id !== id);
      }
    };

    const verFicha = (id) => {
      alert(`Ver ficha ID: ${id}`);
    };

    const buscarFicha = () => {
      alert(`Buscando ficha de paciente: ${buscarPaciente.value}`);
    };

    return {
      buscarPaciente,
      fichasFiltradas,
      crearFicha,
      editarFicha,
      eliminarFicha,
      verFicha,
      buscarFicha,
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
