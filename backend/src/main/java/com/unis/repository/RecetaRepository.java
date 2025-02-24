package com.unis.repository;

import com.unis.model.Receta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecetaRepository implements PanacheRepository<Receta> {
    // Métodos personalizados si se necesitan
}