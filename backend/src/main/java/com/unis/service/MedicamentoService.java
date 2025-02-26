package com.unis.service;

import com.unis.model.Medicamento;
import com.unis.repository.MedicamentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MedicamentoService {

    @Inject
    MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.listAll();
    }

    public Medicamento obtenerPorId(Long id) {
        return medicamentoRepository.findById(id);
    }
}
