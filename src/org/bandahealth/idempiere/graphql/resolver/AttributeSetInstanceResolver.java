package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.AttributeSetDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class AttributeSetInstanceResolver extends BaseResolver<MAttributeSetInstance>
		implements GraphQLResolver<MAttributeSetInstance> {

	public CompletableFuture<MAttributeSet> attributeSet(MAttributeSetInstance entity,
			DataFetchingEnvironment environment) {
		final DataLoader<String, MAttributeSet> attributeSetDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(AttributeSetDataLoader.ATTRIBUTE_SET_BY_ID_DATA_LOADER);
		return attributeSetDataLoader.load(ModelUtil.getModelKey(entity, entity.getM_AttributeSet_ID()));
	}
}
