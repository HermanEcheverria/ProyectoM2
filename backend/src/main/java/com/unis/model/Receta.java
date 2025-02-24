package com.unis.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RECETA")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta_seq")
    @SequenceGenerator(name = "receta_seq", sequenceName = "RECETA_SEQ", allocationSize = 1)
    @Column(name = "ID_RECETA") 
    private Long idReceta;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", nullable = false, insertable = false, updatable = false)
    private Paciente paciente;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "FECHA") 
    private Date fecha;

    @Column(name = "CODIGO_RECETA") 
    private String codigoReceta;

    @Column(name = "ID_DOCTOR") 
    private Long idDoctor;

    @Column(name = "ID_HOSPITAL") 
    private Long idHospital;

    @Column(name = "ID_SEGURO") 
    private Long idSeguro;

    @Column(name = "OBSERVACIONES") 
    private String anotaciones;

    @Column(name = "NOTAS_ESPECIALES") 
    private String notasEspeciales;

    @Column(name = "DIAGNOSTICO") 
    private String diagnostico;

    @Column(name = "ESTADO") 
    private String estado;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medicamento> detalleMedicamentos;

    // Getters y Setters

    public Long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.idPaciente = (paciente != null) ? paciente.getIdPaciente() : null;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Long getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }

    public Long getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(Long idSeguro) {
        this.idSeguro = idSeguro;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public String getNotasEspeciales() {
        return notasEspeciales;
    }

    public void setNotasEspeciales(String notasEspeciales) {
        this.notasEspeciales = notasEspeciales;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Medicamento> getDetalleMedicamentos() {
        return detalleMedicamentos;
    }

    public void setDetalleMedicamentos(List<Medicamento> detalleMedicamentos) {
        this.detalleMedicamentos = detalleMedicamentos;
    }
}