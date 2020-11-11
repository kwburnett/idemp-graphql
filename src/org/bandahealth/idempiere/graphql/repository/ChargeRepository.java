package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.ChargeInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MElementValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ChargeRepository extends BaseRepository<MCharge_BH, ChargeInput> {

	private final AccountRepository accountRepository;
	private final ChargeTypeRepository chargeTypeRepository;

	public ChargeRepository() {
		accountRepository = new AccountRepository();
		chargeTypeRepository = new ChargeTypeRepository();
	}

	@Override
	protected MCharge_BH createModelInstance(Properties idempiereContext) {
		return new MCharge_BH(idempiereContext, 0, null);
	}

	@Override
	public MCharge_BH mapInputModelToModel(ChargeInput entity, Properties idempiereContext) {
		try {
			MCharge_BH charge = getByUuid(entity.getC_Charge_UU(), idempiereContext);
			if (charge == null) {
				charge = createModelInstance(idempiereContext);
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), charge::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), charge::setDescription);

			if (entity.getAccount() != null) {
				MElementValue account = accountRepository.getByUuid(entity.getAccount().getC_ElementValue_UU(),
						idempiereContext);
				if (account != null) {
					charge.setC_ElementValue_ID(account.getC_ElementValue_ID());
				}
			}

			ModelUtil.setPropertyIfPresent(entity.isActive(), charge::setIsActive);
			if (entity.getChargeType() != null) {
				charge.setC_ChargeType_ID(entity.getChargeType().getC_ChargeType_ID());
			}

			return charge;
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	public MCharge_BH saveExpenseCategory(ChargeInput charge, Properties idempiereContext) {
		MChargeType_BH expenseCategoryChargeType = chargeTypeRepository.getBaseQuery(idempiereContext,
				MChargeType_BH.Table_Name + "." + MChargeType_BH.COLUMNNAME_Name + "=?",
				MChargeType_BH.CHARGETYPENAME_DEFAULT_CATEGORY).first();
		if (expenseCategoryChargeType == null) {
			throw new AdempiereException("Expense Category Charge Type not defined for client");
		}
		charge.setC_ChargeType_ID(expenseCategoryChargeType.getC_ChargeType_ID());
		return save(charge, idempiereContext);
	}

	@Override
	public String getDefaultJoinClause(Properties idempiereContext) {
		return " JOIN " + MChargeType_BH.Table_Name + " ON " + MChargeType_BH.Table_Name + "." +
				MChargeType_BH.COLUMNNAME_C_ChargeType_ID + "=" + MCharge_BH.Table_Name + "." +
				MCharge_BH.COLUMNNAME_C_ChargeType_ID;
	}

	public Connection<MCharge_BH> getExpenseCategories(String filterJson, String sortJson, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>() {{
			add(MChargeType_BH.CHARGETYPENAME_DEFAULT_CATEGORY);
		}};

		return get(filterJson, sortJson, pagingInfo, MChargeType_BH.Table_Name + "." +
				MChargeType_BH.COLUMNNAME_Name + "=?", parameters, environment);
	}
}
