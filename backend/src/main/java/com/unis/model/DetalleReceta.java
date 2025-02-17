package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DETALLE_RECETA")
public class DetalleReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE")
    private int idDetalle;

    @Column(name = "ID_RECETA", nullable = false)
    private int idReceta;

    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 100)
    private String principioActivo;

    @Column(name = "CONCENTRACION", nullable = false, length = 50)
    private String concentracion;

    @Column(name = "PRESENTACION", nullable = false, length = 50)
    private String presentacion;

    @Column(name = "DOSIS", nullable = false, length = 50)
    private String dosis;

    @Column(name = "FRECUENCIA", nullable = false, length = 50)
    private String frecuencia;

    @Column(name = "DURACION", nullable = false)
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA", insertable = false, updatable = false)
    private Receta receta;

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public Receta getReceta() { return receta; }
    public void setReceta(Receta receta) { this.receta = receta; }
}
