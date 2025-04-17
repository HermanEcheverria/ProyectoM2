
// ReporteModeracionController
package com.unis.controller;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/reporte-moderacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteModeracionController {

    @Inject
    ReporteModeracionService service;

    @GET
    public List<ModeracionReporteDTO> obtener(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            return service.obtenerUsuariosConRechazos(fechaInicio, fechaFin, limite);
        } catch (Exception e) {
            throw new WebApplicationException("Formato de fecha inválido", 400);
        }
    }
}