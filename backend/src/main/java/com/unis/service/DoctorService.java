package com.unis.service;

import java.util.List;
import java.util.Optional;

import com.unis.model.Doctor;
import com.unis.repository.DoctorRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class DoctorService {

    @Inject
    DoctorRepository doctorRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Doctor> getAllDoctores() {
        return doctorRepository.listAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findByIdOptional(id);
    }

    @Transactional
    public void registrarDoctor(Doctor doctor) {
        try {
            entityManager.createNativeQuery("BEGIN REGISTRAR_DOCTOR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
                .setParameter(1, doctor.getUsuario().getNombreUsuario())
                .setParameter(2, doctor.getApellido())
                .setParameter(3, doctor.getDocumento())
                .setParameter(4, doctor.getFechaNacimiento())
                .setParameter(5, doctor.getGenero())
                .setParameter(6, doctor.getTelefono())
                .setParameter(7, doctor.getUsuario().getCorreo())
                .setParameter(8, doctor.getUsuario().getContrasena())
                .setParameter(9, doctor.getEspecialidad())
                .setParameter(10, doctor.getNumeroColegiado())
                .setParameter(11, doctor.getHorarioAtencion())
                .setParameter(12, doctor.getFechaGraduacion())
                .setParameter(13, doctor.getUniversidadGraduacion())
                .executeUpdate();
        } catch (Exception e) {
            throw new WebApplicationException("Error en la transacción.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public boolean actualizarDoctor(Long id, Doctor doctor) {
        try {
            entityManager.createNativeQuery("BEGIN ACTUALIZAR_DOCTOR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
            .setParameter(1, id)
            .setParameter(2, doctor.getUsuario().getNombreUsuario())
            .setParameter(3, doctor.getApellido())
            .setParameter(4, doctor.getDocumento())
            .setParameter(5, doctor.getFechaNacimiento())
            .setParameter(6, doctor.getGenero())
            .setParameter(7, doctor.getTelefono())
            .setParameter(8, doctor.getUsuario().getCorreo())
            .setParameter(9, doctor.getUsuario().getContrasena())
            .setParameter(10, doctor.getEspecialidad())
            .setParameter(11, doctor.getNumeroColegiado())
            .setParameter(12, doctor.getHorarioAtencion())
            .setParameter(13, doctor.getFechaGraduacion())
            .setParameter(14, doctor.getUniversidadGraduacion())
            .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean eliminarDoctor(Long id) {
        try {
            entityManager.createNativeQuery("BEGIN ELIMINAR_DOCTOR(?); END;")
                .setParameter(1, id)
                .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
