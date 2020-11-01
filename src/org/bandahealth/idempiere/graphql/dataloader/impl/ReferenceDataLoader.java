package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.repository.ReferenceRepository;
import org.compiere.model.MReference;

public class ReferenceDataLoader extends BaseDataLoader<MReference, MReference, ReferenceRepository>
		implements DataLoaderRegisterer {
	public static final String REFERENCE_DATA_LOADER = "referenceDataLoader";
	public static final String REFERENCE_BY_UUID_DATA_LOADER = "referenceByUuidDataLoader";
	private final ReferenceRepository referenceRepository;

	public ReferenceDataLoader() {
		referenceRepository = new ReferenceRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return REFERENCE_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return REFERENCE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ReferenceRepository getRepositoryInstance() {
		return referenceRepository;
	}
}
