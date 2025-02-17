<template>
  <div class="historia">
    <h1>{{ historia.nombreEntidad }}</h1>

    <section>
      <h2>Historia</h2>
      <div v-html="historia.historia"></div>
    </section>

    <section>
      <h2>Méritos</h2>
      <div v-html="historia.meritos"></div>
    </section>

    <section>
      <h2>Línea del Tiempo</h2>
      <div class="timeline">
        <div
          v-for="(evento, index) in timelineData"
          :key="index"
          class="timeline-item"
        >
          <!-- Punto circular a la izquierda -->
          <div class="timeline-dot"></div>
          <!-- Contenido de cada evento -->
          <div class="timeline-content">
            <h3 class="timeline-year">{{ evento.year }}</h3>
            <h4 class="timeline-title">{{ evento.title }}</h4>
            <p class="timeline-description">
              {{ evento.description }}
            </p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HistoriaView",
  data() {
    return {
      historia: {
        nombreEntidad: "",
        historia: "",
        meritos: "",
        lineaDelTiempo: ""
      },
      timelineData: []
    };
  },
  async created() {
    try {
      const response = await axios.get("http://localhost:8080/historias");
      if (response.data && response.data.length > 0) {
        this.historia = response.data[0];
        // Parseamos el JSON de la línea del tiempo
        try {
          this.timelineData = JSON.parse(this.historia.lineaDelTiempo || "[]");
        } catch (error) {
          console.error("Error al parsear la línea del tiempo:", error);
          this.timelineData = [];
        }
      }
    } catch (error) {
      console.error("Error al obtener la historia:", error);
    }
  }
};
</script>

<style scoped>
/* Paleta de colores:
   #DAFDBA (Muy claro, verde pastel)
   #A4F0C4 (Verde menta claro)
   #45C4B0 (Teal vibrante)
   #13678A (Azul teal más oscuro)
   #012030 (Muy oscuro, azul grisáceo)
*/

/* Contenedor principal centrado */
.historia {
  background-color: #012030; /* Fondo oscuro */
  color: #DAFDBA;           /* Texto claro */
  padding: 20px;
  max-width: 800px;
  margin: 40px auto;        /* Centra horizontalmente */
  border-radius: 8px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* Títulos y secciones */
.historia h1 {
  text-align: center;
  margin-bottom: 30px;
}

.historia section {
  margin-bottom: 30px;
}

.historia h2 {
  margin-bottom: 15px;
  color: #A4F0C4; /* Resaltar subtítulos en verde menta claro */
}

/* Estilos del timeline */
.timeline {
  position: relative;
  padding: 0 0 20px 0;
  /* Borde vertical a la izquierda */
  border-left: 3px solid #45C4B0;
  margin-left: 20px; /* Separación del borde del contenedor */
}

/* Cada item */
.timeline-item {
  position: relative;
  padding: 20px 0 20px 40px; /* Espacio para el punto circular */
}

/* Punto circular */
.timeline-dot {
  position: absolute;
  left: -10px; /* Centrado en la línea de 3px */
  top: 20px;   /* Ajusta para alinear con el contenido */
  width: 20px;
  height: 20px;
  background-color: #45C4B0;
  border-radius: 50%;
  border: 2px solid #012030; /* Borde del color de fondo, simulando un aro */
}

/* Contenedor de texto de cada evento */
.timeline-content {
  background-color: #A4F0C4; /* Tarjeta verde menta */
  color: #012030;           /* Texto oscuro */
  padding: 15px;
  border-radius: 6px;
  max-width: 600px;
}

/* Estilos para año, título y descripción */
.timeline-year {
  font-size: 1.2em;
  color: #13678A; /* Más oscuro para resaltar el año */
  margin-bottom: 5px;
}

.timeline-title {
  margin: 0;
  font-weight: 600;
  color: #012030;
}

.timeline-description {
  margin-top: 5px;
}
</style>
