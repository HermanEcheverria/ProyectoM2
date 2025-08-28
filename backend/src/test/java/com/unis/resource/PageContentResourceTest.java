package com.unis.resource;

import com.unis.model.PageContent;
import com.unis.service.PageContentService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PageContentResourceTest {

  @Mock
  PageContentService service;

  @InjectMocks
  PageContentResource resource;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  // --------- GET /api/page-content/{pageName} ---------

  @Test
  void getPublished_ok_lista() {
    PageContent pc = new PageContent();
    when(service.getPublishedContent("home")).thenReturn(List.of(pc));

    List<PageContent> out = resource.getPublished("home");

    assertNotNull(out);
    assertEquals(1, out.size());
    verify(service).getPublishedContent("home");
  }

  // --------- GET /api/page-content/drafts ---------

  @Test
  void getDrafts_ok_lista() {
    when(service.getDraftContent()).thenReturn(List.of(new PageContent(), new PageContent()));

    List<PageContent> out = resource.getDrafts();

    assertEquals(2, out.size());
    verify(service).getDraftContent();
  }

  // --------- GET /api/page-content/pendientes ---------

  @Test
  void getPendientesModeracion_ok_lista() {
    when(service.getByStatus("PROCESO")).thenReturn(List.of());

    List<PageContent> out = resource.getPendientesModeracion();

    assertNotNull(out);
    assertEquals(0, out.size());
    verify(service).getByStatus("PROCESO");
  }

  // --------- GET /api/page-content/contenido/{id} ---------

  @Test
  void getById_encontrado_200() {
    PageContent pc = new PageContent();
    when(service.findById(10L)).thenReturn(pc);

    Response r = resource.getById(10L);

    assertEquals(200, r.getStatus());
    assertSame(pc, r.getEntity());
    verify(service).findById(10L);
  }

  @Test
  void getById_noEncontrado_404() {
    when(service.findById(77L)).thenReturn(null);

    Response r = resource.getById(77L);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(service).findById(77L);
  }

  // --------- POST /api/page-content ---------

  @Test
  void create_asignaProceso_yDevuelve201_conEntidadCreada() {
    PageContent input = new PageContent();
    PageContent creado = new PageContent();

    when(service.create(any(PageContent.class))).thenReturn(creado);

    Response r = resource.create(input);

    assertEquals(201, r.getStatus());
    assertSame(creado, r.getEntity());
    // al menos confirma que el status se setea antes de pasar a service
    assertEquals("PROCESO", input.getStatus());
    verify(service).create(input);
  }

  // --------- PUT /api/page-content/{id} ---------

  @Test
  void update_ok_200_devuelveActualizado() {
    PageContent actualizado = new PageContent();
    when(service.update(eq(5L), any(PageContent.class))).thenReturn(actualizado);

    Response r = resource.update(5L, new PageContent());

    assertEquals(200, r.getStatus());
    assertSame(actualizado, r.getEntity());
    verify(service).update(eq(5L), any(PageContent.class));
  }

  // --------- PUT /api/page-content/{id}/publish ---------

  @Test
  void publish_ok_200_devuelvePublicado() {
    PageContent publicado = new PageContent();
    when(service.publish(9L)).thenReturn(publicado);

    Response r = resource.publish(9L);

    assertEquals(200, r.getStatus());
    assertSame(publicado, r.getEntity());
    verify(service).publish(9L);
  }

  // --------- PUT /api/page-content/{id}/reject?motivo=... ---------

  @Test
  void reject_ok_200_devuelveRechazado() {
    PageContent rechazado = new PageContent();
    when(service.reject(12L, "falta info")).thenReturn(rechazado);

    Response r = resource.reject(12L, "falta info");

    assertEquals(200, r.getStatus());
    assertSame(rechazado, r.getEntity());
    verify(service).reject(12L, "falta info");
  }

  // --------- DELETE /api/page-content/{id} ---------

  @Test
  void delete_ok_204() {
    Response r = resource.delete(22L);

    assertEquals(204, r.getStatus());
    assertNull(r.getEntity());
    verify(service).delete(22L);
  }
}
