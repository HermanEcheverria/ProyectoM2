package com.unis.service;

import com.unis.dto.MedicinasReporteDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ReporteMedicinaService {

    @Inject
    EntityManager em;

    public List<MedicinasReporteDTO> obtenerReporte(Date fechaInicio, Date fechaFin, int limite) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("REPORTE_MEDICINAS_POPULARES");

        query.registerStoredProcedureParameter("FECHA_INICIO", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("FECHA_FIN", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("LIMITE_RESULTADOS", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("CURSOR_RESULT", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("FECHA_INICIO", fechaInicio);
        query.setParameter("FECHA_FIN", fechaFin);
        query.setParameter("LIMITE_RESULTADOS", limite);

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object[]> resultados = query.getResultList();

        List<MedicinasReporteDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            int popularidad = ((Number) fila[0]).intValue();
            String principio = (String) fila[1];
            int total = ((Number) fila[2]).intValue();
            lista.add(new MedicinasReporteDTO(popularidad, principio, total));
        }

        return lista;
    }
}
