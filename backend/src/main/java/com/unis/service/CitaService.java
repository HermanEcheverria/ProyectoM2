package com.unis.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.unis.model.Hospital;
import com.unis.model.Servicio;
import com.unis.model.Usuario;
import com.unis.model.Aseguradora;
import com.unis.dto.CitaDTO;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.EstadoCita;
import com.unis.model.Paciente;
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
            throw new IllegalArgumentException(" El ID del doctor y paciente son obligatorios.");
        }

        Doctor doctor = entityManager.find(Doctor.class, cita.getIdDoctor());
        Paciente paciente = entityManager.find(Paciente.class, cita.getIdPaciente());

        if (doctor == null || paciente == null) {
            throw new IllegalArgumentException(" Doctor o paciente no encontrados.");
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
            throw new IllegalArgumentException(" Cita no encontrada");
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
                .add("idCita", cita.getId())
                .add("dpi", cita.getPaciente().getDocumento())
                .add("diagnostico", cita.getDiagnostico())
                .add("resultados", cita.getResultados())
                .add("fecha", cita.getFecha().toString())
                .build();

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
    public void recibirCitaExternaDesdeAseguradora(CitaDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO de cita es null");

        System.out.println(" Recibiendo cita externa desde aseguradora: " + dto);

        Paciente paciente = entityManager
            .createQuery("SELECT p FROM Paciente p WHERE p.documento = :dpi", Paciente.class)
            .setParameter("dpi", dto.dpi)
            .getResultStream()
            .findFirst()
            .orElse(null);

        if (paciente == null) {
            paciente = new Paciente();
            paciente.setDocumento(dto.dpi);
            paciente.setApellido(dto.apellido);

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(dto.nombre);
            usuario.setCorreo("auto_" + dto.dpi + "@hospital.com");
            usuario.setContrasena("1234");
            entityManager.persist(usuario);

            paciente.setUsuario(usuario);
            paciente.setIdUsuario(usuario.getId());
            entityManager.persist(paciente);

            System.out.println(" Paciente y usuario creados automáticamente");
        }

        Hospital hospital = entityManager.find(Hospital.class, dto.idHospital);
        if (hospital == null) {
            hospital = new Hospital();
            hospital.setId(dto.idHospital);
            hospital.setNombre("Hospital Auto " + dto.idHospital);
            hospital.setDireccion("Auto-generada");
            hospital.setTelefono("00000000");
            hospital.setCorreo("auto@hospital.com");
            hospital = entityManager.merge(hospital);
            System.out.println(" Hospital creado automáticamente");
        }

        Servicio servicio = entityManager.find(Servicio.class, dto.idServicio);
        if (servicio == null) {
            servicio = new Servicio();
            servicio.id = dto.idServicio;
            servicio.nombre = "Servicio Auto " + dto.idServicio;
            servicio.costo = 0.0;
            servicio.cubiertoSeguro = false;
            servicio = entityManager.merge(servicio);
            System.out.println(" Servicio creado automáticamente");
        }

        Aseguradora aseguradora = null;
        if (dto.idAseguradora != null) {
            aseguradora = entityManager.find(Aseguradora.class, dto.idAseguradora);
            if (aseguradora == null) {
                aseguradora = new Aseguradora();
                aseguradora.setId(dto.idAseguradora);
                aseguradora.setNombre("Aseguradora Auto " + dto.idAseguradora);
                aseguradora = entityManager.merge(aseguradora);
                System.out.println(" Aseguradora creada automáticamente");
            }
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setIdPaciente(paciente.getIdPaciente());
        cita.setFecha(dto.fecha);
        cita.setHoraInicio(dto.horaInicio);
        cita.setHoraFin(dto.horaFin);
        cita.setMotivo(dto.motivo);
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setNumeroAutorizacion(dto.numeroAutorizacion);
        cita.setHospital(hospital);
        cita.setIdHospital(hospital.getId());
        cita.setServicio(servicio);
        cita.setIdServicio(servicio.getId());

        if (aseguradora != null) {
            cita.setAseguradora(aseguradora);
            cita.setIdAseguradora(aseguradora.getId());
        }

        citaRepository.persist(cita);
        System.out.println(" Cita registrada exitosamente desde aseguradora");
    }
}