package org.bandahealth.idempiere.graphql.scalar;

import graphql.schema.*;

public class IntOrStringInputScalar {
	public static final GraphQLScalarType IntOrStringInput = GraphQLScalarType.newScalar().name("IntOrStringInput")
			.coercing(new Coercing() {
				@Override
				public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
					return dataFetcherResult;
				}

				@Override
				public Object parseValue(Object input) throws CoercingParseValueException {
					return input;
				}

				@Override
				public Object parseLiteral(Object input) throws CoercingParseLiteralException {
					return input;
				}
			}).description("a scalar to hold either Int or String for inputs").build();
}
