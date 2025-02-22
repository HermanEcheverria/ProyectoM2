package com.unis.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICIO")
public class Servicio extends PanacheEntity {
    
    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public double costo;

    @Column(nullable = false)
    public boolean cubiertoSeguro;

    // 🔹 Evita recursión infinita en JSON
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonBackReference
    public Servicio servicioPadre;

    @OneToMany(mappedBy = "servicioPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public Set<Servicio> subServicios;

    public Long getParentId() {
        return servicioPadre != null ? servicioPadre.id : null;
    }

    public void setParentId(Long parentId) {
        if (parentId != null) {
            this.servicioPadre = new Servicio();
            this.servicioPadre.id = parentId;
        } else {
            this.servicioPadre = null;
        }
    }
}
