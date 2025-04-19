/**
 * Entity representing a patient.
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
@Table(name = "PACIENTE")
public class PacienteAcc implements Serializable {

    /** The unique identifier of the patient. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The user account associated with the patient. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    /** The last name of the patient. */
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    /** The document identifier of the patient. */
    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    /** The birth date of the patient. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the patient. */
    @Column(name = "GENERO", length = 10)
    private String genero;

    /** The phone number of the patient. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The ID of the hospital associated with the patient. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    // Getters and Setters

    /** @return the unique identifier of the patient. */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /** @param idPaciente the unique identifier of the patient. */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /** @return the user account associated with the patient. */
    public UserAcc getUsuario() {
        return usuario;
    }

    /** @param usuario the user account associated with the patient. */
    public void setUsuario(UserAcc usuario) {
        this.usuario = usuario;
    }

    /** @return the last name of the patient. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name of the patient. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier of the patient. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier of the patient. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date of the patient. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the patient. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender of the patient. */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender of the patient. */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number of the patient. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the patient. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the ID of the hospital associated with the patient. */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the ID of the hospital associated with the patient. */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }
}
