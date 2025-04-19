/**
 * Entity representing a service.
 */
package com.unis.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICIO")
public class Servicio extends PanacheEntity {

    /** The name of the service. */
    @Column(nullable = false)
    public String nombre;

    /** The cost of the service. */
    @Column(nullable = false)
    public double costo;

    /** Indicates whether the service is covered by insurance. */
    @Column(nullable = false)
    public boolean cubiertoSeguro;

    /** The parent service, if applicable. */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonBackReference
    public Servicio servicioPadre;

    /** The set of sub-services associated with this service. */
    @OneToMany(mappedBy = "servicioPadre", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    public Set<Servicio> subServicios = new HashSet<>();

    // Getters

    /** @return the ID of the parent service, if applicable. */
    public Long getParentId() {
        return servicioPadre != null ? servicioPadre.id : null;
    }

    /** @return the unique identifier of the service. */
    public Long getId() {
        return this.id;
    }

    /** @return the unique identifier of the service. */
    public Long getIdServicio() {
        return this.id;
    }
}
