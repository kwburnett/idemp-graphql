package org.bandahealth.idempiere.graphql.model;

import org.compiere.model.MClient;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client extends MClient {

	private List<Organization> organizations = new ArrayList<>();

	public Client(Properties ctx, int AD_Client_ID, boolean createNew, String trxName) {
		super(ctx, AD_Client_ID, createNew, trxName);
	}

	public Client(Properties ctx, int AD_Client_ID, String trxName) {
		super(ctx, AD_Client_ID, trxName);
	}

	public Client(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public Client(Properties ctx, String trxName) {
		super(ctx, trxName);
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}
}
