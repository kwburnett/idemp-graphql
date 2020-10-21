package org.bandahealth.idempiere.graphql.resolver;

import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.dataloader.ClientDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.OrganizationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.UserDataLoader;
import org.bandahealth.idempiere.graphql.model.DocStatus;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

/**
 * This class is meant to put the common fetchers that exist on every entity
 */
public class BaseResolver<T extends PO> {
	public CompletableFuture<MClient> client(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MClient> clientDa =
				environment.getDataLoaderRegistry().getDataLoader(ClientDataLoader.CLIENT_DATA_LOADER);
		return clientDa.load(entity.getAD_Client_ID());
	}

	public String id(T entity) {
		return entity.get_Value(entity.getUUIDColumnName()).toString();
	}

	public CompletableFuture<MOrg> organization(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MOrg> organizationDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrganizationDataLoader.ORGANIZATION_DATA_LOADER);
		return organizationDataLoader.load(entity.getAD_Org_ID());
	}

	public CompletableFuture<MUser_BH> createdBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_DATA_LOADER);
		return userDataLoader.load(entity.getCreatedBy());
	}

	public CompletableFuture<MUser_BH> updatedBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_DATA_LOADER);
		return userDataLoader.load(entity.getUpdatedBy());
	}

	public DocStatus docStatus(T entity) {
		if (entity instanceof DocAction) {
			return DocStatus.valueOf(((DocAction) entity).getDocStatus());
		}
		throw new IllegalArgumentException("doc status does not exist on entity");
	}
}
