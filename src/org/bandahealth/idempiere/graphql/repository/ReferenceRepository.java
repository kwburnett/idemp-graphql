package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MReference;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ReferenceRepository extends BaseRepository<MReference, MReference> {
	@Override
	protected MReference createModelInstance(Properties idempiereContext) {
		return new MReference(idempiereContext, 0, null);
	}

	@Override
	public MReference mapInputModelToModel(MReference entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<MReference> getByUuids(List<String> uuids, Properties idempiereContext) {
		return getBaseQuery(idempiereContext, MReference.COLUMNNAME_AD_Reference_UU + " IN (" +
				uuids.stream().map(uuid -> "'" + uuid + "'").collect(Collectors.joining(","))).list();
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
