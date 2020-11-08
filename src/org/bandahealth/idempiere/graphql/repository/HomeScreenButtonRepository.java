package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MHomeScreenButton;
import org.bandahealth.idempiere.base.model.MHomeScreenButtonGroup;
import org.bandahealth.idempiere.graphql.utils.BandaQuery;
import org.compiere.model.MRoleIncluded;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenButtonRepository extends BaseRepository<MHomeScreenButton, MHomeScreenButton> {

	private final RoleRepository roleRepository;

	public HomeScreenButtonRepository() {
		roleRepository = new RoleRepository();
	}

	@Override
	protected MHomeScreenButton createModelInstance() {
		return new MHomeScreenButton(Env.getCtx(), 0, null);
	}

	@Override
	public MHomeScreenButton mapInputModelToModel(MHomeScreenButton entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}

	public List<MHomeScreenButton> get(boolean isAdmin) {
		return getMenuGroupLineItems(isAdmin);
	}

	public boolean hasAccessToReports(boolean isAdmin) {
		// Retrieve Reports Menu
		MHomeScreenButtonGroup menu = new Query(Env.getCtx(), MHomeScreenButtonGroup.Table_Name,
				MHomeScreenButtonGroup.COLUMNNAME_Name + "=?", null)
				.setOrderBy(MHomeScreenButtonGroup.COLUMNNAME_LineNo).setOnlyActiveRecords(true)
				.setParameters("Reports").first();
		if (menu == null) {
			return false;
		}

		// Get reports
		List<MHomeScreenButton> reports = getMenuGroupLineItems(isAdmin, menu.get_ID(), null);
		return reports != null && !reports.isEmpty();
	}

	private List<MHomeScreenButton> getMenuGroupLineItems(boolean isAdmin) {
		return getMenuGroupLineItems(isAdmin, 0);
	}

	/**
	 * Get group line items
	 *
	 * @param menuGroupItemId
	 * @return
	 */
	private List<MHomeScreenButton> getMenuGroupLineItems(boolean isAdmin, int menuGroupItemId) {
		return getMenuGroupLineItems(isAdmin, menuGroupItemId, "'Metrics', 'Reports'");
	}

	private List<MHomeScreenButton> getMenuGroupLineItems(boolean isAdmin, int menuGroupItemId, String exclude) {
		try {
			List<Object> parameters = new ArrayList<>();

			StringBuilder whereClause = new StringBuilder();
			if (menuGroupItemId > 0) {
				whereClause = new StringBuilder(
						MHomeScreenButton.Table_Name + "." + MHomeScreenButton.COLUMNNAME_BH_HmScrn_ButtonGroup_ID)
						.append("=?");
				parameters.add(menuGroupItemId);
			} else {
				// filter out metrics and reports by default
				whereClause.append(MHomeScreenButtonGroup.Table_Name).append(".")
						.append(MHomeScreenButtonGroup.COLUMNNAME_Name);

				if (exclude != null) {
					whereClause.append(" NOT IN (").append(exclude).append(")");
				}
			}

			if (!isAdmin) {
				whereClause.append(" AND ");

				whereClause.append(MRoleIncluded.Table_Name + "." + MRoleIncluded.COLUMNNAME_AD_Role_ID + "=?");
				parameters.add(Env.getAD_Role_ID(Env.getCtx()));
			}

			BandaQuery<MHomeScreenButton> query = getBaseQuery(whereClause.toString(), parameters)
					.setOnlyActiveRecords(true)
					.setOrderBy(MHomeScreenButton.Table_Name + "." + MHomeScreenButton.COLUMNNAME_LineNo);

			// join MHomeScreenButtonGroup Table
			query = query.addJoinClause(" JOIN " + MHomeScreenButtonGroup.Table_Name + " ON "
					+ MHomeScreenButton.Table_Name + "." + MHomeScreenButton.COLUMNNAME_BH_HmScrn_ButtonGroup_ID + "="
					+ MHomeScreenButtonGroup.Table_Name + "."
					+ MHomeScreenButtonGroup.COLUMNNAME_BH_HmScrn_ButtonGroup_ID);

			if (!isAdmin) {
				// join Role Table
				query = query.addJoinClause(" JOIN " + MRoleIncluded.Table_Name + " ON " + MHomeScreenButton.Table_Name
						+ "." + MHomeScreenButton.COLUMNNAME_Included_Role_ID + "=" + MRoleIncluded.Table_Name + "."
						+ MRoleIncluded.COLUMNNAME_Included_Role_ID);
			}

			return query.list();
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}

		return null;
	}
}
