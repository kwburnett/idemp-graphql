package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MDashboardButtonGroup;
import org.bandahealth.idempiere.base.model.MDashboardButtonGroupButton;
import org.bandahealth.idempiere.graphql.utils.BandaQuery;
import org.compiere.model.MRoleIncluded;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HomeScreenButtonRepository extends BaseRepository<MDashboardButtonGroupButton, MDashboardButtonGroupButton> {

	private final RoleRepository roleRepository;

	public HomeScreenButtonRepository() {
		roleRepository = new RoleRepository();
	}

	@Override
	protected MDashboardButtonGroupButton createModelInstance(Properties idempiereContext) {
		return new MDashboardButtonGroupButton(idempiereContext, 0, null);
	}

	@Override
	public MDashboardButtonGroupButton mapInputModelToModel(MDashboardButtonGroupButton entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}

	public List<MDashboardButtonGroupButton> get(boolean isAdmin, Properties idempiereContext) {
		return getMenuGroupLineItems(isAdmin, idempiereContext);
	}

	public boolean hasAccessToReports(boolean isAdmin, Properties idempiereContext) {
		// Retrieve Reports Menu
		MDashboardButtonGroup menu = new Query(idempiereContext, MDashboardButtonGroup.Table_Name,
				MDashboardButtonGroup.COLUMNNAME_Name + "=?", null)
				.setOrderBy(MDashboardButtonGroup.COLUMNNAME_LineNo).setOnlyActiveRecords(true)
				.setParameters("Reports").first();
		if (menu == null) {
			return false;
		}

		// Get reports
		List<MDashboardButtonGroupButton> reports = getMenuGroupLineItems(isAdmin, menu.get_ID(), null);
		return reports != null && !reports.isEmpty();
	}

	private List<MDashboardButtonGroupButton> getMenuGroupLineItems(boolean isAdmin, Properties idempiereContext) {
		return getMenuGroupLineItems(isAdmin, 0, idempiereContext);
	}

	/**
	 * Get group line items
	 *
	 * @param menuGroupItemId
	 * @return
	 */
	private List<MDashboardButtonGroupButton> getMenuGroupLineItems(boolean isAdmin, int menuGroupItemId,
			Properties idempiereContext) {
		return getMenuGroupLineItems(isAdmin, menuGroupItemId, "'Metrics', 'Reports'", idempiereContext);
	}

	private List<MDashboardButtonGroupButton> getMenuGroupLineItems(boolean isAdmin, int menuGroupItemId, String exclude,
			Properties idempiereContext) {
		try {
			List<Object> parameters = new ArrayList<>();

			StringBuilder whereClause = new StringBuilder();
			if (menuGroupItemId > 0) {
				whereClause = new StringBuilder(
						MDashboardButtonGroupButton.Table_Name + "." + MDashboardButtonGroupButton.COLUMNNAME_BH_DbrdBtnGrp_ID)
						.append("=?");
				parameters.add(menuGroupItemId);
			} else {
				// filter out metrics and reports by default
				whereClause.append(MDashboardButtonGroup.Table_Name).append(".")
						.append(MDashboardButtonGroup.COLUMNNAME_Name);

				if (exclude != null) {
					whereClause.append(" NOT IN (").append(exclude).append(")");
				}
			}

			if (!isAdmin) {
				whereClause.append(" AND ");

				whereClause.append(MRoleIncluded.Table_Name + "." + MRoleIncluded.COLUMNNAME_AD_Role_ID + "=?");
				parameters.add(Env.getAD_Role_ID(idempiereContext));
			}

			BandaQuery<MDashboardButtonGroupButton> query = getBaseQuery(idempiereContext, whereClause.toString(), parameters)
					.setOnlyActiveRecords(true)
					.setOrderBy(MDashboardButtonGroupButton.Table_Name + "." + MDashboardButtonGroupButton.COLUMNNAME_LineNo);

			// join MDashboardButtonGroup Table
			query = query.addJoinClause(" JOIN " + MDashboardButtonGroup.Table_Name + " ON "
					+ MDashboardButtonGroupButton.Table_Name + "." + MDashboardButtonGroupButton.COLUMNNAME_BH_DbrdBtnGrp_ID + "="
					+ MDashboardButtonGroup.Table_Name + "."
					+ MDashboardButtonGroup.COLUMNNAME_BH_DbrdBtnGrp_ID);

			if (!isAdmin) {
				// join Role Table
				query = query.addJoinClause(" JOIN " + MRoleIncluded.Table_Name + " ON " + MDashboardButtonGroupButton.Table_Name
						+ "." + MDashboardButtonGroupButton.COLUMNNAME_Included_Role_ID + "=" + MRoleIncluded.Table_Name + "."
						+ MRoleIncluded.COLUMNNAME_Included_Role_ID);
			}

			return query.list();
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}

		return null;
	}
}
