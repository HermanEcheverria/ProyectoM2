/**
 * Entity representing a doctor.
 */
package com.unis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "DOCTOR")
public class Doctor {

    /** The unique identifier of the doctor. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    /** The unique identifier of the user associated with the doctor. */
    @Column(name = "ID_USUARIO", nullable = false, unique = true)
    private Long idUsuario;

    /** The last name of the doctor. */
    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    /** The document identifier of the doctor. */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** The birth date of the doctor. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the doctor. */
    @Column(name = "GENERO")
    private String genero;

    /** The phone number of the doctor. */
    @Column(name = "TELEFONO")
    private String telefono;

    /** The specialty of the doctor. */
    @Column(name = "ESPECIALIDAD")
    private String especialidad;

    /** The collegiate number of the doctor. */
    @Column(name = "NUMERO_COLEGIADO")
    private String numeroColegiado;

    /** The working hours of the doctor. */
    @Column(name = "HORARIO_ATENCION")
    private String horarioAtencion;

    /** The graduation date of the doctor. */
    @Column(name = "FECHA_GRADUACION")
    @Temporal(TemporalType.DATE)
    private Date fechaGraduacion;

    /** The university where the doctor graduated. */
    @Column(name = "UNIVERSIDAD_GRADUACION")
    private String universidadGraduacion;

    /** The user entity associated with the doctor. */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    /** The list of appointments associated with the doctor. */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas = new ArrayList<>();

    // Getters and Setters

    /** @return the unique identifier of the doctor. */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /** @param idDoctor the unique identifier of the doctor. */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /** @return the unique identifier of the user associated with the doctor. */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /** @param idUsuario the unique identifier of the user associated with the doctor. */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /** @return the last name of the doctor. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name of the doctor. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier of the doctor. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier of the doctor. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date of the doctor. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the doctor. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender of the doctor. */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender of the doctor. */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number of the doctor. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the doctor. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the specialty of the doctor. */
    public String getEspecialidad() {
        return especialidad;
    }

    /** @param especialidad the specialty of the doctor. */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /** @return the collegiate number of the doctor. */
    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    /** @param numeroColegiado the collegiate number of the doctor. */
    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    /** @return the working hours of the doctor. */
    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    /** @param horarioAtencion the working hours of the doctor. */
    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    /** @return the graduation date of the doctor. */
    public Date getFechaGraduacion() {
        return fechaGraduacion;
    }

    /** @param fechaGraduacion the graduation date of the doctor. */
    public void setFechaGraduacion(Date fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    /** @return the university where the doctor graduated. */
    public String getUniversidadGraduacion() {
        return universidadGraduacion;
    }

    /** @param universidadGraduacion the university where the doctor graduated. */
    public void setUniversidadGraduacion(String universidadGraduacion) {
        this.universidadGraduacion = universidadGraduacion;
    }

    /** @return the user entity associated with the doctor. */
    public Usuario getUsuario() {
        return usuario;
    }

    /** @param usuario the user entity associated with the doctor. */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /** @return the list of appointments associated with the doctor. */
    public List<Cita> getCitas() {
        return citas;
    }

    /** @param citas the list of appointments associated with the doctor. */
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}
