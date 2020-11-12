package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.dataloader.impl.AccountDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.AttributeSetDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.AttributeSetInstanceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.BusinessPartnerDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeTypeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ClientDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.FormDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.HomeScreenButtonDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.InvoiceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.InvoiceLineDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocatorDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderLineDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrganizationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.PaymentDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProcessDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProcessParameterDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProductCategoryDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProductDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReportViewDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleIncludedDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.StorageOnHandDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.UserDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.WarehouseDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.WorkflowDataLoader;
import org.dataloader.DataLoaderRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for containing all data loaders that need to be registered for each query. It holds a list
 * of available data loaders and, when a new query comes in, it registers them for that query.
 */
public class BandaDataLoaderComposer {
	/**
	 * This list of data loaders available for each query.
	 */
	private final List<DataLoaderRegisterer> dataLoaders;

	public BandaDataLoaderComposer() {
		dataLoaders = new ArrayList<>() {{
			add(new AccountDataLoader());
			add(new AttributeSetDataLoader());
			add(new AttributeSetInstanceDataLoader());
			add(new BusinessPartnerDataLoader());
			add(new ChargeDataLoader());
			add(new ChargeTypeDataLoader());
			add(new ClientDataLoader());
			add(new FormDataLoader());
			add(new HomeScreenButtonDataLoader());
			add(new InvoiceDataLoader());
			add(new InvoiceLineDataLoader());
			add(new LocationDataLoader());
			add(new LocatorDataLoader());
			add(new OrderDataLoader());
			add(new OrderLineDataLoader());
			add(new OrganizationDataLoader());
			add(new PaymentDataLoader());
			add(new ProcessDataLoader());
			add(new ProcessParameterDataLoader());
			add(new ProductCategoryDataLoader());
			add(new ProductDataLoader());
			add(new ReferenceDataLoader());
			add(new ReferenceListDataLoader());
			add(new ReportViewDataLoader());
			add(new RoleDataLoader());
			add(new RoleIncludedDataLoader());
			add(new StorageOnHandDataLoader());
			add(new UserDataLoader());
			add(new WarehouseDataLoader());
			add(new WorkflowDataLoader());
		}};
	}

	/**
	 * This method adds the data loaders when a new request comes in. Note, this is not a static method so hot-swaps
	 * will work if code changes are made to the data loaders.
	 *
	 * @param registry         The registry object that can register each data loader
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 */
	public void addDataLoaders(DataLoaderRegistry registry, Properties idempiereContext) {
		dataLoaders.forEach(dataLoader -> dataLoader.register(registry, idempiereContext));
	}
}
