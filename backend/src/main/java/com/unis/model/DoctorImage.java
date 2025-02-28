package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DOCTOR_IMAGE")
public class DoctorImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_DOCTOR", nullable = false)
    private DoctorAcc doctor;

    @Lob
    @Column(name = "FOTOGRAFIA")
    private byte[] fotografia;

    @Lob
    @Column(name = "FOTO_TITULO")
    private byte[] fotoTitulo;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DoctorAcc getDoctor() { return doctor; }
    public void setDoctor(DoctorAcc doctor) { this.doctor = doctor; }

    public byte[] getFotografia() { return fotografia; }
    public void setFotografia(byte[] fotografia) { this.fotografia = fotografia; }

    public byte[] getFotoTitulo() { return fotoTitulo; }
    public void setFotoTitulo(byte[] fotoTitulo) { this.fotoTitulo = fotoTitulo; }
}
