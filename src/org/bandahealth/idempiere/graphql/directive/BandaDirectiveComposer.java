package org.bandahealth.idempiere.graphql.directive;

import graphql.kickstart.tools.SchemaParserBuilder;

/**
 * This class is meant to add all custom GraphQL directives the system uses
 */
public class BandaDirectiveComposer {
	/**
	 * Add the custom directives needed for the application
	 *
	 * @param builder The GraphQL Schema Parser builder that will allow adding of directives
	 */
	public static void addAll(SchemaParserBuilder builder) {
		builder.directive(DateFormatDirective.DATE_FORMAT_DIRECTIVE_NAME, new DateFormatDirective());
	}
}
