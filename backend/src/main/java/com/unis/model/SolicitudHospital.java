package com.unis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "SOLICITUD_HOSPITAL")
public class SolicitudHospital extends PanacheEntity {

    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public String direccion;

    @Column(nullable = false)
    public String telefono;

    @Column(nullable = false)
    public String aseguradora; // Nueva aseguradora a la que se conecta

    @Column(nullable = false)
    public String estado = "pendiente";
}

