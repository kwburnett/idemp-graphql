package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.AccountDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeTypeDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MElementValue;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class ChargeResolver extends BaseResolver<MCharge_BH> implements GraphQLResolver<MCharge_BH> {

	public boolean isLocked(MCharge_BH entity) {
		return entity.isBH_Locked();
	}

	public CompletableFuture<MElementValue> account(MCharge_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MElementValue> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(AccountDataLoader.ACCOUNT_BY_ID_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getC_ElementValue_ID()));
	}

	public CompletableFuture<MChargeType_BH> chargeType(MCharge_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MChargeType_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ChargeTypeDataLoader.CHARGE_TYPE_BY_ID_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getC_ChargeType_ID()));
	}
}
