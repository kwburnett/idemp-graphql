package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.ChargeInput;
import org.bandahealth.idempiere.graphql.repository.ChargeRepository;

public class ChargeDataLoader extends BaseDataLoader<MCharge_BH, ChargeInput, ChargeRepository>
		implements DataLoaderRegisterer {

	public static final String CHARGE_BY_ID_DATA_LOADER = "chargeByIdDataLoader";
	public static final String CHARGE_BY_UUID_DATA_LOADER = "chargeByUuidDataLoader";
	private final ChargeRepository chargeRepository;

	public ChargeDataLoader() {
		chargeRepository = new ChargeRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return CHARGE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return CHARGE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ChargeRepository getRepositoryInstance() {
		return chargeRepository;
	}
}
