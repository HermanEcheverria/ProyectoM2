package com.unis.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "FICHATECNICA")
public class FichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FICHA")
    private Long idFicha;

    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "HISTORIAL_SERVICIOS")
    private String historialServicios;

    @Column(name = "NUMEROAFILIACION")
    private String numeroAfiliacion;

    @Column(name = "CODIGOSEGURO")
    private String codigoSeguro;

    @Column(name = "CARNETSEGURO")
    private String carnetSeguro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE", referencedColumnName = "ID_PACIENTE", nullable = false)
    private PacienteFT paciente;

    // Getters y Setters
    public Long getIdFicha() { return idFicha; }
    public void setIdFicha(Long idFicha) { this.idFicha = idFicha; }

    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getHistorialServicios() { return historialServicios; }
    public void setHistorialServicios(String historialServicios) { this.historialServicios = historialServicios; }

    public String getNumeroAfiliacion() { return numeroAfiliacion; }
    public void setNumeroAfiliacion(String numeroAfiliacion) { this.numeroAfiliacion = numeroAfiliacion; }

    public String getCodigoSeguro() { return codigoSeguro; }
    public void setCodigoSeguro(String codigoSeguro) { this.codigoSeguro = codigoSeguro; }

    public String getCarnetSeguro() { return carnetSeguro; }
    public void setCarnetSeguro(String carnetSeguro) { this.carnetSeguro = carnetSeguro; }

    public PacienteFT getPaciente() { return paciente; }
    public void setPaciente(PacienteFT paciente) { this.paciente = paciente; }
}
