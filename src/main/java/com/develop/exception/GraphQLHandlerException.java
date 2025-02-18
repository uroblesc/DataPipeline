package com.develop.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.MethodArgumentBuilder;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Clase de Excepciones Controladas
 * Esta diseñada para controlar errores propensos a presentarse durante
 * la ejecucion de la aplicación.
 */
@Component
public class GraphQLHandlerException  {

    @ExceptionHandler(RuntimeException.class)
    public GraphQLError graphQLError(RuntimeException e) {
    	return GraphqlErrorBuilder.newError()
    			.message("Error de la Aplicacion: " + e.getMessage())
    			.errorType(ErrorType.INTERNAL_ERROR).build();
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<GraphQLError> handleValidationException(MethodArgumentNotValidException e) {
    	return e.getBindingResult().getFieldErrors().stream()
                .map(error -> GraphqlErrorBuilder.newError()
                        .message("Error en campo '" + error.getField() + " : " + error.getDefaultMessage())
                        .errorType(ErrorType.BAD_REQUEST)
                        .build())
                .collect(Collectors.toList());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleIllegalArgumentException(IllegalArgumentException e) {
        return GraphqlErrorBuilder.newError()
                .message("Error de validación: " + e.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }
    
}
