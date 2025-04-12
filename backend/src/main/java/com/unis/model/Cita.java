package com.unis.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CITA")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "ID_DOCTOR", insertable = false, updatable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", insertable = false, updatable = false)
    private Paciente paciente;

    @JsonProperty("idDoctor")
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    @JsonProperty("idPaciente")
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @Column(name = "NUMERO_AUTORIZACION")
private String numeroAutorizacion;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "ID_ASEGURADORA")
    private Long idAseguradora;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCita estado;

    @Column(name = "MOTIVO")
    private String motivo;

    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    @Column(name = "RESULTADOS")
    private String resultados;

    @ManyToOne
    @JoinColumn(name = "ID_HOSPITAL", insertable = false, updatable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", insertable = false, updatable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "ID_ASEGURADORA", insertable = false, updatable = false)
    private Aseguradora aseguradora;

    // Getters y Setters

    public Long getIdCita() {
        return idCita;
    }
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        if (doctor != null) {
            this.idDoctor = doctor.getIdDoctor();
        }
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        if (paciente != null) {
            this.idPaciente = paciente.getIdPaciente();
        }
    }
    public Long getIdDoctor() {
        return idDoctor;
    }
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }
    public Long getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }
    public String getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
    public Long getIdHospital() {
        return idHospital;
    }
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }
    public Long getIdServicio() {
        return idServicio;
    }
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }
    public Long getIdAseguradora() {
        return idAseguradora;
    }
    public void setIdAseguradora(Long idAseguradora) {
        this.idAseguradora = idAseguradora;
    }
    public EstadoCita getEstado() {
        return estado;
    }
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public String getDiagnostico() {
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    public String getResultados() {
        return resultados;
    }
    public void setResultados(String resultados) {
        this.resultados = resultados;
    }
    public Hospital getHospital() {
        return hospital;
    }
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
    public Servicio getServicio() {
        return servicio;
    }
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    public Aseguradora getAseguradora() {
        return aseguradora;
    }
    public void setAseguradora(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }
    
    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }
}
