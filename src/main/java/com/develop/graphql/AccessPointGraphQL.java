package com.develop.graphql;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.develop.config.ConfigProperties;
import com.develop.model.AccessPoint;
import com.develop.model.AccessPointPage;
import com.develop.service.AccessPointService;

/**
 * Clase de Conexion GraphQL a Base de Datos
 * Se encarga de traducir las consultas hechas desde GraphQL
 * a un gestor de base de datos.
 */
@Controller
public class AccessPointGraphQL {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessPointGraphQL.class);
	
	@Autowired
	AccessPointService accessPointService;
	@Autowired
	ConfigProperties configProperties;
	
	/*
	 * Metodo para Obtener una lista de Puntos de Acceso
	 * de acuerdo a paginacion y tamaño
	 */
	@QueryMapping
	public List<AccessPoint> getWifiAccess(@Argument Integer page, @Argument Integer size){
		logger.info("Busqueda de Puntos de Acceso: Page [{}] & Size: [{}]", page, size);
		return accessPointService.getAllAccessPoints(page, size);
	}
	
	/*
	 * Obtencion de Punto de Acceso mediante id
	 */
	@QueryMapping
	public AccessPoint getWifiAccessById(@Argument String id) {
		return accessPointService.getAccessById(id);
	}
	
	/*
	 * Obtencion de Punto de Acceso por Colonia
	 * El tamaño y no. de pagina son parametros opcionales
	 */
	@QueryMapping
	public AccessPointPage getWifiAccessByColonia(@Argument String colonia, 
			@Argument Integer page, @Argument Integer size) {
		int finalPage = Objects.nonNull(page) ? page : configProperties.getDefaultPage();
	    int finalSize = Objects.nonNull(size) ? size : configProperties.getDefaultSize();
		if(colonia == null ||  colonia.isEmpty()) {
			logger.error("Colonia no puede venir vacio");
			return new AccessPointPage(Collections.emptyList(), 0L, 0, 0, 0);
		}
		logger.info("Busqueda getWifiAccessByColonia: " + colonia);
		Page<AccessPoint> pageResult = accessPointService.
				getAccessByColonia(colonia, finalPage, finalSize);
		return new AccessPointPage(pageResult.getContent(),
		        pageResult.getTotalElements(),
		        pageResult.getTotalPages(),
		        pageResult.getSize(),
		        pageResult.getNumber());
	}
	
	/*
	 * Obtencion de Punto de Acceso por Proximidad
	 * El tamaño y no. de pagina son parametros opcionales
	 */
	@QueryMapping
	public AccessPointPage getWifiAccessByProximity(@Argument Double lat, 
			@Argument Double lon, @Argument Integer page, @Argument Integer size) {
		int finalPage = (page != null && page >= 0) ? page : configProperties.getDefaultPage();
		int finalSize = (size != null && size > 0) ? size : configProperties.getDefaultSize();
		if(lat == null || lon == null) {
			logger.error("Las coordenadas de proximidad no pueden venir vacias");
			return new AccessPointPage(Collections.emptyList(), 0L, 0, 0, 0);
		}
		Page<AccessPoint> pageResult = accessPointService.
				getAccessByProximity(lat, lon, finalPage, finalSize);
		return new AccessPointPage(pageResult.getContent(), 
				pageResult.getTotalElements(),
		        pageResult.getTotalPages(),
		        pageResult.getSize(),
		        pageResult.getNumber());
	}
	
}
