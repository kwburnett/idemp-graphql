package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.ProcessRepository;
import org.compiere.model.MProcess;

public class ProcessQuery implements GraphQLQueryResolver {

	private final ProcessRepository processRepository;

	public ProcessQuery() {
		processRepository = new ProcessRepository();
	}

	public Connection<MProcess> processes(String filter, String sort, int page, int pageSize) {
		return processRepository.get(filter, sort, new PagingInfo(page, pageSize));
	}
}
