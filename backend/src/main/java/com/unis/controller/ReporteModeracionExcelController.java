package com.unis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.unis.service.ReporteModeracionExcelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * REST controller for exporting moderation report data as an Excel file.
 * <p>
 * Provides a downloadable `.xlsx` report of users with the highest number of content rejections
 * in a given date range, limited by a specified number of entries.
 * </p>
 */
@Path("/reporte-moderacion/excel")
public class ReporteModeracionExcelController {

    private static final String CT_XLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Inject
    ReporteModeracionExcelService excelService;

    /**
     * Downloads the moderation report as an Excel file.
     *
     * @param inicio the start date in format yyyy-MM-dd
     * @param fin the end date in format yyyy-MM-dd
     * @param limite the maximum number of users to include in the report (default is 10)
     * @return a {@link Response} with the Excel file, or a 400 error if validation fails
     */
    @GET
    @Produces(CT_XLSX)
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        // Validaciones de presencia
        if (inicio == null || inicio.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'inicio' es obligatorio.")
                    .build();
        }
        if (fin == null || fin.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'fin' es obligatorio.")
                    .build();
        }

        // Validación de limite
        if (limite <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'limite' debe ser mayor a 0.")
                    .build();
        }

        // Parseo estricto de fechas
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        final Date fechaInicio;
        final Date fechaFin;
        try {
            fechaInicio = sdf.parse(inicio);
            fechaFin = sdf.parse(fin);
        } catch (ParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Use yyyy-MM-dd.")
                    .build();
        }

        // Validación de rango
        if (fechaInicio.after(fechaFin)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La fecha 'fin' debe ser posterior o igual a 'inicio'.")
                    .build();
        }

        try {
            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite);

            String filename = String.format(
                    "moderacion_reporte_%s_a_%s.xlsx", inicio, fin);

            return Response.ok(excel)
                    .type(CT_XLSX) // asegura Content-Type explícito para los tests
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error generando reporte")
                    .build();
        }
    }
}
