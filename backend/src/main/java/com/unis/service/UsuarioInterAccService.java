package com.unis.service;

import com.unis.model.UsuarioInterAcc;
import com.unis.repository.UsuarioInterAccRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioInterAccService {

    @Inject
    UsuarioInterAccRepository usuarioInterAccRepository;

    public List<UsuarioInterAcc> getAllUsuariosInterAcc() {
        return usuarioInterAccRepository.listAll();
    }

    public Optional<UsuarioInterAcc> getUsuarioInterAccById(Long id) {
        return usuarioInterAccRepository.findByIdOptional(id);
    }

    @Transactional
    public void actualizarUsuarioInterAcc(Long id, UsuarioInterAcc usuarioInterAcc) {
        Optional<UsuarioInterAcc> usuarioExistente = usuarioInterAccRepository.findByIdOptional(id);
        if (usuarioExistente.isPresent()) {
            UsuarioInterAcc usuario = usuarioExistente.get();
            usuario.setApellido(usuarioInterAcc.getApellido());
            usuario.setDocumento(usuarioInterAcc.getDocumento());
            usuario.setFechaNacimiento(usuarioInterAcc.getFechaNacimiento());
            usuario.setGenero(usuarioInterAcc.getGenero());
            usuario.setTelefono(usuarioInterAcc.getTelefono());
            usuario.setIdHospital(usuarioInterAcc.getIdHospital());
            usuarioInterAccRepository.persist(usuario);
        }
    }

    @Transactional
    public void eliminarUsuarioInterAcc(Long id) {
        usuarioInterAccRepository.deleteById(id);
    }
}

