package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.AttributeSetInstanceInput;
import org.bandahealth.idempiere.graphql.repository.AttributeSetInstanceRepository;
import org.compiere.model.MAttributeSetInstance;

public class AttributeSetInstanceDataLoader
		extends BaseDataLoader<MAttributeSetInstance, AttributeSetInstanceInput, AttributeSetInstanceRepository>
		implements DataLoaderRegisterer {

	public static final String ATTRIBUTE_SET_INSTANCE_DATA_LOADER = "attributeSetInstanceDataLoader";
	public static final String ATTRIBUTE_SET_INSTANCE_BY_UUID_DATA_LOADER = "attributeSetInstanceByUuidDataLoader";
	private final AttributeSetInstanceRepository attributeSetInstanceRepository;

	public AttributeSetInstanceDataLoader() {
		attributeSetInstanceRepository = new AttributeSetInstanceRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return ATTRIBUTE_SET_INSTANCE_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return ATTRIBUTE_SET_INSTANCE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected AttributeSetInstanceRepository getRepositoryInstance() {
		return attributeSetInstanceRepository;
	}
}
