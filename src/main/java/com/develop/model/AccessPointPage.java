package com.develop.model;

import java.util.List;

import lombok.Data;

/*
 * Mapeo de Campos y/o Objetos para una Lista Paginada
 */
@Data
public class AccessPointPage {
	
	private List<AccessPoint> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;

    public AccessPointPage(List<AccessPoint> content, long totalElements, int totalPages, int size, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

}
