package com.develop.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.develop.model.AccessPointPageTest;

@SpringBootTest(classes = ConfigProperties.class)
@TestPropertySource(properties = {
    "app.files.csv-path=/data/files/access_points.csv",
    "app.files.graphql-path=/data/graphql/schema.graphqls",
    "app.files.sqlfile-path=/data/sql/init.sql",
    "pagination.default-page=0",
    "pagination.default-size=20"
})
public class ConfigPropertiesTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigPropertiesTest.class);
	
	@Autowired
    private ConfigProperties configProperties;
	
	@Test
    void ConfigPropertiesLoadedTest() {
		logger.info("Iniciando Prueba ConfigPropertiesLoadedTest");
        assertThat(configProperties.getCSVFilePath()).isEqualTo("/data/files/access_points.csv");
        assertThat(configProperties.getGraphQLFilePath()).isEqualTo("/data/graphql/schema.graphqls");
        assertThat(configProperties.getSQLFilePath()).isEqualTo("/data/sql/init.sql");
        assertThat(configProperties.getDefaultPage()).isEqualTo(0);
        assertThat(configProperties.getDefaultSize()).isEqualTo(20);
    }

}
