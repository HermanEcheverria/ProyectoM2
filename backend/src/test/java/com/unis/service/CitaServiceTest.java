package com.unis.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.unis.model.Aseguradora;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.EstadoCita;
import com.unis.model.FichaTecnica;
import com.unis.model.Paciente;
import com.unis.model.PacienteFT;
import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.repository.CitaRepository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CitaServiceTest {

    @Mock CitaRepository citaRepository;
    @Mock EntityManager entityManager;
    @Mock DoctorService doctorService;

    @InjectMocks CitaService citaService;

    private AutoCloseable mocks;

    // --- HTTP server embebido para cubrir enviarResultadosAAseguradora ---
    static HttpServer server;
    static AtomicInteger hits;

    @BeforeAll
    static void startServer() throws Exception {
        hits = new AtomicInteger(0);
        server = HttpServer.create(new InetSocketAddress(5001), 0);
        server.createContext("/api/resultados", new HttpHandler() {
            @Override public void handle(HttpExchange exchange) throws IOException {
                hits.incrementAndGet();
                byte[] resp = "OK".getBytes();
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(resp); }
            }
        });
        server.start();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) server.stop(0);
    }

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    // ------------------ TUS PRUEBAS BÁSICAS (mantengo equivalentes) ------------------

    @Test
    public void testObtenerCitas() {
        Cita c1 = new Cita(); Cita c2 = new Cita();
        List<Cita> esperadas = Arrays.asList(c1, c2);
        when(citaRepository.listAll()).thenReturn(esperadas);

        List<Cita> reales = citaService.obtenerCitas();
        assertEquals(esperadas, reales);
    }

    @Test
    public void testObtenerCitaPorId() {
        Long id = 1L;
        Cita c = new Cita();
        when(citaRepository.findById(id)).thenReturn(c);
        assertEquals(c, citaService.obtenerCitaPorId(id));
    }

    @Test
    public void testAgendarCitaSuccessful() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        Doctor doctor = new Doctor();
        Paciente paciente = new Paciente();

        when(entityManager.find(Doctor.class, 10L)).thenReturn(doctor);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(paciente);

        citaService.agendarCita(cita);

        assertEquals(doctor, cita.getDoctor());
        assertEquals(paciente, cita.getPaciente());
        verify(citaRepository, times(1)).persist(cita);
    }

    @Test
    public void testAgendarCitaDoctorIdNull() {
        Cita cita = new Cita();
        cita.setIdDoctor(null);
        cita.setIdPaciente(20L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.agendarCita(cita));
        assertEquals("El ID del doctor y paciente son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAgendarCitaPacienteIdNull() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.agendarCita(cita));
        assertEquals("El ID del doctor y paciente son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAgendarCitaDoctorNotFound() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(null);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(new Paciente());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.agendarCita(cita));
        assertEquals("Doctor o paciente no encontrados.", ex.getMessage());
    }

    @Test
    public void testAgendarCitaPacienteNotFound() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(new Doctor());
        when(entityManager.find(Paciente.class, 20L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.agendarCita(cita));
        assertEquals("Doctor o paciente no encontrados.", ex.getMessage());
    }

    @Test
    public void testCancelarCitaSuccessful() {
        Long id = 1L;
        Cita cita = new Cita();
        when(citaRepository.findById(id)).thenReturn(cita);

        citaService.cancelarCita(id);

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
    }

    @Test
    public void testCancelarCitaNotFound() {
        when(citaRepository.findById(1L)).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.cancelarCita(1L));
        assertEquals("Cita no encontrada", ex.getMessage());
    }

    @Test
    public void testActualizarCitaSuccessful() {
        Long id = 1L;
        Cita existente = new Cita();
        existente.setEstado(EstadoCita.PENDIENTE);
        existente.setDiagnostico("Old Dx");
        existente.setResultados("Old Rs");

        Cita actualizada = new Cita();
        actualizada.setEstado(EstadoCita.CONFIRMADA);
        actualizada.setDiagnostico("New Dx");
        actualizada.setResultados("New Rs");

        when(citaRepository.findById(id)).thenReturn(existente);

        citaService.actualizarCita(id, actualizada);

        assertEquals(EstadoCita.CONFIRMADA, existente.getEstado());
        assertEquals("New Dx", existente.getDiagnostico());
        assertEquals("New Rs", existente.getResultados());
    }

    @Test
    public void testActualizarCitaNotFound() {
        when(citaRepository.findById(1L)).thenReturn(null);
        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> citaService.actualizarCita(1L, new Cita()));
        assertEquals("Cita no encontrada", ex.getMessage());
    }

    // ------------------ NUEVAS PRUEBAS PARA LO QUE FALTABA ------------------

    // buscarDoctorPorId usa DoctorService.getDoctorById(...)
    @Test
    public void testBuscarDoctorPorId_found() {
        Doctor d = new Doctor();
        when(doctorService.getDoctorById(5L)).thenReturn(Optional.of(d));
        assertSame(d, citaService.buscarDoctorPorId(5L));
    }

    @Test
    public void testBuscarDoctorPorId_notFound() {
        when(doctorService.getDoctorById(99L)).thenReturn(Optional.empty());
        assertNull(citaService.buscarDoctorPorId(99L));
    }

    // procesarCita
    @Test
    public void testProcesarCita_ok() {
        Cita c = new Cita();
        when(citaRepository.findById(10L)).thenReturn(c);
        citaService.procesarCita(10L);
        assertEquals(EstadoCita.FINALIZADA, c.getEstado());
    }

    @Test
    public void testProcesarCita_noExiste() {
        when(citaRepository.findById(10L)).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> citaService.procesarCita(10L));
        assertEquals("Cita no encontrada", ex.getMessage());
    }

    // reasignarDoctor
    @Test
    public void testReasignarDoctor_ok() {
        Cita c = new Cita();
        Doctor nuevo = new Doctor();
        when(citaRepository.findById(7L)).thenReturn(c);

        citaService.reasignarDoctor(7L, nuevo);

        assertSame(nuevo, c.getDoctor());
    }

    @Test
    public void testReasignarDoctor_invalido() {
        when(citaRepository.findById(7L)).thenReturn(null);
        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> citaService.reasignarDoctor(7L, new Doctor()));
        assertEquals("Cita o doctor no válidos", ex.getMessage());
    }

    // procesarCitaYEnviarResultados  -> cubre la lambda de enviarResultadosAAseguradora
    @Test
    public void testProcesarCitaYEnviarResultados_ok_conHttpServer() throws Exception {
        // Arrange: cita completa con datos necesarios para construir el JSON
        Cita c = new Cita();
        c.setIdCita(200L);                 // id interno (por si tu entidad lo usa)
        c.setIdCita(200L);             // el método usa getIdCita() para el JSON
        c.setFecha(LocalDate.now());

        // Paciente + Usuario
        Usuario uPac = new Usuario();
        uPac.setNombreUsuario("Juan");
        Paciente pac = new Paciente();
        pac.setDocumento("DPI-123");
        pac.setApellido("Pérez");
        pac.setUsuario(uPac);
        c.setPaciente(pac);

        // Doctor + Usuario
        Usuario uDoc = new Usuario();
        uDoc.setNombreUsuario("Dra. López");
        Doctor doc = new Doctor();
        doc.setUsuario(uDoc);
        c.setDoctor(doc);

        when(citaRepository.findById(200L)).thenReturn(c);

        int before = hits.get();

        // Act: dispara procesamiento (pone FINALIZADA) y envío HTTP async
        citaService.procesarCitaYEnviarResultados(200L, "Dx final", "Resultados finales");

        // Assert básicos sobre la cita
        assertEquals(EstadoCita.FINALIZADA, c.getEstado());
        assertEquals("Dx final", c.getDiagnostico());
        assertEquals("Resultados finales", c.getResultados());

        // La llamada HTTP es async; damos un margen corto para que complete
        Thread.sleep(200); // 200ms suele bastar en local

        // Verificamos que el endpoint fue alcanzado al menos una vez
        assertTrue(hits.get() > before, "El endpoint /api/resultados no recibió la petición");
    }

    // crearCitaDesdeJson — caso: no existe paciente; crea usuario, paciente, ficha y (si aplica) aseguradora
    @Test
    public void testCrearCitaDesdeJson_creaTodoYPersiste() {
        // DTO de entrada
        JsonObject dto = Json.createObjectBuilder()
            .add("documento", "DPI-999")
            .add("nombre", "María")
            .add("apellido", "Gómez")
            .add("fecha", LocalDate.now().toString())
            .add("horaInicio", "09:00")
            .add("horaFin", "09:30")
            .add("motivo", "Consulta general")
            .add("numeroAutorizacion", "AUTO-1")
            .add("nombreAseguradora", "Seguros GT")
            .add("numeroAfiliacion", "AFI-001")
            .add("codigoSeguro", "COD-001")
            .add("carnetSeguro", "CAR-001")
            .build();

        // ---- Mock query Paciente (no existe) ----
        @SuppressWarnings("unchecked")
        TypedQuery<Paciente> qPac = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT p FROM Paciente p WHERE p.documento = :doc", Paciente.class))
            .thenReturn(qPac);
        when(qPac.setParameter(eq("doc"), any())).thenReturn(qPac);
        when(qPac.getResultStream()).thenReturn(Stream.empty());

        // Rol paciente id=4
        Rol rol = new Rol();
        rol.setId(4L);
        when(entityManager.find(Rol.class, 4L)).thenReturn(rol);

        // ---- Mock query PacienteFT (getSingleResult) ----
        @SuppressWarnings("unchecked")
        TypedQuery<PacienteFT> qPacFT = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT p FROM PacienteFT p WHERE p.documento = :doc", PacienteFT.class))
            .thenReturn(qPacFT);
        when(qPacFT.setParameter(eq("doc"), any())).thenReturn(qPacFT);
        PacienteFT pacFT = new PacienteFT();
        when(qPacFT.getSingleResult()).thenReturn(pacFT);

        // ---- Mock query Aseguradora (no existe -> crear) ----
        @SuppressWarnings("unchecked")
        TypedQuery<Aseguradora> qAseg = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT a FROM Aseguradora a WHERE UPPER(a.nombre) = :nombre", Aseguradora.class))
            .thenReturn(qAseg);
        when(qAseg.setParameter(eq("nombre"), any())).thenReturn(qAseg);
        when(qAseg.getResultStream()).thenReturn(Stream.empty());

        // Persistencias: simulamos que asigna IDs
        doAnswer(inv -> { Usuario u = inv.getArgument(0); u.setId(100L); return null; })
            .when(entityManager).persist(isA(Usuario.class));
        doAnswer(inv -> { Paciente p = inv.getArgument(0); p.setIdPaciente(200L); return null; })
            .when(entityManager).persist(isA(Paciente.class));
        doNothing().when(entityManager).persist(isA(FichaTecnica.class));
        doAnswer(inv -> { Aseguradora a = inv.getArgument(0); a.setId(300L); return null; })
            .when(entityManager).persist(isA(Aseguradora.class));

        // Act
        citaService.crearCitaDesdeJson(dto);

        // Assert: se persiste la cita
        verify(citaRepository, times(1)).persist(isA(Cita.class));
    }

    // crearCitaDesdeJson — caso: ya existe paciente, no viene aseguradora
    @Test
    public void testCrearCitaDesdeJson_conPacienteExistente_sinAseguradora() {
        JsonObject dto = Json.createObjectBuilder()
            .add("documento", "DPI-777")
            .add("nombre", "Luis")
            .add("apellido", "Ramírez")
            .add("fecha", LocalDate.now().toString())
            .add("horaInicio", "10:00")
            .add("horaFin", "10:20")
            .add("motivo", "Control")
            .build();

        // Mock: Paciente existente
        Paciente existente = new Paciente();
        existente.setIdPaciente(555L);

        @SuppressWarnings("unchecked")
        TypedQuery<Paciente> qPac = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT p FROM Paciente p WHERE p.documento = :doc", Paciente.class))
            .thenReturn(qPac);
        when(qPac.setParameter(eq("doc"), any())).thenReturn(qPac);
        when(qPac.getResultStream()).thenReturn(Stream.of(existente));

        // No debería consultar PacienteFT ni asegurarora en este camino
        citaService.crearCitaDesdeJson(dto);

        verify(citaRepository, times(1)).persist(isA(Cita.class));
        verify(entityManager, never()).persist(isA(Usuario.class));
        verify(entityManager, never()).persist(isA(FichaTecnica.class));
    }

    // crearCitaDesdeJson — error por documento faltante
    @Test
    public void testCrearCitaDesdeJson_faltaDocumento() {
        JsonObject dto = Json.createObjectBuilder()
            .add("nombre", "X")
            .add("fecha", LocalDate.now().toString())
            .add("horaInicio", "08:00")
            .add("horaFin", "08:15")
            .add("motivo", "Y")
            .build();

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> citaService.crearCitaDesdeJson(dto));
        assertEquals("El campo 'documento' es obligatorio", ex.getMessage());
    }
}
