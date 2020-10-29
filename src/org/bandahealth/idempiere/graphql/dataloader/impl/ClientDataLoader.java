package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.ClientRepository;
import org.compiere.model.MClient;

public class ClientDataLoader extends BaseDataLoader<MClient, MClient, ClientRepository>
		implements DataLoaderRegisterer {

	public static String CLIENT_DATA_LOADER = "clientDataLoader";
	private final ClientRepository clientRepository;

	public ClientDataLoader() {
		clientRepository = new ClientRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return CLIENT_DATA_LOADER;
	}

	@Override
	protected ClientRepository getRepositoryInstance() {
		return clientRepository;
	}
}
