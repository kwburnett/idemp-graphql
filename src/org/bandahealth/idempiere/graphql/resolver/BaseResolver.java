package org.bandahealth.idempiere.graphql.resolver;

import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.ClientDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrganizationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.UserDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MRefList;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

/**
 * This class is meant to put the common fetchers that exist on every entity
 */
public class BaseResolver<T extends PO> {
	public CompletableFuture<MClient> client(T entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MClient> clientDa =
				environment.getDataLoaderRegistry().getDataLoader(ClientDataLoader.CLIENT_BY_ID_DATA_LOADER);
		return clientDa.load(ModelUtil.getModelKey(entity, entity.getAD_Client_ID()));
	}

	public String id(T entity) {
		return (String) entity.get_Value(entity.getUUIDColumnName());
	}

	public CompletableFuture<MOrg> organization(T entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MOrg> organizationDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrganizationDataLoader.ORGANIZATION_BY_ID_DATA_LOADER);
		return organizationDataLoader.load(ModelUtil.getModelKey(entity, entity.getAD_Org_ID()));
	}

	public CompletableFuture<MUser_BH> createdBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_BY_ID_DATA_LOADER);
		return userDataLoader.load(ModelUtil.getModelKey(entity, entity.getCreatedBy()));
	}

	public CompletableFuture<MUser_BH> updatedBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_BY_ID_DATA_LOADER);
		return userDataLoader.load(ModelUtil.getModelKey(entity, entity.getUpdatedBy()));
	}

	public CompletableFuture<MRefList> docStatus(T entity, DataFetchingEnvironment environment) {
		if (entity instanceof DocAction) {
			final DataLoader<String, MRefList> dataLoader = environment.getDataLoaderRegistry()
					.getDataLoader(ReferenceListDataLoader.REFERENCE_LIST_BY_DOCUMENT_STATUS_DATA_LOADER);
			return dataLoader.load(((DocAction) entity).getDocStatus());
		}
		throw new IllegalArgumentException("doc status does not exist on entity");
	}
}
