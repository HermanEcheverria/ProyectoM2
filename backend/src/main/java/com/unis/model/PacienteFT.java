/**
 * Entity representing a patient with technical records.
 */
package com.unis.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PACIENTE")
public class PacienteFT {

    /** The unique identifier of the patient. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The unique identifier of the user associated with the patient. */
    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    /** The document identifier of the patient. */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** The birth date of the patient. */
    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    /** The photograph of the patient. */
    @Lob
    @Column(name = "FOTOGRAFIA")
    private byte[] fotografia;

    /** The user entity associated with the patient. */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    /** The list of technical records associated with the patient. */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<FichaTecnica> fichasTecnicas;

    // Getters and Setters

    /** @return the unique identifier of the patient. */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /** @param idPaciente the unique identifier of the patient. */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /** @return the unique identifier of the user associated with the patient. */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /** @param idUsuario the unique identifier of the user associated with the patient. */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the patient. */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the photograph of the patient. */
    public byte[] getFotografia() {
        return fotografia;
    }

    /** @param fotografia the photograph of the patient. */
    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    /** @return the user entity associated with the patient. */
    public Usuario getUsuario() {
        return usuario;
    }

    /** @param usuario the user entity associated with the patient. */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /** @return the list of technical records associated with the patient. */
    public List<FichaTecnica> getFichasTecnicas() {
        return fichasTecnicas;
    }

    /** @param fichasTecnicas the list of technical records associated with the patient. */
    public void setFichasTecnicas(List<FichaTecnica> fichasTecnicas) {
        this.fichasTecnicas = fichasTecnicas;
    }
}
