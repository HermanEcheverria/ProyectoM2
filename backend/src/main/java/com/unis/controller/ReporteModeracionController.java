package com.unis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

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
        // Validaciones de requeridos
        if (inicio == null || inicio.trim().isEmpty()) {
            throw new WebApplicationException("El parámetro 'inicio' es requerido", 400);
        }
        if (fin == null || fin.trim().isEmpty()) {
            throw new WebApplicationException("El parámetro 'fin' es requerido", 400);
        }
        if (limite <= 0) {
            throw new WebApplicationException("El parámetro 'limite' debe ser mayor que cero", 400);
        }

        // Parseo estricto de fechas
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        final Date fechaInicio;
        final Date fechaFin;
        try {
            fechaInicio = sdf.parse(inicio);
            fechaFin = sdf.parse(fin);
        } catch (ParseException e) {
            throw new WebApplicationException("Formato de fecha inválido (use yyyy-MM-dd)", 400);
        }

        // Validación de rango
        if (fechaInicio.after(fechaFin)) {
            throw new WebApplicationException("El rango de fechas es inválido (inicio > fin)", 400);
        }

        // Llamada al servicio
        return service.obtenerUsuariosConRechazos(fechaInicio, fechaFin, limite);
    }
}
