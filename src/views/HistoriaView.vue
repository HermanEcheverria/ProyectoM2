<template>
  <div class="historia-container">
    <div class="historia">
      <h1>{{ historia.nombreEntidad }}</h1>

      <div class="historia-layout">
        <!-- Sección de Historia -->
        <section class="historia-info">
          <h2>Historia</h2>
          <div v-html="historia.historia"></div>
        </section>

        <!-- Sección de Méritos -->
        <section class="meritos-info">
          <h2>Méritos</h2>
          <div class="meritos-grid">
            <div v-for="(merito, index) in meritosData" :key="index" class="merito-card">
              <span class="merito-icon">✔️</span>
              <div class="merito-content">
                <h4 class="merito-title">{{ merito.title }}</h4>
                <p class="merito-description">{{ merito.description }}</p>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- Sección Línea del Tiempo -->
      <section class="timeline-section">
        <h2>Línea del Tiempo</h2>
        <div class="timeline">
          <div v-for="(evento, index) in timelineData" :key="index" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h3 class="timeline-year">{{ evento.year }}</h3>
              <h4 class="timeline-title">{{ evento.title }}</h4>
              <p class="timeline-description">{{ evento.description }}</p>
            </div>
          </div>
        </div>
      </section>
    </div>
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
      timelineData: [],
      meritosData: []
    };
  },
  async created() {
    try {
      const response = await axios.get("http://localhost:8080/historias");
      if (response.data && response.data.length > 0) {
        this.historia = response.data[0];

        try {
          // Parseamos la línea del tiempo si es un JSON válido
          this.timelineData = JSON.parse(this.historia.lineaDelTiempo || "[]");
        } catch (error) {
          console.error("Error al parsear la línea del tiempo:", error);
          this.timelineData = [];
        }

        try {
          // Validamos si 'meritos' es un JSON válido antes de parsearlo
          if (typeof this.historia.meritos === "string" && this.historia.meritos.trim().startsWith("[")) {
            this.meritosData = JSON.parse(this.historia.meritos);
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
  }
};
</script>

<style scoped>
.historia-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: #0b3d5d;
  padding: 4rem 2rem;
}

.historia {
  width: 100%;
  max-width: 1400px;
  background-color: #012030;
  color: #DAFDBA;
  padding: 3rem;
  border-radius: 12px;
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.3);
}

/* 🎯 Título Principal */
.historia h1 {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 40px;
  color: #A4F0C4;
}

/* 📌 Distribución en columnas */
.historia-layout {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

/* 📜 Sección Historia */
.historia-info {
  background-color: #13678A;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.2);
}

/* 🎯 Título de Historia y Méritos */
.historia-info h2, .meritos-info h2 {
  text-align: center;
  margin-bottom: 15px;
  color: #A4F0C4;
  font-size: 1.8rem;
  font-weight: bold;
}

/* ✅ Mejoras en el texto de la historia */
.historia-info div {
  font-size: 1.2rem;
  line-height: 1.9;
  text-align: justify;
  color: #DAFDBA;
}

/* ✨ Resaltar palabras clave */
.historia-info div strong {
  color: #A4F0C4;
  font-weight: bold;
}

/* 🔹 Espaciado entre párrafos */
.historia-info div p {
  margin-bottom: 15px;
}

/* 🎯 Enfatizar información con cursiva */
.historia-info div em {
  color: #45C4B0;
  font-style: italic;
}

/* 🏅 Estilos de Méritos */
.meritos-info {
  background-color: #13678A;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.2);
}

.meritos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.merito-card {
  background-color: #A4F0C4;
  color: #012030;
  padding: 15px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.merito-icon {
  font-size: 1.5rem;
  color: #13678A;
}

.merito-title {
  font-weight: bold;
}

/* 📅 Línea del Tiempo */
.timeline-section {
  margin-top: 50px;
}

.timeline {
  width: 100%;
  max-width: 1100px;
  padding-left: 50px;
  margin: 0 auto;
  border-left: 4px solid #45C4B0;
  padding-top: 20px;
}

.timeline-item {
  position: relative;
  padding-left: 50px;
  margin-bottom: 40px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.timeline-dot {
  width: 20px;
  height: 20px;
  background-color: #45C4B0;
  border-radius: 50%;
  position: absolute;
  left: -10px;
  top: 15px;
}

/* 📱 Adaptación para móviles */
@media (max-width: 768px) {
  .historia-info div {
    font-size: 1rem;
    line-height: 1.6;
  }
}

</style>
