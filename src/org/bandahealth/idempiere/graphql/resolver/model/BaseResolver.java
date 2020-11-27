package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.ClientDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrganizationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.UserDataLoader;
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
	/**
	 * Return the client entity for this object leveraging the client data loader
	 *
	 * @param entity      The entity to fetch data for
	 * @param environment The GraphQL environment object
	 * @return A completable future of the client
	 */
	public CompletableFuture<MClient> client(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MClient> clientDa =
				environment.getDataLoaderRegistry().getDataLoader(ClientDataLoader.CLIENT_BY_ID_DATA_LOADER);
		return clientDa.load(entity.getAD_Client_ID());
	}

	/**
	 * By default, map UUIDs to the ID field
	 *
	 * @param entity The entity to fetch data for
	 * @return An external ID for consumers
	 */
	public String id(T entity) {
		return (String) entity.get_Value(entity.getUUIDColumnName());
	}

	/**
	 * Return the organization entity for this object leveraging the organization data loader
	 *
	 * @param entity      The entity to fetch data for
	 * @param environment The GraphQL environment object
	 * @return A completable future of the organization
	 */
	public CompletableFuture<MOrg> organization(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MOrg> organizationDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrganizationDataLoader.ORGANIZATION_BY_ID_DATA_LOADER);
		return organizationDataLoader.load(entity.getAD_Org_ID());
	}

	/**
	 * Return the user entity for this object of who created this entity, leveraging the user data loader
	 *
	 * @param entity      The entity to fetch data for
	 * @param environment The GraphQL environment object
	 * @return A completable future of the user who created the entity
	 */
	public CompletableFuture<MUser_BH> createdBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_BY_ID_DATA_LOADER);
		return userDataLoader.load(entity.getCreatedBy());
	}

	/**
	 * Return the user entity for this object of who last updated this entity, leveraging the user data loader
	 *
	 * @param entity      The entity to fetch data for
	 * @param environment The GraphQL environment object
	 * @return A completable future of the user last updated the entity
	 */
	public CompletableFuture<MUser_BH> updatedBy(T entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MUser_BH> userDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(UserDataLoader.USER_BY_ID_DATA_LOADER);
		return userDataLoader.load(entity.getUpdatedBy());
	}

	/**
	 * Return the reference list of the doc status (if this entity has it), leveraging the user data loader
	 *
	 * @param entity      The entity to fetch data for
	 * @param environment The GraphQL environment object
	 * @return A completable future of the doc status reference list for the entity
	 */
	public CompletableFuture<MRefList> docStatus(T entity, DataFetchingEnvironment environment) {
		if (entity instanceof DocAction) {
			final DataLoader<String, MRefList> dataLoader = environment.getDataLoaderRegistry()
					.getDataLoader(ReferenceListDataLoader.REFERENCE_LIST_BY_DOCUMENT_STATUS_DATA_LOADER);
			return dataLoader.load(((DocAction) entity).getDocStatus());
		}
		throw new IllegalArgumentException("doc status does not exist on entity");
	}
}
