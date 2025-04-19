/**
 * Entity representing a doctor's schedule.
 */
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AGENDA")
public class Agenda {

    /** The unique identifier of the schedule. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The ID of the doctor associated with the schedule. */
    @Column(name = "ID_DOCTOR", nullable = false)
    private Long idDoctor;

    /** The days of attention in string format, e.g., "Monday,Tuesday,Wednesday". */
    @Column(name = "DIAS_ATENCION", nullable = false)
    private String diasAtencion;

    /** The start time of the schedule. */
    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    /** The end time of the schedule. */
    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    /** Additional notes for the schedule. */
    @Column(name = "NOTAS")
    private String notas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getDiasAtencion() {
        return diasAtencion;
    }

    public void setDiasAtencion(String diasAtencion) {
        this.diasAtencion = diasAtencion;
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
