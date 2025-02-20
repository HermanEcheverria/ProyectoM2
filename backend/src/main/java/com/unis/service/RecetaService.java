package com.unis.service;

import com.unis.model.Receta;
import com.unis.model.DetalleReceta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecetaService {

    @Inject
    EntityManager entityManager;

    // Crear nueva receta con detalles de medicamentos
    @Transactional
    public void crearReceta(Receta receta) {
        try {
            Receta nuevaReceta = new Receta();
            nuevaReceta.setIdPaciente(receta.getIdPaciente());
            nuevaReceta.setIdDoctor(receta.getIdDoctor());
            nuevaReceta.setDiagnostico(receta.getDiagnostico());
            nuevaReceta.setObservaciones(receta.getObservaciones());
            nuevaReceta.setEstado("activa");
            nuevaReceta.setFecha(Timestamp.valueOf(LocalDateTime.now()));
            entityManager.persist(nuevaReceta);

            // Insertar detalles de medicamentos
            receta.getDetalleMedicamentos().forEach(m -> {
                DetalleReceta detalle = new DetalleReceta();
                detalle.setIdReceta(nuevaReceta.getIdReceta());
                detalle.setPrincipioActivo(m.getPrincipioActivo());
                detalle.setConcentracion(m.getConcentracion());
                detalle.setPresentacion(m.getPresentacion());
                detalle.setDosis(m.getDosis());
                detalle.setFrecuencia(m.getFrecuencia());
                detalle.setDuracion(m.getDuracion());
                entityManager.persist(detalle);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear receta: " + e.getMessage());
        }
    }

    // Obtener recetas por paciente
    public List<Receta> obtenerRecetasPorPaciente(int idPaciente) {
        try {
            return entityManager.createQuery("SELECT r FROM Receta r WHERE r.idPaciente = :idPaciente", Receta.class)
                    .setParameter("idPaciente", idPaciente)
                    .getResultList()
                    .stream()
                    .map(Receta::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener recetas: " + e.getMessage());
        }
    }
}
