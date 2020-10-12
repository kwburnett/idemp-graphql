package org.bandahealth.idempiere.graphql;

public interface IRestConfigs {

	String ROOT_PATH = "/";
	String AUTHENTICATION = "auth";
	String AUTHENTICATION_PATH = ROOT_PATH + AUTHENTICATION;

	String AUTHENTICATION_SESSION_PATH = "/session";
	String TERMSOFSERVICE_PATH = AUTHENTICATION_SESSION_PATH + "/terms";
	String CHANGEPASSWORD_PATH = AUTHENTICATION_SESSION_PATH + "/changePassword";

	String SAVE_PATH = "/save";
	String DELETE_PATH = "/delete";

	String SEARCH_PATH = "/search";

	String UUID_PATH = "/{uuid}";

	String PRINT_RECEIPT_PATH = "/printreceipt/{uuid}";

	String PATIENTS_PATH = AUTHENTICATION_PATH + "/patients";
	String PATIENT_PATH = "/patient/{uuid}";
	String PATIENT_SUMMARY_PATH = AUTHENTICATION_PATH + "/patientsummary";
	String PATIENT_GENERATE_ID = "/generatepatientid";

	String VENDORS_PATH = AUTHENTICATION_PATH + "/vendors";
	String VENDOR_PATH = "/vendor/{uuid}";

	String MENUS_PATH = AUTHENTICATION_PATH + "/menus";
	String MENU_PATH = "/menu/{uuid}";

	String MENU_LINES_PATH = AUTHENTICATION_PATH + "/menulines";
	String MENU_LINE_PATH = "/menulines/{uuid}";

	String PROCESS_PATH = AUTHENTICATION_PATH + "/process";

	String REPORTS_PATH = AUTHENTICATION_PATH + "/reports";
	String GENERATE_PATH = "/generate";

	String PRODUCTS_PATH = AUTHENTICATION_PATH + "/products";
	String PRODUCT_PATH = "/product/{uuid}";
	String SEARCH_ITEMS_PATH = "/searchitems";

	// 'services' used by iDemp WebServices.
	String SERVICES_PATH = AUTHENTICATION_PATH + "/bhservices";
	String SERVICE_PATH = "/service/{uuid}";

	String EXPENSE_CATEGORIES_PATH = AUTHENTICATION_PATH + "/expensecategories";
	String EXPENSE_CATEGORY_PATH = "/expensecategory/{uuid}";

	String PRODUCT_CATEGORIES_PATH = AUTHENTICATION_PATH + "/productcategories";

	String STOCK_TAKE_ITEMS_PATH = AUTHENTICATION_PATH + "/stocktake";
	String STOCK_TAKE_ITEM_PATH = "/stocktake/{uuid}";

	String ENTITY_PROCESS_PATH = "/process/{uuid}";
	String ENTITY_SAVE_AND_PROCESS_PATH = "/saveandprocess";

	String VISITS_PATH = AUTHENTICATION_PATH + "/visits";
	String VISIT_PATH = "/visit/{uuid}";
	String VISIT_QUEUE_PATH = "/visitqueue";
	String VISIT_OPEN_DRAFTS = "/opendrafts";
	String VISIT_OPEN_DRAFTS_COUNT = VISIT_OPEN_DRAFTS + "/count";

	String PAYMENTS_PATH = AUTHENTICATION_PATH + "/payments";
	String PAYMENT_PATH = "/payment/{uuid}";

	String METADATA_PATH = AUTHENTICATION_PATH + "/metadata";

	String RECEIVE_PRODUCTS_PATH = AUTHENTICATION_PATH + "/receiveproducts";
	String RECEIVE_PRODUCT_PATH = "/receiveproduct/{uuid}";
	String RECEIVE_PRODUCT_PROCESS_PATH = "/process/{uuid}";
	String RECEIVE_PRODUCT_SAVE_AND_PROCESS_PATH = "/saveandprocess";

	String EXPENSES_PATH = AUTHENTICATION_PATH + "/expenses";
	String EXPENSE_PATH = "/expense/{uuid}";

	String ACCOUNTS_PATH = AUTHENTICATION_PATH + "/accounts";
	String ACCOUNTS_UUID_PATH = "/account/{uuid}";

	String APPLICATION_PDF = "application/pdf";
}
