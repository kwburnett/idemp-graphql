package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInput;
import org.compiere.model.MAttributeSet;
import org.compiere.util.Env;

import java.util.Collections;
import java.util.List;

public class AttributeSetRepository extends BaseRepository<MAttributeSet, AttributeSetInput> {
	@Override
	public MAttributeSet getModelInstance() {
		return new MAttributeSet(Env.getCtx(), 0, null);
	}

	@Override
	public MAttributeSet save(AttributeSetInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getDefaultWhereClause() {
		return MAttributeSet.Table_Name + "." + MAttributeSet.COLUMNNAME_Name + "=?";
	}

	@Override
	public List<Object> getDefaultParameters() {
		return Collections.singletonList("BandaHealthProductAttributeSet");
	}
}
