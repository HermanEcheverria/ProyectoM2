package com.unis.repository;

import com.unis.model.Historia;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HistoriaRepository implements PanacheRepository<Historia> {
    // Aquí puedes agregar métodos personalizados si lo requieres.
}
