package com.develop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.develop","com.develop.service","com.develop.graphql","com.develop.repository"})
@EntityScan(basePackages =  "com.develop.model")
@EnableJpaRepositories(basePackages = "com.develop.repository")
public class DataPipeLineApplication {
	
	public static void main(String[] args) {
		// Clase de Ejecucion Spring Boot
		SpringApplication.run(DataPipeLineApplication.class, args);
	}

}
