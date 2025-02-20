package com.unis.repository;

import com.unis.model.Servicio;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioRepository implements PanacheRepository<Servicio> {
}
