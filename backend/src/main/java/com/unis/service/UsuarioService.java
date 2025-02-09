package com.unis.service;

import com.unis.model.Usuario;
import com.unis.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    // Obtener la lista de usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listAll();
    }

    // Obtener usuario por correo
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Registrar usuario con validación de duplicado
    @Transactional
    public void registrarUsuario(Usuario usuario) {
        // 🔍 Verificar si el correo ya está registrado
        Usuario usuarioExistente = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (usuarioExistente != null) {
            throw new WebApplicationException("El correo ya está registrado", Response.Status.BAD_REQUEST);
        }

        // Guardar usuario si el correo no existe
        usuarioRepository.persist(usuario);
    }
}
