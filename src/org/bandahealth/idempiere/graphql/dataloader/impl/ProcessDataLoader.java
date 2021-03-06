package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.ProcessInput;
import org.bandahealth.idempiere.graphql.repository.ProcessRepository;
import org.compiere.model.MProcess;

public class ProcessDataLoader extends BaseDataLoader<MProcess, ProcessInput, ProcessRepository>
		implements DataLoaderRegisterer {
	public static final String PROCESS_BY_ID_DATA_LOADER = "processByIdDataLoader";
	public static final String PROCESS_BY_UUID_DATA_LOADER = "processByUuidDataLoader";
	private final ProcessRepository processRepository;

	public ProcessDataLoader() {
		processRepository = new ProcessRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return PROCESS_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return PROCESS_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ProcessRepository getRepositoryInstance() {
		return processRepository;
	}
}
