package com.unis.service;

import java.util.List;

import com.unis.model.Medicamento;
import com.unis.repository.MedicamentoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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

    @Transactional
    public Medicamento crearMedicamento(Medicamento medicamento) {
        medicamentoRepository.persist(medicamento);
        return medicamento;
    }

    @Transactional
    public Medicamento actualizarMedicamento(Long id, Medicamento medicamentoNuevo) {
        Medicamento existente = medicamentoRepository.findById(id);
        if (existente == null) {
            return null;
        }
        // Actualizar los campos del medicamento existente
        existente.setPrincipioActivo(medicamentoNuevo.getPrincipioActivo());
        existente.setConcentracion(medicamentoNuevo.getConcentracion());
        existente.setPresentacion(medicamentoNuevo.getPresentacion());
        existente.setFormaFarmaceutica(medicamentoNuevo.getFormaFarmaceutica());
        existente.setVentaLibre(medicamentoNuevo.getVentaLibre());
        return existente;
    }

    @Transactional
    public boolean eliminarMedicamento(Long id) {
        return medicamentoRepository.deleteById(id);
    }
}
