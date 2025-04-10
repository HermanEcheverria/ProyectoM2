package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
 
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.EstadoCita;
import com.unis.model.Paciente;
import com.unis.repository.CitaRepository;

import jakarta.persistence.EntityManager;

public class CitaServiceTest {

    @Mock
    CitaRepository citaRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    CitaService citaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para obtener todas las citas
    @Test
    public void testObtenerCitas() {
        // Arrange
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();
        List<Cita> expectedCitas = Arrays.asList(cita1, cita2);
        when(citaRepository.listAll()).thenReturn(expectedCitas);

        // Act
        List<Cita> actualCitas = citaService.obtenerCitas();

        // Assert
        assertEquals(expectedCitas, actualCitas);
    }

    // Prueba para obtener una cita por ID
    @Test
    public void testObtenerCitaPorId() {
        // Arrange
        Long id = 1L;
        Cita expectedCita = new Cita();
        when(citaRepository.findById(id)).thenReturn(expectedCita);

        // Act
        Cita actualCita = citaService.obtenerCitaPorId(id);

        // Assert
        assertEquals(expectedCita, actualCita);
    }

    // Prueba exitosa para agendar una cita
    @Test
    public void testAgendarCitaSuccessful() {
        // Arrange
        Cita cita = new Cita();
        // Se asignan IDs válidos para doctor y paciente
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        Doctor doctor = new Doctor();
        Paciente paciente = new Paciente();

        when(entityManager.find(Doctor.class, 10L)).thenReturn(doctor);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(paciente);

        // Act
        citaService.agendarCita(cita);

        // Assert
        // Se verifica que se hayan asignado correctamente el doctor y paciente en la cita
        assertEquals(doctor, cita.getDoctor());
        assertEquals(paciente, cita.getPaciente());
        // Se verifica que se haya llamado al método persist del repositorio
        verify(citaRepository, times(1)).persist(cita);
    }

    // Prueba cuando el ID del doctor es nulo
    @Test
    public void testAgendarCitaDoctorIdNull() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdDoctor(null); // ID de doctor inválido
        cita.setIdPaciente(20L);

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertTrue(exception.getMessage().contains("El ID del doctor es obligatorio"));
    }

    // Prueba cuando el ID del paciente es nulo
    @Test
    public void testAgendarCitaPacienteIdNull() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(null); // ID de paciente inválido

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertTrue(exception.getMessage().contains("El ID del paciente es obligatorio"));
    }

    // Prueba cuando no se encuentra el doctor
    @Test
    public void testAgendarCitaDoctorNotFound() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(null);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(new Paciente());

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertTrue(exception.getMessage().contains("No se encontró el doctor con ID"));
    }

    // Prueba cuando no se encuentra el paciente
    @Test
    public void testAgendarCitaPacienteNotFound() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(new Doctor());
        when(entityManager.find(Paciente.class, 20L)).thenReturn(null);

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertTrue(exception.getMessage().contains("No se encontró el paciente con ID"));
    }

    // Prueba para cancelar una cita exitosamente
    @Test
    public void testCancelarCitaSuccessful() {
        // Arrange
        Long id = 1L;
        Cita cita = new Cita();
        when(citaRepository.findById(id)).thenReturn(cita);

        // Act
        citaService.cancelarCita(id);

        // Assert
        verify(citaRepository, times(1)).delete(cita);
    }

    // Prueba para cancelar una cita cuando no se encuentra
    @Test
    public void testCancelarCitaNotFound() {
        // Arrange
        Long id = 1L;
        when(citaRepository.findById(id)).thenReturn(null);

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.cancelarCita(id);
        });
        assertTrue(exception.getMessage().contains("No se encontró la cita con ID"));
    }

    // Prueba para actualizar una cita exitosamente
    @Test
    public void testActualizarCitaSuccessful() {
        // Arrange
        Long id = 1L;
        Cita citaExistente = new Cita();
        // Se asignan valores iniciales
        citaExistente.setEstado(EstadoCita.PENDIENTE);
        citaExistente.setDiagnostico("Diagnóstico antiguo");
        citaExistente.setResultados("Resultados antiguos");

        Cita citaActualizada = new Cita();
        // Valores nuevos para actualizar
        citaActualizada.setEstado(EstadoCita.CONFIRMADA);
        citaActualizada.setDiagnostico("Nuevo diagnóstico");
        citaActualizada.setResultados("Nuevos resultados");

        when(citaRepository.findById(id)).thenReturn(citaExistente);

        // Act
        citaService.actualizarCita(id, citaActualizada);

        // Assert
        assertEquals(EstadoCita.CONFIRMADA, citaExistente.getEstado());

        assertEquals("Nuevo diagnóstico", citaExistente.getDiagnostico());
        assertEquals("Nuevos resultados", citaExistente.getResultados());
    }

    // Prueba para actualizar una cita cuando no se encuentra
    @Test
    public void testActualizarCitaNotFound() {
        // Arrange
        Long id = 1L;
        Cita citaActualizada = new Cita();
        when(citaRepository.findById(id)).thenReturn(null);

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.actualizarCita(id, citaActualizada);
        });
        assertTrue(exception.getMessage().contains("No se encontró la cita con ID"));
    }
}
