package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.ProcessParameterRepository;
import org.compiere.model.MProcessPara;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

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
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(PROCESS_PARAMETER_BY_PROCESS_DATA_LOADER, DataLoader
				.newMappedDataLoader(getByProcessBatchLoader(), getOptionsWithCache(idempiereContext)));
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

	private MappedBatchLoaderWithContext<String, List<MProcessPara>> getByProcessBatchLoader() {
		return (keys, batchLoaderEnvironment) -> processParameterRepository.getGroupsByIdsCompletableFuture(
				MProcessPara::getAD_Process_ID, MProcessPara.COLUMNNAME_AD_Process_ID, keys,
				batchLoaderEnvironment.getContext());
	}
}
