package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.repository.ChargeRepository;

public class ChargeDataLoader extends BaseDataLoader<MCharge_BH, ChargeRepository> implements DataLoaderRegisterer {

	public static final String CHARGE_DATA_LOADER = "chargeDataLoader";
	private final ChargeRepository chargeRepository;

	public ChargeDataLoader() {
		chargeRepository = new ChargeRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return CHARGE_DATA_LOADER;
	}

	@Override
	protected ChargeRepository getRepositoryInstance() {
		return chargeRepository;
	}
}
