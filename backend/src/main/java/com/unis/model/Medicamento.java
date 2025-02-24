package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MEDICAMENTO")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicamento_seq_gen")
    @SequenceGenerator(name = "medicamento_seq_gen", sequenceName = "medicamento_seq", allocationSize = 1)
    @Column(name = "ID_MEDICAMENTO")
    private Long idMedicamento;


    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 100)
    private String principioActivo;

    @Column(name = "CONCENTRACION", nullable = false, length = 50)
    private String concentracion;

    @Column(name = "PRESENTACION", nullable = false, length = 50)
    private String presentacion;

    @Column(name = "FORMA_FARMACEUTICA", nullable = false, length = 50)
    private String formaFarmaceutica;

    @Column(name = "DOSIS", nullable = false, length = 50)
    private String dosis;

    @Column(name = "FRECUENCIA", nullable = false, length = 50)
    private String frecuencia;

    @Column(name = "DURACION", nullable = false)
    private int duracion;

    @Column(name = "DIAGNOSTICO", length = 255)
    private String diagnostico;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = true) // Se permite NULL en caso de medicamentos sin receta
    private Receta receta;

    // Constructor vacío
    public Medicamento() {}

    // Getters y Setters
    public Long getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(Long idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getFormaFarmaceutica() { return formaFarmaceutica; }
    public void setFormaFarmaceutica(String formaFarmaceutica) { this.formaFarmaceutica = formaFarmaceutica; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public Receta getReceta() { return receta; }
    public void setReceta(Receta receta) { this.receta = receta; }
}