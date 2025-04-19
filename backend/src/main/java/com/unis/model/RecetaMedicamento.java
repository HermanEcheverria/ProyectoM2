/**
 * Entity representing the relationship between a prescription and its medications.
 */
package com.unis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "RECETAMEDICAMENTO")
public class RecetaMedicamento {

    /** The unique identifier of the prescription-medication relationship. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETAMEDICAMENTO", nullable = false)
    private Long idRecetaMedicamento;

    /** The prescription associated with the medication. */
    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = false)
    @JsonBackReference
    private Receta receta;

    /** The medication associated with the prescription. */
    @ManyToOne
    @JoinColumn(name = "ID_MEDICAMENTO", nullable = false)
    private Medicamento medicamento;

    /** The dosage of the medication. */
    @Column(name = "DOSIS", length = 50)
    private String dosis;

    /** The frequency of the medication intake. */
    @Column(name = "FRECUENCIA", length = 50)
    private String frecuencia;

    /** The duration of the medication intake. */
    @Column(name = "DURACION", length = 50)
    private String duracion;

    /** The diagnosis associated with the prescription. */
    @Column(name = "DIAGNOSTICO", length = 255)
    private String diagnostico;

    // Methods to avoid errors in RecetaService

    /**
     * @return the ID of the associated prescription.
     */
    public Long getIdReceta() {
        return receta != null ? receta.getIdReceta() : null;
    }

    /**
     * @return the ID of the associated medication.
     */
    public Long getIdMedicamento() {
        return medicamento != null ? medicamento.getIdMedicamento() : null;
    }

    // Getters and Setters

    /** @return the unique identifier of the prescription-medication relationship. */
    public Long getIdRecetaMedicamento() {
        return idRecetaMedicamento;
    }

    /** @param idRecetaMedicamento the unique identifier of the prescription-medication relationship. */
    public void setIdRecetaMedicamento(Long idRecetaMedicamento) {
        this.idRecetaMedicamento = idRecetaMedicamento;
    }

    /** @return the prescription associated with the medication. */
    public Receta getReceta() {
        return receta;
    }

    /** @param receta the prescription associated with the medication. */
    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    /** @return the medication associated with the prescription. */
    public Medicamento getMedicamento() {
        return medicamento;
    }

    /** @param medicamento the medication associated with the prescription. */
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    /** @return the dosage of the medication. */
    public String getDosis() {
        return dosis;
    }

    /** @param dosis the dosage of the medication. */
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    /** @return the frequency of the medication intake. */
    public String getFrecuencia() {
        return frecuencia;
    }

    /** @param frecuencia the frequency of the medication intake. */
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    /** @return the duration of the medication intake. */
    public String getDuracion() {
        return duracion;
    }

    /** @param duracion the duration of the medication intake. */
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    /** @return the diagnosis associated with the prescription. */
    public String getDiagnostico() {
        return diagnostico;
    }

    /** @param diagnostico the diagnosis associated with the prescription. */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
