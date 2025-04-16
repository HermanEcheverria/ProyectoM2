package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "HISTORIA")
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historia_seq")
    @SequenceGenerator(name = "historia_seq", sequenceName = "HISTORIA_SEQ", allocationSize = 1)
    private Long id;

    private String nombreEntidad;

    @Lob
    private String historia;

    @Lob
    private String meritos;

    @Lob
    private String lineaDelTiempo;
    

    @Column(name = "STATUS", length = 20)
    private String status; // PROCESO, PUBLICADO, RECHAZADO

    @Column(name = "REJECTION_REASON", length = 500)
    private String rejectionReason;

    @Column(name = "EDITOR_EMAIL", length = 150)
private String editorEmail;


    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getMeritos() {
        return meritos;
    }

    public void setMeritos(String meritos) {
        this.meritos = meritos;
    }

    public String getLineaDelTiempo() {
        return lineaDelTiempo;
    }

    public void setLineaDelTiempo(String lineaDelTiempo) {
        this.lineaDelTiempo = lineaDelTiempo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getEditorEmail() {
        return editorEmail;
    }
    
    public void setEditorEmail(String editorEmail) {
        this.editorEmail = editorEmail;
    }
    
}
