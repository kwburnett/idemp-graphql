package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.dataloader.impl.*;
import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for containing all data loaders that need to be registered for each query. It holds a list
 * of available data loaders and, when a new query comes in, it registers them for that query.
 */
public class BandaDataLoaderComposer {
	/**
	 * This list of data loaders available for each query.
	 */
	private final List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoaderComposer() {
		dataLoaders = new ArrayList<>() {{
			add(new AccountDataLoader());
			add(new AttributeSetDataLoader());
			add(new AttributeSetInstanceDataLoader());
			add(new BusinessPartnerDataLoader());
			add(new ChargeDataLoader());
			add(new ChargeTypeDataLoader());
			add(new ClientDataLoader());
			add(new FormDataLoader());
			add(new HomeScreenButtonDataLoader());
			add(new InvoiceDataLoader());
			add(new InvoiceLineDataLoader());
			add(new LocationDataLoader());
			add(new LocatorDataLoader());
			add(new OrderDataLoader());
			add(new OrderLineDataLoader());
			add(new OrganizationDataLoader());
			add(new PaymentDataLoader());
			add(new ProcessDataLoader());
			add(new ProcessParameterDataLoader());
			add(new ProductCategoryDataLoader());
			add(new ProductDataLoader());
			add(new ReferenceDataLoader());
			add(new ReferenceListDataLoader());
			add(new ReportViewDataLoader());
			add(new RoleDataLoader());
			add(new RoleIncludedDataLoader());
			add(new StorageOnHandDataLoader());
			add(new UserDataLoader());
			add(new WarehouseDataLoader());
			add(new WorkflowDataLoader());
		}};
	}

	/**
	 * This method adds the data loaders when a new request comes in. Note, this is not a static method so hot-swaps
	 * will work if code changes are made to the data loaders.
	 *
	 * @param registry The registry object that can register each data loader
	 */
	public void addDataLoaders(DataLoaderRegistry registry) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry));
	}
}
