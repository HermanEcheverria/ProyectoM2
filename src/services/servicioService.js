import axios from "axios";
import API_URL from "../config";

const API_ENDPOINT = `${API_URL}/api/servicios`;

export const getServicios = async () => {
  const response = await axios.get(API_ENDPOINT);
  return response.data;
};

export const addServicio = async (servicio) => {
  await axios.post(API_ENDPOINT, servicio);
};

export const deleteServicio = async (id) => {
  await axios.delete(`${API_ENDPOINT}/${id}`);
};
