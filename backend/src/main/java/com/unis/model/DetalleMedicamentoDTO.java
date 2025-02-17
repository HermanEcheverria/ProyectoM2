package com.unis.model;

public class DetalleMedicamentoDTO {

    private String principioActivo;
    private String concentracion;
    private String presentacion;
    private String dosis;
    private String frecuencia;
    private int duracion;

    // Constructor vacío
    public DetalleMedicamentoDTO() {}

    // Constructor desde DetalleReceta
    public DetalleMedicamentoDTO(DetalleReceta detalle) {
        this.principioActivo = detalle.getPrincipioActivo();
        this.concentracion = detalle.getConcentracion();
        this.presentacion = detalle.getPresentacion();
        this.dosis = detalle.getDosis();
        this.frecuencia = detalle.getFrecuencia();
        this.duracion = detalle.getDuracion();
    }

    // Getters y Setters
    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
}
