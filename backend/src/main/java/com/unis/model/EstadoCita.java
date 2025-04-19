/**
 * Enum representing the possible states of a medical appointment.
 */
package com.unis.model;

public enum EstadoCita {
    /** The appointment is pending confirmation. */
    PENDIENTE,

    /** The appointment has been confirmed. */
    CONFIRMADA,

    /** The appointment has been canceled. */
    CANCELADA,

    /** The appointment has been completed. */
    FINALIZADA;
}
