package com.unis.repository;

import com.unis.model.Medicamento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MedicamentoRepository implements PanacheRepository<Medicamento> {

    // Método para obtener un medicamento por ID (Ya viene incluido en Panache)
    public Medicamento buscarPorId(Long id) {
        return findById(id);
    }
}