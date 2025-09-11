package com.unis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.unis.dto.MedicinasReporteDTO;
import com.unis.service.ReporteMedicinaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

/**
 * REST controller for generating medicine report data.
 * <p>
 * Provides an endpoint to retrieve a list of medicines ranked by popularity,
 * within a specified date range and limited by a maximum number of results.
 * </p>
 */
@Path("/reporte-medicinas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteMedicinaController {

    @Inject
    ReporteMedicinaService service;

    /**
     * Retrieves a list of top prescribed medicines between two dates.
     *
     * @param inicio the start date in format yyyy-MM-dd
     * @param fin the end date in format yyyy-MM-dd
     * @param limite the maximum number of results to return (default is 10)
     * @return a list of {@link MedicinasReporteDTO} objects representing the report data
     * @throws WebApplicationException if input is invalid
     */
    @GET
    public List<MedicinasReporteDTO> obtener(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        // Validaciones básicas de presencia
        if (inicio == null || inicio.trim().isEmpty()) {
            throw new WebApplicationException("El parámetro 'inicio' es obligatorio", 400);
        }
        if (fin == null || fin.trim().isEmpty()) {
            throw new WebApplicationException("El parámetro 'fin' es obligatorio", 400);
        }
        if (limite <= 0) {
            throw new WebApplicationException("El parámetro 'limite' debe ser mayor a 0", 400);
        }

        // Parseo estricto de fechas
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        final Date fechaInicio;
        final Date fechaFin;
        try {
            fechaInicio = sdf.parse(inicio.trim());
            fechaFin = sdf.parse(fin.trim());
        } catch (ParseException e) {
            throw new WebApplicationException("Formato de fecha inválido. Use yyyy-MM-dd", 400);
        }

        // Validación de rango
        if (fechaFin.before(fechaInicio)) {
            throw new WebApplicationException("El parámetro 'fin' debe ser posterior o igual a 'inicio'", 400);
        }

        // Delegación al servicio
        return service.obtenerReporte(fechaInicio, fechaFin, limite);
    }
}
