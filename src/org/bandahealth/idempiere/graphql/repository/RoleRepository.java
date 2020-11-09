package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleIncludedDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MRole;
import org.compiere.model.MRoleIncluded;
import org.compiere.util.Env;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RoleRepository extends BaseRepository<MRole, MRole> {
	private static final String ALL_SUBROLES_INCLUDED = "BandaGo Admin";

	@Override
	protected MRole createModelInstance() {
		return new MRole(Env.getCtx(), 0, null);
	}

	@Override
	public MRole mapInputModelToModel(MRole entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}

	public CompletableFuture<List<MRole>> includedRoles(int roleId, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MRole> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(RoleDataLoader.ROLE_BY_ID_DATA_LOADER);
		return rolesIncluded(roleId, environment).thenApply(rolesIncluded -> {
			List<CompletableFuture<MRole>> roles = rolesIncluded.stream().map(roleIncluded -> dataLoader
					.load(roleIncluded.getIncluded_Role_ID()))
					.collect(Collectors.toList());
			// Limitation of Java that this has to be called here
			dataLoader.dispatch();
			return roles.stream().map(CompletableFuture::join).collect(Collectors.toList());
		});
	}

	public CompletableFuture<Boolean> isAdmin(int roleId, DataFetchingEnvironment environment) {
		return includedRoles(roleId, environment).thenApply(includedRoles -> includedRoles.stream()
				.anyMatch(includedRole -> includedRole.getName().equalsIgnoreCase(ALL_SUBROLES_INCLUDED)));
	}

	private CompletableFuture<List<MRoleIncluded>> rolesIncluded(int roleId, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MRoleIncluded>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(RoleIncludedDataLoader.ROLE_INCLUDED_BY_ROLE_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(MRole.Table_Name, roleId));
	}
}
