package com.unis.resource;

import com.unis.model.FichaTecnica;
import com.unis.service.FichaTecnicaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FichaTecnicaResourceTest {

  @Mock
  FichaTecnicaService fichaTecnicaService;

  @InjectMocks
  FichaTecnicaResource resource; // cubre el constructor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void obtenerTodasLasFichas_debeDevolverListaDelServicio() {
    // arrange
    var f1 = new FichaTecnica();
    var f2 = new FichaTecnica();
    var esperada = List.of(f1, f2);
    when(fichaTecnicaService.getAllFichas()).thenReturn(esperada);

    // act
    var actual = resource.obtenerTodasLasFichas();

    // assert
    assertSame(esperada, actual, "La resource debe devolver exactamente la lista del servicio");
    verify(fichaTecnicaService, times(1)).getAllFichas();
    verifyNoMoreInteractions(fichaTecnicaService);
  }

  @Test
  void obtenerTodasLasFichas_casoListaVacia() {
    when(fichaTecnicaService.getAllFichas()).thenReturn(List.of());

    var actual = resource.obtenerTodasLasFichas();

    assertTrue(actual.isEmpty(), "Debe poder devolver lista vacía");
    verify(fichaTecnicaService).getAllFichas();
    verifyNoMoreInteractions(fichaTecnicaService);
  }

  @Test
  void registrarFicha_debeDelegarEnElServicio() {
    var ficha = new FichaTecnica();

    resource.registrarFicha(ficha);

    verify(fichaTecnicaService, times(1)).registrarFicha(same(ficha));
    verifyNoMoreInteractions(fichaTecnicaService);
  }
}
