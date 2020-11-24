package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.dataloader.impl.WarehouseDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MOrg;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.util.Env;
import org.dataloader.DataLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * The Organization resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class OrganizationResolver extends BaseResolver<MOrg> implements GraphQLResolver<MOrg> {

	public CompletableFuture<List<MWarehouse>> warehouses(MOrg entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MWarehouse>> warehousesByOrgDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(WarehouseDataLoader.WAREHOUSE_BY_ORGANIZATION_DATA_LOADER);
		return warehousesByOrgDataLoader.load(ModelUtil.getModelKey(entity, entity.getAD_Org_ID()));
	}

	public List<MRole> roles(MOrg entity, DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MUser_BH user = new MUser_BH(idempiereContext, Env.getAD_User_ID(idempiereContext), null);
		return Arrays.asList(user.getRoles(entity.getAD_Org_ID()));
	}
}
