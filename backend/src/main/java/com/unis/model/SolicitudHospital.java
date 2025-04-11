package com.unis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "SOLICITUD_HOSPITAL")
public class SolicitudHospital extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
        name = "solicitudHospitalSeq",
        sequenceName = "SOLICITUD_HOSPITAL_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitudHospitalSeq")
    public Long id;

    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public String direccion;

    @Column(nullable = false)
    public String telefono;

    @Column(nullable = false)
    public String aseguradora;

    @Column(nullable = false)
    public String estado = "pendiente";

    @Column(nullable = false)
    public String origen = "hospital"; // Campo obligatorio para MongoDB
}

