package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.DocStatus;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.bandahealth.idempiere.graphql.utils.DateUtil;
import org.compiere.model.MRefList;

import java.math.BigDecimal;

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

	public MRefList paymentType(MPayment_BH entity) {
		return referenceListRepository.getInvoicePaymentType(entity.getTenderType());
	}

	// TODO: Update our model to capture these fields
	public String nhifType(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	// TODO: Update our model to capture these fields
	public String nhifRelationship(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	// TODO: Update our model to capture these fields
	public String claimNumber(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	// TODO: Update our model to capture these fields
	public String memberId(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	// TODO: Update our model to capture these fields
	public String number(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	// TODO: Update our model to capture these fields
	public String memberName(MPayment_BH entity) {
//		return entity.getbh
		return "not working yet";
	}

	public DocStatus docStatus(MPayment_BH entity) {
		return DocStatus.valueOf(entity.getDocStatus());
	}

	public String transactionDate(MPayment_BH entity) {
		return DateUtil.parseDateOnly(entity.getDateTrx());
	}

	public BigDecimal tenderAmount(MPayment_BH entity) {
		return entity.getBH_TenderAmount();
	}
}
