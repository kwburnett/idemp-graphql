package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.model.input.ChargeInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MElementValue;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class ChargeRepository extends BaseRepository<MCharge_BH, ChargeInput> {

	private final AccountRepository accountRepository;

	public ChargeRepository() {
		accountRepository = new AccountRepository();
	}

	@Override
	public MCharge_BH getModelInstance() {
		return new MCharge_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MCharge_BH save(ChargeInput entity) {
		try {
			MCharge_BH charge = getByUuid(entity.getC_Charge_UU());
			if (charge == null) {
				charge = getModelInstance();
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), charge::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), charge::setDescription);

			if (entity.getAccount() != null) {
				MElementValue account = accountRepository.getByUuid(entity.getAccount().getC_ElementValue_UU());
				if (account != null) {
					charge.setC_ElementValue_ID(account.getC_ElementValue_ID());
				}
			}

			ModelUtil.setPropertyIfPresent(entity.isActive(), charge::setIsActive);

			MChargeType_BH expenseCategoryChargeType = new Query(
					Env.getCtx(),
					MChargeType_BH.Table_Name,
					MChargeType_BH.COLUMNNAME_AD_Client_ID + "=? AND " + MChargeType_BH.COLUMNNAME_Name + "=?",
					null
			)
					.setParameters(entity.getAD_Client_ID(), MChargeType_BH.CHARGETYPENAME_DEFAULT_CATEGORY)
					.first();
			if (expenseCategoryChargeType == null) {
				throw new AdempiereException("Expense Category Charge Type not defined for client");
			}
			charge.setC_ChargeType_ID(expenseCategoryChargeType.getC_ChargeType_ID());

			charge.saveEx();

			cache.delete(charge.get_ID());

			return getByUuid(charge.getC_Charge_UU());

		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}
}
