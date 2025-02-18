package com.develop.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.develop.graphql.AccessPointGraphQL;
import com.develop.utll.Const;

import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.GraphqlErrorException;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;

/*
 * Clase de Configuracion GraphQL
 */
@Configuration
public class GraphQLConfig {

	/*
	 * Se carga una instancia del objeto configProperties
	 */

	private static final Logger logger = LoggerFactory.getLogger(GraphQLConfig.class);

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private AccessPointGraphQL accessPointGraphQL;

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public GraphQL graphQL() throws IOException {
		/*
		 * Cargamos el schema graphqls desde una ruta dinamica, y se parsea a un objeto
		 * TypeDefinitionRegistry. Se genera la conexion a GraphQL mediante a Wiring.
		 * Generamos una instancia de GraphQL para proceder a realizar consultas.
		 */
		logger.info("Ruta GraphQL en configuración: {}", configProperties.getGraphQLFilePath());
		Resource resource = resourceLoader.getResource(configProperties.getGraphQLFilePath());
		if (!resource.exists()) {
			throw new FileNotFoundException(
					"No se encontró el archivo GraphQL: " + configProperties.getGraphQLFilePath());
		}
		try (InputStream inputStream = resource.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String schemaContent = bufferedReader.lines().reduce("", (acc, line) -> acc + line + "\n");
			TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaContent);
			RuntimeWiring runtimeWiring = buildRuntimeWiring();
			GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
			return GraphQL.newGraphQL(graphQLSchema).build();
		} catch (GraphqlErrorException e) {
			throw new GraphQLException("Ocurrio un error en el proceso.", e);
		}
	}

	/*
	 * Metodo para Registrar DataFetcher de ejecucion de consultas.
	 * Asignamos los metodos junto con sus parametros, para su ejecucion
	 * al momento de ejecutar las consultas GraphQL
	 */
	private RuntimeWiring buildRuntimeWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", typeWiring -> typeWiring
						.dataFetcher("getWifiAccess",
								env -> accessPointGraphQL.getWifiAccess(env.getArgument("page"),env.getArgument("size")))
						.dataFetcher(
								"getWifiAccessById", env -> accessPointGraphQL.getWifiAccessById(env.getArgument("id")))
						.dataFetcher("getWifiAccessByColonia",
								env -> accessPointGraphQL.getWifiAccessByColonia(env.getArgument("colonia"),env.getArgument("page"), env.getArgument("size")))
						.dataFetcher("getWifiAccessByProximity",
								env -> accessPointGraphQL.getWifiAccessByProximity(env.getArgument("lat"),env.getArgument("lon"), env.getArgument("page"), env.getArgument("size"))))
				.build();
	}

}
