package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrderLineRepository extends BaseRepository<MOrderLine_BH> {
	@Override
	public MOrderLine_BH getModelInstance() {
		return new MOrderLine_BH(Env.getCtx(), 0, null);
	}
}
