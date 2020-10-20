package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BusinessPartnerRepository extends BaseRepository<MBPartner_BH> {
	@Override
	public MBPartner_BH getModelInstance() {
		return new MBPartner_BH(Env.getCtx(), 0, null);
	}
}
