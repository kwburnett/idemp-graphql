package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.ChargeInput;
import org.bandahealth.idempiere.graphql.repository.ChargeRepository;

public class ChargeMutation implements GraphQLMutationResolver {
	private final ChargeRepository chargeRepository;

	public ChargeMutation() {
		chargeRepository = new ChargeRepository();
	}

	public MCharge_BH saveExpenseCategory(ChargeInput charge, DataFetchingEnvironment environment) {
		return chargeRepository.saveExpenseCategory(charge, BandaGraphQLContext.getCtx(environment));
	}
}
