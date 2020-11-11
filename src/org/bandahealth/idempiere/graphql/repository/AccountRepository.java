package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.graphql.model.input.AccountInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MElementValue;

import java.util.Properties;

public class AccountRepository extends BaseRepository<MElementValue, AccountInput> {
	@Override
	protected MElementValue createModelInstance(Properties idempiereContext) {
		return new MElementValue(idempiereContext, 0, null);
	}

	@Override
	public MElementValue mapInputModelToModel(AccountInput entity, Properties idempiereContext) {
		try {
			MElementValue account = getByUuid(entity.getC_ElementValue_UU(), idempiereContext);
			if (account == null) {
				account = createModelInstance(idempiereContext);
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
