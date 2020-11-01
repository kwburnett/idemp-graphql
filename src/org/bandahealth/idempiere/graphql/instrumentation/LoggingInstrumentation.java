package org.bandahealth.idempiere.graphql.instrumentation;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import org.compiere.util.CLogger;

public class LoggingInstrumentation extends SimpleInstrumentation {
	private final CLogger logger = CLogger.getCLogger(LoggingInstrumentation.class);

	@Override
	public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
		long startMillis = System.currentTimeMillis();
		return new SimpleInstrumentationContext<>(){
			@Override
			public void onCompleted(ExecutionResult result, Throwable t) {
				logger.info(parameters.getQuery() + " execution time (ms): " + (System.currentTimeMillis() - startMillis));
			}
		};
	}
}
