package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInput;
import org.bandahealth.idempiere.graphql.repository.AttributeSetRepository;
import org.compiere.model.MAttributeSet;

public class AttributeSetDataLoader extends BaseDataLoader<MAttributeSet, AttributeSetInput, AttributeSetRepository>
		implements DataLoaderRegisterer {

	public static final String ATTRIBUTE_SET_DATA_LOADER = "attributeSetDataLoader";
	private final AttributeSetRepository attributeSetRepository;

	public AttributeSetDataLoader() {
		attributeSetRepository = new AttributeSetRepository();
	}
	@Override
	protected String getDefaultDataLoaderName() {
		return ATTRIBUTE_SET_DATA_LOADER;
	}

	@Override
	protected AttributeSetRepository getRepositoryInstance() {
		return attributeSetRepository;
	}
}
