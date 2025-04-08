package com.unis.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class AuthIntegrationTest {

    @Test
    public void testRegistroUsuario_exitoso() {
        String jsonUsuario = """
            {
              "nombreUsuario": "Juan Test",
              "correo": "testjuan@email.com",
              "contrasena": "123456",
              "rol": null
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(jsonUsuario)
        .when()
            .post("/api/usuarios/registro")
        .then()
            .statusCode(201) // Asegúrate que tu backend responde 201. Si no, pon 200
            .body("mensaje", equalTo("Usuario registrado con éxito")); // Cambia si tu backend responde diferente
    }
}
