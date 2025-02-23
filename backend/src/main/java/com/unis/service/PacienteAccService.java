package com.unis.service;

import com.unis.model.PacienteAcc;
import com.unis.repository.PacienteAccRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class PacienteAccService {
    @Inject
    PacienteAccRepository pacienteAccRepository;

    public Optional<PacienteAcc> getPacienteById(Long id) {
        return pacienteAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    @Transactional
    public void updatePaciente(Long id, PacienteAcc pacienteData) {
        Optional<PacienteAcc> existingPaciente = pacienteAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingPaciente.isPresent()) {
            PacienteAcc paciente = existingPaciente.get();
            paciente.setApellido(pacienteData.getApellido());
            paciente.setDocumento(pacienteData.getDocumento());
            paciente.setFechaNacimiento(pacienteData.getFechaNacimiento());
            paciente.setGenero(pacienteData.getGenero());
            paciente.setTelefono(pacienteData.getTelefono());
            pacienteAccRepository.persist(paciente);
        }
    }
}
