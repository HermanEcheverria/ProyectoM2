package com.unis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOSPITAL")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HOSPITAL")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "DIRECCION", nullable = false, length = 200)
    private String direccion;

    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Column(name = "CORREO", length = 100)
    private String correo;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @Column(name = "MONGO_ID", length = 50)
private String mongoId;


    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getMongoId() {
        return mongoId;
    }
    
    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }
    
}
