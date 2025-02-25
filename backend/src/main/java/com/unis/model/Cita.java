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

    @JsonProperty("idDoctor")  // 🔹 Asegura que se mapea correctamente desde el JSON
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    @JsonProperty("idPaciente")  // 🔹 Asegura que se mapea correctamente desde el JSON
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCita estado;

    @Column(name = "MOTIVO")
    private String motivo;

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
}
