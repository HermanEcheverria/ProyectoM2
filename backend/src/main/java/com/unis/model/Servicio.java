package com.unis.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "SERVICIO")
public class Servicio extends PanacheEntity {

    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public double costo;

    @Column(nullable = false)
    public boolean cubiertoSeguro;

    // 🔹 Relación ManyToOne para indicar el servicio padre
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonBackReference
    public Servicio servicioPadre;

    // 🔹 Relación OneToMany con subservicios
    @OneToMany(mappedBy = "servicioPadre", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    public Set<Servicio> subServicios = new HashSet<>(); // ✅ Inicializado para evitar `null`

    // ✅ Quitar setParentId() ya que causa entidades detached
    public Long getParentId() {
        return servicioPadre != null ? servicioPadre.id : null;
    }
}
