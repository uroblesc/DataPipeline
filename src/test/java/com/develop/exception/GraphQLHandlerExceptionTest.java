package com.develop.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import graphql.GraphQLError;

public class GraphQLHandlerExceptionTest {
	
	private static final Logger logger = LoggerFactory.
			getLogger(GraphQLHandlerExceptionTest.class);
	
	private GraphQLHandlerException exceptionHandler;
	
	@BeforeEach
	void setUp() {
        exceptionHandler = new GraphQLHandlerException();
    }
	
	@Test
    void testGraphQLError() {
		logger.info("Iniciando Prueba testGraphQLError");
        RuntimeException runtimeException = new RuntimeException("Fallo interno");
        GraphQLError graphQLError = exceptionHandler.graphQLError(runtimeException);
        assertThat(graphQLError.getMessage()).isEqualTo(
        		"Error de la Aplicacion: Fallo interno");
        assertThat(graphQLError.getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR);
    }
	
	@Test
    void testHandleValidationException() {
		logger.info("Iniciando Prueba testHandleValidationException");
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("accessPoint", "nombre", "No puede estar vacío");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException validationException = 
            new MethodArgumentNotValidException(null, bindingResult);
        List<GraphQLError> graphQLErrors = exceptionHandler.handleValidationException(validationException);
        assertThat(graphQLErrors).hasSize(1);
        assertThat(graphQLErrors.get(0).getMessage()).isEqualTo("Error en campo 'nombre : No puede estar vacío");
        assertThat(graphQLErrors.get(0).getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        logger.info("Error: {}", graphQLErrors.get(0).getMessage());
        logger.info("Error: {}", graphQLErrors.get(0).getErrorType());
    }

    @Test
    void testHandleIllegalArgumentException() {
    	logger.info("Iniciando Prueba testHandleIllegalArgumentException");
        IllegalArgumentException exception = new IllegalArgumentException("Dato inválido");
        GraphQLError graphQLError = exceptionHandler.handleIllegalArgumentException(exception);
        assertThat(graphQLError.getMessage()).isEqualTo("Error de validación: Dato inválido");
        assertThat(graphQLError.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        logger.info("Error: {}", graphQLError.getMessage());
        logger.info("Error: {}", graphQLError.getErrorType());
    }

}
