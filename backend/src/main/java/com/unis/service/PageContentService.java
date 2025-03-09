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

    public List<PageContent> getPublishedContent(String pageName){
        return repository.findPublishedByPage(pageName);
    }

    public List<PageContent> getDraftContent(){
        return repository.findDrafts();
    }

    @Transactional
    public PageContent create(PageContent content){
        content.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        repository.persist(content);
        return content;
    }

    @Transactional
    public PageContent update(Long id, PageContent updatedContent){
        PageContent existing = repository.findById(id);
        if(existing == null){
            throw new NotFoundException("Contenido no encontrado");
        }
        existing.setPageName(updatedContent.getPageName());
        existing.setSectionName(updatedContent.getSectionName());
        existing.setContentTitle(updatedContent.getContentTitle());
        existing.setContentBody(updatedContent.getContentBody());
        existing.setImage(updatedContent.getImage());
        existing.setModifiedBy(updatedContent.getModifiedBy());
        existing.setStatus(updatedContent.getStatus());
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    @Transactional
    public PageContent publish(Long id){
        PageContent existing = repository.findById(id);
        if(existing == null){
            throw new NotFoundException("Contenido no encontrado");
        }
        existing.setStatus("PUBLICADO");
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    @Transactional
    public void delete(Long id){
        repository.deleteById(id);
    }
}
