package org.bandahealth.idempiere.graphql.directive;

import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaDirectiveComposer {

	public static void addAll(SchemaParserBuilder builder) {
		builder.directive(DateFormatDirective.DATE_FORMAT_DIRECTIVE_NAME, new DateFormatDirective());
	}
}
