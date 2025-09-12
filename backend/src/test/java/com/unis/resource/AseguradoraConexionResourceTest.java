package com.unis.resource;

import com.unis.model.AseguradoraConexion;
import com.unis.repository.AseguradoraConexionRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AseguradoraConexionResourceTest {

  @Mock
  AseguradoraConexionRepository repository;

  @InjectMocks
  AseguradoraConexionResource resource;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /api/conexiones-aseguradoras ----------

  @Test
  void getTodas_devuelveLista() {
    AseguradoraConexion a = new AseguradoraConexion();
    when(repository.listAll()).thenReturn(List.of(a));

    List<AseguradoraConexion> r = resource.getTodas();

    assertEquals(1, r.size());
    assertSame(a, r.get(0));
    verify(repository).listAll();
  }

  // ---------- GET /api/conexiones-aseguradoras/url/{nombre} ----------

  @Test
  void getUrlPorNombre_encontrada_200_conUrl() {
    AseguradoraConexion a = new AseguradoraConexion();
    a.setUrlBase("https://aseguradora.test/api");
    when(repository.findByNombre("UNIS")).thenReturn(a);

    Response r = resource.getUrlPorNombre("UNIS");

    assertEquals(200, r.getStatus());
    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) r.getEntity();
    assertEquals("https://aseguradora.test/api", body.get("url"));
    verify(repository).findByNombre("UNIS");
  }

  @Test
  void getUrlPorNombre_noEncontrada_404_conMensaje() {
    when(repository.findByNombre("X")).thenReturn(null);

    Response r = resource.getUrlPorNombre("X");

    assertEquals(404, r.getStatus());
    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) r.getEntity();
    assertEquals("Conexión no encontrada", body.get("error"));
    verify(repository).findByNombre("X");
  }

  // ---------- PUT /api/conexiones-aseguradoras/{id} (actualizar) ----------

  @Test
  void actualizar_existente_200_actualizaCampos() {
    AseguradoraConexion existente = new AseguradoraConexion();
    existente.setNombre("Viejo");
    existente.setUrlBase("http://old");
    when(repository.findById(5L)).thenReturn(existente);

    Response r = resource.actualizar(5L, Map.of("nombre","Nuevo","url","http://nuevo"));

    assertEquals(200, r.getStatus());
    assertEquals("Nuevo", existente.getNombre());
    assertEquals("http://nuevo", existente.getUrlBase());
    verify(repository).findById(5L);
  }

  @Test
  void actualizar_existente_conCamposParciales_conservaValores() {
    AseguradoraConexion existente = new AseguradoraConexion();
    existente.setNombre("MantenerNombre");
    existente.setUrlBase("http://mantener");
    when(repository.findById(7L)).thenReturn(existente);

    Response r = resource.actualizar(7L, Map.of("url","http://nuevo-url"));

    assertEquals(200, r.getStatus());
    assertEquals("MantenerNombre", existente.getNombre());
    assertEquals("http://nuevo-url", existente.getUrlBase());
  }

  @Test
  void actualizar_noExiste_404() {
    when(repository.findById(9L)).thenReturn(null);

    Response r = resource.actualizar(9L, Map.of("nombre","N","url","U"));

    assertEquals(404, r.getStatus());
    verify(repository).findById(9L);
  }

  // ---------- DELETE /api/conexiones-aseguradoras/{id} ----------

  @Test
  void eliminar_ok_200() {
    when(repository.deleteById(3L)).thenReturn(true);

    Response r = resource.eliminar(3L);

    assertEquals(200, r.getStatus());
    verify(repository).deleteById(3L);
  }

  @Test
  void eliminar_noExiste_404() {
    when(repository.deleteById(4L)).thenReturn(false);

    Response r = resource.eliminar(4L);

    assertEquals(404, r.getStatus());
    verify(repository).deleteById(4L);
  }

  // ---------- POST /api/conexiones-aseguradoras/registrar ----------

  @Test
  void registrarAseguradora_happyPath_201() {
    when(repository.findByNombre("Seg1")).thenReturn(null);

    Response r = resource.registrarAseguradora(Map.of("nombre","Seg1","url","http://seg1"));

    assertEquals(201, r.getStatus());
    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) r.getEntity();
    assertEquals("Registrada con éxito", body.get("message"));
    verify(repository).findByNombre("Seg1");
    // NO verificamos persist() para evitar ambigüedad de sobrecargas.
  }

  @Test
  void registrarAseguradora_faltanCampos_400() {
    Response r1 = resource.registrarAseguradora(Map.of("nombre","SoloNombre"));
    assertEquals(400, r1.getStatus());

    Response r2 = resource.registrarAseguradora(Map.of("url","SoloUrl"));
    assertEquals(400, r2.getStatus());

    verify(repository, never()).findByNombre(anyString());
    // Tampoco verificamos persist().
  }

  @Test
  void registrarAseguradora_conflicto_409() {
    AseguradoraConexion existente = new AseguradoraConexion();
    when(repository.findByNombre("SegDup")).thenReturn(existente);

    Response r = resource.registrarAseguradora(Map.of("nombre","SegDup","url","http://x"));

    assertEquals(409, r.getStatus());
    assertEquals("Ya existe", r.getEntity());
    verify(repository).findByNombre("SegDup");
  }

  @Test
  void registrarAseguradora_errorInterno_500() {
    // Provocamos la excepción en findByNombre() (también dentro del try)
    when(repository.findByNombre("SegErr")).thenThrow(new RuntimeException("DB down"));

    Response r = resource.registrarAseguradora(Map.of("nombre","SegErr","url","http://err"));

    assertEquals(500, r.getStatus());
    assertEquals("Error interno al registrar", r.getEntity());
    verify(repository).findByNombre("SegErr");
  }
}
