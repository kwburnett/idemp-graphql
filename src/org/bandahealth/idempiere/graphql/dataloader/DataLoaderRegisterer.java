package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

public interface DataLoaderRegisterer {
	public void register(DataLoaderRegistry registry);
}
