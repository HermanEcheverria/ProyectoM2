package com.unis.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DOCTOR")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    @Column(name = "ID_USUARIO", nullable = false, unique = true)
    private Long idUsuario;

    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(name = "GENERO")
    private String genero;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "ESPECIALIDAD")
    private String especialidad;

    @Column(name = "NUMERO_COLEGIADO")
    private String numeroColegiado;

    @Column(name = "HORARIO_ATENCION")
    private String horarioAtencion;

    @Column(name = "FECHA_GRADUACION")
    @Temporal(TemporalType.DATE)
    private Date fechaGraduacion;

    @Column(name = "UNIVERSIDAD_GRADUACION")
    private String universidadGraduacion;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    // Getters y Setters

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public Date getFechaGraduacion() {
        return fechaGraduacion;
    }

    public void setFechaGraduacion(Date fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    public String getUniversidadGraduacion() {
        return universidadGraduacion;
    }

    public void setUniversidadGraduacion(String universidadGraduacion) {
        this.universidadGraduacion = universidadGraduacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
