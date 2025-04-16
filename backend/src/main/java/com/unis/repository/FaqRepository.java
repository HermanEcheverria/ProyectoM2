package com.unis.repository;

import com.unis.model.Faq;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FaqRepository implements PanacheRepository<Faq> {

    public List<Faq> findByStatus(String status) {
        return list("status = ?1", status);
    }

    public Faq findById(Long id) {
        return find("id = ?1", id).firstResult();
    }
}
