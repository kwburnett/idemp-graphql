package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.process.ExpenseProcess;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.Env;

public class ProcessRepository extends BaseRepository<MProcess, MProcess> {

	public final static String PROCESSING_MESSAGE = "Processing Transaction";

	private final String SALES_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.SalesProcess";
	private final String STOCKTAKE_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.StockTakeProcess";
	private final String EXPENSE_PROCESS_CLASS_NAME = "org.bandahealth.idempiere.base.process.ExpenseProcess";
	private final String QUANTITY = "QUANTITY";

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

		processInfo.setParameter(new ProcessInfoParameter[] {
				new ProcessInfoParameter(MOrder_BH.COLUMNNAME_C_Order_ID, orderId, null, null, null) });

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

		processInfo.setParameter(new ProcessInfoParameter[] {
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
				.setParameter(new ProcessInfoParameter[] {
						new ProcessInfoParameter(MStorageOnHand.COLUMNNAME_M_Product_ID, productID, null, null, null),
						new ProcessInfoParameter(MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID,
								attributeSetInstanceId, null, null, null),
						new ProcessInfoParameter(QUANTITY, quantity, null, null, null) });

		ServerProcessCtl.process(processInfo, null);

		return processInfo.getSummary();
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

	@Override
	public MProcess getModelInstance() {
		return new MProcess(Env.getCtx(), 0, null);
	}

	@Override
	public MProcess save(MProcess entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
