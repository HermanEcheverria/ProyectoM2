package com.unis.repository;

import com.unis.model.RecetaMedicamento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RecetaMedicamentoRepository implements PanacheRepository<RecetaMedicamento> {

    public List<RecetaMedicamento> listarPorReceta(Long idReceta) {
        return list("receta.idReceta", idReceta);
    }

    public List<RecetaMedicamento> listarPorMedicamento(Long idMedicamento) {
        return list("medicamento.idMedicamento", idMedicamento);
    }
}
