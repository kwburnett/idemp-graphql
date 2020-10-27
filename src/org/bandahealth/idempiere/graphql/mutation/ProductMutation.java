package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.input.ProductInput;
import org.bandahealth.idempiere.graphql.repository.ProductRepository;

public class ProductMutation implements GraphQLMutationResolver {

	private final ProductRepository productRepository;

	public ProductMutation() {
		productRepository = new ProductRepository();
	}

	public MProduct_BH saveProduct(ProductInput product) {
		return productRepository.save(product);
	}
}
