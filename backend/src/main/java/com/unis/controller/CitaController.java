package com.unis.controller;

import java.time.LocalTime;
import java.util.List;

import com.unis.model.Cita;
import com.unis.service.CitaService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CitaController {

    @Inject
    CitaService citaService;

    public List<Cita> obtenerCitas() {
        return citaService.obtenerCitas();
    }

    public Cita obtenerCitaPorId(Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    public void agendarCita(Cita cita) {
        try {
            // Convertir horaInicio y horaFin de String a LocalTime
            LocalTime horaInicio = LocalTime.parse(cita.getHoraInicio());
            LocalTime horaFin = LocalTime.parse(cita.getHoraFin());

            // Validar que la cita esté dentro del horario permitido
            if (horaInicio.isBefore(LocalTime.of(8, 0)) || horaFin.isAfter(LocalTime.of(16, 30))) {
                throw new IllegalArgumentException("⚠️ Las citas solo pueden ser entre 8:00 AM y 4:30 PM.");
            }

            // Validar que la hora de fin sea mayor que la hora de inicio
            if (!horaFin.isAfter(horaInicio)) {
                throw new IllegalArgumentException("⚠️ La hora de fin debe ser posterior a la hora de inicio.");
            }

            citaService.agendarCita(cita);
        } catch (Exception e) {
            throw new IllegalArgumentException("⚠️ Formato de hora incorrecto. Use el formato HH:mm");
        }
    }

    public void cancelarCita(Long id) {
        citaService.cancelarCita(id);
    }
}
