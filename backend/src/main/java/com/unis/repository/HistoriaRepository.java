package com.unis.repository;

import com.unis.model.Historia;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class HistoriaRepository implements PanacheRepository<Historia> {

    public List<Historia> findByStatus(String status) {
        return list("status", status);
    }

    public Historia findById(Long id) {
        return find("id", id).firstResult();
    }
}
