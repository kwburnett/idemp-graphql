package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.graphql.respository.UserRepository;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;

import java.util.List;

public class UserQuery implements GraphQLQueryResolver {

	private final CLogger logger = CLogger.getCLogger(UserQuery.class);

	private final UserRepository userRepository;

	public UserQuery() {
		this.userRepository = new UserRepository();
	}

	public List<MUser> getUsers() {
		return userRepository.getUsers();
	}
}
