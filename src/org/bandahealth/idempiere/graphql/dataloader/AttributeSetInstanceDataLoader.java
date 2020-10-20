package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.respository.AttributeSetInstanceRepository;
import org.compiere.model.MAttributeSetInstance;

public class AttributeSetInstanceDataLoader
		extends BaseDataLoader<MAttributeSetInstance, AttributeSetInstanceRepository> implements DataLoaderRegisterer {

	public static final String ATTRIBUTE_SET_INSTANCE_DATA_LOADER = "attributeSetInstanceDataLoader";
	private final AttributeSetInstanceRepository attributeSetInstanceRepository;

	public AttributeSetInstanceDataLoader() {
		attributeSetInstanceRepository = new AttributeSetInstanceRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ATTRIBUTE_SET_INSTANCE_DATA_LOADER;
	}

	@Override
	protected AttributeSetInstanceRepository getRepositoryInstance() {
		return attributeSetInstanceRepository;
	}
}
