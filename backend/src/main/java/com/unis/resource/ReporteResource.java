package com.unis.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;
import com.unis.dto.ReporteRequest;
import com.unis.dto.ReporteResponse;
import com.unis.service.ReporteService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.StreamingOutput;

@Path("/api/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteResource {

    @Inject
    ReporteService reporteService;

    // Endpoint para generar reporte en JSON
    @POST
    @Path("/consultas")
    public Response generarReporte(ReporteRequest request) {
        // Validación de parámetros básicos
        if (request.getIdDoctor() == null || request.getFechaInicio() == null || request.getFechaFin() == null ||
            request.getFechaInicio().isAfter(request.getFechaFin())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Parámetros inválidos")
                           .build();
        }

        // Generar el encabezado del reporte
        String encabezado = "Reporte generado el: " + LocalDateTime.now() + "\n" +
                            "Usuario: [NombreUsuario]\n" +
                            "Parámetros: Doctor ID = " + request.getIdDoctor() +
                            ", Fecha Inicio = " + request.getFechaInicio() +
                            ", Fecha Fin = " + request.getFechaFin() +
                            ", Tipo Reporte = " + request.getTipoReporte() + "\n";

        if ("AGRUPADO".equalsIgnoreCase(request.getTipoReporte())) {
            List<ReporteAgregadoDTO> reporte = reporteService.obtenerReporteAgregado(
                    request.getIdDoctor(),
                    request.getFechaInicio(),
                    request.getFechaFin()
            );
            return Response.ok(new ReporteResponse<>(encabezado, reporte)).build();
        } else { // Se asume "DETALLADO" para cualquier otro valor
            List<ReporteDetalladoDTO> reporte = reporteService.obtenerReporteDetallado(
                    request.getIdDoctor(),
                    request.getFechaInicio(),
                    request.getFechaFin()
            );
            return Response.ok(new ReporteResponse<>(encabezado, reporte)).build();
        }
    }

    // Endpoint para exportar el reporte a Excel
    @GET
    @Path("/consultas/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarReporteExcel(
            @QueryParam("idDoctor") Long idDoctor,
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("tipoReporte") String tipoReporte,
            @Context SecurityContext securityContext) {

        // Convertir parámetros de fecha
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr);

        // Obtener el usuario del contexto o asignar un valor por defecto
        String usuario = (securityContext.getUserPrincipal() != null)
                ? securityContext.getUserPrincipal().getName()
                : "[NombreUsuario]";

        // Obtener los datos según el tipo de reporte
        List<?> reporte;
        if ("AGRUPADO".equalsIgnoreCase(tipoReporte)) {
            reporte = reporteService.obtenerReporteAgregado(idDoctor, fechaInicio, fechaFin);
        } else {
            reporte = reporteService.obtenerReporteDetallado(idDoctor, fechaInicio, fechaFin);
        }

        // Crear Workbook de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");
        int rownum = 0;

        // Encabezado 1: Fecha de generación
        Row headerRow1 = sheet.createRow(rownum++);
        headerRow1.createCell(0).setCellValue("Reporte generado el: " + LocalDateTime.now());

        // Encabezado 2: Usuario
        Row headerRow2 = sheet.createRow(rownum++);
        headerRow2.createCell(0).setCellValue("Usuario: " + usuario);

        // Encabezado 3: Parámetros
        Row headerRow3 = sheet.createRow(rownum++);
        headerRow3.createCell(0).setCellValue("Parámetros: Doctor ID = " + idDoctor +
                ", Fecha Inicio = " + fechaInicio + ", Fecha Fin = " + fechaFin +
                ", Tipo Reporte = " + tipoReporte);

        // Fila en blanco para separar el encabezado de los datos
        rownum++;

        // Escribir datos si existen registros
        if (!reporte.isEmpty()) {
            // Obtenemos los nombres de las propiedades del primer registro
            Object primerRegistro = reporte.get(0);
            java.lang.reflect.Field[] fields = primerRegistro.getClass().getDeclaredFields();

            Row tableHeader = sheet.createRow(rownum++);
            int colnum = 0;
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                tableHeader.createCell(colnum++).setCellValue(field.getName());
            }

            // Escribimos cada registro en una fila
            for (Object registro : reporte) {
                Row row = sheet.createRow(rownum++);
                colnum = 0;
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(registro);
                    } catch (IllegalAccessException e) {
                        value = "Error";
                    }
                    row.createCell(colnum++).setCellValue(value != null ? value.toString() : "");
                }
            }
            // Ajustamos el ancho de las columnas
            int numCols = fields.length;
            for (int i = 0; i < numCols; i++) {
                sheet.autoSizeColumn(i);
            }
        } else {
            Row noDataRow = sheet.createRow(rownum++);
            noDataRow.createCell(0).setCellValue("No se encontraron datos para los parámetros seleccionados.");
        }

        StreamingOutput stream = output -> {
            try {
                workbook.write(output);
            } finally {
                workbook.close();
            }
        };

        return Response.ok(stream)
                .header("Content-Disposition", "attachment; filename=\"Reporte.xlsx\"")
                .build();
    }
}
