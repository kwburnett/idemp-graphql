package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.compiere.model.MReference;

/**
 * The Reference resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class ReferenceResolver extends BaseResolver<MReference> implements GraphQLResolver<MReference> {
}
