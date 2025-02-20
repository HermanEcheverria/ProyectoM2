package com.unis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Servicio extends PanacheEntity {
    
    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public String categoria;

    @Column(nullable = false)
    public String subcategoria;

    @Column(nullable = false)
    public double costo;

    @Column(nullable = false)
    public boolean cubiertoSeguro;
}
