/**
 * Entity representing a hospital request.
 */
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

    /** The unique identifier of the hospital request. */
    @Id
    @SequenceGenerator(
        name = "solicitudHospitalSeq",
        sequenceName = "SOLICITUD_HOSPITAL_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitudHospitalSeq")
    public Long id;

    /** The name of the hospital. */
    @Column(nullable = false)
    public String nombre;

    /** The address of the hospital. */
    @Column(nullable = false)
    public String direccion;

    /** The phone number of the hospital. */
    @Column(nullable = false)
    public String telefono;

    /** The insurance company associated with the hospital. */
    @Column(nullable = false)
    public String aseguradora;

    /** The status of the request (e.g., "pendiente"). */
    @Column(nullable = false)
    public String estado = "pendiente";

    /** The origin of the request (e.g., "hospital"). */
    @Column(nullable = false)
    public String origen = "hospital";
}

