package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInstanceInput;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.util.Env;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeSetInstanceRepository extends BaseRepository<MAttributeSetInstance, AttributeSetInstanceInput> {
	private final AttributeSetRepository attributeSetRepository;

	public AttributeSetInstanceRepository() {
		attributeSetRepository = new AttributeSetRepository();
	}

	@Override
	public MAttributeSetInstance getModelInstance() {
		return new MAttributeSetInstance(Env.getCtx(), 0, null);
	}

	@Override
	public MAttributeSetInstance save(AttributeSetInstanceInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getDefaultJoinClause() {
		return "JOIN " + MAttributeSet.Table_Name + " ON " + MAttributeSet.Table_Name + "." +
				MAttributeSet.COLUMNNAME_M_AttributeSet_ID + "=" + MAttributeSetInstance.Table_Name + "." +
				MAttributeSetInstance.COLUMNNAME_M_AttributeSet_ID + " AND " + attributeSetRepository.getDefaultWhereClause();
	}
}
