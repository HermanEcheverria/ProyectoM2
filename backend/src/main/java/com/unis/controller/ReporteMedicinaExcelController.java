package com.unis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.unis.service.ReporteMedicinaExcelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/reporte-medicinas/excel")
public class ReporteMedicinaExcelController {

    static final String CT_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Inject
    ReporteMedicinaExcelService excelService;

    @GET
    @Produces(CT_XLSX)
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite,
            @QueryParam("usuario") @DefaultValue("admin@hospital.com") String usuario
    ) {
        // Validaciones de entrada esperadas por los tests
        if (inicio == null || inicio.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'inicio' es requerido").build();
        }
        if (fin == null || fin.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'fin' es requerido").build();
        }
        if (limite <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'limite' debe ser mayor a 0").build();
        }

        // Normalizar usuario (test espera default si viene vacío)
        String usuarioNorm = (usuario == null || usuario.trim().isEmpty())
                ? "admin@hospital.com" : usuario.trim();

        // Parseo de fechas estricto
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        final Date fechaInicio;
        final Date fechaFin;
        try {
            fechaInicio = sdf.parse(inicio);
            fechaFin = sdf.parse(fin);
        } catch (ParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Use yyyy-MM-dd").build();
        }

        // Validar rango
        if (fechaInicio.after(fechaFin)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'inicio' no puede ser posterior a 'fin'").build();
        }

        try {
            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite, usuarioNorm);

            // Nombre de archivo con el rango (los tests lo verifican)
            String filename = String.format("medicinas_reporte_%s_a_%s.xlsx", inicio, fin);

            return Response.ok(excel)
                    .type(CT_XLSX) // asegurar Content-Type (el @Produces no siempre aplica en Response.ok(byte[]))
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error generando reporte").build();
        }
    }
}
