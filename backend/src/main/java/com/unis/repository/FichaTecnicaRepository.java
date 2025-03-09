package com.unis.repository;

import com.unis.model.FichaTecnica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FichaTecnicaRepository implements PanacheRepository<FichaTecnica> {
}
