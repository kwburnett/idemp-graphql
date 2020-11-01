package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.repository.ProductCategoryRepository;

import java.util.List;

public class ProductCategoryQuery implements GraphQLQueryResolver {
	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategoryQuery() {
		productCategoryRepository = new ProductCategoryRepository();
	}

	public List<MProductCategory_BH> productCategories() {
		return productCategoryRepository.get();
	}
}
