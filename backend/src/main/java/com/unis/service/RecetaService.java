package com.unis.service;

import com.unis.model.Receta;
import com.unis.repository.RecetaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@ApplicationScoped
public class RecetaService {

    @Inject
    RecetaRepository recetaRepository;
    @PersistenceContext
    EntityManager entityManager;


    public List<Receta> listar() {
        return recetaRepository.listAll();
    }

    public Receta obtenerPorId(Long id) {
        return recetaRepository.findById(id);
    }

    public List<Receta> obtenerPorPaciente(Long idPaciente) {
        return entityManager.createQuery("SELECT r FROM Receta r WHERE r.paciente.id = :idPaciente", Receta.class)
                            .setParameter("idPaciente", idPaciente)
                            .getResultList();
    }
    
    public List<Receta> obtenerRecetasPorPacienteYEstado(Long idPaciente, String estado) {
        return entityManager.createQuery("SELECT r FROM Receta r WHERE r.paciente.id = :idPaciente AND r.estado = :estado", Receta.class)
                            .setParameter("idPaciente", idPaciente)
                            .setParameter("estado", estado)
                            .getResultList();
    }
    

    @Transactional
    public Receta crear(Receta receta) {
        recetaRepository.persist(receta);
        return receta;
    }

    @Transactional
    public Receta actualizar(Long id, Receta recetaNueva) {
        Receta r = recetaRepository.findById(id);
        if (r != null) {
            r.setFecha(recetaNueva.getFecha());
            r.setCodigoReceta(recetaNueva.getCodigoReceta());
            r.setIdPaciente(recetaNueva.getIdPaciente());
            r.setIdDoctor(recetaNueva.getIdDoctor());
            r.setIdHospital(recetaNueva.getIdHospital());
            r.setIdSeguro(recetaNueva.getIdSeguro());
            r.setAnotaciones(recetaNueva.getAnotaciones());
            r.setNotasEspeciales(recetaNueva.getNotasEspeciales());
            r.setDiagnostico(recetaNueva.getDiagnostico());
            r.setEstado(recetaNueva.getEstado());
            return r;
        }
        return null;
    }

    @Transactional
    public boolean eliminar(Long id) {
        return recetaRepository.deleteById(id);
    }
}