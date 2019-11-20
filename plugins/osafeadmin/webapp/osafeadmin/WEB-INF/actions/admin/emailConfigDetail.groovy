package admin;

import org.apache.ofbiz.base.util.UtilValidate;

import org.apache.ofbiz.product.store.ProductStoreWorker;

productStore = ProductStoreWorker.getProductStore(request);
productStoreId="";

if (UtilValidate.isNotEmpty(productStore))
{
	productStoreId = productStore.productStoreId;
	context.productStoreId = productStoreId;
	emailType = parameters.emailType;
    if (UtilValidate.isNotEmpty(emailType))
    {
      context.emailConfiguration = delegator.findOne("ProductStoreEmailSetting",["productStoreId":productStoreId,"emailType":emailType], false);
    }
}
      