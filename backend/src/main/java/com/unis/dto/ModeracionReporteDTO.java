// DTO
package com.unis.dto;

public class ModeracionReporteDTO {
    private int numeroOrden;
    private String usuario;
    private int totalRechazos;

    public ModeracionReporteDTO() {}

    public ModeracionReporteDTO(int numeroOrden, String usuario, int totalRechazos) {
        this.numeroOrden = numeroOrden;
        this.usuario = usuario;
        this.totalRechazos = totalRechazos;
    }

    public int getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(int numeroOrden) { this.numeroOrden = numeroOrden; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getTotalRechazos() { return totalRechazos; }
    public void setTotalRechazos(int totalRechazos) { this.totalRechazos = totalRechazos; }
}
