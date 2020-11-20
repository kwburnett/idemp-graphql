package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.compiere.model.MReportView;

/**
 * The Report View resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class ReportViewResolver extends BaseResolver<MReportView> implements GraphQLResolver<MReportView> {
}
