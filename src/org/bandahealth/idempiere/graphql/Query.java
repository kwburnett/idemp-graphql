package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.graphql.respository.UserRepository;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;

import java.util.List;

public class Query implements GraphQLQueryResolver {

	private final CLogger logger = CLogger.getCLogger(Query.class);

	private final UserRepository userRepository;

	public Query() {
		this.userRepository = new UserRepository();
	}

	public List<MUser> getUsers() {
		return userRepository.getUsers();
	}
}
