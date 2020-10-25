package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
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

	public CompletableFuture<Connection<MProduct_BH>> products(String filter, String sort, int page, int pageSize) {
		return CompletableFuture.supplyAsync(() ->
				productRepository.get(filter, sort, new PagingInfo(page, pageSize)));
	}
}
