package com.unis.repository;

import java.util.List;

import com.unis.model.RecetaMedicamento;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link RecetaMedicamento} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class RecetaMedicamentoRepository implements PanacheRepository<RecetaMedicamento> {

    /**
     * Lists all {@link RecetaMedicamento} entities associated with a specific recipe.
     *
     * @param idReceta the ID of the recipe
     * @return a list of {@link RecetaMedicamento} entities linked to the given recipe
     */
    public List<RecetaMedicamento> listarPorReceta(Long idReceta) {
        return list("receta.idReceta", idReceta);
    }

    /**
     * Lists all {@link RecetaMedicamento} entities associated with a specific medication.
     *
     * @param idMedicamento the ID of the medication
     * @return a list of {@link RecetaMedicamento} entities linked to the given medication
     */
    public List<RecetaMedicamento> listarPorMedicamento(Long idMedicamento) {
        return list("medicamento.idMedicamento", idMedicamento);
    }
}
