package com.develop.model;

import javax.persistence.*;

import lombok.Data;

/*
 * Clase de Entidad JPA para mapeo de Objetos
 */
@Entity
@Table(name = "access_points")
public class AccessPoint {
	
	@Id
	private String id;
	private String programa;
	
	@Column(name = "fecha_instalacion")
	private String fechaInstalacion;
	private Double latitud;
	private Double longitud;
	private String colonia;
	private String alcaldia;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the programa
	 */
	public String getPrograma() {
		return programa;
	}
	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	/**
	 * @return the fechaInstalacion
	 */
	public String getFechaInstalacion() {
		return fechaInstalacion;
	}
	/**
	 * @param fechaInstalacion the fechaInstalacion to set
	 */
	public void setFechaInstalacion(String fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}
	/**
	 * @return the latitud
	 */
	public Double getLatitud() {
		return latitud;
	}
	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	/**
	 * @return the longitud
	 */
	public Double getLongitud() {
		return longitud;
	}
	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	/**
	 * @return the colonia
	 */
	public String getColonia() {
		return colonia;
	}
	/**
	 * @param colonia the colonia to set
	 */
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	/**
	 * @return the alcaldia
	 */
	public String getAlcaldia() {
		return alcaldia;
	}
	/**
	 * @param alcaldia the alcaldia to set
	 */
	public void setAlcaldia(String alcaldia) {
		this.alcaldia = alcaldia;
	}
	
}
