package com.unis.controller;

import com.unis.service.ReporteMedicinaExcelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

@Path("/reporte-medicinas/excel")
public class ReporteMedicinaExcelController {

    @Inject
    ReporteMedicinaExcelService excelService;

    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite,
            @QueryParam("usuario") @DefaultValue("admin@hospital.com") String usuario
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite, usuario);

            return Response.ok(excel)
                    .header("Content-Disposition", "attachment; filename=medicinas_reporte.xlsx")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error generando reporte").build();
        }
    }
}
