package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

public class BandaDataLoader {

	private List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoader() {
		dataLoaders = new ArrayList<>() {{
			add(new BusinessPartnerDataLoaderRegisterer());
			add(new OrderDataLoaderRegisterer());
			add(new OrderDataLoaderRegisterer());
			add(new OrderLineDataLoaderRegisterer());
			add(new PaymentDataLoaderRegisterer());
			add(new ReferenceListDataLoaderRegisterer());
		}};
	}

	public void addDataLoaders(DataLoaderRegistry registry) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry));
	}
}
