package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.process.ExpenseProcess;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessRepository extends BaseRepository<MProcess, MProcess> {

	// report names
	public static final String INCOME_EXPENSE_REPORT = "Income & Expenses";
	public static final String THERMAL_RECEIPT_REPORT = "BH Thermal Receipt";
	public static final String PATIENT_TRANSACTIONS_REPORT = "Patient Transactions";
	public static final String STOCK_REORDER_REPORT = "Stock to be Ordered";
	public static final String PRODUCT_AND_PRICES_REPORT = "Products and Prices";
	public static final String VALUE_OPENING_CLOSING_STOCK_REPORT = "Value of Opening and Closing Stock";
	public static final String MOH705A_PATIENT_VISITS_REFERRALS_REPORT = "MoH705A Patient Visits and Referrals";
	public static final String MOH705A_OUTPATIENT_UNDER_5_SUMMARY_REPORT = "MoH705A Out Patient Under 5yr Summary";
	public static final String MOH717_NEW_REVISIT_PATIENT_COUNT_REPORT = "MoH717 New and Revisit Patient Count";
	public static final String MOH705B_OUTPATIENT_OVER5_SUMMARY_REPORT = "MoH705B Out Patient Over 5yr Summary";
	public static final String INVENTORY_SOLD_REPORT = "Inventory Sold Report";
	public static final String STOCK_DISCREPANCY_REPORT = "Stock Discrepancy Report";
	public static final String DONOR_FUND_REPORT = "Donor Fund Report";
	public static final String DEBT_PAYMENT_RECEIPT = "Debt Payment Receipt";

	public final static String PROCESSING_MESSAGE = "Processing Transaction";

	private final String SALES_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.SalesProcess";
	private final String STOCKTAKE_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.StockTakeProcess";
	private final String EXPENSE_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.ExpenseProcess";
	private final String QUANTITY = "QUANTITY";

	public Connection<MProcess> get(String filter, String sort, PagingInfo pagingInfo) {
		List<Object> parameters = new ArrayList<>();
		List<String> reportNames = new ArrayList<>() {{
			add(INCOME_EXPENSE_REPORT);
			add(THERMAL_RECEIPT_REPORT);
			add(PATIENT_TRANSACTIONS_REPORT);
			add(STOCK_REORDER_REPORT);
			add(PRODUCT_AND_PRICES_REPORT);
			add(VALUE_OPENING_CLOSING_STOCK_REPORT);
			add(MOH705A_PATIENT_VISITS_REFERRALS_REPORT);
			add(MOH705A_OUTPATIENT_UNDER_5_SUMMARY_REPORT);
			add(MOH717_NEW_REVISIT_PATIENT_COUNT_REPORT);
			add(MOH705B_OUTPATIENT_OVER5_SUMMARY_REPORT);
			add(INVENTORY_SOLD_REPORT);
			add(STOCK_DISCREPANCY_REPORT);
			add(DONOR_FUND_REPORT);
			add(DEBT_PAYMENT_RECEIPT);
		}};

		String reportNameList = reportNames.stream().map(reportName -> "'" + reportName + "'")
				.collect(Collectors.joining(","));

		// Get the list of IDs of de-duplicated report names
		String query = "SELECT " + MProcess.COLUMNNAME_AD_Process_ID + " FROM " + MProcess.Table_Name +
				" ap JOIN (SELECT " + MProcess.COLUMNNAME_Name + ", MAX(" + MProcess.COLUMNNAME_Updated + ") AS " +
				MProcess.COLUMNNAME_Updated + " FROM " + MProcess.Table_Name + " WHERE " + MProcess.COLUMNNAME_Name + " IN (" +
				reportNameList + ") GROUP BY " + MProcess.COLUMNNAME_Name + ") apgroup ON ap." + MProcess.COLUMNNAME_Name +
				"=apgroup." + MProcess.COLUMNNAME_Name + " AND ap." + MProcess.COLUMNNAME_Updated + "=apgroup." +
				MProcess.COLUMNNAME_Updated;
		List<Integer> processIDs = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = DB.prepareStatement(query, null);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				processIDs.add(resultSet.getInt(1));
			}
		} catch (Exception e) {
			logger.warning("Error fetching deduplicated process name list");
			processIDs = new ArrayList<>();
		}

		String whereClause = MProcess.COLUMNNAME_AD_Process_ID + " IN (" + processIDs.stream().map(Object::toString)
				.collect(Collectors.joining(",")) + ")";
		if (processIDs.isEmpty()) {
			whereClause = MProcess.COLUMNNAME_Name + " IN (" + reportNames.stream()
					.map(reportName -> "'" + reportName + "'").collect(Collectors.joining(",")) + ")";
		}

		return super.get(filter, sort, pagingInfo, whereClause, parameters);
	}

	/**
	 * Run process
	 *
	 * @param request
	 * @return
	 */
	public static ProcessInfo runProcess(ProcessInfo request) {
		if (request == null) {
			return null;
		}

		MProcess mprocess = new Query(Env.getCtx(), MProcess.Table_Name, MProcess.COLUMNNAME_AD_Process_UU
				+ "=?", null)
				.setOnlyActiveRecords(true).setParameters(request.getAD_Process_UU()).first();

		MPInstance mpInstance = new MPInstance(mprocess, 0);

		ProcessInfo processInfo = new ProcessInfo(mprocess.getName(), mprocess.getAD_Process_ID());
		processInfo.setAD_PInstance_ID(mpInstance.getAD_PInstance_ID());
		processInfo.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		processInfo.setAD_PInstance_ID(mpInstance.getAD_PInstance_ID());
		processInfo.setAD_Process_UU(mprocess.getAD_Process_UU());

		if (request.getParameter() != null && request.getParameter().length > 0) {
			processInfo.setParameter(request.getParameter());
		}

		ServerProcessCtl.process(processInfo, null);

		return processInfo;
	}

	/**
	 * Call and run the SalesProcess
	 *
	 * @param orderId
	 */
	public String runOrderProcess(int orderId) {
		MProcess mprocess = new Query(Env.getCtx(), MProcess.Table_Name, MProcess.COLUMNNAME_Classname + "=?",
				null)
				.setOnlyActiveRecords(true).setParameters(SALES_PROCESS_CLASS_NAME).first();

		MPInstance mpInstance = new MPInstance(mprocess, 0);

		ProcessInfo processInfo = new ProcessInfo(mprocess.getName(), mprocess.getAD_Process_ID());
		processInfo.setAD_PInstance_ID(mpInstance.getAD_PInstance_ID());
		processInfo.setAD_Process_UU(mprocess.getAD_Process_UU());

		processInfo.setParameter(new ProcessInfoParameter[]{
				new ProcessInfoParameter(MOrder_BH.COLUMNNAME_C_Order_ID, orderId, null, null, null)});

		ServerProcessCtl.process(processInfo, null);

		return PROCESSING_MESSAGE;
	}

	/**
	 * Call and run the SalesProcess
	 *
	 * @param invoiceId
	 */
	public String runExpenseProcess(int invoiceId, boolean delete) {
		MProcess mprocess = new Query(
				Env.getCtx(),
				MProcess.Table_Name,
				MProcess.COLUMNNAME_Classname + "=?",
				null
		)
				.setOnlyActiveRecords(true).setParameters(EXPENSE_PROCESS_CLASS_NAME).first();

		MPInstance mpInstance = new MPInstance(mprocess, 0);

		ProcessInfo processInfo = new ProcessInfo(mprocess.getName(), mprocess.getAD_Process_ID());
		processInfo.setAD_PInstance_ID(mpInstance.getAD_PInstance_ID());
		processInfo.setAD_Process_UU(mprocess.getAD_Process_UU());

		processInfo.setParameter(new ProcessInfoParameter[]{
				new ProcessInfoParameter(ExpenseProcess.PARAMETERNAME_C_INVOICE_ID, invoiceId, null, null, null),
				new ProcessInfoParameter(
						ExpenseProcess.PARAMETERNAME_PROCESS_ACTION,
						delete ? ExpenseProcess.PROCESSACTION_Remove : ExpenseProcess.PROCESSACTION_Complete,
						null,
						null,
						null
				)
		});

		ServerProcessCtl.process(processInfo, null);

		return PROCESSING_MESSAGE;
	}

	/**
	 * Call and run StockTake process
	 *
	 * @param productID
	 * @param attributeSetInstanceId
	 * @param quantity
	 */
	public String runStockTakeProcess(int productID, int attributeSetInstanceId, int quantity) {
		MProcess mprocess = new Query(Env.getCtx(), MProcess.Table_Name, MProcess.COLUMNNAME_Classname + "=?", null)
				.setOnlyActiveRecords(true).setParameters(STOCKTAKE_PROCESS_CLASS_NAME).first();

		MPInstance mpInstance = new MPInstance(mprocess, 0);

		ProcessInfo processInfo = new ProcessInfo(mprocess.getName(), mprocess.getAD_Process_ID());
		processInfo.setAD_PInstance_ID(mpInstance.getAD_PInstance_ID());
		processInfo.setAD_Process_UU(mprocess.getAD_Process_UU());

		processInfo
				.setParameter(new ProcessInfoParameter[]{
						new ProcessInfoParameter(MStorageOnHand.COLUMNNAME_M_Product_ID, productID, null, null, null),
						new ProcessInfoParameter(MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID,
								attributeSetInstanceId, null, null, null),
						new ProcessInfoParameter(QUANTITY, quantity, null, null, null)});

		ServerProcessCtl.process(processInfo, null);

		return processInfo.getSummary();
	}

	@Override
	public MProcess getModelInstance() {
		return new MProcess(Env.getCtx(), 0, null);
	}

	@Override
	public MProcess save(MProcess entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
