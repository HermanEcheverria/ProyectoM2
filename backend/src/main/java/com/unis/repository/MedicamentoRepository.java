package com.unis.repository;

import com.unis.model.Medicamento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MedicamentoRepository implements PanacheRepository<Medicamento> {
    // Métodos personalizados si se requieren consultas específicas

    public Medicamento buscarPorNombre(String principioActivo) {
        return find("principioActivo", principioActivo).firstResult();
    }
}
