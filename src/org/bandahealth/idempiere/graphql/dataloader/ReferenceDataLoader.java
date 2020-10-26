package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.ReferenceRepository;
import org.compiere.model.MReference;

public class ReferenceDataLoader extends BaseDataLoader<MReference, MReference, ReferenceRepository>
		implements DataLoaderRegisterer {

	public static final String REFERENCE_DATA_LOADER = "referenceDataLoader";
	private final ReferenceRepository referenceRepository;

	public ReferenceDataLoader() {
		referenceRepository = new ReferenceRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return REFERENCE_DATA_LOADER;
	}

	@Override
	protected ReferenceRepository getRepositoryInstance() {
		return referenceRepository;
	}
}
