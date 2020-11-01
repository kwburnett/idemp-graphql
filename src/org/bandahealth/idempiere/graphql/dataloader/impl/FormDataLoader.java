package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.FormRepository;
import org.compiere.model.MForm;

public class FormDataLoader extends BaseDataLoader<MForm, MForm, FormRepository> implements DataLoaderRegisterer {
	public static final String FORM_BY_ID_DATA_LOADER = "formByIdDataLoader";
	public static final String FORM_BY_UUID_DATA_LOADER = "formByUuidDataLoader";
	private final FormRepository formRepository;

	public FormDataLoader() {
		formRepository = new FormRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return FORM_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return FORM_BY_UUID_DATA_LOADER;
	}

	@Override
	protected FormRepository getRepositoryInstance() {
		return formRepository;
	}
}
