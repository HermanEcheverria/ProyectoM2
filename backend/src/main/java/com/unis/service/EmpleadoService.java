package com.unis.service;

import java.util.List;
import java.util.Optional;
import com.unis.model.Empleado;
import com.unis.repository.EmpleadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EmpleadoService {

    @Inject
    EmpleadoRepository empleadoRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.listAll();
    }

    public Optional<Empleado> getEmpleadoById(Long id) {
        return empleadoRepository.findByIdOptional(id);
    }

    @Transactional
    public void registrarEmpleado(Empleado empleado) {
        try {
            entityManager.createNativeQuery("BEGIN REGISTRAR_EMPLEADO(?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
                .setParameter(1, empleado.getUsuario().getNombreUsuario()) // Se agregó el nombre del usuario
                .setParameter(2, empleado.getApellido())
                .setParameter(3, empleado.getDocumento())
                .setParameter(4, empleado.getFechaNacimiento())
                .setParameter(5, empleado.getGenero())
                .setParameter(6, empleado.getTelefono())
                .setParameter(7, empleado.getUsuario().getCorreo())
                .setParameter(8, empleado.getUsuario().getContrasena())
                .setParameter(9, empleado.getPuesto()) 
                .executeUpdate();
        } catch (Exception e) {
            if (e.getMessage().contains("ORA-20001")) {
                throw new WebApplicationException("Error: El correo ya está registrado.", Response.Status.BAD_REQUEST);
            }
            throw new WebApplicationException("Error interno del servidor.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public boolean actualizarEmpleado(Long id, Empleado empleado) {
        try {
            entityManager.createNativeQuery("BEGIN ACTUALIZAR_EMPLEADO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
                .setParameter(1, id)
                .setParameter(2, empleado.getUsuario().getNombreUsuario()) 
                .setParameter(3, empleado.getApellido())
                .setParameter(4, empleado.getDocumento())
                .setParameter(5, empleado.getFechaNacimiento())
                .setParameter(6, empleado.getGenero())
                .setParameter(7, empleado.getTelefono())
                .setParameter(8, empleado.getUsuario().getCorreo())
                .setParameter(9, empleado.getUsuario().getContrasena())
                .setParameter(10, empleado.getPuesto()) 
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
    public boolean eliminarEmpleado(Long id) {
        try {
            entityManager.createNativeQuery("BEGIN ELIMINAR_EMPLEADO(?); END;")
                .setParameter(1, id)
                .executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
