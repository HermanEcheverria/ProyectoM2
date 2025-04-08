import axios from "axios";

// Este apunta a tu backend Quarkus (el hospital)
const API = "http://localhost:8080/hospital/solicitudes";

export const enviarSolicitudHospital = async (hospital) => {
  const res = await axios.post(API, hospital);
  return res.data;
};
