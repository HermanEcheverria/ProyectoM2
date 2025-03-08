package com.unis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "RECETAMEDICAMENTO")
public class RecetaMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETAMEDICAMENTO", nullable = false)
    private Long idRecetaMedicamento;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = false)
    @JsonBackReference  // 🔹 Evita la serialización infinita en JSON
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "ID_MEDICAMENTO", nullable = false)
    private Medicamento medicamento;

    @Column(name = "DOSIS", length = 50)
    private String dosis;

    @Column(name = "FRECUENCIA", length = 50)
    private String frecuencia;

    @Column(name = "DURACION", length = 50)
    private String duracion;

    @Column(name = "DIAGNOSTICO", length = 255)
    private String diagnostico;

    // Métodos necesarios para evitar errores en RecetaService
    public Long getIdReceta() {
        return receta != null ? receta.getIdReceta() : null;
    }

    public Long getIdMedicamento() {
        return medicamento != null ? medicamento.getIdMedicamento() : null;
    }

    // Getters y Setters
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
}
