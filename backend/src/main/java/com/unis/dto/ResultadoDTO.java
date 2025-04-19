/**
 * Data Transfer Object (DTO) representing the results of a medical appointment.
 */
package com.unis.dto;

import java.time.LocalDate;

public class ResultadoDTO {
    /** The document associated with the results. */
    public String documento;

    /** The diagnosis provided during the appointment. */
    public String diagnostico;

    /** The detailed results of the appointment. */
    public String resultados;

    /** The date when the results were recorded. */
    public LocalDate fecha;

    /** The ID of the associated appointment. */
    public Long idCita;
}
