package com.unis.service;

import com.unis.model.PageContent;
import com.unis.repository.PageContentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.sql.Timestamp;
import java.util.List;

@ApplicationScoped
public class PageContentService {

    @Inject
    PageContentRepository repository;

    // ✅ Contenido publicado para mostrar en la vista pública
    public List<PageContent> getPublishedContent(String pageName) {
        return repository.findPublishedByPage(pageName);
    }

    // ✅ Contenido en borrador (PROCESO)
    public List<PageContent> getDraftContent() {
        return repository.findDrafts();
    }

    // ✅ Contenido por estado (para moderación)
    public List<PageContent> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    // ✅ Buscar por ID
    public PageContent findById(Long id) {
        return repository.findById(id);
    }

    // ✅ Crear nuevo contenido (por defecto se asume PROCESO)
    @Transactional
    public PageContent create(PageContent content) {
        content.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        if (content.getStatus() == null) {
            content.setStatus("PROCESO");
        }
        if (content.getEditorEmail() == null) {
            throw new IllegalArgumentException("Editor email es requerido");
        }
        repository.persist(content);
        return content;
    }

    // ✅ Actualizar contenido
    @Transactional
    public PageContent update(Long id, PageContent updatedContent) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        // 🔁 Si el contenido actual está PUBLICADO, crear una nueva versión en estado PROCESO
        if ("PUBLICADO".equals(existing.getStatus())) {
            PageContent nuevo = new PageContent();
            nuevo.setPageName(updatedContent.getPageName());
            nuevo.setSectionName(updatedContent.getSectionName());
            nuevo.setContentTitle(updatedContent.getContentTitle());
            nuevo.setContentBody(updatedContent.getContentBody());
            nuevo.setImage(updatedContent.getImage());
            nuevo.setModifiedBy(updatedContent.getModifiedBy());
            nuevo.setEditorEmail(updatedContent.getEditorEmail());
            nuevo.setStatus("PROCESO");
            nuevo.setRejectionReason(null);
            nuevo.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
            repository.persist(nuevo);
            return nuevo;
        }

        // Si está en PROCESO o RECHAZADO, actualizamos el mismo registro
        existing.setPageName(updatedContent.getPageName());
        existing.setSectionName(updatedContent.getSectionName());
        existing.setContentTitle(updatedContent.getContentTitle());
        existing.setContentBody(updatedContent.getContentBody());
        existing.setImage(updatedContent.getImage());
        existing.setModifiedBy(updatedContent.getModifiedBy());
        existing.setEditorEmail(updatedContent.getEditorEmail());
        existing.setStatus(updatedContent.getStatus());
        existing.setRejectionReason(updatedContent.getRejectionReason());
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));

        return existing;
    }

    // ✅ Aprobar contenido (PUBLICADO)
    @Transactional
    public PageContent publish(Long id) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        existing.setStatus("PUBLICADO");
        existing.setRejectionReason(null);
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    // ✅ Rechazar contenido con motivo
    @Transactional
    public PageContent reject(Long id, String motivo) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        existing.setStatus("RECHAZADO");
        existing.setRejectionReason(motivo);
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    // ✅ Eliminar contenido
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
