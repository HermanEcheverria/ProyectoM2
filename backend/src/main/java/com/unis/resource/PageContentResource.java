package com.unis.resource;

import com.unis.model.PageContent;
import com.unis.service.PageContentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/page-content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PageContentResource {

    @Inject
    PageContentService service;

    @GET
    @Path("/{pageName}")
    public List<PageContent> getPublished(@PathParam("pageName") String pageName){
        return service.getPublishedContent(pageName);
    }

    @GET
    @Path("/drafts")
    public List<PageContent> getDrafts(){
        return service.getDraftContent();
    }

    @POST
    public Response create(PageContent content){
        PageContent created = service.create(content);
        return Response.ok(created).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, PageContent content){
        PageContent updated = service.update(id, content);
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/{id}/publish")
    public Response publish(@PathParam("id") Long id){
        PageContent published = service.publish(id);
        return Response.ok(published).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.noContent().build();
    }
}
