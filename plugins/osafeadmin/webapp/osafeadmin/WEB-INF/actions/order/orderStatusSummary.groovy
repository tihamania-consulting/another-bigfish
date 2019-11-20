package content;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionBuilder;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.entity.transaction.*

import java.sql.Timestamp;

// Paging variables
viewIndex = Integer.valueOf(parameters.viewIndex  ?: 1);
viewSize = Integer.valueOf(parameters.viewSize ?: UtilProperties.getPropertyValue("osafeAdmin", "default-view-size"));
context.viewIndex = viewIndex;
context.viewSize = viewSize;

Map<String, Object> svcCtx = new HashMap();
userLogin = session.getAttribute("userLogin");
svcCtx.put("userLogin", userLogin);

//ORDERS REQUIRING WORK
orderBy = ["sequenceId"];
statusItems = delegator.findByAnd("StatusItem", UtilMisc.toMap("statusTypeId", "ORDER_STATUS"), orderBy);


// For each status, count the number of orders

// All Sales Orders
ecl = EntityCondition.makeCondition([
	EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, globalContext.productStoreId),
	EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER")
],
EntityOperator.AND);

total = 0;
ordersRequiringWorkList =[];
requiringWorkHeaders = delegator.findList("OrderHeader", ecl, null, null, null, false);
for(GenericValue status : statusItems)
{
	statusIdToExclude = status.statusId;
	statusIdHeaders = EntityUtil.filterByAnd(requiringWorkHeaders, ["statusId" : statusIdToExclude]);
	headerCount = statusIdHeaders.size();
	workMap = [:];
	workMap["description"] = status.description;
	workMap["count"] = headerCount;
	workMap["statusId"] = status.statusId;
	ordersRequiringWorkList.add(workMap);
	
	total = total + headerCount;
}
context.total = total;
context.ordersRequiringWork= ordersRequiringWorkList
//ORDERS REQUIRING WORK
