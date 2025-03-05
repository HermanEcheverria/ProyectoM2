<template>
  <div class="admin-historia">
    <div class="header">Editar Historia de la Institución</div>

    <form @submit.prevent="saveHistoria">
      <!-- Sección: Nombre, Historia -->
      <div class="form-group">
        <label for="nombreEntidad">Nombre de la Entidad:</label>
        <input
          type="text"
          id="nombreEntidad"
          v-model="historia.nombreEntidad"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label for="historia">Historia:</label>
        <textarea
          id="historia"
          v-model="historia.historia"
          class="form-textarea"
        ></textarea>
      </div>

      <hr class="divider" />

      <!-- Sección para Méritos -->
      <h2>Méritos</h2>
      <div v-for="(merito, index) in meritosData" :key="index" class="merito-item">
        <div class="merito-header">
          <input
            type="text"
            v-model="merito.title"
            class="form-input"
            placeholder="Título del mérito"
          />
          <button type="button" class="btn-delete" @click="removeMerito(index)">
            Eliminar
          </button>
        </div>
        <textarea
          v-model="merito.description"
          class="form-textarea"
          placeholder="Descripción del mérito"
        ></textarea>
      </div>
      <button type="button" class="btn-add" @click="addMerito">Agregar mérito</button>

      <hr class="divider" />

      <!-- Sección para la Línea del Tiempo -->
      <h2>Línea del Tiempo</h2>
      <div v-for="(evento, index) in timelineEvents" :key="index" class="timeline-event">
        <div class="event-header">
          <input
            type="text"
            v-model="evento.year"
            class="form-input"
            placeholder="Año"
          />
          <button type="button" class="btn-delete" @click="removeEvent(index)">
            Eliminar
          </button>
        </div>
        <input
          type="text"
          v-model="evento.title"
          class="form-input"
          placeholder="Título del evento"
        />
        <textarea
          v-model="evento.description"
          class="form-textarea"
          placeholder="Descripción del evento"
        ></textarea>
      </div>
      <button type="button" class="btn-add" @click="addEvent">Agregar evento</button>

      <hr class="divider" />

      <button type="submit" class="btn-save">Guardar</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import API_URL from "@/config";

export default {
  name: "AdminHistoriaView",
  data() {
    return {
      historia: {
        id: null,
        nombreEntidad: "",
        historia: "",
        meritos: "",
        lineaDelTiempo: ""
      },
      timelineEvents: [],
      meritosData: []
    };
  },
  async created() {
    await this.fetchHistoria();
  },
  methods: {
    async fetchHistoria() {
      try {
        const response = await axios.get(`${API_URL}/historias`);
        if (response.data && response.data.length > 0) {
          const historiaDB = response.data[0];
          this.historia = historiaDB;

          try {
            // Si la línea del tiempo no es un JSON válido, inicializar con un array vacío
            this.timelineEvents = JSON.parse(historiaDB.lineaDelTiempo || "[]");
          } catch (error) {
            console.error("Error al parsear la línea del tiempo:", error);
            this.timelineEvents = [];
          }

          try {
            // 🛠️ Si meritos no es un JSON válido, inicializarlo como array vacío
            if (typeof historiaDB.meritos === "string" && historiaDB.meritos.trim().startsWith("[")) {
              this.meritosData = JSON.parse(historiaDB.meritos);
            } else {
              this.meritosData = [];
            }
          } catch (error) {
            console.error("Error al parsear los méritos:", error);
            this.meritosData = [];
          }
        }
      } catch (error) {
        console.error("Error al obtener la historia:", error);
      }
    },

    addMerito() {
      this.meritosData.push({ title: "", description: "" });
    },
    removeMerito(index) {
      this.meritosData.splice(index, 1);
    },
    addEvent() {
      this.timelineEvents.push({ year: "", title: "", description: "" });
    },
    removeEvent(index) {
      this.timelineEvents.splice(index, 1);
    },
    async saveHistoria() {
      try {
        this.historia.lineaDelTiempo = JSON.stringify(this.timelineEvents);
        this.historia.meritos = JSON.stringify(this.meritosData);

        if (this.historia.id) {
          await axios.put(`${API_URL}/historias/${this.historia.id}`, this.historia);
        } else {
          const response = await axios.post(`${API_URL}/historias`, this.historia);
          this.historia = response.data;
        }
        alert("Historia guardada correctamente.");
      } catch (error) {
        console.error("Error al guardar la historia:", error);
      }
    }
  }
};
</script>


<style scoped>
/* Contenedor principal */
.admin-historia {
  background: #f9f9f9;
  color: #e0e1dd;          /* Texto verde/menta */
  min-height: 100vh;
  padding: 2rem;
  border-radius: 10px;
  max-width: 900px;
  margin: 0 auto;
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

/* Título */
.admin-historia h1 {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  color: #b3f5e3;
}

/* Formulario */
.admin-historia form {
  background-color: #13678a;
  padding: 1.5rem;
  border-radius: 8px;
}

/* Subtítulos */
.admin-historia h2 {
  margin-top: 1.5rem;
  margin-bottom: 1rem;
  color: #b3f5e3;
}

/* Grupos del formulario */
.form-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 0.3rem;
  color: #f9f9f9;
}

/* Inputs y Textareas */
.form-input,
.form-textarea {
  background-color: #13678a;
  color: #f9f9f9;
  border: 1px solid #45C4B0;
  padding: 0.6rem;
  border-radius: 4px;
  font-size: 1rem;
}

.form-textarea {
  resize: vertical;
}

/* Divider */
.divider {
  border: 0;
  border-top: 1px solid #b3f5e3;
  margin: 2rem 0;
}

/* Méritos */
.merito-item {
  background-color: #0b1b2b;
  padding: 1rem;
  margin-bottom: 1rem;
  border: 1px solid #b3f5e3;
  border-radius: 4px;
}

.merito-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

/* Botones */
.btn-add,
.btn-delete,
.btn-save {
  cursor: pointer;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 4px;
  color: #fff;
  font-weight: bold;
}

.btn-add {
  background-color: #f0ad4e;
  margin-top: 1rem;
}

.btn-delete {
  background-color: #ff8a7d;
}

.btn-save {
  background-color: #DAFDBA;
  color: #012030;
  margin-top: 1rem;
}

.btn-add:hover {
  background-color: #DAFDBA;
}

.btn-delete:hover {
  background-color: #ff8a7d;
}

.btn-save:hover {
  background-color: #f0ad4e;
}
</style>
