package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.dataloader.WarehouseDataLoader;
import org.compiere.model.MOrg;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.util.Env;
import org.dataloader.DataLoader;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrganizationResolver extends BaseResolver<MOrg> implements GraphQLResolver<MOrg> {

	public CompletableFuture<List<MWarehouse>> warehouses(MOrg entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MWarehouse>> warehousesByOrgDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(WarehouseDataLoader.WAREHOUSE_BY_ORGANIZATION_DATA_LOADER);
		return warehousesByOrgDataLoader.load(entity.getAD_Org_ID());
	}

	public List<MRole> roles(MOrg entity) {
		MUser_BH user = new MUser_BH(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);
		return Arrays.asList(user.getRoles(entity.getAD_Org_ID()));
	}
}
