package dashboard;

import org.apache.ofbiz.base.util.ObjectType;

import java.util.Locale;
import java.util.TimeZone;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.entity.condition.*;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.product.ProductWorker;
import com.osafe.util.OsafeAdminUtil;

import com.ibm.icu.util.Calendar;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.Context;

orderStatusIncDashboard = globalContext.get("ORDER_STATUS_INC_DASHBOARD");
List includedOrderStatusList = new ArrayList();
if(UtilValidate.isNotEmpty(orderStatusIncDashboard))
{
    orderStatusIncDashboardList = StringUtil.split(orderStatusIncDashboard,",")
	for (String orderStatus : orderStatusIncDashboardList) 
	{
	    includedOrderStatusList.add(orderStatus.trim());
	}
}

// Last Order
ecl = EntityCondition.makeCondition([
        EntityCondition.makeCondition("statusId", EntityOperator.IN, includedOrderStatusList),
        EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER")
],
EntityOperator.AND);

List orderBy = UtilMisc.toList("-orderDate");

List lastOrderHeaders = delegator.findList("OrderHeader", ecl, null, orderBy, null, false);
if (UtilValidate.isNotEmpty(lastOrderHeaders))
{
    GenericValue lastOrder = EntityUtil.getFirst(lastOrderHeaders);
    context.lastOrderAmount = lastOrder.grandTotal ?: BigDecimal.ZERO;
    context.lastOrderDateTimeString = OsafeAdminUtil.convertDateTimeFormat(lastOrder.orderDate, preferredDateTimeFormat);
}
