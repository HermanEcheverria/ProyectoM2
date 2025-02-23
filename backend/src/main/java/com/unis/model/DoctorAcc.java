package com.unis.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DOCTOR")
public class DoctorAcc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(name = "GENERO", length = 10)
    private String genero;

    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    @Column(name = "ESPECIALIDAD", length = 100)
    private String especialidad;

    @Column(name = "NUMERO_COLEGIADO", length = 50)
    private String numeroColegiado;

    @Column(name = "HORARIO_ATENCION", length = 255)
    private String horarioAtencion;

    @Column(name = "FECHA_GRADUACION")
    @Temporal(TemporalType.DATE)
    private Date fechaGraduacion;

    @Column(name = "UNIVERSIDAD_GRADUACION", length = 150)
    private String universidadGraduacion;

    @Column(name = "DISPONIBILIDAD", length = 255)
    private String disponibilidad;

    // 🔹 Getters y Setters
    public Long getIdDoctor() { return idDoctor; }
    public void setIdDoctor(Long idDoctor) { this.idDoctor = idDoctor; }

    public UserAcc getUsuario() { return usuario; }
    public void setUsuario(UserAcc usuario) { this.usuario = usuario; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Long getIdHospital() { return idHospital; }
    public void setIdHospital(Long idHospital) { this.idHospital = idHospital; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }

    public Date getFechaGraduacion() { return fechaGraduacion; }
    public void setFechaGraduacion(Date fechaGraduacion) { this.fechaGraduacion = fechaGraduacion; }

    public String getUniversidadGraduacion() { return universidadGraduacion; }
    public void setUniversidadGraduacion(String universidadGraduacion) { this.universidadGraduacion = universidadGraduacion; }

    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
}
