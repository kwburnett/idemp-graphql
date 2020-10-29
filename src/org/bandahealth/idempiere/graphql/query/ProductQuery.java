package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.ProductRepository;

import java.util.concurrent.CompletableFuture;

public class ProductQuery implements GraphQLQueryResolver {

	private final ProductRepository productRepository;

	public ProductQuery() {
		productRepository = new ProductRepository();
	}

	public Connection<MProduct_BH> items(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return productRepository.getItems(filter, sort, new PagingInfo(page, pageSize), environment);
	}

	public Connection<MProduct_BH> services(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return productRepository.getServices(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
