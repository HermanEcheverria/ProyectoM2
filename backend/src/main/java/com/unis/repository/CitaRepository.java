package com.unis.repository;

import com.unis.model.Cita;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CitaRepository implements PanacheRepository<Cita> {
}
