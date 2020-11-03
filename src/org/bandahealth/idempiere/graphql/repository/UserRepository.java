package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.sql.Timestamp;

public class UserRepository extends BaseRepository<MUser_BH, MUser_BH> {
	@Override
	public MUser_BH getModelInstance() {
		return new MUser_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MUser_BH mapInputModelToModel(MUser_BH entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * User accepts terms of service.
	 */
	public MUser_BH acceptTermsOfUse() {
		MUser_BH user = new MUser_BH(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);
		user.setBH_HasAcceptedTermsOfUse(true);
		user.setBH_TOSDateAccepted(new Timestamp(System.currentTimeMillis()));
		user.save();

		return user;
	}
}
