package com.unis.repository;

import java.util.Optional;

import com.unis.model.Paciente;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PacienteRepository implements PanacheRepository<Paciente> {

    public Optional<Paciente> buscarPorDocumento(String documento) {
        return find("documento", documento).firstResultOptional();
    }
}
