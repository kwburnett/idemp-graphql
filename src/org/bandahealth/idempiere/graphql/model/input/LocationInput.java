package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class LocationInput extends MLocation {
	public LocationInput() {
		super(Env.getCtx(), 0, null);
	}

	public LocationInput(Properties ctx, int C_Location_ID, String trxName) {
		super(ctx, C_Location_ID, trxName);
	}

	public LocationInput(MCountry country, MRegion region) {
		super(country, region);
	}

	public LocationInput(Properties ctx, int C_Country_ID, int C_Region_ID, String city, String trxName) {
		super(ctx, C_Country_ID, C_Region_ID, city, trxName);
	}

	public LocationInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
