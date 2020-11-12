package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.ProductInput;
import org.bandahealth.idempiere.graphql.repository.ProductRepository;

/**
 * Handle all mutations relating to products
 */
public class ProductMutation implements GraphQLMutationResolver {
	private final ProductRepository productRepository;

	public ProductMutation() {
		productRepository = new ProductRepository();
	}

	/**
	 * Save information specific to an item, which is a product
	 *
	 * @param product     The information to save
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated product
	 */
	public MProduct_BH saveItem(ProductInput product, DataFetchingEnvironment environment) {
		return productRepository.save(product, BandaGraphQLContext.getCtx(environment));
	}

	/**
	 * Save information specific to a service, which is a product
	 *
	 * @param product     The information to save
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated product
	 */
	public MProduct_BH saveService(ProductInput product, DataFetchingEnvironment environment) {
		return productRepository.save(product, BandaGraphQLContext.getCtx(environment));
	}
}
