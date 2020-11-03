package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.graphql.model.input.AccountInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MElementValue;
import org.compiere.util.Env;

public class AccountRepository extends BaseRepository<MElementValue, AccountInput> {
	@Override
	public MElementValue getModelInstance() {
		return new MElementValue(Env.getCtx(), 0, null);
	}

	@Override
	public MElementValue save(AccountInput entity) {
		try {
			MElementValue account = getByUuid(entity.getC_ElementValue_UU());
			if (account == null) {
				account = getModelInstance();
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), account::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), account::setDescription);
			ModelUtil.setPropertyIfPresent(entity.isActiva(), account::setIsActive);

			account.saveEx();
			MElementValue savedAccount = getByUuid(account.getC_ElementValue_UU());
			updateCacheAfterSave(savedAccount);

			return savedAccount;
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	@Override
	public MElementValue mapInputModelToModel(AccountInput entity) {
		try {
			MElementValue account = getByUuid(entity.getC_ElementValue_UU());
			if (account == null) {
				account = getModelInstance();
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), account::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), account::setDescription);
			ModelUtil.setPropertyIfPresent(entity.isActiva(), account::setIsActive);

			return account;
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}
}
