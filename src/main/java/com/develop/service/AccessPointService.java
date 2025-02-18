package com.develop.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.develop.config.ConfigProperties;
import com.develop.exception.NotFoundException;
import com.develop.graphql.AccessPointGraphQL;
import com.develop.model.AccessPoint;
import com.develop.repository.AccessPointRepository;

/*
 * Clase de Negocio
 * Invocamos los metodos declarados de acuerdo al parametro solicitado
 */
@Service
public class AccessPointService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessPointGraphQL.class);
	
	@Autowired
	private AccessPointRepository accessPointRepository;
	
	@Autowired
	private ConfigProperties configProperties;
	
	@PostConstruct
	public void init() {
	    logger.info("AccessPointRepository fue inyectado correctamente: {}", accessPointRepository != null);
	}
	
	public List<AccessPoint> getAllAccessPoints(Integer page, Integer size){
		return accessPointRepository.findAll(PageRequest.of(page, size)).getContent();
	}
	
	public AccessPoint getAccessById(String id) {
		return accessPointRepository.findById(id).
				orElseThrow(() -> new NotFoundException("Punto Acceso Wifi con ID " + id + " No Encontrado."));
	}
	
	public Page<AccessPoint> getAccessByColonia(String colonia, Integer page, Integer size) {
		Pageable pageable;
		logger.info("Localizando Puntos de Acces por Colonia: {}", colonia);
		if (page == null || size == null || size < 1) { 
		    pageable = PageRequest.of(configProperties.getDefaultPage(), Math.max(1, configProperties.getDefaultSize()));
		} else {
		    pageable = PageRequest.of(page, size);
		}
		Page<AccessPoint> res = accessPointRepository.findByColonia(colonia, pageable);
		return res != null ? res: Page.empty();
	}
	
	public Page<AccessPoint> getAccessByProximity(Double lat, Double lon, Integer page, Integer size) {
		Pageable pageable;
		logger.info("Localizando Puntos de Acces por Proximidad: [{},{}}", lat, lon);
		if (page == null || size == null || size < 1) { 
		    pageable = PageRequest.of(configProperties.getDefaultPage(), Math.max(1, configProperties.getDefaultSize()));
		} else {
		    pageable = PageRequest.of(page, size);
		}
		Page<AccessPoint> res = accessPointRepository.findByProximity(lat, lon, pageable);
		if(res == null || res.isEmpty()) {
			throw new NotFoundException("No se Encontraron Puntos de Acceso "
					+ "Cercanos a la Ubicacion [" + lat + "," + lon + "]");
		}
		return res;
	}

}
