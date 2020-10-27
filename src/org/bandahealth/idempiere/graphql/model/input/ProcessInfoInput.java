package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.graphql.model.ReportOutput;
import org.compiere.process.ProcessInfo;

import java.util.List;

public class ProcessInfoInput extends ProcessInfo {
	private ProcessInput process;
	private ReportOutput reportOutputType;
	private List<ProcessInfoParameterInput> parameters;

	public ProcessInfoInput() {
		super("", 0);
	}

	public ProcessInfoInput(String Title, int AD_Process_ID, int Table_ID, int Record_ID) {
		super(Title, AD_Process_ID, Table_ID, Record_ID);
	}

	public ProcessInfoInput(String Title, int AD_Process_ID) {
		super(Title, AD_Process_ID);
	}

	public void setProcess(ProcessInput processInput) {
		this.process = processInput;
	}

	public ProcessInput getProcess() {
		return process;
	}

	public void setReportOutputType(ReportOutput reportOutputType) {
		this.reportOutputType = reportOutputType;
	}

	public ReportOutput getReportOutputType() {
		return reportOutputType;
	}

	public void setParameters(List<ProcessInfoParameterInput> parameters) {
		this.parameters = parameters;
	}

	public List<ProcessInfoParameterInput> getParameters() {
		return parameters;
	}
}
