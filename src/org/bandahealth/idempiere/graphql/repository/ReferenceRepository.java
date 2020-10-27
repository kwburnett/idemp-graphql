package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MReference;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;
import java.util.stream.Collectors;

public class ReferenceRepository extends BaseRepository<MReference, MReference> {
	@Override
	public MReference getModelInstance() {
		return new MReference(Env.getCtx(), 0, null);
	}

	@Override
	public MReference save(MReference entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<MReference> getByUuids(List<String> uuids) {
		return new Query(Env.getCtx(), MReference.Table_Name, MReference.COLUMNNAME_AD_Reference_UU + " IN (" +
				uuids.stream().map(uuid -> "'" + uuid + "'").collect(Collectors.joining(",")), null).list();
	}
}
