<template>
  <div class="admin-historia">
    <h1>Editar Historia de la Institución</h1>

    <form @submit.prevent="saveHistoria">
      <!-- Sección: Nombre, Historia, Méritos -->
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

      <div class="form-group">
        <label for="meritos">Méritos:</label>
        <textarea
          id="meritos"
          v-model="historia.meritos"
          class="form-textarea"
        ></textarea>
      </div>

      <hr class="divider" />

      <!-- Sección para la Línea del Tiempo -->
      <h2>Línea del Tiempo</h2>
      <div
        v-for="(evento, index) in timelineEvents"
        :key="index"
        class="timeline-event"
      >
        <div class="event-header">
          <h3>Evento {{ index + 1 }}</h3>
          <button type="button" class="btn-delete" @click="removeEvent(index)">
            Eliminar
          </button>
        </div>

        <div class="event-grid">
          <div>
            <label>Año:</label>
            <input
              type="text"
              v-model="evento.year"
              class="form-input"
              placeholder="Ej: 1970"
            />
          </div>
          <div>
            <label>Título:</label>
            <input
              type="text"
              v-model="evento.title"
              class="form-input"
              placeholder="Ej: Fundación"
            />
          </div>
          <div class="grid-span">
            <label>Descripción:</label>
            <textarea
              v-model="evento.description"
              class="form-textarea"
              placeholder="Ej: El hospital se fundó con 50 camas..."
            ></textarea>
          </div>
        </div>
      </div>

      <button type="button" class="btn-add" @click="addEvent">Agregar evento</button>

      <hr class="divider" />

      <button type="submit" class="btn-save">Guardar</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

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
      timelineEvents: [] // Arreglo de eventos
    };
  },
  async created() {
    await this.fetchHistoria();
  },
  methods: {
    async fetchHistoria() {
      try {
        const response = await axios.get("http://localhost:8080/historias");
        if (response.data && response.data.length > 0) {
          const historiaDB = response.data[0];
          this.historia = historiaDB;

          try {
            this.timelineEvents = JSON.parse(historiaDB.lineaDelTiempo || "[]");
          } catch (error) {
            console.error("Error al parsear la línea del tiempo:", error);
            this.timelineEvents = [];
          }
        }
      } catch (error) {
        console.error("Error al obtener la historia:", error);
      }
    },
    addEvent() {
      this.timelineEvents.push({
        year: "",
        title: "",
        description: ""
      });
    },
    removeEvent(index) {
      this.timelineEvents.splice(index, 1);
    },
    async saveHistoria() {
      try {
        this.historia.lineaDelTiempo = JSON.stringify(this.timelineEvents);
        if (this.historia.id) {
          await axios.put(
            `http://localhost:8080/historias/${this.historia.id}`,
            this.historia
          );
        } else {
          const response = await axios.post(
            "http://localhost:8080/historias",
            this.historia
          );
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
  background-color: #0b1b2b; /* Fondo oscuro */
  color: #b3f5e3;           /* Texto verde claro */
  min-height: 100vh;
  padding: 2rem;
}

/* Título principal */
.admin-historia h1 {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  color: #b3f5e3;
}

/* Formulario */
.admin-historia form {
  background-color: #102538; /* Caja algo más clara */
  padding: 1.5rem;
  border-radius: 8px;
}

/* Subtítulos (h2) dentro del form */
.admin-historia h2 {
  margin-top: 1.5rem;
  margin-bottom: 1rem;
  color: #b3f5e3;
}

/* Grupos de formulario */
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

/* Inputs y textareas */
.form-input,
.form-textarea {
  background-color: #b3f5e3; /* Texto editable claro */
  color: #102538;           /* Texto oscuro */
  border: none;
  outline: none;
  padding: 0.6rem;
  border-radius: 4px;
  font-size: 1rem;
  width: 100%;
}

.form-textarea {
  resize: vertical;
}

/* Divider (la línea de separación) */
.divider {
  border: 0;
  border-top: 1px solid #b3f5e3;
  margin: 2rem 0;
}

/* Sección línea del tiempo */
.timeline-event {
  background-color: #0b1b2b; /* Igual que el contenedor principal */
  padding: 1rem;
  margin-bottom: 1rem;
  border: 1px solid #b3f5e3;
  border-radius: 4px;
}

.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.event-header h3 {
  color: #b3f5e3;
  margin: 0;
}

/* Layout para cada evento */
.event-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.grid-span {
  grid-column: span 2;
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
  background-color: #1e90ff;
  margin-top: 1rem;
}

.btn-delete {
  background-color: #d9534f;
}

.btn-save {
  background-color: #20b283;
  margin-top: 1rem;
}

/* Efectos hover en botones */
.btn-add:hover {
  background-color: #1675c7;
}

.btn-delete:hover {
  background-color: #c64743;
}

.btn-save:hover {
  background-color: #18946d;
}
</style>
