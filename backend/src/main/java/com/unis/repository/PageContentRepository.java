package com.unis.repository;

import com.unis.model.PageContent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PageContentRepository implements PanacheRepository<PageContent> {

    public List<PageContent> findPublishedByPage(String pageName){
        return list("pageName = ?1 and status = ?2", pageName, "PUBLICADO");
    }

    public List<PageContent> findDrafts(){
        return list("status", "PROCESO");
    }
}
