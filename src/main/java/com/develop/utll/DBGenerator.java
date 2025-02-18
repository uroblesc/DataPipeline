package com.develop.utll;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import com.develop.config.ConfigProperties;

/*
 * Clase Generadora de Base de Datos
 */
@Component
public class DBGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(DBGenerator.class);

	private final ConfigProperties configProperties;
	
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;
    
    public DBGenerator(ConfigProperties configProperties, JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
        this.configProperties = configProperties;
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void executeStatament() {
    	Connection connection = null;
    	// Verificamos que la ruta del archivo sql sea correcta
    	logger.info("Ejecutando script SQL desde: {}", configProperties.getSQLFilePath());
        try {
        	// Consulta para Validar la existencia de la Tabla previo a generarla
        	Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM information_schema.tables "
        			+ "WHERE table_schema = 'wifi_access_points' AND table_name = 'access_points'", Integer.class);
        	if (count != null && count > 0) {
                logger.info("La tabla 'access_points' ya existe.");
                return;
            }
        	// Genera la base de datos a partir del archivo sql dentro de una ruta especifica
            Resource resource = resourceLoader.getResource("classpath:" + configProperties.getSQLFilePath());
            InputStream inputStream = resource.getInputStream();
            if(inputStream == null) {
            	throw new RuntimeException("No se pudo cargar el archivo");
            }
            connection = jdbcTemplate.getDataSource().getConnection();
            ScriptUtils.executeSqlScript(connection, resource);
            logger.info("Script SQL ejecutado correctamente.");
            
        } catch (Exception e) {
            logger.error("Error al ejecutar el script SQL: {}", e.getMessage(), e);
        } finally {
        	if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Error al cerrar la conexión: {}", ex.getMessage(), ex);
                }
            }
        }
    }

}
