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
	public static final String PRODUCT_CATEGORY_BY_UUID_DATA_LOADER = "productCategoryByUuidDataLoader";
	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategoryDataLoader() {
		productCategoryRepository = new ProductCategoryRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return PRODUCT_CATEGORY_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return PRODUCT_CATEGORY_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ProductCategoryRepository getRepositoryInstance() {
		return productCategoryRepository;
	}
}
