package com.unis.service;

import com.unis.model.UserAcc;
import com.unis.repository.UserAccRepository;
import com.unis.repository.DoctorAccRepository;
import com.unis.repository.EmpleadoAccRepository;
import com.unis.repository.PacienteAccRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserAccService {

    @Inject
    UserAccRepository userAccRepository;

    @Inject
    DoctorAccRepository doctorAccRepository;

    @Inject
    EmpleadoAccRepository empleadoAccRepository;

    @Inject
    PacienteAccRepository pacienteAccRepository;

    public Optional<UserAcc> getUserById(Long id) {
        return userAccRepository.findByIdOptional(id);
    }

    @Transactional
    public void updateUser(Long id, UserAcc updatedUser) {
        UserAcc existingUser = userAccRepository.findById(id);
        if (existingUser != null) {
            existingUser.setNombreUsuario(updatedUser.getNombreUsuario());
            existingUser.setCorreo(updatedUser.getCorreo());
            existingUser.setContrasena(updatedUser.getContrasena());
            userAccRepository.persist(existingUser);
        }
    }

    @Transactional
    public void changeUserRole(Long id, int nuevoRol) {
        UserAcc user = userAccRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // Eliminar los datos antiguos dependiendo del rol anterior
        switch (user.getRolId()) {
            case 2:
                doctorAccRepository.delete("idUsuario", id);
                break;
            case 3:
                empleadoAccRepository.delete("idUsuario", id);
                break;
            case 4:
                pacienteAccRepository.delete("idUsuario", id);
                break;
        }

        // Asignar nuevo rol
        user.setRolId(nuevoRol);
        userAccRepository.persist(user);
    }
}
