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

    // Listar medicamentos
    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepository.listAll();
    }

    // Buscar medicamento por ID
    public Medicamento obtenerMedicamentoPorId(Long id) {
        return medicamentoRepository.findById(id);
    }

    // Crear medicamento
    @Transactional
    public void crearMedicamento(Medicamento medicamento) {
        medicamentoRepository.persist(medicamento);
    }

    // Actualizar medicamento
    @Transactional
    public void actualizarMedicamento(Medicamento medicamento) {
        medicamentoRepository.persist(medicamento);
    }

    // Eliminar medicamento
    @Transactional
    public boolean eliminarMedicamento(Long id) {
        return medicamentoRepository.deleteById(id);
    }
}