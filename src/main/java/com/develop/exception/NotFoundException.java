package com.develop.exception;

/**
 * Clase para Excepcioens Personalizadas
 */
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
