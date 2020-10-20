package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

public class BandaDataLoader {

	private List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoader() {
		dataLoaders = new ArrayList<>() {{
			add(new BusinessPartnerDataLoader());
			add(new OrderDataLoader());
			add(new OrderDataLoader());
			add(new OrderLineDataLoader());
			add(new PaymentDataLoader());
			add(new ReferenceListDataLoader());
		}};
	}

	public void addDataLoaders(DataLoaderRegistry registry) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry));
	}
}
