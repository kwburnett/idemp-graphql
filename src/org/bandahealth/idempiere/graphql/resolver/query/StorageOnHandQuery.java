package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.StorageOnHandRepository;
import org.compiere.model.MStorageOnHand;

public class StorageOnHandQuery implements GraphQLQueryResolver {
	private final StorageOnHandRepository storageOnHandRepository;

	public StorageOnHandQuery() {
		storageOnHandRepository = new StorageOnHandRepository();
	}

	public Connection<MStorageOnHand> allStorageOnHand(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return storageOnHandRepository.get(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
