package com.unis.model;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USUARIO")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOMBRE_USUARIO", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "CORREO", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "CONTRASENA", nullable = false, length = 100)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "ROL_ID", nullable = true)  // La relación con la tabla ROL, por ahora, será NULL hasta ser asignado por el admin
    private Rol rol;

    @Column(name = "ESTADO", nullable = false)
    private int estado = 0;  // Estado siempre será 0 (inactivo) al inicio

    @Column(name = "FECHA_CREACTION", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreaction = new Date();  // Fecha de creación se establece con la fecha y hora actual

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaCreaction() {
        return fechaCreaction;
    }

    public void setFechaCreaction(Date fechaCreaction) {
        this.fechaCreaction = fechaCreaction;
    }
}
