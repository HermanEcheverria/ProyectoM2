package com.unis.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MEDICAMENTO")
public class Medicamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO", nullable = false)
    private Long idMedicamento;

    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 255)
    private String principioActivo;

    @Column(name = "CONCENTRACION", nullable = false, length = 50)
    private String concentracion;

    @Column(name = "PRESENTACION", nullable = false, length = 100)
    private String presentacion;

    @Column(name = "FORMA_FARMACEUTICA", nullable = false, length = 100)
    private String formaFarmaceutica;

    @Column(name = "VENTA_LIBRE")
    private Integer ventaLibre; // 0 = Requiere receta, 1 = Venta libre

    // 🔹 Getters y Setters

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public Integer getVentaLibre() {
        return ventaLibre;
    }

    public void setVentaLibre(Integer ventaLibre) {
        this.ventaLibre = ventaLibre;
    }
}
