package com.unis.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USUARIO")
public class UserAcc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "NOMBRE_USUARIO", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "CONTRASENA", nullable = false, length = 128)
    private String contrasena;

    @Column(name = "ROL_ID", nullable = false)
    private int rolId;

    @Column(name = "CORREO", nullable = false, length = 100)
    private String correo;

    @Column(name = "ESTADO")
    private int estado;

    @Column(name = "FECHA_CREACTION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "IDHOSPITAL")
    private Long idHospital;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private DoctorAcc doctor;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private EmpleadoAcc empleado;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PacienteAcc paciente;

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public int getRolId() { return rolId; }
    public void setRolId(int rolId) { this.rolId = rolId; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getIdHospital() { return idHospital; }
    public void setIdHospital(Long idHospital) { this.idHospital = idHospital; }
}
