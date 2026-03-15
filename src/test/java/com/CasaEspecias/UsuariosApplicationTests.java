package com.CasaEspecias;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Aquí indicamos explícitamente la clase principal de tu app
@SpringBootTest(classes = CasaEspeciasApplication.class)
class UsuariosApplicationTests {

	@Test
	void contextLoads() {
		// Este test solo verifica que Spring Boot arranque correctamente
	}

}