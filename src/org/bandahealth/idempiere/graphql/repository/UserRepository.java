package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.util.Env;

import java.sql.Timestamp;
import java.util.Properties;

public class UserRepository extends BaseRepository<MUser_BH, MUser_BH> {
	@Override
	protected MUser_BH createModelInstance(Properties idempiereContext) {
		return new MUser_BH(idempiereContext, 0, null);
	}

	@Override
	public MUser_BH mapInputModelToModel(MUser_BH entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * User accepts terms of service.
	 */
	public MUser_BH acceptTermsOfUse(Properties idempiereContext) {
		MUser_BH user = new MUser_BH(idempiereContext, Env.getAD_User_ID(idempiereContext), null);
		user.setBH_HasAcceptedTermsOfUse(true);
		user.setBH_TOSDateAccepted(new Timestamp(System.currentTimeMillis()));
		user.save();

		return user;
	}
}
