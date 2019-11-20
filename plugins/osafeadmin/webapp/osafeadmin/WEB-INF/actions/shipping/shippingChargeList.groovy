package shipping;

import org.apache.ofbiz.base.util.UtilValidate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionBuilder;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.entity.condition.EntityFunction;
import org.apache.ofbiz.entity.condition.EntityFieldValue;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.product.category.CategoryWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import com.ibm.icu.util.Calendar;


String srchProductStoreShipMethId = StringUtils.trimToEmpty(parameters.srchProductStoreShipMethId);
productStoreId=globalContext.productStoreId;

productStore = ProductStoreWorker.getProductStore(request);

Timestamp now = UtilDateTime.nowTimestamp(); 
orderBy = ["shipmentMethodTypeId"];
// Paging variables
viewIndex = Integer.valueOf(parameters.viewIndex  ?: 1);
viewSize = Integer.valueOf(parameters.viewSize ?: UtilProperties.getPropertyValue("osafeAdmin", "default-view-size"));
context.viewIndex = viewIndex;
context.viewSize = viewSize;

Map<String, Object> svcCtx = new HashMap();
userLogin = session.getAttribute("userLogin");
svcCtx.put("userLogin", userLogin);

//spotListMenuId=context.spotListMenuId;
List contentList = new ArrayList();
context.userLoginId = userLogin.userLoginId;

exprs = new ArrayList();
mainCond=null;
prodCond=null;
statusCond=null;

if (UtilValidate.isNotEmpty(productStoreId))
{

	exprs.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId));
}

// Product Store Ship Method Id
if (UtilValidate.isNotEmpty(srchProductStoreShipMethId))
{
	productStoreShipMethId=srchProductStoreShipMethId;
	exprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("productStoreShipMethId"), EntityOperator.LIKE, "%" + productStoreShipMethId.toUpperCase() + "%"));
	context.productStoreShipMethId=productStoreShipMethId;
}

if (UtilValidate.isNotEmpty(exprs))
{
	prodCond=EntityCondition.makeCondition(exprs, EntityOperator.AND);
	mainCond=prodCond;
}

orderBy = ["productStoreShipMethId"];

productStoreShipmentMethList=new ArrayList();
if(UtilValidate.isNotEmpty(preRetrieved) && preRetrieved != "N")
{
	productStoreShipmentMethList = delegator.findList("ProductStoreShipmentMeth",mainCond, null, orderBy, null, false);
}


context.resultList = productStoreShipmentMethList;


