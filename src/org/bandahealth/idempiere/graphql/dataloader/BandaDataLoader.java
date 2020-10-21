package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

public class BandaDataLoader {

	private final List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoader() {
		dataLoaders = new ArrayList<>() {{
			add(new BusinessPartnerDataLoader());
			add(new OrderDataLoader());
			add(new OrderDataLoader());
			add(new OrderLineDataLoader());
			add(new PaymentDataLoader());
			add(new ReferenceListDataLoader());
			add(new ClientDataLoader());
			add(new OrganizationDataLoader());
			add(new RoleDataLoader());
			add(new WarehouseDataLoader());
			add(new UserDataLoader());
			add(new AttributeSetInstanceDataLoader());
			add(new AttributeSetDataLoader());
			add(new ChargeDataLoader());
			add(new AccountDataLoader());
			add(new ProductDataLoader());
		}};
	}

	public void addDataLoaders(DataLoaderRegistry registry) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry));
	}
}
