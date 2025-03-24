package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.repository.RolRepository;
import com.unis.repository.UsuarioRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.ws.rs.WebApplicationException;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarUsuarios() {
        List<Usuario> usuarios = Arrays.asList(new Usuario(), new Usuario());
        when(usuarioRepository.listAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarUsuarios();
        assertEquals(2, result.size());
    }

    @Test
    public void testObtenerUsuarioPorCorreo() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByCorreo("test@correo.com")).thenReturn(usuario);

        Usuario result = usuarioService.obtenerUsuarioPorCorreo("test@correo.com");
        assertNotNull(result);
    }

    @Test
    public void testRegistrarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        when(usuarioRepository.findByCorreo("test@correo.com")).thenReturn(null);

        usuarioService.registrarUsuario(usuario);
        verify(usuarioRepository).persist(usuario);
    }

    @Test
    public void testRegistrarUsuarioCorreoDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        when(usuarioRepository.findByCorreo("test@correo.com")).thenReturn(usuario);

        assertThrows(WebApplicationException.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });
    }

    @Test
    public void testListarUsuariosInactivos() {
        PanacheQuery<Usuario> query = mock(PanacheQuery.class);
        when(usuarioRepository.find("estado", 0)).thenReturn(query);
        when(query.list()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));

        List<Usuario> result = usuarioService.listarUsuariosInactivos();
        assertNotNull(result);
    }

    @Test
    public void testActivarUsuario() {
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        when(usuarioRepository.findById(1L)).thenReturn(usuario);
        when(rolRepository.findById(1L)).thenReturn(rol);

        Usuario result = usuarioService.activarUsuario(1L, 1L);
        assertEquals(1, result.getEstado());
        assertEquals(rol, result.getRol());
    }

    @Test
    public void testActivarUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(null);

        assertThrows(WebApplicationException.class, () -> {
            usuarioService.activarUsuario(1L, 1L);
        });
    }

    @Test
    public void testDesactivarUsuario() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(usuario);

        Usuario result = usuarioService.desactivarUsuario(1L);
        assertEquals(0, result.getEstado());
    }

    @Test
    public void testDesactivarUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(null);

        assertThrows(WebApplicationException.class, () -> {
            usuarioService.desactivarUsuario(1L);
        });
    }

    @Test
    public void testListarRoles() {
        List<Rol> roles = Arrays.asList(new Rol(), new Rol());
        when(rolRepository.listAll()).thenReturn(roles);

        List<Rol> result = usuarioService.listarRoles();
        assertEquals(2, result.size());
    }
}
