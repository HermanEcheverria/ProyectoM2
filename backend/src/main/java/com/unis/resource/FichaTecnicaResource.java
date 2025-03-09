package com.unis.resource;

import com.unis.model.FichaTecnica;
import com.unis.service.FichaTecnicaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/fichas-tecnicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FichaTecnicaResource {

    @Inject
    FichaTecnicaService fichaTecnicaService;

    @GET
    public List<FichaTecnica> obtenerTodasLasFichas() {
        return fichaTecnicaService.getAllFichas();
    }

    @POST
    public void registrarFicha(FichaTecnica ficha) {
        fichaTecnicaService.registrarFicha(ficha);
    }
}
