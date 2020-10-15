package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.AuthGraphQLContext;
import org.compiere.util.CLogger;

import java.util.List;

public class Query implements GraphQLQueryResolver {

	private final CLogger logger = CLogger.getCLogger(Query.class);

	private final LinkRepository linkRepository;

	public Query(LinkRepository linkRepository) {
		this.linkRepository = linkRepository;
	}

	public List<Link> allLinks(DataFetchingEnvironment environment) {
		logger.warning(((AuthGraphQLContext) environment.getContext()).getUserId());
		return linkRepository.getAllLinks();
	}
}
