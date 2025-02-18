package com.develop.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class AccessPointPageTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessPointPageTest.class);
	
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
    void testAccessPointPageConstructor() {
		long totalElements = 20L;
        int totalPages = 4;
        int size = 5;
        int number = 1;
		logger.info("Iniciando Prueba testAccessPointPageConstructor");
		List<AccessPoint> content = Arrays.asList(accessPoint, accessPoint);
		
		AccessPointPage accessPointPage = new 
				AccessPointPage(content, totalElements, totalPages, size, number);
		assertThat(accessPointPage.getContent()).isEqualTo(content);
        assertThat(accessPointPage.getTotalElements()).isEqualTo(totalElements);
        assertThat(accessPointPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(accessPointPage.getSize()).isEqualTo(size);
        assertThat(accessPointPage.getNumber()).isEqualTo(number);
	}
}
