<template>
  <div class="container">
    <h1>Gestión de Imágenes de Doctor</h1>

    <div v-if="doctor">
      <h2>ID: {{ doctor.idDoctor }}</h2>
      <h3>Nombre: {{ doctor.nombre }} {{ doctor.apellido }}</h3>
      <h3>Correo: {{ doctor.correo }}</h3>

      <div v-if="doctor.fotografia">
        <h3>Foto Actual</h3>
        <img :src="doctor.fotografia" alt="Doctor Image" width="200" />
      </div>

      <input type="file" @change="handleFileUpload" />
      <button @click="uploadImage">Subir Imagen</button>
      <button v-if="doctor.fotografia" @click="deleteImage">Eliminar Imagen</button>
    </div>

    <div v-else>
      <p>Cargando datos del doctor...</p>
    </div>
  </div>
</template>

<script>
import doctorImageService from "../services/doctorImageService";

export default {
  data() {
    return {
      doctorId: 1, // Cambia esto según el doctor a consultar
      doctor: null,
      selectedFile: null
    };
  },
  methods: {
    async loadDoctorData() {
      try {
        const response = await doctorImageService.getDoctorData(this.doctorId);
        this.doctor = response.data;

        if (this.doctor.fotografia) {
          this.doctor.fotografia = `data:image/png;base64,${this.doctor.fotografia}`;
        }
      } catch (error) {
        console.error("Error obteniendo datos del doctor:", error);
      }
    },

    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },

    async uploadImage() {
      if (!this.selectedFile) return;
      await doctorImageService.uploadImage(this.doctorId, this.selectedFile);
      this.loadDoctorData();
    },

    async deleteImage() {
      await doctorImageService.deleteDoctorImage(this.doctorId);
      this.doctor.fotografia = null;
    }
  },
  mounted() {
    this.loadDoctorData();
  }
};
</script>

<style scoped>
.container {
  text-align: center;
  color: white;
  background: #222;
  padding: 20px;
  border-radius: 10px;
}
button {
  margin: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
</style>
