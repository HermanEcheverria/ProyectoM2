package com.unis.repository;

import com.unis.model.PacienteFT;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PacienteFTRepository implements PanacheRepository<PacienteFT> {
}
