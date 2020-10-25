package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.FormRepository;
import org.compiere.model.MForm;

public class FormDataLoader extends BaseDataLoader<MForm, MForm, FormRepository> implements DataLoaderRegisterer {

	public static final String FORM_DATA_LOADER = "formDataLoader";
	private final FormRepository formRepository;

	public FormDataLoader() {
		formRepository = new FormRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return FORM_DATA_LOADER;
	}

	@Override
	protected FormRepository getRepositoryInstance() {
		return formRepository;
	}
}
