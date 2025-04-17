package com.unis.dto;

import java.time.LocalDate;

public class ReporteRequest {
    private Long idDoctor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoReporte; // "AGRUPADO" o "DETALLADO"
   private String usuario;

    public ReporteRequest() {
    }

    // Getters y Setters existentes
    public Long getIdDoctor() {
        return idDoctor;
    }
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    public String getTipoReporte() {
        return tipoReporte;
    }
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
   // Nuevo campo usuario   
   public String getUsuario() {
      return usuario;
  }
   public void setUsuario(String usuario) {
      this.usuario = usuario;
   }
}
