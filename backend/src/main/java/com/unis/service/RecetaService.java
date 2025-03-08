package com.unis.service;
import com.unis.repository.RecetaRepository;

import com.unis.model.Medicamento;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class RecetaService {

    @Inject
    EntityManager em;

    @Inject
    RecetaRepository recetaRepository;

    @Transactional
    public Receta crearReceta(Receta receta) {
        try {
            System.out.println("📌 Iniciando creación de receta...");

            // Validaciones de datos obligatorios
            if (receta.getIdDoctor() == null || receta.getIdPaciente() == null) {
                throw new RuntimeException("❌ Error: idDoctor e idPaciente son obligatorios.");
            }
            if (receta.getCodigoReceta() == null || receta.getCodigoReceta().isEmpty()) {
                throw new RuntimeException("❌ Error: Código de receta es obligatorio.");
            }

            // Asignar fecha de creación si es null
            if (receta.getFechaCreacion() == null) {
                receta.setFechaCreacion(new Date());
            }

            // Guardar la receta en la base de datos
            em.persist(receta);
            em.flush(); // 💡 Importante para obtener el ID generado

            System.out.println("✅ Receta guardada con ID: " + receta.getIdReceta());
            return receta;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al guardar la receta: " + e.getMessage());
        }
    }

     @Transactional
public Receta buscarPorIdCita(int idCita) {
    Receta receta = recetaRepository.find("idCita", idCita).firstResult();
    if (receta != null) {
        // 💡 Forzar carga de medicamentos antes de devolver la receta
        receta.getMedicamentos().size(); // Esto obliga a Hibernate a traer la lista
    }
    return receta;
}


@Transactional
public Receta actualizarReceta(Long idReceta, Receta recetaActualizada) {
    try {
        System.out.println("📌 Iniciando actualización de receta con ID: " + idReceta);

        Receta recetaExistente = em.find(Receta.class, idReceta);
        if (recetaExistente == null) {
            throw new RuntimeException("❌ Error: No se encontró la receta con ID " + idReceta);
        }

        // ⚡ Actualizar solo los campos editables
        recetaExistente.setAnotaciones(recetaActualizada.getAnotaciones());
        recetaExistente.setNotasEspeciales(recetaActualizada.getNotasEspeciales());

        // ⚡ Eliminar medicamentos anteriores
        recetaExistente.getMedicamentos().clear();
        em.flush(); // 🔥 Necesario para aplicar el cambio antes de agregar nuevos medicamentos

        // ⚡ Agregar medicamentos actualizados
        for (RecetaMedicamento med : recetaActualizada.getMedicamentos()) {
            Medicamento medicamento = em.find(Medicamento.class, med.getMedicamento().getIdMedicamento());
            if (medicamento == null) {
                throw new RuntimeException("❌ Error: No se encontró el medicamento con ID " + med.getMedicamento().getIdMedicamento());
            }

            med.setReceta(recetaExistente);
            med.setMedicamento(medicamento);

            // ⚠️ Usar merge en lugar de persist para evitar error de detached entity
            em.merge(med);
            recetaExistente.getMedicamentos().add(med);
        }

        // 💾 Guardar cambios en la receta
        em.merge(recetaExistente);

        System.out.println("✅ Receta actualizada correctamente con ID: " + idReceta);
        return recetaExistente;
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("❌ Error al actualizar la receta: " + e.getMessage());
    }
}






    @Transactional
    public RecetaMedicamento agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        try {
            System.out.println("📌 Iniciando adición de medicamento...");

            // Validaciones
            if (recetaMedicamento.getIdReceta() == null || recetaMedicamento.getIdMedicamento() == null) {
                throw new RuntimeException("❌ Error: ID de receta y ID de medicamento son obligatorios.");
            }

            Receta receta = em.find(Receta.class, recetaMedicamento.getIdReceta());
            if (receta == null) {
                throw new RuntimeException("❌ Error: No se encontró la receta con ID " + recetaMedicamento.getIdReceta());
            }

            Medicamento medicamento = em.find(Medicamento.class, recetaMedicamento.getIdMedicamento());
            if (medicamento == null) {
                throw new RuntimeException("❌ Error: No se encontró el medicamento con ID " + recetaMedicamento.getIdMedicamento());
            }

            recetaMedicamento.setReceta(receta);
            recetaMedicamento.setMedicamento(medicamento);

            // Guardar en la BD
            em.persist(recetaMedicamento);
            em.flush();

            System.out.println("✅ Medicamento agregado correctamente a la receta con ID " + receta.getIdReceta());
            return recetaMedicamento;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al agregar medicamento: " + e.getMessage());
        }
    }
}
