/**
 * Entity representing an insurance company.
 */
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ASEGURADORA")
public class Aseguradora {

    /** The unique identifier of the insurance company. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aseguradora_seq")
    @SequenceGenerator(name = "aseguradora_seq", sequenceName = "SEQ_ASEGURADORA", allocationSize = 1)
    @Column(name = "ID_ASEGURADORA")
    private Long id;

    /** The name of the insurance company. */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
