package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.StorageOnHandInput;
import org.bandahealth.idempiere.graphql.repository.StorageOnHandRepository;
import org.compiere.model.MStorageOnHand;

public class StorageOnHandMutation implements GraphQLMutationResolver {

	private final StorageOnHandRepository storageOnHandRepository;

	public StorageOnHandMutation() {
		storageOnHandRepository = new StorageOnHandRepository();
	}

	public MStorageOnHand updateStorageOnHand(StorageOnHandInput storageOnHand, DataFetchingEnvironment environment) {
		return storageOnHandRepository.save(storageOnHand, BandaGraphQLContext.getCtx(environment));
	}
}
