package com.unis.dto;

import java.time.LocalDate;

public class ReporteAgregadoDTO {
    private LocalDate fecha;
    private Long totalConsultas;
    private Long totalSeguro;
    private Long totalDirecto;

    public ReporteAgregadoDTO(LocalDate fecha, Long totalConsultas, Long totalSeguro, Long totalDirecto) {
        this.fecha = fecha;
        this.totalConsultas = totalConsultas;
        this.totalSeguro = totalSeguro;
        this.totalDirecto = totalDirecto;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Long getTotalConsultas() {
        return totalConsultas;
    }
    public void setTotalConsultas(Long totalConsultas) {
        this.totalConsultas = totalConsultas;
    }
    public Long getTotalSeguro() {
        return totalSeguro;
    }
    public void setTotalSeguro(Long totalSeguro) {
        this.totalSeguro = totalSeguro;
    }
    public Long getTotalDirecto() {
        return totalDirecto;
    }
    public void setTotalDirecto(Long totalDirecto) {
        this.totalDirecto = totalDirecto;
    }
}
