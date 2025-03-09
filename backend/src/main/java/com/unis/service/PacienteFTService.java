package com.unis.service;

import java.util.List;
import java.util.Optional;
import com.unis.model.PacienteFT;
import com.unis.repository.PacienteFTRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PacienteFTService {

    @Inject
    PacienteFTRepository pacienteFTRepository;

    public List<PacienteFT> getAllPacientes() {
        return pacienteFTRepository.listAll();
    }

    public Optional<PacienteFT> getPacienteById(Long id) {
        return pacienteFTRepository.findByIdOptional(id);
    }

    @Transactional
    public void registrarPaciente(PacienteFT paciente) {
        pacienteFTRepository.persist(paciente);
    }
}
