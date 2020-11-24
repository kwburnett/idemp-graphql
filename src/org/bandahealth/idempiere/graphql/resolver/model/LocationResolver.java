package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.compiere.model.MLocation;

/**
 * The Location resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class LocationResolver extends BaseResolver<MLocation> implements GraphQLResolver<MLocation> {
}
