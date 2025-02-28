package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DOCTOR")
public class DoctorImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    @Lob
    @Column(name = "FOTOGRAFIA")
    private byte[] fotografia;

    @Lob
    @Column(name = "FOTO_TITULO")
    private byte[] fotoTitulo;

    // Getters y Setters

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public byte[] getFotoTitulo() {
        return fotoTitulo;
    }

    public void setFotoTitulo(byte[] fotoTitulo) {
        this.fotoTitulo = fotoTitulo;
    }
}
