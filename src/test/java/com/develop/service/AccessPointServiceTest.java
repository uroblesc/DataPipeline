package com.develop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.develop.config.ConfigProperties;
import com.develop.exception.NotFoundException;
import com.develop.model.AccessPoint;
import com.develop.repository.AccessPointRepository;

@ExtendWith(MockitoExtension.class)
class AccessPointServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessPointServiceTest.class);
	
	@Mock
    private AccessPointRepository accessPointRepository;
	
	@InjectMocks
    private AccessPointService accessPointService;
	
	@Mock
    private ConfigProperties configProperties;
	
	private AccessPoint accessPoint;
	
	@BeforeEach
	void setup() {
		accessPoint = new AccessPoint();
		accessPoint.setId("'AICMT1-GW001'");
		accessPoint.setPrograma("AICM_T1");
		accessPoint.setFechaInstalacion("NA");
		accessPoint.setLatitud(19.432707);
		accessPoint.setLongitud(-99.086743);
		accessPoint.setColonia("PEÑON DE LOS BAÑOS");
		accessPoint.setAlcaldia("Venustiano Carranza");
	}
	
	@Test
    void testInitMethod() {
		accessPointService.init();
		logger.info("Mostrando los elementos del Objeto AccessPoint");
		logger.info("Id: {}", accessPoint.getId());
		logger.info("Programa: {}", accessPoint.getPrograma());
		logger.info("Fecha_Instalacion: {}", accessPoint.getFechaInstalacion());
		logger.info("Latitud: {}", accessPoint.getLatitud());
		logger.info("Longitud: {}", accessPoint.getLongitud());
		logger.info("Alcaldia: {}", accessPoint.getAlcaldia());
		logger.info("Colonia: {}", accessPoint.getColonia());
        //verify(logger).info("AccessPointRepository fue inyectado correctamente: {}", true);
    }
	
	@Test
    void getWifiAccessTest() {
		logger.info("Iniciando Prueba getWifiAccessTest");
        List<AccessPoint> accessPoints = Collections.singletonList(accessPoint);
        Page<AccessPoint> page = new PageImpl<>(accessPoints);
        logger.debug("Mockeando findWifiAccess()...");
        when(accessPointRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<AccessPoint> result = accessPointService.getAllAccessPoints(0, 10);
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(accessPoint);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(accessPointRepository, times(1)).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable).isEqualTo(PageRequest.of(0, 10));
    }
	
	@Test
    void getAccessByColoniaTest() {
		logger.info("Iniciando Prueba getAccessByIdTest");
	    List<AccessPoint> accessPoints = Collections.singletonList(accessPoint);
	    Page<AccessPoint> page = new PageImpl<>(accessPoints);
	    logger.debug("Mockeando findByColonia()...");
	    Pageable pageable = PageRequest.of(0, 10);
	    when(accessPointRepository.findByColonia(eq(accessPoint.getColonia()), eq(pageable))).thenReturn(page);
	    logger.info("Llamando a accessPointService.getAccessByColonia...");
	    Page<AccessPoint> result = accessPointService.getAccessByColonia(accessPoint.getColonia(), 0, 10);
	    assertThat(result).hasSize(1);
	    assertThat(result).containsExactly(accessPoint);
	    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
	    verify(accessPointRepository, times(1)).findByColonia(eq(accessPoint.getColonia()), pageableCaptor.capture());
	    logger.info("Pageable capturado: {}", pageableCaptor.getValue());
	    assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
	}
	
	@Test
    void getAccessByColoniaPageableNullTest() {
		Integer pageNumber = null;
	    Integer pageSize = null;
	    int defaultPage = configProperties.getDefaultPage();
	    int defaultSize = Math.max(1, configProperties.getDefaultSize());
	    logger.info("Iniciando Prueba getAccessByColoniaPageableNullTest");
	    List<AccessPoint> accessPoints = Collections.singletonList(accessPoint);
	    Page<AccessPoint> page = new PageImpl<>(accessPoints);
	    logger.debug("Mockeando findByColonia() con valores por defecto...");
	    Pageable defaultPageable = PageRequest.of(defaultPage, defaultSize);
	    when(accessPointRepository.findByColonia(eq(accessPoint.
	    		getColonia()), eq(defaultPageable))).thenReturn(page);
	    Page<AccessPoint> result = accessPointService.
	    		getAccessByColonia(accessPoint.getColonia(), pageNumber, pageSize);
	    assertThat(result).hasSize(1);
	    assertThat(result).containsExactly(accessPoint);
	    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
	    verify(accessPointRepository, times(1)).findByColonia(eq(accessPoint.getColonia()), pageableCaptor.capture());
	    assertThat(pageableCaptor.getValue()).isEqualTo(defaultPageable);
	}
	
	@Test
    void getAccessByIdTest() {
		logger.info("Iniciando Prueba getAccessByIdTest");
	    logger.debug("Mockeando findById()...");
	    when(accessPointRepository.findById(accessPoint.getId())).thenReturn(Optional.of(accessPoint));
	    logger.info("Llamando a accessPointService.getAccessById...");
	    AccessPoint result = accessPointService.getAccessById(accessPoint.getId());
	    assertThat(result).isNotNull();
	    assertThat(result.getId()).isEqualTo(accessPoint.getId());
	    verify(accessPointRepository, times(1)).findById(accessPoint.getId());
	}
	
	@Test
    void getAccessByProximityTest() {
		double lat = 19.4326;
	    double lon = -99.1332;
		logger.info("Iniciando Prueba getAccessByProximityTest");
	    List<AccessPoint> accessPoints = Collections.singletonList(accessPoint);
	    Page<AccessPoint> page = new PageImpl<>(accessPoints);
	    logger.debug("Mockeando findByProximity()...");
	    Pageable pageable = PageRequest.of(0, 10);
	    when(accessPointRepository.findByProximity(eq(lat), eq(lon), eq(pageable))).thenReturn(page);
	    logger.info("Llamando a accessPointService.getAccessByproximity...");
	    Page<AccessPoint> result = accessPointService.getAccessByProximity(lat, lon, 0, 10);
	    assertThat(result).hasSize(1);
	    assertThat(result).containsExactly(accessPoint);
	    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
	    verify(accessPointRepository, times(1)).findByProximity(eq(lat), eq(lon), pageableCaptor.capture());
	    logger.info("Pageable capturado: {}", pageableCaptor.getValue());
	    assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
	}
	
	@Test
	void getAccessByProximityDefaultPaginationTest() {
		Double lat = 19.4326;
		Double lon = -99.1332;
		Integer page = null;
		Integer size = 0;
		int defaultPage = 0;
		int defaultSize = 10;

		logger.info("Iniciando Prueba getAccessByProximity_DefaultPaginationTest");
		when(configProperties.getDefaultPage()).thenReturn(defaultPage);
		when(configProperties.getDefaultSize()).thenReturn(defaultSize);
		Pageable expectedPageable = PageRequest.of(defaultPage, Math.max(1, defaultSize));
		List<AccessPoint> accessPoints = Collections.singletonList(accessPoint);
		Page<AccessPoint> pageResult = new PageImpl<>(accessPoints);
		when(accessPointRepository.findByProximity(lat, lon, expectedPageable)).thenReturn(pageResult);
		logger.info("Llamando a accessPointService.getAccessByProximity con valores nulos o inválidos...");
		Page<AccessPoint> result = accessPointService.getAccessByProximity(lat, lon, page, size);
		assertThat(result).hasSize(1);
		assertThat(result).containsExactly(accessPoint);
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(accessPointRepository, times(1)).findByProximity(eq(lat), eq(lon), pageableCaptor.capture());
		logger.info("Pageable capturado: {}", pageableCaptor.getValue());
		assertThat(pageableCaptor.getValue()).isEqualTo(expectedPageable);
	}
	
	@Test
	void getAccessByProximity_NotFoundExceptionTest() {
		Double lat = 19.4326;
	    Double lon = -99.1332;
	    Integer page = 1;
	    Integer size = 5;
	    Pageable pageable = PageRequest.of(page, size);
	    logger.info("Iniciando Prueba getAccessByProximity_NotFoundExceptionTest");
	    when(accessPointRepository.findByProximity(lat, lon, pageable)).thenReturn(Page.empty());
	    assertThatThrownBy(() -> accessPointService.getAccessByProximity(lat, lon, page, size))
	        .isInstanceOf(NotFoundException.class)
	        .hasMessageContaining("No se Encontraron Puntos de Acceso Cercanos a la Ubicacion [" + 
	        lat + "," + lon + "]");
	    verify(accessPointRepository, times(1)).findByProximity(eq(lat), eq(lon), eq(pageable));
	}
	
}
