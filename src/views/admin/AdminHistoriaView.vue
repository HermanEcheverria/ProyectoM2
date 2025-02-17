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
/* Paleta de colores:
   #DAFDBA (Muy claro, verde pastel)
   #A4F0C4 (Verde menta claro)
   #45C4B0 (Teal más vibrante)
   #13678A (Azul teal más oscuro)
   #012030 (Muy oscuro, azul grisáceo)
*/

/* Contenedor principal */
.admin-historia {
  padding: 20px;
  max-width: 800px;
  margin: auto;
  background-color: #012030; /* Fondo oscuro principal */
  color: #DAFDBA;            /* Texto en tono claro */
  border-radius: 8px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* Grupos de formulario */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
}

/* Línea divisoria */
.divider {
  border: 0;
  height: 1px;
  background: #45C4B0; /* Un teal vibrante */
  margin: 20px 0;
}

/* Inputs y Textareas */
.form-input,
.form-textarea {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #45C4B0;
  background-color: #A4F0C4; /* Verde menta claro */
  color: #012030;           /* Texto oscuro para buen contraste */
  outline: none;
  font-size: 14px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus,
.form-textarea:focus {
  border-color: #13678A; /* Enfoque más oscuro */
  box-shadow: 0 0 0 2px rgba(19, 103, 138, 0.2);
}

/* Botones base */
button {
  cursor: pointer;
  border: none;
  border-radius: 4px;
  font-weight: 600;
  transition: background-color 0.2s, opacity 0.2s;
}

button:hover {
  opacity: 0.9;
}

/* Botón para agregar eventos */
.btn-add {
  background-color: #45C4B0; /* Teal vibrante */
  color: #012030;
  padding: 8px 16px;
  margin-bottom: 20px;
}

.btn-add:hover {
  background-color: #13678A; /* Oscurecer en hover */
}

/* Botón para guardar */
.btn-save {
  background-color: #45C4B0;
  color: #012030;
  padding: 10px 20px;
}

.btn-save:hover {
  background-color: #13678A;
}

/* Botón para eliminar */
.btn-delete {
  background-color: #13678A;
  color: #DAFDBA;
  padding: 5px 10px;
}

.btn-delete:hover {
  background-color: #45C4B0;
  color: #012030;
}

/* Contenedor de cada evento */
.timeline-event {
  background-color: #A4F0C4; /* Mismo color que inputs */
  color: #012030;           /* Texto oscuro */
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 30px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
}

/* Encabezado del evento (título + botón eliminar) */
.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.event-header h3 {
  margin: 0;
  font-size: 1rem;
  color: #012030;
}

/* Layout en Grid para acomodar los campos */
.event-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Para que la descripción ocupe todo el ancho */
.grid-span {
  grid-column: 1 / span 2;
}

.event-grid label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
}
</style>
