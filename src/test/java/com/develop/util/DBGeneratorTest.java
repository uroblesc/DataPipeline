package com.develop.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import com.develop.config.ConfigProperties;
import com.develop.utll.DBGenerator;

@ExtendWith(MockitoExtension.class)
public class DBGeneratorTest {
	
	@Mock
    private ConfigProperties configProperties;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @Mock
    private InputStream inputStream;

    @Mock
    private Connection connection;

    @Mock
    private DataSource dataSource;

    private DBGenerator dbGenerator;
	
	@BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dbGenerator = new DBGenerator(configProperties, jdbcTemplate, resourceLoader);
        when(configProperties.getSQLFilePath()).thenReturn("2024-06-30-puntos_de_acceso_wifi.sql");
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(resourceLoader.getResource("classpath:2024-06-30-puntos_de_acceso_wifi.sql")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(inputStream);
    }

    @Test
    void testExecuteStatementWhenTableDoesNotExist() throws Exception {
    	dbGenerator.executeStatament();
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
        verify(resourceLoader, times(1)).getResource("classpath:2024-06-30-puntos_de_acceso_wifi.sql");
        verify(jdbcTemplate, times(1)).getDataSource();
        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).close();
    }

}
