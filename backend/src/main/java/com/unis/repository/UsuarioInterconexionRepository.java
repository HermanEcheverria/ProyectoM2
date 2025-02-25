package com.unis.repository;

import com.unis.model.UsuarioInter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioInterconexionRepository implements PanacheRepository<UsuarioInter> {
}

