package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MProcessPara;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Process Parameter resolver containing specific methods to fetch non-standard iDempiere properties for the
 * consumer
 */
public class ProcessParameterResolver extends BaseResolver<MProcessPara> implements GraphQLResolver<MProcessPara> {

	public CompletableFuture<MReference> reference(MProcessPara entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MReference> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ReferenceDataLoader.REFERENCE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getAD_Reference_ID());
	}

	public CompletableFuture<List<MRefList>> referenceValue(MProcessPara entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MRefList>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ReferenceListDataLoader.REFERENCE_LIST_BY_REFERENCE_DATA_LOADER);
		CompletableFuture<List<MRefList>> referenceValues = dataLoader
				.load(ModelUtil.getModelKey(entity, entity.getAD_Reference_Value_ID()));
		return reference(entity, environment).thenCombine(referenceValues, (reference, referenceList) -> {
			if (reference != null && !StringUtil.isNullOrEmpty(reference.getName()) &&
					reference.getName().equalsIgnoreCase("List")) {
				return referenceList;
			}
			return new ArrayList<>();
		});
	}
}
