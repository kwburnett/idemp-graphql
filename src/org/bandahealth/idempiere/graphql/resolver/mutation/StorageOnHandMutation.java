package org.bandahealth.idempiere.graphql.resolver.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.StorageOnHandInput;
import org.bandahealth.idempiere.graphql.repository.StorageOnHandRepository;
import org.compiere.model.MStorageOnHand;

/**
 * Handle all mutations relating to storage on hand
 */
public class StorageOnHandMutation implements GraphQLMutationResolver {
	private final StorageOnHandRepository storageOnHandRepository;

	public StorageOnHandMutation() {
		storageOnHandRepository = new StorageOnHandRepository();
	}

	/**
	 * Update information specific to storage on hand
	 *
	 * @param storageOnHand The information to save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated storage on hand
	 */
	public MStorageOnHand updateStorageOnHand(StorageOnHandInput storageOnHand, DataFetchingEnvironment environment) {
		return storageOnHandRepository.save(storageOnHand, BandaGraphQLContext.getCtx(environment));
	}
}
