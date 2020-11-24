package org.bandahealth.idempiere.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.ProductCategoryRepository;

import java.util.List;

public class ProductCategoryQuery implements GraphQLQueryResolver {
	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategoryQuery() {
		productCategoryRepository = new ProductCategoryRepository();
	}

	public List<MProductCategory_BH> productCategories(DataFetchingEnvironment environment) {
		return productCategoryRepository.get(BandaGraphQLContext.getCtx(environment));
	}
}
