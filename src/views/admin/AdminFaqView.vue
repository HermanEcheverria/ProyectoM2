<template>
  <div class="admin-faq">
    <h1>Administrar Preguntas Frecuentes (FAQ)</h1>

    <!-- Form para Crear/Editar FAQ -->
    <form @submit.prevent="saveFaq" class="faq-form">
      <div class="form-group">
        <label>Pregunta:</label>
        <input v-model="faq.pregunta" type="text" required />
      </div>

      <div class="form-group">
        <label>Respuesta:</label>
        <textarea v-model="faq.respuesta" rows="3"></textarea>
      </div>

      <div class="form-group">
        <label>Autor:</label>
        <input v-model="faq.autor" type="text" />
      </div>

      <!-- Botón para Guardar (Crear/Editar) -->
      <div class="button-row">
        <button type="submit" class="btn-save">
          {{ faq.id ? 'Actualizar' : 'Crear' }} FAQ
        </button>
        <button type="button" class="btn-reset" @click="clearForm">
          Limpiar
        </button>
      </div>
    </form>

    <hr class="divider" />

    <!-- Tabla de FAQs existentes -->
    <table class="faq-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Pregunta</th>
          <th>Respuesta</th>
          <th>Autor</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in faqs" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.pregunta }}</td>
          <td>{{ item.respuesta || '---' }}</td>
          <td>{{ item.autor || '---' }}</td>
          <td>
            <button class="btn-edit" @click="editFaq(item)">Editar</button>
            <button class="btn-delete" @click="deleteFaq(item.id)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminFaqView",
  data() {
    return {
      // Objeto FAQ en el formulario
      faq: {
        id: null,
        pregunta: "",
        respuesta: "",
        autor: ""
      },
      // Lista de FAQs obtenidas del backend
      faqs: []
    };
  },
  async created() {
    // Cargar FAQs al montar
    await this.fetchFaqs();
  },
  methods: {
    /**
     * Obtener todas las FAQs (GET /faq)
     */
    async fetchFaqs() {
      try {
        const resp = await axios.get("http://localhost:8080/faq");
        if (Array.isArray(resp.data)) {
          this.faqs = resp.data;
        } else {
          console.error("La API no devolvió un array.");
        }
      } catch (error) {
        console.error("Error al obtener FAQs:", error);
      }
    },

    /**
     * Guardar la FAQ del formulario:
     * - Si tiene ID => PUT /faq/editar/:id
     * - Si no tiene ID => POST /faq/crear
     */
    async saveFaq() {
      try {
        if (this.faq.id) {
          // Actualizar (PUT)
          await axios.put(`http://localhost:8080/faq/editar/${this.faq.id}`, this.faq);
        } else {
          // Crear (POST)
          await axios.post("http://localhost:8080/faq/crear", this.faq);
        }
        alert("FAQ guardada correctamente.");

        // Recargar tabla
        await this.fetchFaqs();
        // Limpiar el formulario
        this.clearForm();
      } catch (error) {
        console.error("Error al guardar FAQ:", error);
      }
    },

    /**
     * Cargar los datos en el formulario para editar
     */
    editFaq(item) {
      // Clonamos 'item' para no modificar la tabla en vivo
      this.faq = { ...item };
    },

    /**
     * Eliminar una FAQ (DELETE /faq/eliminar/:id)
     */
    async deleteFaq(id) {
      if (!confirm("¿Estás seguro de eliminar esta pregunta?")) return;
      try {
        await axios.delete(`http://localhost:8080/faq/eliminar/${id}`);
        alert("FAQ eliminada correctamente.");
        this.fetchFaqs();
      } catch (error) {
        console.error("Error al eliminar FAQ:", error);
      }
    },

    /**
     * Limpiar el formulario (nueva FAQ)
     */
    clearForm() {
      this.faq = {
        id: null,
        pregunta: "",
        respuesta: "",
        autor: ""
      };
    }
  }
};
</script>

<style scoped>
/* Contenedor principal oscuro (similar a tu "Historia") */
.admin-faq {
  background-color: #0b1b2b; /* Fondo oscuro */
  color: #b3f5e3;           /* Texto verde/menta */
  min-height: 100vh;
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
}

/* Título */
.admin-faq h1 {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  color: #b3f5e3;
}

/* Formulario estilo */
.faq-form {
  background-color: #102538; /* Bloque más claro */
  padding: 1.5rem;
  border-radius: 8px;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 0.3rem;
  color: #b3f5e3;
}

.form-group input,
.form-group textarea {
  background-color: #b3f5e3; /* Campos claros */
  color: #102538;           /* Texto oscuro */
  border: none;
  outline: none;
  padding: 0.6rem;
  border-radius: 4px;
  font-size: 1rem;
}

/* Fila de botones en el formulario */
.button-row {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

/* Botones del form */
.btn-save,
.btn-reset {
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save {
  background-color: #20b283;
  color: #fff;
  margin-right: 1rem;
}
.btn-save:hover {
  background-color: #18946d;
}

.btn-reset {
  background-color: #f44336;
  color: #fff;
}
.btn-reset:hover {
  background-color: #d6382c;
}

/* Separador */
.divider {
  border: 0;
  border-top: 1px solid #b3f5e3;
  margin: 2rem 0;
}

/* Tabla de FAQs */
.faq-table {
  width: 100%;
  border-collapse: collapse;
  color: #b3f5e3; /* Texto claro */
}

.faq-table th,
.faq-table td {
  border: 1px solid #b3f5e3;
  padding: 0.5rem 0.8rem;
  text-align: left;
}

.faq-table th {
  background-color: #0b1b2b;
}

/* Botones en la tabla */
.faq-table button {
  margin-right: 0.3rem;
  padding: 0.4rem 0.7rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: #fff;
}

.faq-table button:hover {
  opacity: 0.8;
}

/* Botón Editar */
.btn-edit {
  background-color: #5bc0de; /* celeste */
}
.btn-delete {
  background-color: #d9534f; /* rojo */
}
</style>
