package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.model.input.ProductInput;
import org.bandahealth.idempiere.graphql.repository.ProductRepository;

public class ProductDataLoader extends BaseDataLoader<MProduct_BH, ProductInput, ProductRepository>
		implements DataLoaderRegisterer {

	public static final String PRODUCT_DATA_LOADER = "productDataLoader";
	private final ProductRepository productRepository;

	public ProductDataLoader() {
		productRepository = new ProductRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return PRODUCT_DATA_LOADER;
	}

	@Override
	protected ProductRepository getRepositoryInstance() {
		return productRepository;
	}
}
