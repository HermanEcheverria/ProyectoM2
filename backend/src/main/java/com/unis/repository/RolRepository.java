package com.unis.repository;

import com.unis.model.Rol;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio para la entidad {@link Rol}.
 * Esta clase utiliza PanacheRepository para simplificar las operaciones CRUD.
 */
@ApplicationScoped
public class RolRepository implements PanacheRepository<Rol> {
    // Métodos CRUD básicos ya están implementados en PanacheRepository.
    // Si necesitas consultas personalizadas, agrégalas aquí.
}
