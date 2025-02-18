package com.develop.app;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import lombok.var;


public class DataPipeLineApplicationTest {
	
	@Test
    void testMainMethod() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            String[] args = {};
            DataPipeLineApplication.main(args);
            mockedSpringApplication.verify(() -> SpringApplication.
            		run(DataPipeLineApplication.class, args));
        }
    }

}
