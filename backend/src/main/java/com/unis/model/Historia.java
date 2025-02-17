package com.unis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "HISTORIA")
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historia_seq")
    @SequenceGenerator(name = "historia_seq", sequenceName = "HISTORIA_SEQ", allocationSize = 1)
    private Long id;
    
    private String nombreEntidad;
    
    @Lob
    private String historia;
    
    @Lob
    private String meritos;
    
    @Lob
    private String lineaDelTiempo;

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreEntidad() {
        return nombreEntidad;
    }
    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }
    public String getHistoria() {
        return historia;
    }
    public void setHistoria(String historia) {
        this.historia = historia;
    }
    public String getMeritos() {
        return meritos;
    }
    public void setMeritos(String meritos) {
        this.meritos = meritos;
    }
    public String getLineaDelTiempo() {
        return lineaDelTiempo;
    }
    public void setLineaDelTiempo(String lineaDelTiempo) {
        this.lineaDelTiempo = lineaDelTiempo;
    }
}
