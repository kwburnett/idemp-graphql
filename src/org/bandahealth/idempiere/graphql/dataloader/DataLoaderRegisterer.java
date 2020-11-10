package org.bandahealth.idempiere.graphql.dataloader;

import org.dataloader.DataLoaderRegistry;

/**
 * An interface for the data loader impls to implement so they can register their data loaders
 */
public interface DataLoaderRegisterer {
	/**
	 * Register any data loaders needed.
	 *
	 * @param registry The data loader registry that holds the data loaders
	 */
	void register(DataLoaderRegistry registry);
}
