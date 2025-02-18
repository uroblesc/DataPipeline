package com.develop.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.develop.graphql.AccessPointGraphQL;

import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.GraphqlErrorException;
import graphql.schema.GraphQLSchema;

@ExtendWith(MockitoExtension.class)
public class GraphQLConfigTest {
	
	private static final Logger logger = LoggerFactory.getLogger(GraphQLConfigTest.class);
	
	@Mock
    private ConfigProperties configProperties;

    @Mock
    private AccessPointGraphQL accessPointGraphQL;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @InjectMocks
    private GraphQLConfig graphQLConfig;
    
    @BeforeEach
    void setUp() throws Exception {
        when(configProperties.getGraphQLFilePath()).thenReturn("classpath:schema.graphqls");
        when(resourceLoader.getResource("classpath:schema.graphqls")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        
    }
    
    @Test
    void GraphQLBeanCreationTest() throws Exception {
    	String schemaContent = "type Query { getWifiAccess(page: Int, size: Int): String }";
    	InputStream schemaStream = new ByteArrayInputStream(schemaContent.
        		getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(schemaStream);
    	logger.info("Iniciando Prueba GraphQLBeanCreationTest");
        GraphQL graphQL = graphQLConfig.graphQL();
        assertThat(graphQL).isNotNull();
        GraphQLSchema schema = graphQL.getGraphQLSchema();
        assertThat(schema).isNotNull();
        assertThat(schema.getQueryType().getFieldDefinition("getWifiAccess")).isNotNull();
        verify(resourceLoader, times(1)).getResource("classpath:schema.graphqls");
        verify(resource, times(1)).getInputStream();
    }
    
    @Test
    void FileNotFoundExceptionTest() throws IOException {
    	logger.info("Iniciando Prueba FileNotFoundExceptionTest");
        when(configProperties.getGraphQLFilePath()).thenReturn("classpath:schema.graphqls");
        when(resourceLoader.getResource("classpath:schema.graphqls")).thenReturn(resource);
        when(resource.exists()).thenReturn(false);
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            graphQLConfig.graphQL();
        });
        assertThat(exception.getMessage()).contains("No se encontró el archivo GraphQL");
        verify(resource, never()).getInputStream();
    }
    
    @Test
    void GraphqlErrorExceptionTest() throws Exception {
    	String schemaContent = "type Query { getWifiAccess(page: Int, size: Int): String }";
    	InputStream schemaStream = new ByteArrayInputStream(schemaContent.
        		getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(schemaStream);
        when(resourceLoader.getResource("classpath:schema.graphqls")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);

        when(resource.getInputStream()).thenThrow(new GraphqlErrorException.
        		Builder().message("Error en GraphQL").build());
        GraphQLException exception = assertThrows(GraphQLException.class, () -> {
            graphQLConfig.graphQL();
        });
        assertThat(exception.getMessage()).contains("Ocurrio un error en el proceso.");
    }

}
