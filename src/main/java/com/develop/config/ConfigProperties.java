package com.develop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Clase de Configuracion para Variables Dinamicas
 */
@Component
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
	
	@Value("${app.files.csv-path}")
    private String csvFilePath;

    @Value("${app.files.graphql-path}")
    private String graphQLPath;
    
    @Value("${app.files.sqlfile-path}")
    private String sqlFilePath;

    @Value("${pagination.default-page}")
    private int defaultPage;

    @Value("${pagination.default-size}")
    private int defaultSize;
	
	public String getCSVFilePath() {
		return csvFilePath;
	}
	
	public String getGraphQLFilePath() {
		return graphQLPath;
	}
	
	public int getDefaultPage() {
		return defaultPage;
	}
	
	public int getDefaultSize() {
		return defaultSize;
	}
	
	public String getSQLFilePath() {
		return sqlFilePath;
	}	

}
