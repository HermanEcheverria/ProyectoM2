package com.unis.service;

import com.unis.model.EmpleadoAcc;
import com.unis.repository.EmpleadoAccRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class EmpleadoAccService {
    @Inject
    EmpleadoAccRepository empleadoAccRepository;

    public Optional<EmpleadoAcc> getEmpleadoById(Long id) {
        return empleadoAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    @Transactional
    public void updateEmpleado(Long id, EmpleadoAcc empleadoData) {
        Optional<EmpleadoAcc> existingEmpleado = empleadoAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingEmpleado.isPresent()) {
            EmpleadoAcc empleado = existingEmpleado.get();
            empleado.setApellido(empleadoData.getApellido());
            empleado.setDocumento(empleadoData.getDocumento());
            empleado.setFechaNacimiento(empleadoData.getFechaNacimiento());
            empleado.setGenero(empleadoData.getGenero());
            empleado.setTelefono(empleadoData.getTelefono());
            empleado.setPuesto(empleadoData.getPuesto());
            empleadoAccRepository.persist(empleado);
        }
    }
}
