package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrganizationRepository extends BaseRepository<MOrg> {

	@Override
	public MOrg getModelInstance() {
		return new MOrg(Env.getCtx(), 0, null);
	}
}
