/**
 * Entity representing an employee.
 */
package com.unis.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "EMPLEADOS")
public class Empleado implements Serializable {

    /** The unique identifier of the employee. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;

    /** The user account associated with the employee. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

    /** The last name of the employee. */
    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    /** The document identifier of the employee. */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** The birth date of the employee. */
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the employee. */
    @Column(name = "GENERO", nullable = false)
    private String genero;

    /** The phone number of the employee. */
    @Column(name = "TELEFONO", nullable = false)
    private String telefono;

    /** The position of the employee. */
    @Column(name = "PUESTO", nullable = false)
    private String puesto;

    // Getters and Setters

    /** @return the unique identifier of the employee. */
    public Long getIdEmpleado() {
        return idEmpleado;
    }

    /** @param idEmpleado the unique identifier of the employee. */
    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /** @return the user account associated with the employee. */
    public Usuario getUsuario() {
        return usuario;
    }

    /** @param usuario the user account associated with the employee. */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /** @return the last name of the employee. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name of the employee. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier of the employee. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier of the employee. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date of the employee. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the employee. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender of the employee. */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender of the employee. */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number of the employee. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the employee. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the position of the employee. */
    public String getPuesto() {
        return puesto;
    }

    /** @param puesto the position of the employee. */
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
