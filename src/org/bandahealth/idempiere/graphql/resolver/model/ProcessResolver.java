package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.FormDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProcessParameterDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReportViewDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.WorkflowDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MForm;
import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.model.MReportView;
import org.compiere.wf.MWorkflow;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Process resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class ProcessResolver extends BaseResolver<MProcess> implements GraphQLResolver<MProcess> {

	public CompletableFuture<MForm> form(MProcess entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MForm> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(FormDataLoader.FORM_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getAD_Process_ID());
	}

	public CompletableFuture<MWorkflow> workflow(MProcess entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MWorkflow> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(WorkflowDataLoader.WORKFLOW_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getAD_Workflow_ID());
	}

	public CompletableFuture<MReportView> reportView(MProcess entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MReportView> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ReportViewDataLoader.REPORT_VIEW_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getAD_ReportView_ID());
	}

	public CompletableFuture<List<MProcessPara>> parameters(MProcess entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MProcessPara>> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(
						ProcessParameterDataLoader.PROCESS_PARAMETER_BY_PROCESS_DATA_LOADER
				);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getAD_Process_ID()));
	}
}
