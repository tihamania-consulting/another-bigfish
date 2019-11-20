package admin;

import org.apache.ofbiz.base.util.UtilValidate;

import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.base.util.UtilMisc;

productStore = ProductStoreWorker.getProductStore(request);
productStoreId="";
orderBy = ["emailType"];
if (UtilValidate.isNotEmpty(productStore))
{
	productStoreId = productStore.productStoreId;
	emailConfigs = delegator.findByAnd("ProductStoreEmailSetting",["productStoreId":productStoreId],orderBy);
	if(emailConfigs)
	{
	    context.resultList = emailConfigs;
	}
}


