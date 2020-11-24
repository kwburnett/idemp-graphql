package org.bandahealth.idempiere.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProcessDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.ProcessInfoInput;
import org.bandahealth.idempiere.graphql.repository.ProcessRepository;
import org.compiere.model.MProcess;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class ProcessQuery implements GraphQLQueryResolver {

	private final ProcessRepository processRepository;

	public ProcessQuery() {
		processRepository = new ProcessRepository();
	}

	public CompletableFuture<MProcess> process(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MProcess> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ProcessDataLoader.PROCESS_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public Connection<MProcess> processes(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return processRepository.get(filter, sort, new PagingInfo(page, pageSize), environment);
	}

	public String generateReport(ProcessInfoInput processInfo, DataFetchingEnvironment environment) {
		return processRepository.generateReport(processInfo, BandaGraphQLContext.getCtx(environment));
	}
}
