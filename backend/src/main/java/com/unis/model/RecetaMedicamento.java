package com.unis.model;

import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "RECETAMEDICAMENTO")
public class RecetaMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETAMEDICAMENTO")
    private Long idRecetaMedicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RECETA", nullable = false)
    private Receta receta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEDICAMENTO", nullable = false)
    private Medicamento medicamento;

    @Column(name = "DOSIS", nullable = false)
    private String dosis;

    @Column(name = "FRECUENCIA", nullable = false)
    private String frecuencia;

    @Column(name = "DURACION", nullable = false)
    private String duracion;

    @Column(name = "DIAGNOSTICO", nullable = true)
    private String diagnostico;

    // ✅ GETTERS & SETTERS
    public Long getIdRecetaMedicamento() { return idRecetaMedicamento; }
    public void setIdRecetaMedicamento(Long idRecetaMedicamento) { this.idRecetaMedicamento = idRecetaMedicamento; }

    public Receta getReceta() { return receta; }
    public void setReceta(Receta receta) { this.receta = receta; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public Long getIdReceta() {
        return (receta != null) ? receta.getIdReceta() : null;
    }

    public Long getIdMedicamento() {
        return (medicamento != null) ? medicamento.getIdMedicamento() : null;
    }
}
