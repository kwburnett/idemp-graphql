package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.BusinessPartnerDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.compiere.util.Env;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

public class PaymentResolver extends BaseResolver<MPayment_BH> implements GraphQLResolver<MPayment_BH> {

	private final ReferenceListRepository referenceListRepository;

	public PaymentResolver() {
		referenceListRepository = new ReferenceListRepository();
	}

	public CompletableFuture<MBPartner_BH> businessPartner(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MBPartner_BH> businessPartnerDataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(BusinessPartnerDataLoader.BUSINESS_PARTNER_DATA_LOADER);
		return businessPartnerDataLoader.load(entity.getC_BPartner_ID());
	}

	public BigDecimal payAmount(MPayment_BH entity) {
		return entity.getPayAmt();
	}

	public CompletableFuture<MRefList> paymentType(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> paymentTypeDataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(ReferenceListDataLoader.ORDER_PAYMENT_TYPE_DATA_LOADER);
		return paymentTypeDataLoader.load(entity.getTenderType());
	}

	public CompletableFuture<MRefList> nhifType(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(ReferenceListDataLoader.NHIF_TYPE_DATA_LOADER);
		if (entity.getBH_NHIF_Type() == null) {
			return CompletableFuture.supplyAsync(() -> new MRefList(Env.getCtx(), 0, null));
		}
		return dataLoader.load(entity.getBH_NHIF_Type());
	}

	public CompletableFuture<MRefList> nhifRelationship(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(ReferenceListDataLoader.NHIF_RELATIONSHIP_DATA_LOADER);
		return dataLoader.load(entity.getbh_nhif_relationship());
	}

	public String claimNumber(MPayment_BH entity) {
		return entity.getbh_nhif_claim_number();
	}

	public String memberId(MPayment_BH entity) {
		return entity.getbh_nhif_member_id();
	}

	public String number(MPayment_BH entity) {
		return entity.getNHIF_Number();
	}

	public String memberName(MPayment_BH entity) {
		return entity.getbh_nhif_member_name();
	}

	public Timestamp transactionDate(MPayment_BH entity) {
		return entity.getDateTrx();
	}

	public BigDecimal tenderAmount(MPayment_BH entity) {
		return entity.getBH_TenderAmount();
	}

	public CompletableFuture<MOrder_BH> order(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MOrder_BH> orderDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderDataLoader.ORDER_DATA_LOADER);
		return orderDataLoader.load(entity.getBH_C_Order_ID() == 0 ? entity.getC_Order_ID() : entity.getBH_C_Order_ID());
	}

	public CompletableFuture<MCharge_BH> charge(MPayment_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MCharge_BH> chargeDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ChargeDataLoader.CHARGE_DATA_LOADER);
		return chargeDataLoader.load(entity.getC_Charge_ID());
	}
}
