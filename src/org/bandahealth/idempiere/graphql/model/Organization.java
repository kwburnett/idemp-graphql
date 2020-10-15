package org.bandahealth.idempiere.graphql.model;

import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Organization extends MOrg {

	private List<MRole> roles = new ArrayList<>();
	private List<MWarehouse> warehouses = new ArrayList<>();

	public Organization(Properties ctx, int AD_Org_ID, String trxName) {
		super(ctx, AD_Org_ID, trxName);
	}

	public Organization(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public Organization(MClient client, String value, String name) {
		super(client, value, name);
	}

	public void setRoles(List<MRole> roles) {
		this.roles = roles;
	}

	public List<MRole> getRoles() {
		return roles;
	}

	public void setWarehouses(List<MWarehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public List<MWarehouse> getWarehouses() {
		return warehouses;
	}
}
