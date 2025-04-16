package com.unis.service;

import com.unis.model.Historia;
import com.unis.repository.HistoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class HistoriaService {

    @Inject
    HistoriaRepository historiaRepository;

    /**
     * Listar todas las historias.
     */
    public List<Historia> listar() {
        return historiaRepository.listAll();
    }

    /**
     * Listar historias por estado (PROCESO, PUBLICADO, RECHAZADO).
     */
    public List<Historia> listarPorEstado(String estado) {
        return historiaRepository.findByStatus(estado);
    }

    /**
     * Obtener historia por ID.
     */
    public Historia obtenerPorId(Long id) {
        return historiaRepository.findById(id);
    }

    /**
     * Crear una nueva historia.
     */
    @Transactional
    public Historia crear(Historia historia) {
        if (historia.getEditorEmail() == null) {
            throw new IllegalArgumentException("El email del editor es obligatorio.");
        }
        historia.setStatus("PROCESO");
        historiaRepository.persist(historia);
        return historia;
    }

    /**
     * Actualizar una historia existente.
     */
    @Transactional
    public Historia actualizar(Long id, Historia historiaActualizada) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }

        historia.setNombreEntidad(historiaActualizada.getNombreEntidad());
        historia.setHistoria(historiaActualizada.getHistoria());
        historia.setMeritos(historiaActualizada.getMeritos());
        historia.setLineaDelTiempo(historiaActualizada.getLineaDelTiempo());
        historia.setStatus(historiaActualizada.getStatus());
        historia.setRejectionReason(historiaActualizada.getRejectionReason());
        historia.setEditorEmail(historiaActualizada.getEditorEmail());

        return historia;
    }

    /**
     * Aprobar historia (cambia estado a PUBLICADO).
     */
    @Transactional
    public Historia aprobar(Long id) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }
        historia.setStatus("PUBLICADO");
        historia.setRejectionReason(null);
        return historia;
    }

    /**
     * Rechazar historia con motivo.
     */
    @Transactional
    public Historia rechazar(Long id, String motivo) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }
        historia.setStatus("RECHAZADO");
        historia.setRejectionReason(motivo);
        return historia;
    }

    /**
     * Eliminar historia por ID.
     */
    @Transactional
    public boolean eliminar(Long id) {
        return historiaRepository.deleteById(id);
    }
}
