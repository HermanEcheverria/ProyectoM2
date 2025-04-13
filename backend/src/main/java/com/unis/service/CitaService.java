package com.unis.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

import com.unis.model.*;
import com.unis.dto.CitaDTO;
import com.unis.repository.CitaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CitaService {

    @Inject
    CitaRepository citaRepository;

    @Inject
    DoctorService doctorService;

    @PersistenceContext
    EntityManager entityManager;

    public List<Cita> obtenerCitas() {
        return citaRepository.listAll();
    }

    public Cita obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    public Doctor buscarDoctorPorId(Long id) {
        return doctorService.getDoctorById(id).orElse(null);
    }

    @Transactional
    public void agendarCita(Cita cita) {
        if (cita.getIdDoctor() == null || cita.getIdPaciente() == null) {
            throw new IllegalArgumentException("El ID del doctor y paciente son obligatorios.");
        }

        Doctor doctor = entityManager.find(Doctor.class, cita.getIdDoctor());
        Paciente paciente = entityManager.find(Paciente.class, cita.getIdPaciente());

        if (doctor == null || paciente == null) {
            throw new IllegalArgumentException("Doctor o paciente no encontrados.");
        }

        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        citaRepository.persist(cita);
    }

    @Transactional
    public void cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id);
        if (cita != null) {
            cita.setEstado(EstadoCita.CANCELADA);
        } else {
            throw new IllegalArgumentException("Cita no encontrada");
        }
    }

    @Transactional
    public void actualizarCita(Long id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        if (citaActualizada.getEstado() != null) cita.setEstado(citaActualizada.getEstado());
        if (citaActualizada.getDiagnostico() != null) cita.setDiagnostico(citaActualizada.getDiagnostico());
        if (citaActualizada.getResultados() != null) cita.setResultados(citaActualizada.getResultados());
    }

    @Transactional
    public void procesarCita(Long id) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        cita.setEstado(EstadoCita.FINALIZADA);
    }

    @Transactional
    public void procesarCitaYEnviarResultados(Long id, String diagnostico, String resultados) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) throw new IllegalArgumentException("Cita no encontrada");

        cita.setDiagnostico(diagnostico);
        cita.setResultados(resultados);
        cita.setEstado(EstadoCita.FINALIZADA);

        enviarResultadosAAseguradora(cita);
    }

    private void enviarResultadosAAseguradora(Cita cita) {
        try {
            JsonObject json = Json.createObjectBuilder()
    .add("idCita", cita.getIdCita())
    .add("documento", cita.getPaciente().getDocumento())
    .add("nombre", cita.getPaciente().getUsuario().getNombreUsuario())
    .add("apellido", cita.getPaciente().getApellido())
    .add("diagnostico", cita.getDiagnostico())
    .add("resultados", cita.getResultados())
    .add("fecha", cita.getFecha().toString())
    .add("doctor", cita.getDoctor().getUsuario().getNombreUsuario())
    .build();


            System.out.println("📤 Enviando resultado: " + json);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5001/api/resultados"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> System.out.println("✅ Resultado enviado: " + response.statusCode()));
        } catch (Exception e) {
            System.err.println("❌ Error enviando resultados: " + e.getMessage());
        }
    }

    @Transactional
    public void reasignarDoctor(Long idCita, Doctor nuevoDoctor) {
        Cita cita = citaRepository.findById(idCita);
        if (cita == null || nuevoDoctor == null) {
            throw new IllegalArgumentException("Cita o doctor no válidos");
        }
        cita.setDoctor(nuevoDoctor);
    }

    @Transactional
public void crearCitaDesdeJson(JsonObject dto) {
    Cita cita = new Cita();

    cita.setFecha(LocalDate.parse(dto.getString("fecha"))); 
    cita.setHoraInicio(dto.getString("horaInicio"));
    cita.setHoraFin(dto.getString("horaFin"));
    cita.setMotivo(dto.getString("motivo"));
    cita.setNumeroAutorizacion(dto.getString("numeroAutorizacion"));
    cita.setEstado(EstadoCita.CONFIRMADA);

    citaRepository.persist(cita);
    System.out.println("✅ Cita guardada en la base de datos");
}

    
}
