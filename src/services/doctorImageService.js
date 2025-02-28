import axios from 'axios';

const API_URL = "http://localhost:8080/api/doctor-images";

export default {
  async getDoctorData(doctorId) {
    return await axios.get(`${API_URL}/${doctorId}`);
  },

  async uploadImage(doctorId, file, fileTitle) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileTitle', fileTitle);

    return await axios.post(`${API_URL}/${doctorId}/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },

  async deleteDoctorImage(doctorId) {
    return await axios.delete(`${API_URL}/${doctorId}/delete`);
  }
};
