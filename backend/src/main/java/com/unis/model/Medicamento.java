/**
 * Entity representing a medication.
 */
package com.unis.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEDICAMENTO")
public class Medicamento implements Serializable {

    /** The unique identifier of the medication. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO", nullable = false)
    private Long idMedicamento;

    /** The active ingredient of the medication. */
    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 255)
    private String principioActivo;

    /** The concentration of the medication. */
    @Column(name = "CONCENTRACION", nullable = false, length = 50)
    private String concentracion;

    /** The presentation of the medication (e.g., tablet, capsule). */
    @Column(name = "PRESENTACION", nullable = false, length = 100)
    private String presentacion;

    /** The pharmaceutical form of the medication (e.g., liquid, solid). */
    @Column(name = "FORMA_FARMACEUTICA", nullable = false, length = 100)
    private String formaFarmaceutica;

    /** Indicates whether the medication is sold over the counter (1) or requires a prescription (0). */
    @Column(name = "VENTA_LIBRE")
    private Integer ventaLibre;

    // Getters and Setters

    /** @return the unique identifier of the medication. */
    public Long getIdMedicamento() {
        return idMedicamento;
    }

    /** @param idMedicamento the unique identifier of the medication. */
    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    /** @return the active ingredient of the medication. */
    public String getPrincipioActivo() {
        return principioActivo;
    }

    /** @param principioActivo the active ingredient of the medication. */
    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    /** @return the concentration of the medication. */
    public String getConcentracion() {
        return concentracion;
    }

    /** @param concentracion the concentration of the medication. */
    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    /** @return the presentation of the medication. */
    public String getPresentacion() {
        return presentacion;
    }

    /** @param presentacion the presentation of the medication. */
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    /** @return the pharmaceutical form of the medication. */
    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    /** @param formaFarmaceutica the pharmaceutical form of the medication. */
    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    /** @return whether the medication is sold over the counter (1) or requires a prescription (0). */
    public Integer getVentaLibre() {
        return ventaLibre;
    }

    /** @param ventaLibre whether the medication is sold over the counter (1) or requires a prescription (0). */
    public void setVentaLibre(Integer ventaLibre) {
        this.ventaLibre = ventaLibre;
    }
}
