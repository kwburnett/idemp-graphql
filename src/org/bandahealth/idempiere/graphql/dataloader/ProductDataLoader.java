package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.repository.ProductRepository;

public class ProductDataLoader extends BaseDataLoader<MProduct_BH, ProductRepository> implements DataLoaderRegisterer {

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
