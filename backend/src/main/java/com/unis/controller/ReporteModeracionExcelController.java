
// ReporteModeracionExcelController
package com.unis.controller;

import com.unis.service.ReporteModeracionExcelService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

@Path("/reporte-moderacion/excel")
public class ReporteModeracionExcelController {

    @Inject
    ReporteModeracionExcelService excelService;

    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite);
            return Response.ok(excel)
                    .header("Content-Disposition", "attachment; filename=moderacion_reporte.xlsx")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error generando reporte").build();
        }
    }
}