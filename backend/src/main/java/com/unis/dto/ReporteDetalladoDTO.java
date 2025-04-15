package com.unis.dto;

import java.time.LocalDate;

public class ReporteDetalladoDTO {
    private LocalDate fecha;
    private String horaConsulta;
    private String nombrePaciente;
    private String tipoPago;

    public ReporteDetalladoDTO(LocalDate fecha, String horaConsulta, String nombrePaciente, String tipoPago) {
        this.fecha = fecha;
        this.horaConsulta = horaConsulta;
        this.nombrePaciente = nombrePaciente;
        this.tipoPago = tipoPago;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getHoraConsulta() {
        return horaConsulta;
    }
    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }
    public String getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
    public String getTipoPago() {
        return tipoPago;
    }
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
}
