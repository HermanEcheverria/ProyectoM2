package com.unis.repository;

import com.unis.model.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PacienteRepository implements PanacheRepository<Paciente> {
}
