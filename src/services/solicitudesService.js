import axios from "axios";

// Usa la URL dinámica basada en el entorno
const API = `${window.location.protocol}//${window.location.hostname}:5001/api`;

// Enviar solicitud hospitalaria a la aseguradora
export const enviarSolicitudHospital = async ({ afiliado, servicio, monto, hospital, aseguradoraId }) => {
  try {
    const payload = {
      afiliado,
      servicio,
      hospital,
      monto,
      aseguradora: aseguradoraId
    };

    const res = await fetch(`${API}/solicitudes-atencion`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const error = await res.json();
      throw new Error(error.message || "Error al enviar solicitud");
    }

    return await res.json();

  } catch (err) {
    console.error("❌ Error en enviarSolicitudHospital:", err.message);
    throw err;
  }
};

