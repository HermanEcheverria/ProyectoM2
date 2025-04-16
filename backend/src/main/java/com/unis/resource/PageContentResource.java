package com.unis.resource;

import com.unis.model.PageContent;
import com.unis.service.PageContentService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    // ✅ Obtener contenido publicado por página
    @GET
    @Path("/{pageName}")
    public List<PageContent> getPublished(@PathParam("pageName") String pageName) {
        return service.getPublishedContent(pageName);
    }

    // ✅ Obtener contenido en estado PROCESO
    @GET
    @Path("/drafts")
    public List<PageContent> getDrafts() {
        return service.getDraftContent();
    }

    // ✅ Obtener todos los pendientes (moderación)
    @GET
    @Path("/pendientes")
    public List<PageContent> getPendientesModeracion() {
        return service.getByStatus("PROCESO");
    }

    @GET
@Path("/contenido/{id}")
public Response getById(@PathParam("id") Long id) {
    PageContent content = service.findById(id);
    if (content == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(content).build();
}


    // ✅ Crear nuevo contenido (se guarda en estado PROCESO)
    @POST
    @Transactional
    public Response create(PageContent content) {
        content.setStatus("PROCESO");
        PageContent created = service.create(content);
        return Response.ok(created).status(Response.Status.CREATED).build();
    }

    // ✅ Actualizar contenido
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, PageContent content) {
        PageContent updated = service.update(id, content);
        return Response.ok(updated).build();
    }

    // ✅ Publicar contenido (aprobar)
    @PUT
    @Path("/{id}/publish")
    @Transactional
    public Response publish(@PathParam("id") Long id) {
        PageContent published = service.publish(id);
        return Response.ok(published).build();
    }

    // ✅ Rechazar contenido (guardar motivo)
    @PUT
    @Path("/{id}/reject")
    @Transactional
    public Response reject(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        PageContent rejected = service.reject(id, motivo);
        return Response.ok(rejected).build();
    }

    // ✅ Eliminar contenido
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
