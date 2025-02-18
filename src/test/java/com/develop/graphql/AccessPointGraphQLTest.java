package com.develop.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.develop.config.ConfigProperties;
import com.develop.model.AccessPoint;
import com.develop.model.AccessPointPage;
import com.develop.service.AccessPointService;

@ExtendWith(MockitoExtension.class)
public class AccessPointGraphQLTest {
	
	@Mock
	private Logger logger = LoggerFactory.getLogger(AccessPointGraphQLTest.class);

	@InjectMocks
    private AccessPointGraphQL accessPointGraphQL;

    @Mock
    private AccessPointService accessPointService;

    @Mock
    private ConfigProperties configProperties;

    private AccessPoint accessPoint;
    private Page<AccessPoint> pageResult;
    private List<AccessPoint> accessPointList;
    
    @BeforeEach
    void setUp() {
        accessPoint = new AccessPoint();
        accessPoint.setId("800738_P_01");
        accessPoint.setColonia("CENTRO");
        accessPointList = Collections.singletonList(accessPoint);
        pageResult = new PageImpl<>(accessPointList, PageRequest.of(0, 10), accessPointList.size());
    }
    
    @Test
    void getWifiAccessTest() {
    	logger.info("Iniciando Prueba getWifiAccessTest");
        when(accessPointService.getAllAccessPoints(0, 10)).thenReturn(accessPointList);
        List<AccessPoint> result = accessPointGraphQL.getWifiAccess(0, 10);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("800738_P_01");
        verify(accessPointService, times(1)).getAllAccessPoints(0, 10);
    }
    
    @Test
    void getWifiAccessByIdTest() {
    	logger.info("Iniciando Prueba getWifiAccessByIdTest");
        when(accessPointService.getAccessById("800738_P_01")).thenReturn(accessPoint);
        AccessPoint result = accessPointGraphQL.getWifiAccessById("800738_P_01");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("800738_P_01");
        verify(accessPointService, times(1)).getAccessById("800738_P_01");
    }
    
    @Test
    void testGetWifiAccessByColonia() {
    	logger.info("Iniciando Prueba testGetWifiAccessByColonia");
    	when(configProperties.getDefaultPage()).thenReturn(0);
        when(configProperties.getDefaultSize()).thenReturn(10);
        when(accessPointService.getAccessByColonia("CENTRO", 0, 10)).thenReturn(pageResult);
        AccessPointPage result = accessPointGraphQL.getWifiAccessByColonia("CENTRO", null, null);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(configProperties, times(1)).getDefaultPage();
        verify(configProperties, times(1)).getDefaultSize();
        verify(accessPointService, times(1)).getAccessByColonia("CENTRO", 0, 10);
    }

    @Test
    void testGetWifiAccessByColoniaWithEmptyColonia() {
    	logger.info("Iniciando Prueba testGetWifiAccessByColoniaWithEmptyColonia");
    	//when(configProperties.getDefaultPage()).thenReturn(0);
        //when(configProperties.getDefaultSize()).thenReturn(10);
        AccessPointPage result = accessPointGraphQL.getWifiAccessByColonia("", 0, 10);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        //verify(logger, times(1)).error("Colonia no puede venir vacio");
    }

    @Test
    void testGetWifiAccessByProximity() {
    	logger.info("Iniciando Prueba testGetWifiAccessByProximity");
    	when(configProperties.getDefaultPage()).thenReturn(0);
        when(configProperties.getDefaultSize()).thenReturn(10);
        when(accessPointService.getAccessByProximity(19.4326, -99.1332, 0, 10)).thenReturn(pageResult);
        AccessPointPage result = accessPointGraphQL.getWifiAccessByProximity(19.4326, -99.1332, null, null);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(configProperties, times(1)).getDefaultPage();
        verify(configProperties, times(1)).getDefaultSize();
        verify(accessPointService, times(1)).getAccessByProximity(19.4326, -99.1332, 0, 10);
    }
}
