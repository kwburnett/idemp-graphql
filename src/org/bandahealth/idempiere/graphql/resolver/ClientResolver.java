package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrganizationDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClientResolver extends BaseResolver<MClient> implements GraphQLResolver<MClient> {

	public CompletableFuture<List<MOrg>> organizations(MClient entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MOrg>> organizationsDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrganizationDataLoader.ORGANIZATION_BY_CLIENT_DATA_LOADER);
		return organizationsDataLoader.load(ModelUtil.getModelKey(entity, entity.getAD_Client_ID()));
	}
}
