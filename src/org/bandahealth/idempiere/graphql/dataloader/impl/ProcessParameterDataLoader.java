package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.ProcessParameterRepository;
import org.compiere.model.MProcessPara;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class ProcessParameterDataLoader extends BaseDataLoader<MProcessPara, MProcessPara, ProcessParameterRepository>
		implements DataLoaderRegisterer {
	public static final String PROCESS_PARAMETER_BY_ID_DATA_LOADER = "processParameterByIdDataLoader";
	public static final String PROCESS_PARAMETER_BY_UUID_DATA_LOADER = "processParameterByUuidDataLoader";
	public static final String PROCESS_PARAMETER_BY_PROCESS_DATA_LOADER = "processParameterByProcessDataLoader";
	private final ProcessParameterRepository processParameterRepository;

	public ProcessParameterDataLoader() {
		processParameterRepository = new ProcessParameterRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(PROCESS_PARAMETER_BY_PROCESS_DATA_LOADER, DataLoader
				.newMappedDataLoader(getByProcessBatchLoader(), getOptionsWithCache()));
	}

	@Override
	protected String getByIdDataLoaderName() {
		return PROCESS_PARAMETER_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return PROCESS_PARAMETER_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ProcessParameterRepository getRepositoryInstance() {
		return processParameterRepository;
	}

	private MappedBatchLoader<String, List<MProcessPara>> getByProcessBatchLoader() {
		return keys -> processParameterRepository.getGroupsByIdsCompletableFuture(MProcessPara::getAD_Process_ID,
				MProcessPara.COLUMNNAME_AD_Process_ID, keys);
	}
}
