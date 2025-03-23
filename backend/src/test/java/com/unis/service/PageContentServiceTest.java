package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.PageContent;
import com.unis.repository.PageContentRepository;

import jakarta.ws.rs.NotFoundException;

public class PageContentServiceTest {

    @Mock
    PageContentRepository repository;

    @InjectMocks
    PageContentService pageContentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getPublishedContent
    @Test
    public void testGetPublishedContent() {
        String pageName = "home";
        List<PageContent> expectedList = Arrays.asList(new PageContent(), new PageContent());
        when(repository.findPublishedByPage(pageName)).thenReturn(expectedList);

        List<PageContent> result = pageContentService.getPublishedContent(pageName);
        assertEquals(expectedList, result, "La lista de contenidos publicados debe coincidir con la esperada");
    }

    // Test para getDraftContent
    @Test
    public void testGetDraftContent() {
        List<PageContent> expectedDrafts = Arrays.asList(new PageContent(), new PageContent());
        when(repository.findDrafts()).thenReturn(expectedDrafts);

        List<PageContent> result = pageContentService.getDraftContent();
        assertEquals(expectedDrafts, result, "La lista de borradores debe coincidir con la esperada");
    }

    // Test para create
    @Test
    public void testCreate() {
        PageContent content = new PageContent();
        // Antes de crear, lastModifiedDate puede ser nulo
        content.setLastModifiedDate(null);

        // No simulamos nada en persist (es void), solo verificamos que se invoque
        PageContent result = pageContentService.create(content);

        verify(repository, times(1)).persist(content);
        // Se espera que se asigne un Timestamp
        assertNotNull(result.getLastModifiedDate(), "El contenido debe tener asignada una fecha de modificación");
    }

    // Test para update (caso exitoso)
    @Test
    public void testUpdateFound() {
        Long id = 1L;
        PageContent existing = new PageContent();
        // Asignar valores iniciales
        existing.setPageName("OldPage");
        existing.setSectionName("OldSection");
        existing.setContentTitle("OldTitle");
        existing.setContentBody("OldBody");
        // Si image es byte[] y modifiedBy es Long:
        existing.setImage("oldImage.png".getBytes());
        existing.setModifiedBy(111L);
        existing.setStatus("DRAFT");
        // Se ignora lastModifiedDate inicial

        PageContent updateData = new PageContent();
        updateData.setPageName("NewPage");
        updateData.setSectionName("NewSection");
        updateData.setContentTitle("NewTitle");
        updateData.setContentBody("NewBody");
        updateData.setImage("newImage.png".getBytes());
        updateData.setModifiedBy(222L);
        updateData.setStatus("UPDATED");

        when(repository.findById(id)).thenReturn(existing);

        PageContent result = pageContentService.update(id, updateData);

        assertEquals("NewPage", result.getPageName());
        assertEquals("NewSection", result.getSectionName());
        assertEquals("NewTitle", result.getContentTitle());
        assertEquals("NewBody", result.getContentBody());
        // Comparar arrays de bytes:
        assertArrayEquals("newImage.png".getBytes(), result.getImage());
        assertEquals(222L, result.getModifiedBy());
        assertEquals("UPDATED", result.getStatus());
        assertNotNull(result.getLastModifiedDate(), "La fecha de modificación debe ser actualizada");
    }

    // Test para update cuando no se encuentra el contenido
    @Test
    public void testUpdateNotFound() {
        Long id = 1L;
        PageContent updateData = new PageContent();
        when(repository.findById(id)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            pageContentService.update(id, updateData);
        });
        assertEquals("Contenido no encontrado", ex.getMessage());
    }

    // Test para publish cuando se encuentra el contenido
    @Test
    public void testPublishFound() {
        Long id = 1L;
        PageContent existing = new PageContent();
        existing.setStatus("DRAFT");
        existing.setLastModifiedDate(null);
        when(repository.findById(id)).thenReturn(existing);

        PageContent result = pageContentService.publish(id);

        assertEquals("PUBLICADO", result.getStatus(), "El estado debe actualizarse a 'PUBLICADO'");
        assertNotNull(result.getLastModifiedDate(), "La fecha de modificación debe actualizarse");
    }

    // Test para publish cuando no se encuentra el contenido
    @Test
    public void testPublishNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            pageContentService.publish(id);
        });
        assertEquals("Contenido no encontrado", ex.getMessage());
    }

    // Test para delete
    @Test
    public void testDelete() {
        Long id = 1L;
        // Simulamos que deleteById 
        doNothing().when(repository).deleteById(id);

        pageContentService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
