package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.AttributeSetDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.OrderDataLoader;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class AttributeSetInstanceResolver extends BaseResolver<MAttributeSetInstance>
		implements GraphQLResolver<MAttributeSetInstance> {

	public CompletableFuture<MAttributeSet> attributeSet(MAttributeSetInstance entity,
																											 DataFetchingEnvironment environment) {
		final DataLoader<Integer, MAttributeSet> attributeSetDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(AttributeSetDataLoader.ATTRIBUTE_SET_DATA_LOADER);
		return attributeSetDataLoader.load(entity.getM_AttributeSet_ID());
	}
}
