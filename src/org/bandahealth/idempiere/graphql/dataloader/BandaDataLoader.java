package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

public class BandaDataLoader {

	private final List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoader() {
		dataLoaders = new ArrayList<>() {{
			add(new AccountDataLoader());
			add(new AttributeSetDataLoader());
			add(new AttributeSetInstanceDataLoader());
			add(new BusinessPartnerDataLoader());
			add(new ChargeDataLoader());
			add(new ClientDataLoader());
			add(new FormDataLoader());
			add(new LocationDataLoader());
			add(new OrderDataLoader());
			add(new OrderLineDataLoader());
			add(new OrganizationDataLoader());
			add(new PaymentDataLoader());
			add(new ProcessParameterDataLoader());
			add(new ProductCategoryDataLoader());
			add(new ProductDataLoader());
			add(new ReferenceDataLoader());
			add(new ReferenceListDataLoader());
			add(new ReportViewDataLoader());
			add(new RoleDataLoader());
			add(new StorageOnHandDataLoader());
			add(new UserDataLoader());
			add(new WarehouseDataLoader());
			add(new WorkflowDataLoader());
		}};
	}

	public void addDataLoaders(DataLoaderRegistry registry) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry));
	}
}
