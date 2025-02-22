package com.unis.service;

import java.util.List;

import com.unis.model.Servicio;
import com.unis.repository.ServicioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ServicioService {

    @Inject
    ServicioRepository servicioRepository;

    public List<Servicio> listarTodos() {
        return servicioRepository.listAll();
    }
    public Servicio buscarPorId(Long id) {
    return servicioRepository.findById(id);
}


    @Transactional
    public Servicio agregarServicio(Servicio servicio, Long parentId) {
        if (parentId != null) {
            Servicio servicioPadre = servicioRepository.findById(parentId);
            if (servicioPadre != null) {
                servicio.servicioPadre = servicioPadre;
            }
        }
        servicioRepository.persist(servicio);
        return servicio;
    }

    @Transactional
    public void eliminarServicio(Long id) {
        Servicio servicio = servicioRepository.findById(id);
        if (servicio != null) {
            servicioRepository.delete(servicio);
        }
    }

    public List<Servicio> listarSubServicios(Long id) {
        Servicio servicioPadre = servicioRepository.findById(id);
        return servicioPadre != null ? servicioPadre.subServicios.stream().toList() : List.of();
    }
}
