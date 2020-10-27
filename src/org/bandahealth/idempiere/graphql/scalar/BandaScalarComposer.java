package org.bandahealth.idempiere.graphql.scalar;

import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaScalarComposer {

	public static void addAll(SchemaParserBuilder builder) {
		builder.scalars(
				ObjectScalar.Object
		);
	}
}
