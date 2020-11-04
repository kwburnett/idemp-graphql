package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.input.StorageOnHandInput;
import org.bandahealth.idempiere.graphql.utils.*;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MStorageOnHand;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class StorageOnHandRepository extends BaseRepository<MStorageOnHand, StorageOnHandInput> {

	public static final List<String> GROUPING_COLUMNS_FOR_COLUMN_MODIFICATIONS = Arrays.asList(
			MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_Product_ID,
			MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_Locator_ID,
			MAttributeSetInstance.Table_Name + "." + MAttributeSetInstance.COLUMNNAME_GuaranteeDate
	);
	private static final String COLUMNSELECT_QtyOnHand = "SUM(" + MStorageOnHand.Table_Name + "." +
			MStorageOnHand.COLUMNNAME_QtyOnHand + ")";
	private static final String COLUMNSELECT_M_AttributeSetInstance_ID = "MAX(" + MStorageOnHand.Table_Name + "." +
			MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + ")";
	public static final Map<String, String> MODIFIED_COLUMNS = new HashMap<>() {{
		put(MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID, COLUMNSELECT_M_AttributeSetInstance_ID);
		put(MStorageOnHand.COLUMNNAME_QtyOnHand, COLUMNSELECT_QtyOnHand);
	}};
	private static final List<String> UNMODIFIED_COLUMNS = Arrays.asList(
			MStorageOnHand.COLUMNNAME_AD_Client_ID,
			MStorageOnHand.COLUMNNAME_AD_Org_ID,
			MStorageOnHand.COLUMNNAME_Created,
			MStorageOnHand.COLUMNNAME_CreatedBy,
			MStorageOnHand.COLUMNNAME_DateLastInventory,
			MStorageOnHand.COLUMNNAME_IsActive,
			MStorageOnHand.COLUMNNAME_M_Locator_ID,
			MStorageOnHand.COLUMNNAME_M_Product_ID,
			MStorageOnHand.COLUMNNAME_Updated,
			MStorageOnHand.COLUMNNAME_UpdatedBy,
			MStorageOnHand.COLUMNNAME_M_StorageOnHand_UU,
			MStorageOnHand.COLUMNNAME_DateMaterialPolicy
	);
	private final String unmodifiedTableAlias = "usoh";
	private final String modifiedTableAlias = "msoh";
	/**
	 * This generates a query like the following:
	 * SELECT
	 * soh.ad_client_id,
	 * soh.ad_org_id,
	 * soh.created,
	 * soh.createdby,
	 * soh.datelastinventory,
	 * soh.isactive,
	 * groupsoh.m_attributesetinstance_id,
	 * soh.m_locator_id,
	 * soh.m_product_id,
	 * groupsoh.qtyonhand,
	 * soh.updated,
	 * soh.updatedby,
	 * soh.m_storageonhand_uu,
	 * soh.datematerialpolicy
	 * FROM m_storageonhand soh
	 * INNER JOIN (
	 * SELECT s_1.m_product_id,
	 * s_1.m_locator_id,
	 * sum(s_1.qtyonhand) as qtyonhand,
	 * max(s_1.m_attributesetinstance_id) AS m_attributesetinstance_id,
	 * asi_1.guaranteedate
	 * FROM adempiere.m_storage s_1
	 * LEFT JOIN adempiere.m_attributesetinstance asi_1
	 * ON s_1.m_attributesetinstance_id = asi_1.m_attributesetinstance_id
	 * GROUP BY s_1.m_product_id, s_1.m_locator_id, asi_1.guaranteedate
	 * ) as groupsoh
	 * ON groupsoh.m_product_id = soh.m_product_id
	 * AND groupsoh.m_locator_id = soh.m_locator_id
	 * AND groupsoh.m_attributesetinstance_id = soh.m_attributesetinstance_id
	 */
	private final String tableQuery = "SELECT " + UNMODIFIED_COLUMNS.stream().map(unmodifiedColumn ->
			"usoh." + unmodifiedColumn).collect(Collectors.joining(",")) + "," + MODIFIED_COLUMNS.keySet().stream()
			.map(modifiedColumnName -> "msoh." + modifiedColumnName).collect(Collectors.joining(",")) + " FROM " +
			MStorageOnHand.Table_Name + " usoh INNER JOIN (SELECT " +
			String.join(",", GROUPING_COLUMNS_FOR_COLUMN_MODIFICATIONS) + "," +
			MODIFIED_COLUMNS.entrySet().stream().map(modifiedColumn -> modifiedColumn.getValue() + " AS " +
					modifiedColumn.getKey()).collect(Collectors.joining(",")) + " FROM " +
			MStorageOnHand.Table_Name + " LEFT JOIN " + MAttributeSetInstance.Table_Name + " ON " +
			MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + "=" +
			MAttributeSetInstance.Table_Name + "." + MAttributeSetInstance.COLUMNNAME_M_AttributeSetInstance_ID +
			" GROUP BY " + String.join(",", GROUPING_COLUMNS_FOR_COLUMN_MODIFICATIONS) + ") AS msoh ON msoh." +
			MStorageOnHand.COLUMNNAME_M_Product_ID + "=usoh." + MStorageOnHand.COLUMNNAME_M_Product_ID + " AND msoh." +
			MStorageOnHand.COLUMNNAME_M_Locator_ID + "=usoh." + MStorageOnHand.COLUMNNAME_M_Locator_ID + " AND msoh." +
			MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + "=usoh." +
			MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID;

	private final AttributeSetInstanceRepository attributeSetInstanceRepository;
	private final ProcessRepository processRepository;

	public StorageOnHandRepository() {
		attributeSetInstanceRepository = new AttributeSetInstanceRepository();
		processRepository = new ProcessRepository();
	}

	@Override
	public MStorageOnHand getModelInstance() {
		return new StorageOnHandInput();
	}

	@Override
	public MStorageOnHand mapInputModelToModel(StorageOnHandInput entity) {
		MStorageOnHand storageOnHand = getByUuid(entity.getM_StorageOnHand_UU());
		if (storageOnHand == null) {
			throw new AdempiereException("Storage entity not specified");
		}
		processRepository.runStockTakeProcess(storageOnHand.getM_Product_ID(),
				storageOnHand.getM_AttributeSetInstance_ID(), entity.getQuantityOnHand());
		return getBaseQuery(MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_Product_ID +
						"=? AND " + MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + "=?",
				storageOnHand.getM_Product_ID(), storageOnHand.getM_AttributeSetInstance_ID()).first();
	}

	@Override
	public BandaQuery<MStorageOnHand> getBaseQuery(String additionalWhereClause, Object... parameters) {
		StringBuilder sqlSelect = new StringBuilder().append("SELECT ").append(UNMODIFIED_COLUMNS.stream()
				.map(unmodifiedColumn -> MStorageOnHand.Table_Name + "." + unmodifiedColumn)
				.collect(Collectors.joining(","))).append(",").append(MODIFIED_COLUMNS.keySet().stream()
				.map(modifiedColumn -> MStorageOnHand.Table_Name + "." + modifiedColumn)
				.collect(Collectors.joining(",")));
		StringBuilder sqlFromAndJoin = new StringBuilder(" FROM (").append(tableQuery);
		// If there is anything dealing with guarantee date in the where clause, it should be moved into this table
		// query to improve performance
		boolean wasGuaranteeDateClauseMoved = false;
		int numberOfParametersAfterGuaranteeDate = 0;
		if (additionalWhereClause.toLowerCase().contains((MAttributeSetInstance.Table_Name + "." +
				MAttributeSetInstance.COLUMNNAME_GuaranteeDate).toLowerCase())) {
			wasGuaranteeDateClauseMoved = true;
			int indexOfAliasForGuaranteeDate = additionalWhereClause.toLowerCase().indexOf(
					(MAttributeSetInstance.Table_Name + "." + MAttributeSetInstance.COLUMNNAME_GuaranteeDate).toLowerCase());
			int indexOfEndOfGuaranteeDateClause = additionalWhereClause.indexOf("?", indexOfAliasForGuaranteeDate) + 1;
			String guaranteeDateWhereClause = additionalWhereClause.substring(indexOfAliasForGuaranteeDate,
					indexOfEndOfGuaranteeDateClause);
			int indexToSearchForParametersFrom = indexOfEndOfGuaranteeDateClause;
			while (additionalWhereClause.indexOf("?", indexToSearchForParametersFrom) > -1) {
				indexToSearchForParametersFrom = additionalWhereClause.indexOf("?", indexToSearchForParametersFrom) + 1;
				numberOfParametersAfterGuaranteeDate++;
			}
			// Replace the condition we pulled out to just be
			additionalWhereClause = additionalWhereClause.replace(guaranteeDateWhereClause, "1=1");
			int indexOfAliasSeparator = guaranteeDateWhereClause.indexOf(".");
			sqlFromAndJoin.append(" WHERE ").append(modifiedTableAlias)
					.append(guaranteeDateWhereClause.substring(indexOfAliasSeparator));
		}
		sqlFromAndJoin.append(") AS ").append(MStorageOnHand.Table_Name).append(getDefaultJoinClause());

		// Since the JOIN clause was added, add any parameters
		final List<Object> parametersToUse = new ArrayList<>();
		if (getDefaultJoinClauseParameters() != null) {
			parametersToUse.addAll(getDefaultJoinClauseParameters());
		}

		StringBuilder sqlWhere = new StringBuilder().append(" WHERE ").append(MStorageOnHand.Table_Name).append(".")
				.append(MStorageOnHand.COLUMNNAME_AD_Client_ID).append("=?").append(" AND ").append(MStorageOnHand.Table_Name)
				.append(".").append(MStorageOnHand.COLUMNNAME_AD_Org_ID).append("=?");
		parametersToUse.add(Env.getAD_Client_ID(Env.getCtx()));
		parametersToUse.add(Env.getAD_Org_ID(Env.getCtx()));

		// Add the defaults
		sqlWhere.append(getDefaultWhereClause());
		if (getDefaultWhereClauseParameters() != null) {
			parametersToUse.addAll(getDefaultWhereClauseParameters());
		}

		if (!StringUtil.isNullOrEmpty(additionalWhereClause)) {
			sqlWhere.append(" AND ").append(additionalWhereClause);
		}
		// Lastly, handle any that were passed in
		if (parameters != null) {
			Arrays.stream(parameters).forEach(parameter -> {
				if (parameter instanceof List<?>) {
					parametersToUse.addAll((List<?>) parameter);
				} else {
					parametersToUse.add(parameter);
				}
			});
		}

		// Now we might need to move some parameters around
		if (wasGuaranteeDateClauseMoved) {
			// Remove the parameter from the list
			Object guaranteeDateParameter = parametersToUse.remove(parametersToUse.size() - 1
					- numberOfParametersAfterGuaranteeDate);
			// Add it to the front of the list, since it'll be first before even any JOIN clause parameters
			parametersToUse.add(0, guaranteeDateParameter);
		}

		String sqlFromJoinAndWhereClause = sqlFromAndJoin + sqlWhere.toString();
		final StringBuilder additionalJoins = new StringBuilder();
		final StringBuilder orderByBuilder = new StringBuilder();
		return new BandaQuery<>() {
			private boolean onlyActiveRecords = false;
			private int pageSize = 0;
			private int pagesToSkip = 0;

			@Override
			public List<MStorageOnHand> list() throws DBException {
				ResultSet resultSet = null;
				PreparedStatement statement = null;
				List<MStorageOnHand> results = new ArrayList<>();
				try {
					statement = buildSql();

					logger.warning(statement.toString());
					resultSet = statement.executeQuery();
					while (resultSet.next()) {
						results.add(new MStorageOnHand(Env.getCtx(), resultSet, null));
					}
				} catch (SQLException e) {
					logger.log(Level.SEVERE, getSQL(), e);
					throw new DBException(e, getSQL());
				} finally {
					DB.close(resultSet, statement);
				}
				return results;
			}

			@Override
			public MStorageOnHand first() throws DBException {
				ResultSet resultSet = null;
				PreparedStatement statement = null;
				try {
					statement = buildSql();

					resultSet = statement.executeQuery();
					if (resultSet.next()) {
						return new MStorageOnHand(Env.getCtx(), resultSet, null);
					}
					return null;
				} catch (SQLException e) {
					logger.log(Level.SEVERE, getSQL(), e);
					throw new DBException(e, getSQL());
				} finally {
					DB.close(resultSet, statement);
				}
			}

			@Override
			public String getSQL() throws DBException {
				try {
					return SqlUtil.getSql(buildSql());
				} catch (Exception exception) {
					logger.warning(exception.getMessage());
				}
				return sqlSelect + " " + getSqlFromJoinAndWhere() + " " + orderByBuilder.toString();
			}

			@Override
			public BandaQuery<MStorageOnHand> addJoinClause(String joinClause) {
				additionalJoins.append(joinClause);
				return this;
			}

			@Override
			public BandaQuery<MStorageOnHand> setOrderBy(String orderBy) {
				orderByBuilder.setLength(0);
				orderByBuilder.append(" ORDER BY ").append(orderBy);
				return this;
			}

			@Override
			public int count() throws DBException {
				return SqlUtil.getCount(sqlFromJoinAndWhereClause, parametersToUse);
			}

			@Override
			public BandaQuery<MStorageOnHand> setPage(int pPageSize, int pPagesToSkip) {
				pageSize = pPageSize;
				pagesToSkip = pPagesToSkip;
				return this;
			}

			@Override
			public BandaQuery<MStorageOnHand> setOnlyActiveRecords(boolean onlyActiveRecords) {
				this.onlyActiveRecords = onlyActiveRecords;
				return this;
			}

			@Override
			public BandaQuery<MStorageOnHand> setParameters(List<Object> parameters) {
				parametersToUse.clear();
				parametersToUse.addAll(parameters);
				return this;
			}

			@Override
			public boolean match() throws DBException {
				throw new UnsupportedOperationException("Not implemented");
			}

			private PreparedStatement buildSql() throws SQLException {
				String sqlToUse = sqlSelect.toString() + getSqlFromJoinAndWhere() + " " + orderByBuilder.toString();

				if (pageSize > 0) {
					if (DB.getDatabase().isPagingSupported()) {
						sqlToUse = DB.getDatabase().addPagingSQL(sqlToUse, (pageSize * pagesToSkip) + 1, pageSize *
								(pagesToSkip + 1));
					} else {
						throw new IllegalArgumentException("Pagination not supported by database");
					}
				}

				PreparedStatement statement = DB.prepareStatement(sqlToUse, null);
				DB.setParameters(statement, parametersToUse);
				int i = 1 + parametersToUse.size();

				if (this.onlyActiveRecords) {
					DB.setParameter(statement, i++, true);
					if (logger.isLoggable(Level.FINEST)) logger.finest("Parameter IsActive = Y");
				}

				return statement;
			}

			private String getSqlFromJoinAndWhere() {
				String sql = sqlFromAndJoin + " " + additionalJoins.toString();
				if (this.onlyActiveRecords) {
					if (sqlWhere.length() > 0) {
						sqlWhere.append(" AND ");
					}
					sqlWhere.append(MStorageOnHand.Table_Name).append(".IsActive=?");
				}
				return sql + sqlWhere.toString();
			}
		};
	}

	@Override
	public List<Object> getDefaultJoinClauseParameters() {
		return attributeSetInstanceRepository.getDefaultJoinClauseParameters();
	}

	@Override
	public String getDefaultJoinClause() {
		return " JOIN " + MAttributeSetInstance.Table_Name + " ON " + MAttributeSetInstance.Table_Name + "." +
				MAttributeSetInstance.COLUMNNAME_M_AttributeSetInstance_ID + "=" + MStorageOnHand.Table_Name + "." +
				MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + " " +
				attributeSetInstanceRepository.getDefaultJoinClause() + " JOIN " + MProduct_BH.Table_Name + " ON " +
				MProduct_BH.Table_Name + "." + MProduct_BH.COLUMNNAME_M_Product_ID + "=" + MStorageOnHand.Table_Name + "." +
				MStorageOnHand.COLUMNNAME_M_Product_ID;
	}
}
