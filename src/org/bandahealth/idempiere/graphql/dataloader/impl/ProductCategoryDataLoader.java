package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.model.input.ProductCategoryInput;
import org.bandahealth.idempiere.graphql.repository.ProductCategoryRepository;

public class ProductCategoryDataLoader
		extends BaseDataLoader<MProductCategory_BH, ProductCategoryInput, ProductCategoryRepository>
		implements DataLoaderRegisterer {

	public static final String PRODUCT_CATEGORY_DATA_LOADER = "productCategoryDataLoader";
	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategoryDataLoader() {
		productCategoryRepository = new ProductCategoryRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return PRODUCT_CATEGORY_DATA_LOADER;
	}

	@Override
	protected ProductCategoryRepository getRepositoryInstance() {
		return productCategoryRepository;
	}
}
