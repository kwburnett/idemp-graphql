package org.bandahealth.idempiere.graphql.resolver;

import org.compiere.model.PO;

/**
 * This class is meant to put the common fetchers that exist on every entity
 */
public class BaseResolver<T extends PO> {
	public int clientId(T entity) {
		return entity.getAD_Client_ID();
	}

	public String id(T entity) {
		return entity.get_Value(entity.getUUIDColumnName()).toString();
	}

	public int orgId(T entity) {
		return entity.getAD_Org_ID();
	}
}
