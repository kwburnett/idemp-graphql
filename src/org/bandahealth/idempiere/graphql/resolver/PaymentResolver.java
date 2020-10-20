package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.model.DocStatus;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

public class PaymentResolver extends BaseResolver<MPayment_BH> implements GraphQLResolver<MPayment_BH> {

	private final ReferenceListRepository referenceListRepository;

	public PaymentResolver() {
		referenceListRepository = new ReferenceListRepository();
	}

	public MBPartner_BH businessPartner(MPayment_BH entity) {
		return (MBPartner_BH) entity.getC_BPartner();
	}

	public int chargeId(MPayment_BH entity) {
		return entity.getC_Charge_ID();
	}

	public int orderId(MPayment_BH entity) {
		return entity.getC_Order_ID() == 0 ? entity.getBH_C_Order_ID() : entity.getC_Order_ID();
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

	public String nhifType(MPayment_BH entity) {
		return entity.getBH_NHIF_Type();
	}

	public String nhifRelationship(MPayment_BH entity) {
		return entity.getbh_nhif_relationship();
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

	public DocStatus docStatus(MPayment_BH entity) {
		return DocStatus.valueOf(entity.getDocStatus());
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
