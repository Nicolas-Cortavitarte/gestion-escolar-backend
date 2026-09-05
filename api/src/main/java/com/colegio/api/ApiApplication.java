package com.colegio.api;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ApiApplication {

	// Zona Horaria para cualquier OffsetDateTime.now()
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
