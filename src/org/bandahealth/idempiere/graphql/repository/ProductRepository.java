package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.ProductInput;
import org.bandahealth.idempiere.graphql.utils.BandaQuery;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MProductCategory;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MUOM;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository extends BaseRepository<MProduct_BH, ProductInput> {

	private final ProductCategoryRepository productCategoryRepository;
	private final UomRepository uomRepository;
	private final StorageOnHandRepository storageOnHandRepository;

	private final String productCategoryJoin = " JOIN " + MProductCategory_BH.Table_Name + " ON " +
			MProductCategory_BH.Table_Name + "." + MProductCategory_BH.COLUMNNAME_M_Product_Category_ID + "=" +
			MProduct_BH.Table_Name + "." + MProduct_BH.COLUMNNAME_M_Product_Category_ID;

	public ProductRepository() {
		productCategoryRepository = new ProductCategoryRepository();
		uomRepository = new UomRepository();
		storageOnHandRepository = new StorageOnHandRepository();
	}

	@Override
	protected MProduct_BH createModelInstance() {
		return new MProduct_BH(Env.getCtx(), 0, null);
	}

	public Connection<MProduct_BH> getItems(String filterJson, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(MProduct_BH.PRODUCTTYPE_Item);

		return super.get(filterJson, sort, pagingInfo, MProduct_BH.COLUMNNAME_ProductType + " = ?", parameters,
				productCategoryJoin, environment);
	}

	public Connection<MProduct_BH> getServices(String filterJson, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(MProduct_BH.PRODUCTTYPE_Service);

		return super.get(filterJson, sort, pagingInfo, MProduct_BH.COLUMNNAME_ProductType + " = ?", parameters,
				productCategoryJoin, environment);
	}

	@Override
	public Map<String, String> getDynamicJoins() {
		String storageOnHandBaseSql = storageOnHandRepository.getBaseQuery(null).getSQL();
		int storageOnHandBaseIndexOfFrom = storageOnHandBaseSql.indexOf("FROM");
		int storageOnHandBaseIndexOfWhere = storageOnHandBaseSql.indexOf("WHERE");
		return new HashMap<>() {{
			put(MStorageOnHand.Table_Name, "LEFT JOIN (" + "SELECT " + MStorageOnHand.Table_Name + "." +
					MStorageOnHand.COLUMNNAME_M_Product_ID + "," + StorageOnHandRepository
					.MODIFIED_COLUMNS.get(MStorageOnHand.COLUMNNAME_QtyOnHand) + " AS " +
					MStorageOnHand.COLUMNNAME_QtyOnHand + " " + storageOnHandBaseSql
					.substring(storageOnHandBaseIndexOfFrom, storageOnHandBaseIndexOfWhere) + " GROUP BY " +
					MStorageOnHand.Table_Name + "." + MStorageOnHand.COLUMNNAME_M_Product_ID + ") AS " +
					MStorageOnHand.Table_Name + " ON " + MStorageOnHand.Table_Name + "." +
					MStorageOnHand.COLUMNNAME_M_Product_ID + "=" + MProduct_BH.Table_Name + "." +
					MProduct_BH.COLUMNNAME_M_Product_ID);
		}};
	}

	@Override
	public MProduct_BH mapInputModelToModel(ProductInput entity) {
		try {
			MProduct_BH product = getByUuid(entity.getM_Product_UU());
			if (product == null) {
				product = createModelInstance();
				product.setProductType(MProduct_BH.PRODUCTTYPE_Item);

				// set default uom (unit of measure).
				MUOM uom = uomRepository.getBaseQuery(MUOM.COLUMNNAME_Name + "=?", "Each").first();
				if (uom != null) {
					product.setC_UOM_ID(uom.get_ID());
				}

				// set product category.
				MProductCategory productCategory = productCategoryRepository
						.getBaseQuery(MProductCategory.COLUMNNAME_Name + "=?", "Pharmacy").first();
				if (productCategory != null) {
					product.setM_Product_Category_ID(productCategory.get_ID());
				}

				// set tax category
				MTaxCategory taxCategory = new Query(Env.getCtx(), MTaxCategory.Table_Name,
						MTaxCategory.COLUMNNAME_Name + "=?", null)
						.setParameters("Standard").setClient_ID().first();
				if (taxCategory != null) {
					product.setC_TaxCategory_ID(taxCategory.get_ID());
				}
			}

			ModelUtil.setPropertyIfPresent(entity.getName(), product::setName);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), product::setDescription);
			ModelUtil.setPropertyIfPresent(entity.isBH_HasExpiration(), product::setBH_HasExpiration);
			ModelUtil.setPropertyIfPresent(entity.getbh_reorder_level(), product::setbh_reorder_level);
			ModelUtil.setPropertyIfPresent(entity.getbh_reorder_quantity(), product::setbh_reorder_quantity);
			ModelUtil.setPropertyIfPresent(entity.getBH_BuyPrice(), product::setBH_BuyPrice);
			ModelUtil.setPropertyIfPresent(entity.getBH_SellPrice(), product::setBH_SellPrice);

			if (entity.getProductCategory() != null) {
				MProductCategory_BH productCategory = productCategoryRepository
						.getByUuid(entity.getProductCategory().getM_Product_Category_UU());
				if (productCategory != null) {
					product.setM_Product_Category_ID(productCategory.getM_Product_Category_ID());
				}
			}

			// calculate price margin
			if (entity.getBH_BuyPrice() != null && entity.getBH_SellPrice() != null) {
				product.setBH_PriceMargin(entity.getBH_SellPrice().subtract(entity.getBH_BuyPrice()));
			}

			product.setIsActive(entity.isActive());

			return product;
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}
}
