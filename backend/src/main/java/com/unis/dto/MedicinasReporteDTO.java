package com.unis.dto;

public class MedicinasReporteDTO {
    public int popularidad;
    public String principioActivo;
    public int totalRecetas;

    public MedicinasReporteDTO(int popularidad, String principioActivo, int totalRecetas) {
        this.popularidad = popularidad;
        this.principioActivo = principioActivo;
        this.totalRecetas = totalRecetas;
    }
}
