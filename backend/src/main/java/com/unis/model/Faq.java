package com.unis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FAQ")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pregunta;
    
    @Lob
    private String respuesta;

    private String autor;

    private LocalDateTime fechaCreacion;

    private String editadoPor; // Nuevo campo para almacenar quién editó la pregunta

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // GETTERS y SETTERS

    public Long getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEditadoPor() {
        return editadoPor;
    }

    public void setEditadoPor(String editadoPor) {
        this.editadoPor = editadoPor;
    }
}

