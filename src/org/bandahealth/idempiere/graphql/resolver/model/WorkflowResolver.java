package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.compiere.wf.MWorkflow;

/**
 * The Workflow resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class WorkflowResolver extends BaseResolver<MWorkflow> implements GraphQLResolver<MWorkflow> {
}
