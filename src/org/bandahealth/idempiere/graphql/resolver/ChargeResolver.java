package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.AccountDataLoader;
import org.compiere.model.MElementValue;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class ChargeResolver extends BaseResolver<MCharge_BH> implements GraphQLResolver<MCharge_BH> {

	public boolean isLocked(MCharge_BH entity) {
		return entity.isBH_Locked();
	}

	public CompletableFuture<MElementValue> account(MCharge_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MElementValue> accountDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(AccountDataLoader.ACCOUNT_DATA_LOADER);
		return accountDataLoader.load(entity.getC_ElementValue_ID());
	}
}
