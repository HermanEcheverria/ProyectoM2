package com.unis.service;

import com.unis.model.UsuarioInter;
import com.unis.repository.UsuarioInterconexionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioInterService {

    @Inject
    UsuarioInterconexionRepository usuarioInterconexionRepository;

    @Inject
    EntityManager entityManager;

    public List<UsuarioInter> getAllUsuarios() {
        return usuarioInterconexionRepository.listAll();
    }

    public Optional<UsuarioInter> getUsuarioById(Long id) {
        return usuarioInterconexionRepository.findByIdOptional(id);
    }

    @Transactional
    public void registrarUsuario(UsuarioInter usuario) {
        entityManager.createNativeQuery("BEGIN REGISTRAR_USUARIOINTER(?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
            .setParameter(1, usuario.getUsuario().getNombreUsuario())
            .setParameter(2, usuario.getApellido())
            .setParameter(3, usuario.getDocumento())
            .setParameter(4, usuario.getFechaNacimiento())
            .setParameter(5, usuario.getGenero())
            .setParameter(6, usuario.getTelefono())
            .setParameter(7, usuario.getUsuario().getCorreo())
            .setParameter(8, usuario.getUsuario().getContrasena())
            .setParameter(9, usuario.getIdHospital())
            .executeUpdate();
    }

    @Transactional
    public void actualizarUsuario(Long id, UsuarioInter usuario) {
        entityManager.createNativeQuery("BEGIN ACTUALIZAR_USUARIOINTER(?, ?, ?, ?, ?, ?, ?, ?, ?); END;")
            .setParameter(1, id)
            .setParameter(2, usuario.getUsuario().getNombreUsuario())
            .setParameter(3, usuario.getApellido())
            .setParameter(4, usuario.getDocumento())
            .setParameter(5, usuario.getFechaNacimiento())
            .setParameter(6, usuario.getGenero())
            .setParameter(7, usuario.getTelefono())
            .setParameter(8, usuario.getUsuario().getCorreo())
            .setParameter(9, usuario.getUsuario().getContrasena())
            .executeUpdate();
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        entityManager.createNativeQuery("BEGIN ELIMINAR_USUARIOINTER(?); END;")
            .setParameter(1, id)
            .executeUpdate();
    }
}
