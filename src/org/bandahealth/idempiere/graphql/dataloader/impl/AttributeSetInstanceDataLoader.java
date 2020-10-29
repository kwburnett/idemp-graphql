package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.AttributeSetInstanceInput;
import org.bandahealth.idempiere.graphql.repository.AttributeSetInstanceRepository;
import org.compiere.model.MAttributeSetInstance;

public class AttributeSetInstanceDataLoader
		extends BaseDataLoader<MAttributeSetInstance, AttributeSetInstanceInput, AttributeSetInstanceRepository>
		implements DataLoaderRegisterer {

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
