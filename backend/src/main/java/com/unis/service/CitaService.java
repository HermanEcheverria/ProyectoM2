package com.unis.service;

import java.util.List;

import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.Paciente;
import com.unis.repository.CitaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CitaService {

    @Inject
    CitaRepository citaRepository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Obtiene la lista de todas las citas registradas.
     */
    public List<Cita> obtenerCitas() {
        return citaRepository.listAll();
    }

    /**
     * Obtiene una cita por su ID.
     */
    public Cita obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    /**
     * Agenda una nueva cita médica, validando la información.
     */
    @Transactional
    public void agendarCita(Cita cita) {
        System.out.println("📌 Datos recibidos en el backend: " + cita);
        System.out.println("📌 ID Doctor recibido: " + cita.getIdDoctor());
        System.out.println("📌 ID Paciente recibido: " + cita.getIdPaciente());

        if (cita.getIdDoctor() == null) {
            throw new IllegalArgumentException("⚠️ Error: El ID del doctor es obligatorio.");
        }
        if (cita.getIdPaciente() == null) {
            throw new IllegalArgumentException("⚠️ Error: El ID del paciente es obligatorio.");
        }

        Doctor doctor = entityManager.find(Doctor.class, cita.getIdDoctor());
        Paciente paciente = entityManager.find(Paciente.class, cita.getIdPaciente());

        if (doctor == null) {
            throw new IllegalArgumentException("⚠️ Error: No se encontró el doctor con ID " + cita.getIdDoctor());
        }
        if (paciente == null) {
            throw new IllegalArgumentException("⚠️ Error: No se encontró el paciente con ID " + cita.getIdPaciente());
        }

        cita.setDoctor(doctor);
        cita.setPaciente(paciente);

        System.out.println("✅ Doctor y Paciente asignados correctamente.");
        citaRepository.persist(cita);
    }

    /**
     * Cancela una cita médica por su ID.
     */
    @Transactional
    public void cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id);
        if (cita != null) {
            citaRepository.delete(cita);
        } else {
            throw new IllegalArgumentException("⚠️ Error: No se encontró la cita con ID " + id);
        }
    }

    /**
     * Actualiza una cita médica, por ejemplo para procesarla (cambiar estado, agregar diagnóstico y resultados).
     */
    @Transactional
    public void actualizarCita(Long id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) {
            throw new IllegalArgumentException("⚠️ Error: No se encontró la cita con ID " + id);
        }
        if (citaActualizada.getEstado() != null) {
            cita.setEstado(citaActualizada.getEstado());
        }
        if (citaActualizada.getDiagnostico() != null) {
            cita.setDiagnostico(citaActualizada.getDiagnostico());
        }
        if (citaActualizada.getResultados() != null) {
            cita.setResultados(citaActualizada.getResultados());
        }
        // Se pueden actualizar otros campos según se requiera.
    }
}
