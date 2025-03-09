package com.unis.service;

import java.util.List;
import java.util.Optional;
import com.unis.model.FichaTecnica;
import com.unis.repository.FichaTecnicaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FichaTecnicaService {

    @Inject
    FichaTecnicaRepository fichaTecnicaRepository;

    public List<FichaTecnica> getAllFichas() {
        return fichaTecnicaRepository.listAll();
    }

    public Optional<FichaTecnica> getFichaById(Long id) {
        return fichaTecnicaRepository.findByIdOptional(id);
    }

    @Transactional
    public void registrarFicha(FichaTecnica ficha) {
        fichaTecnicaRepository.persist(ficha);
    }
}
