package org.bandahealth.idempiere.graphql.repository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.input.ProductInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MProductCategory;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MUOM;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class ProductRepository extends BaseRepository<MProduct_BH, ProductInput> {

	private final ProductCategoryRepository productCategoryRepository;

	public ProductRepository() {
		productCategoryRepository = new ProductCategoryRepository();
	}

	@Override
	public MProduct_BH getModelInstance() {
		return new MProduct_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MProduct_BH save(ProductInput entity) {
		try {
			MProduct_BH product = getByUuid(entity.getM_Product_UU());
			if (product == null) {
				product = getModelInstance();
				product.setProductType(MProduct_BH.PRODUCTTYPE_Item);

				// set default uom (unit of measure).
				MUOM uom = new Query(Env.getCtx(), MUOM.Table_Name, MUOM.COLUMNNAME_Name + "=?", null)
						.setParameters("Each").first();
				if (uom != null) {
					product.setC_UOM_ID(uom.get_ID());
				}

				// set product category.
				MProductCategory productCategory = new Query(Env.getCtx(), MProductCategory.Table_Name,
						MProductCategory.COLUMNNAME_Name + "=?", null)
						.setParameters("Pharmacy").setClient_ID().first();
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

			product.saveEx();

			return getByUuid(product.getM_Product_UU());
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}
}
