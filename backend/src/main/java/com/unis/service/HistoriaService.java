package com.unis.service;

import com.unis.model.Historia;
import com.unis.repository.HistoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class HistoriaService {

    @Inject
    HistoriaRepository historiaRepository;

    public List<Historia> listar() {
        return historiaRepository.listAll();
    }

    public Historia obtenerPorId(Long id) {
        return historiaRepository.findById(id);
    }

    @Transactional
    public Historia crear(Historia historia) {
        historiaRepository.persist(historia);
        return historia;
    }

    @Transactional
    public Historia actualizar(Long id, Historia historia) {
        Historia h = historiaRepository.findById(id);
        if (h != null) {
            h.setNombreEntidad(historia.getNombreEntidad());
            h.setHistoria(historia.getHistoria());
            h.setMeritos(historia.getMeritos());
            h.setLineaDelTiempo(historia.getLineaDelTiempo());
            return h;
        }
        return null;
    }

    @Transactional
    public boolean eliminar(Long id) {
        return historiaRepository.deleteById(id);
    }
}
