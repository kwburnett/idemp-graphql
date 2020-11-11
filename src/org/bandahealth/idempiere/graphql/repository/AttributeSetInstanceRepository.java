package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInstanceInput;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;

import java.util.List;
import java.util.Properties;

public class AttributeSetInstanceRepository extends BaseRepository<MAttributeSetInstance, AttributeSetInstanceInput> {
	private final AttributeSetRepository attributeSetRepository;

	public AttributeSetInstanceRepository() {
		attributeSetRepository = new AttributeSetRepository();
	}

	@Override
	protected MAttributeSetInstance createModelInstance(Properties idempiereContext) {
		return new MAttributeSetInstance(idempiereContext, 0, null);
	}

	@Override
	public MAttributeSetInstance mapInputModelToModel(AttributeSetInstanceInput entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public List<Object> getDefaultJoinClauseParameters(Properties idempiereContext) {
		return attributeSetRepository.getDefaultWhereClauseParameters(idempiereContext);
	}

	@Override
	public String getDefaultJoinClause(Properties idempiereContext) {
		return "JOIN " + MAttributeSet.Table_Name + " ON " + MAttributeSet.Table_Name + "." +
				MAttributeSet.COLUMNNAME_M_AttributeSet_ID + "=" + MAttributeSetInstance.Table_Name + "." +
				MAttributeSetInstance.COLUMNNAME_M_AttributeSet_ID + " AND " +
				attributeSetRepository.getDefaultWhereClause(idempiereContext);
	}
}
