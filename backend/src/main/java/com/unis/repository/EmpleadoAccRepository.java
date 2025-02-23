package com.unis.repository;

import com.unis.model.EmpleadoAcc;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpleadoAccRepository implements PanacheRepository<EmpleadoAcc> {
}
