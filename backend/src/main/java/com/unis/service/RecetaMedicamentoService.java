package com.unis.service;

import com.unis.model.RecetaMedicamento;
import com.unis.repository.RecetaMedicamentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RecetaMedicamentoService {

    @Inject
    RecetaMedicamentoRepository recetaMedicamentoRepository;

    public List<RecetaMedicamento> listarPorReceta(Long idReceta) {
        return recetaMedicamentoRepository.listarPorReceta(idReceta);
    }

    @Transactional
    public void agregarMedicamentoAReceta(RecetaMedicamento recetaMedicamento) {
        recetaMedicamentoRepository.persist(recetaMedicamento);
    }

    @Transactional
    public boolean eliminar(Long id) {
        return recetaMedicamentoRepository.deleteById(id);
    }
}
