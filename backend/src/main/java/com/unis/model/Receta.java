package com.unis.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "RECETA")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private int idReceta;

    @Column(name = "ID_PACIENTE", nullable = false)
    private int idPaciente;

    @Column(name = "ID_DOCTOR", nullable = false)
    private int idDoctor;

    @Column(name = "DIAGNOSTICO", nullable = false, length = 255)
    private String diagnostico;

    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado = "activa";

    @Column(name = "FECHA", nullable = false)
    private Timestamp fecha;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleReceta> detalleMedicamentos;

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

    public List<DetalleReceta> getDetalleMedicamentos() { return detalleMedicamentos; }
    public void setDetalleMedicamentos(List<DetalleReceta> detalleMedicamentos) { this.detalleMedicamentos = detalleMedicamentos; }
}
