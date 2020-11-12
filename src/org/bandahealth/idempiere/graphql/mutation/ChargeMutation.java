package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.ChargeInput;
import org.bandahealth.idempiere.graphql.repository.ChargeRepository;

/**
 * Handle all mutations relating to authentication
 */
public class ChargeMutation implements GraphQLMutationResolver {
	private final ChargeRepository chargeRepository;

	public ChargeMutation() {
		chargeRepository = new ChargeRepository();
	}

	/**
	 * Save information specific to an expense category, which is a charge
	 *
	 * @param charge      The information to save
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated expense category
	 */
	public MCharge_BH saveExpenseCategory(ChargeInput charge, DataFetchingEnvironment environment) {
		return chargeRepository.saveExpenseCategory(charge, BandaGraphQLContext.getCtx(environment));
	}
}
