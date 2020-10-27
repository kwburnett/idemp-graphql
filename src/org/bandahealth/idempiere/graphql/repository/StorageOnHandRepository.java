package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MStorageOnHand;
import org.compiere.util.Env;

import java.util.List;

public class StorageOnHandRepository extends BaseRepository<MStorageOnHand, MStorageOnHand> {

	private AttributeSetInstanceRepository attributeSetInstanceRepository;

	public StorageOnHandRepository() {
		attributeSetInstanceRepository = new AttributeSetInstanceRepository();
	}

	@Override
	public MStorageOnHand getModelInstance() {
		return new MStorageOnHand(Env.getCtx(), 0, null);
	}

	@Override
	public MStorageOnHand save(MStorageOnHand entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public List<Object> getDefaultParameters() {
		return attributeSetInstanceRepository.getDefaultParameters();
	}

	@Override
	public String getDefaultJoinClause() {
		return " JOIN " + MAttributeSetInstance.Table_Name + " ON " + MAttributeSetInstance.Table_Name + "." +
				MAttributeSetInstance.COLUMNNAME_M_AttributeSetInstance_ID + "=" + MStorageOnHand.Table_Name + "." +
				MStorageOnHand.COLUMNNAME_M_AttributeSetInstance_ID + " " +
				attributeSetInstanceRepository.getDefaultJoinClause();
	}
}
