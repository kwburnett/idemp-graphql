package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.utils.QueryConstants;
import org.bandahealth.idempiere.graphql.model.input.AttributeSetInput;
import org.compiere.model.MAttributeSet;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class AttributeSetRepository extends BaseRepository<MAttributeSet, AttributeSetInput> {
	@Override
	protected MAttributeSet createModelInstance(Properties idempiereContext) {
		return new MAttributeSet(idempiereContext, 0, null);
	}

	@Override
	public MAttributeSet mapInputModelToModel(AttributeSetInput entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public List<Object> getDefaultWhereClauseParameters(Properties idempiereContext) {
		return Collections.singletonList(QueryConstants.BANDAHEALTH_PRODUCT_ATTRIBUTE_SET);
	}

	@Override
	public String getDefaultWhereClause(Properties idempiereContext) {
		return MAttributeSet.Table_Name + "." + MAttributeSet.COLUMNNAME_Name + "=?";
	}
}
