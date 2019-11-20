package admin;

import org.apache.ofbiz.base.util.UtilValidate;
import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.FileUtil;

import com.osafe.services.OsafeManageXml;

productStore = globalContext.productStore;
productStoreId=globalContext.productStoreId;

if (UtilValidate.isNotEmpty(productStore))
{
	productStoreId = productStore.productStoreId;
    parmKey = parameters.parmKey;
    if (UtilValidate.isNotEmpty(parmKey))
    {
      context.productStoreParm = delegator.findOne("XProductStoreParm",["productStoreId":productStoreId,"parmKey":parmKey], false);
    }
}

addParameterKey = parameters.addKey;
if (UtilValidate.isNotEmpty(addParameterKey)) {
    tmpDir = FileUtil.getFile("runtime/tmp");
    uploadedFile = new File(tmpDir, context.uploadedFileName);
    context.productStoreParm = OsafeManageXml.findByKeyFromXmlFile(uploadedFile.getAbsolutePath(), "parmKey", addParameterKey);
}