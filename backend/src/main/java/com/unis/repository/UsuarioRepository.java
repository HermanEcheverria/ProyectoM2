package com.unis.repository;

import com.unis.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    
    public Usuario findByCorreo(String correo) {
        return find("correo", correo).firstResult();
    }
}
