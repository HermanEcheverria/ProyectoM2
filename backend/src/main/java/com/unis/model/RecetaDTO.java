package com.unis.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaDTO {

    private int idReceta;
    private int idPaciente;
    private int idDoctor;
    private String diagnostico;
    private String observaciones;
    private String estado;
    private Timestamp fecha;
    private List<DetalleMedicamentoDTO> detalleMedicamentos;

    // Constructor vacío
    public RecetaDTO() {}

    // Constructor que convierte una entidad Receta en un DTO
    public RecetaDTO(Receta receta) {
        this.idReceta = receta.getIdReceta();
        this.idPaciente = receta.getIdPaciente();
        this.idDoctor = receta.getIdDoctor();
        this.diagnostico = receta.getDiagnostico();
        this.observaciones = receta.getObservaciones();
        this.estado = receta.getEstado();
        this.fecha = receta.getFecha();
        // Convertimos los detalles de medicamentos en una lista de DetalleMedicamentoDTO
        this.detalleMedicamentos = receta.getDetalleMedicamentos().stream()
                .map(DetalleMedicamentoDTO::new)
                .collect(Collectors.toList());
    }

    // Getters y Setters
    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public List<DetalleMedicamentoDTO> getDetalleMedicamentos() { return detalleMedicamentos; }
    public void setDetalleMedicamentos(List<DetalleMedicamentoDTO> detalleMedicamentos) { this.detalleMedicamentos = detalleMedicamentos; }
}
