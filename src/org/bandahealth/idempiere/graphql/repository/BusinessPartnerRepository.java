package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MLocation;
import org.compiere.util.Env;

public class BusinessPartnerRepository extends BaseRepository<MBPartner_BH, BusinessPartnerInput> {

	private final LocationRepository locationRepository;

	public BusinessPartnerRepository() {
		locationRepository = new LocationRepository();
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
				businessPartner.setBH_IsPatient(true);
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), businessPartner::setName);
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
}
