package com.unis.controller;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.service.UsuarioService;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarUsuarios_debeRetornarListaUsuarios() {
        List<Usuario> usuarios = Arrays.asList(new Usuario(), new Usuario());
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.listarUsuarios();

        assertEquals(2, resultado.size());
        verify(usuarioService).listarUsuarios();
    }

    @Test
    void registrarUsuario_debeCrearUsuarioYRetornar201() {
        Usuario usuario = new Usuario();

        Response respuesta = usuarioController.registrarUsuario(usuario);

        assertEquals(201, respuesta.getStatus());
        verify(usuarioService).registrarUsuario(usuario);
    }

    @Test
    void login_exitosoDebeRetornar200() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@mail.com");
        usuario.setContrasena("1234");

        when(usuarioService.obtenerUsuarioPorCorreo("test@mail.com")).thenReturn(usuario);

        Response respuesta = usuarioController.login(usuario);

        assertEquals(200, respuesta.getStatus());
        assertEquals(usuario, respuesta.getEntity());
    }

    @Test
    void login_fallidoDebeRetornar401() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@mail.com");
        usuario.setContrasena("wrong");

        when(usuarioService.obtenerUsuarioPorCorreo("test@mail.com")).thenReturn(null);

        Response respuesta = usuarioController.login(usuario);

        assertEquals(401, respuesta.getStatus());
        assertEquals("Credenciales incorrectas", respuesta.getEntity());
    }

    @Test
    void listarUsuariosInactivos_debeRetornarLista() {
        List<Usuario> usuarios = Arrays.asList(new Usuario());
        when(usuarioService.listarUsuariosInactivos()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.listarUsuariosInactivos();

        assertEquals(1, resultado.size());
        verify(usuarioService).listarUsuariosInactivos();
    }

    @Test
    void listarRoles_debeRetornarListaRoles() {
        List<Rol> roles = Arrays.asList(new Rol());
        when(usuarioService.listarRoles()).thenReturn(roles);

        List<Rol> resultado = usuarioController.listarRoles();

        assertEquals(1, resultado.size());
        verify(usuarioService).listarRoles();
    }

    @Test
    void activarUsuario_exitosoDebeRetornar200() {
        Usuario usuario = new Usuario();
        UsuarioController.ActivarUsuarioDTO dto = new UsuarioController.ActivarUsuarioDTO();
        dto.setRolId(1L);

        when(usuarioService.activarUsuario(1L, 1L)).thenReturn(usuario);

        Response respuesta = usuarioController.activarUsuario(1L, dto);

        assertEquals(200, respuesta.getStatus());
        assertEquals(usuario, respuesta.getEntity());
    }

    @Test
    void activarUsuario_errorDebeRetornar400() {
        UsuarioController.ActivarUsuarioDTO dto = new UsuarioController.ActivarUsuarioDTO();
        dto.setRolId(1L);

        when(usuarioService.activarUsuario(1L, 1L)).thenThrow(new RuntimeException("Error al activar"));

        Response respuesta = usuarioController.activarUsuario(1L, dto);

        assertEquals(400, respuesta.getStatus());
        assertEquals("Error al activar", respuesta.getEntity());
    }

    @Test
    void desactivarUsuario_exitosoDebeRetornar200() {
        Usuario usuario = new Usuario();
        when(usuarioService.desactivarUsuario(1L)).thenReturn(usuario);

        Response respuesta = usuarioController.desactivarUsuario(1L);

        assertEquals(200, respuesta.getStatus());
        assertEquals(usuario, respuesta.getEntity());
    }

    @Test
    void desactivarUsuario_errorDebeRetornar400() {
        when(usuarioService.desactivarUsuario(1L)).thenThrow(new RuntimeException("Error al desactivar"));

        Response respuesta = usuarioController.desactivarUsuario(1L);

        assertEquals(400, respuesta.getStatus());
        assertEquals("Error al desactivar", respuesta.getEntity());
    }
}
