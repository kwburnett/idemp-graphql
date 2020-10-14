package org.bandahealth.idempiere.graphql;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Path("/")
public class TestService {

	private final GraphQL build;

	public TestService() {
		String schema = "type Query{hello: String}";

		SchemaParser schemaParser = new SchemaParser();
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

		RuntimeWiring runtimeWiring = newRuntimeWiring()
				.type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
				.build();

		SchemaGenerator schemaGenerator = new SchemaGenerator();
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

		build = GraphQL.newGraphQL(graphQLSchema).build();
	}

	@GET
	public String test() {
		return build.execute("{hello}").getData().toString();
	}
}
