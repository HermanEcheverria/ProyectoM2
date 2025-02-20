package com.unis.service;

import java.util.List;
import java.util.Optional;

import com.unis.model.Paciente;
import com.unis.repository.PacienteRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PacienteService {

    @Inject
    PacienteRepository pacienteRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Paciente> getAllPacientes() {
        return pacienteRepository.listAll();
    }

    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findByIdOptional(id);
    }

    @Transactional
public void registrarPaciente(Paciente paciente) {
    try {
        entityManager.createNativeQuery("BEGIN REGISTRAR_PACIENTE(?, ?, ?, ?, ?, ?, ?, ?); END;")
            .setParameter(1, paciente.getUsuario().getNombreUsuario())
            .setParameter(2, paciente.getApellido())
            .setParameter(3, paciente.getDocumento())
            .setParameter(4, paciente.getFechaNacimiento())
            .setParameter(5, paciente.getGenero())
            .setParameter(6, paciente.getTelefono())
            .setParameter(7, paciente.getUsuario().getCorreo())
            .setParameter(8, paciente.getUsuario().getContrasena()) 
            .executeUpdate();
    } catch (Exception e) {
        if (e.getMessage().contains("ORA-20001")) {
            throw new WebApplicationException(" Error: El correo ya está registrado.", Response.Status.BAD_REQUEST);
        }
        throw new WebApplicationException(" Error interno del servidor.", Response.Status.INTERNAL_SERVER_ERROR);
    }
}

    @Transactional
public boolean actualizarPaciente(Long id, Paciente paciente) {
    try {
        entityManager.createNativeQuery("BEGIN ACTUALIZAR_PACIENTE(?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
            .setParameter(1, id)
            .setParameter(2, paciente.getUsuario().getNombreUsuario())
            .setParameter(3, paciente.getApellido())
            .setParameter(4, paciente.getDocumento())
            .setParameter(5, paciente.getFechaNacimiento())
            .setParameter(6, paciente.getGenero())
            .setParameter(7, paciente.getTelefono())
            .setParameter(8, paciente.getUsuario().getCorreo())
            .setParameter(9, paciente.getUsuario().getContrasena())
            .executeUpdate();
        return true;
    } catch (Exception e) {
        if (e.getMessage().contains("El correo ya está registrado en el sistema")) {
            throw new WebApplicationException("Error: El correo ya está registrado en el sistema.", Response.Status.BAD_REQUEST);
        }
        e.printStackTrace();
        return false;
    }
}



    @Transactional
    public boolean eliminarPaciente(Long id) {
        try {
            entityManager.createNativeQuery("BEGIN ELIMINAR_PACIENTE(?); END;")
                .setParameter(1, id)
                .executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
