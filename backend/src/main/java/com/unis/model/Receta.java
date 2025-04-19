/**
 * Entity representing a medical prescription.
 */
package com.unis.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "RECETA")
public class Receta implements Serializable {

    /** The unique identifier of the prescription. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA", nullable = false)
    private Long idReceta;

    /** The ID of the associated medical appointment. */
    @Column(name = "ID_CITA", nullable = false)
    private Long idCita;

    /** The creation date of the prescription. */
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    /** The ID of the patient associated with the prescription. */
    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    /** The ID of the doctor who issued the prescription. */
    @Column(name = "ID_DOCTOR", nullable = false)
    private Long idDoctor;

    /** The unique code of the prescription. */
    @Column(name = "CODIGO_RECETA", nullable = false, unique = true, length = 50)
    private String codigoReceta;

    /** Additional notes for the prescription. */
    @Column(name = "ANOTACIONES", length = 1000)
    private String anotaciones;

    /** Special notes for the prescription. */
    @Column(name = "NOTAS_ESPECIALES", length = 1000)
    private String notasEspeciales;

    /** The list of medications associated with the prescription. */
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<RecetaMedicamento> medicamentos;

    // Getters and Setters

    /** @return the unique identifier of the prescription. */
    public Long getIdReceta() {
        return idReceta;
    }

    /** @param idReceta the unique identifier of the prescription. */
    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    /** @return the ID of the associated medical appointment. */
    public Long getIdCita() {
        return idCita;
    }

    /** @param idCita the ID of the associated medical appointment. */
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    /** @return the creation date of the prescription. */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion the creation date of the prescription. */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return the ID of the patient associated with the prescription. */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /** @param idPaciente the ID of the patient associated with the prescription. */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /** @return the ID of the doctor who issued the prescription. */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /** @param idDoctor the ID of the doctor who issued the prescription. */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /** @return the unique code of the prescription. */
    public String getCodigoReceta() {
        return codigoReceta;
    }

    /** @param codigoReceta the unique code of the prescription. */
    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    /** @return additional notes for the prescription. */
    public String getAnotaciones() {
        return anotaciones;
    }

    /** @param anotaciones additional notes for the prescription. */
    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    /** @return special notes for the prescription. */
    public String getNotasEspeciales() {
        return notasEspeciales;
    }

    /** @param notasEspeciales special notes for the prescription. */
    public void setNotasEspeciales(String notasEspeciales) {
        this.notasEspeciales = notasEspeciales;
    }

    /** @return the list of medications associated with the prescription. */
    public List<RecetaMedicamento> getMedicamentos() {
        return medicamentos;
    }

    /** @param medicamentos the list of medications associated with the prescription. */
    public void setMedicamentos(List<RecetaMedicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
