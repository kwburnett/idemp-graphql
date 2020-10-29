package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MLocation;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessPartnerRepository extends BaseRepository<MBPartner_BH, BusinessPartnerInput> {

	private final LocationRepository locationRepository;
	private Map<String, String> dynamicJoins = new HashMap<>() {{
		put(MOrder_BH.Table_Name, "LEFT JOIN (" + "SELECT " + MOrder_BH.COLUMNNAME_C_BPartner_ID
				+ ",MAX(" + MOrder_BH.COLUMNNAME_DateOrdered + ") as " + MOrder_BH.COLUMNNAME_DateOrdered + " FROM "
				+ MOrder_BH.Table_Name + " WHERE " + MOrder_BH.COLUMNNAME_IsSOTrx + "='Y' GROUP BY "
				+ MOrder_BH.COLUMNNAME_C_BPartner_ID + ") AS " + MOrder_BH.Table_Name + " ON " + MOrder_BH.Table_Name + "."
				+ MOrder_BH.COLUMNNAME_C_BPartner_ID + "=" + MBPartner_BH.Table_Name + "."
				+ MBPartner_BH.COLUMNNAME_C_BPartner_ID);
	}};

	public BusinessPartnerRepository() {
		locationRepository = new LocationRepository();
	}

	@Override
	public Map<String, String> getDynamicJoins() {
		return dynamicJoins;
	}

	@Override
	public MBPartner_BH getModelInstance() {
		return new MBPartner_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MBPartner_BH save(BusinessPartnerInput entity) {
		try {
			MBPartner_BH businessPartner = getByUuid(entity.getC_BPartner_UU());
			if (businessPartner == null) {
				businessPartner = getModelInstance();
			}

			businessPartner.setBH_IsPatient(entity.isBH_IsPatient());
			businessPartner.setIsCustomer(entity.isCustomer());
			businessPartner.setIsVendor(entity.isVendor());

			ModelUtil.setPropertyIfPresent(entity.getName(), businessPartner::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), businessPartner::setDescription);
			ModelUtil.setPropertyIfPresent(entity.getBH_PatientID(), businessPartner::setBH_PatientID);
			ModelUtil.setPropertyIfPresent(entity.getBH_Birthday(), businessPartner::setBH_Birthday);
			ModelUtil.setPropertyIfPresent(entity.getBH_Phone(), businessPartner::setBH_Phone);

			if (entity.getLocation() != null && !StringUtil.isNullOrEmpty(entity.getLocation().getAddress1())) {
				MLocation location = locationRepository.save(entity.getLocation());
				businessPartner.setBH_C_Location_ID(location.get_ID());
			}

			ModelUtil.setPropertyIfPresent(entity.getbh_gender(), businessPartner::setbh_gender);
			ModelUtil.setPropertyIfPresent(entity.getBH_EMail(), businessPartner::setBH_EMail);
			ModelUtil.setPropertyIfPresent(entity.getbh_nhif_relationship(), businessPartner::setbh_nhif_relationship);
			ModelUtil.setPropertyIfPresent(entity.getbh_nhif_member_name(), businessPartner::setbh_nhif_member_name);
			ModelUtil.setPropertyIfPresent(entity.getNHIF_Number(), businessPartner::setNHIF_Number);
			ModelUtil.setPropertyIfPresent(entity.getBH_NHIF_Type(), businessPartner::setBH_NHIF_Type);
			ModelUtil.setPropertyIfPresent(entity.getNational_ID(), businessPartner::setNational_ID);
			ModelUtil.setPropertyIfPresent(entity.getbh_occupation(), businessPartner::setbh_occupation);
			ModelUtil.setPropertyIfPresent(entity.getNextOfKin_Name(), businessPartner::setNextOfKin_Name);
			ModelUtil.setPropertyIfPresent(entity.getNextOfKin_Contact(), businessPartner::setNextOfKin_Contact);
			ModelUtil.setPropertyIfPresent(entity.getBH_Local_PatientID(), businessPartner::setBH_Local_PatientID);
			ModelUtil.setPropertyIfPresent(entity.isActive(), businessPartner::setIsActive);

			businessPartner.saveEx();

			cache.delete(businessPartner.get_ID());

			return getByUuid(businessPartner.getC_BPartner_UU());
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	public MBPartner_BH saveCustomer(BusinessPartnerInput businessPartner) {
		businessPartner.setBH_IsPatient(true);
		businessPartner.setIsCustomer(true);
		businessPartner.setIsVendor(false);
		return save(businessPartner);
	}

	public MBPartner_BH saveVendor(BusinessPartnerInput businessPartner) {
		businessPartner.setBH_IsPatient(false);
		businessPartner.setIsCustomer(false);
		businessPartner.setIsVendor(true);
		return save(businessPartner);
	}

	public Connection<MBPartner_BH> getCustomers(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		return super.get(filter, sort, pagingInfo, MBPartner_BH.COLUMNNAME_BH_IsPatient + "=?", parameters,
				environment);
	}

	public Connection<MBPartner_BH> getVendors(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		return super.get(filter, sort, pagingInfo, MBPartner_BH.COLUMNNAME_IsVendor + "=?", parameters,
				environment);
	}
}
