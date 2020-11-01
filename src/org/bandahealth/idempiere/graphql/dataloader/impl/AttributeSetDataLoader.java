package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.AttributeSetInput;
import org.bandahealth.idempiere.graphql.repository.AttributeSetRepository;
import org.compiere.model.MAttributeSet;

public class AttributeSetDataLoader extends BaseDataLoader<MAttributeSet, AttributeSetInput, AttributeSetRepository>
		implements DataLoaderRegisterer {

	public static final String ATTRIBUTE_SET_DATA_LOADER = "attributeSetDataLoader";
	public static final String ATTRIBUTE_SET_BY_UUID_DATA_LOADER = "attributeSetByUuidDataLoader";
	private final AttributeSetRepository attributeSetRepository;

	public AttributeSetDataLoader() {
		attributeSetRepository = new AttributeSetRepository();
	}
	@Override
	protected String getDefaultByIdDataLoaderName() {
		return ATTRIBUTE_SET_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return ATTRIBUTE_SET_BY_UUID_DATA_LOADER;
	}

	@Override
	protected AttributeSetRepository getRepositoryInstance() {
		return attributeSetRepository;
	}
}
