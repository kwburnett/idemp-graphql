package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.repository.WorkflowRepository;
import org.compiere.wf.MWorkflow;

public class WorkflowDataLoader extends BaseDataLoader<MWorkflow, MWorkflow, WorkflowRepository>
		implements DataLoaderRegisterer {
	public static final String WORKFLOW_DATA_LOADER = "workflowDataLoader";
	public static final String WORKFLOW_BY_UUID_DATA_LOADER = "workflowByUuidDataLoader";
	private final WorkflowRepository workflowRepository;

	public WorkflowDataLoader() {
		workflowRepository = new WorkflowRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return WORKFLOW_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return WORKFLOW_BY_UUID_DATA_LOADER;
	}

	@Override
	protected WorkflowRepository getRepositoryInstance() {
		return workflowRepository;
	}
}
