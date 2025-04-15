package com.unis.dto;

import java.util.List;

public class ReporteResponse<T> {
    private String encabezado;
    private List<T> datos;

    public ReporteResponse(String encabezado, List<T> datos) {
        this.encabezado = encabezado;
        this.datos = datos;
    }

    // Getters y Setters
    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public List<T> getDatos() {
        return datos;
    }

    public void setDatos(List<T> datos) {
        this.datos = datos;
    }
}
