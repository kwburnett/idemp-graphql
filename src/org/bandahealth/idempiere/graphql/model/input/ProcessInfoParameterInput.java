package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.process.ProcessInfoParameter;

public class ProcessInfoParameterInput extends ProcessInfoParameter {
	private ProcessParameterInput processParameter;
	private Object parameterTo;
	private String infoTo;

	public ProcessInfoParameterInput() {
		super("", null, null, null, null);
	}

	/**
	 * Construct Parameter
	 *
	 * @param parameterName parameter name
	 * @param parameter     parameter
	 * @param parameter_To  to parameter
	 * @param info          info
	 * @param info_To       to info
	 */
	public ProcessInfoParameterInput(String parameterName, Object parameter, Object parameter_To, String info, String info_To) {
		super(parameterName, parameter, parameter_To, info, info_To);
	}

	public void setProcessParameter(ProcessParameterInput processParameter) {
		this.processParameter = processParameter;
	}

	public ProcessParameterInput getProcessParameter() {
		return processParameter;
	}

	public void setParameterTo(Object parameterTo) {
		setParameter_To(parameterTo);
	}

	public void setInfoTo(String infoTo) {
		setInfo_To(infoTo);
	}
}
