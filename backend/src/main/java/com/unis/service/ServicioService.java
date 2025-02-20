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

    @Transactional
    public Servicio agregarServicio(Servicio servicio) {
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
}
