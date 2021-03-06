package dashboard;
import org.apache.ofbiz.base.util.UtilValidate;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.ServletContext;
import org.apache.ofbiz.base.util.UtilProperties;

import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import javolution.util.FastList;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.party.party.PartyHelper;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.order.shoppingcart.*;
import org.apache.ofbiz.webapp.control.*;
import org.apache.ofbiz.webapp.website.WebSiteWorker;
import org.apache.ofbiz.order.order.OrderReadHelper;
import com.osafe.util.OsafeAdminUtil;

BigDecimal calcItemTotal(List headers) 
{
    BigDecimal total = BigDecimal.ZERO;
    headers.each { header ->
        orderReadHelper = new OrderReadHelper(header);
        total = total.plus(orderReadHelper.getOrderItemsSubTotal() ?: BigDecimal.ZERO);
    }
    return total;
}

void sumCol(GenericValue gv1, GenericValue gv2, String columnName) 
{
    BigDecimal result = BigDecimal.ZERO;
    result = gv1.getBigDecimal(columnName);
    if (result != null) 
    {
        result = result.add(gv2.getBigDecimal(columnName));
    } 
    else 
    {
        result = gv2.getBigDecimal(columnName);
    }
    gv1.set(columnName, result);
}

if(!OsafeAdminUtil.isProductStoreParmFalse(request,"ADM_SHOW_DAILY_COUNTER"))
{
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
	
	
	
	
	nowTs = UtilDateTime.nowTimestamp();
	context.nowTimestampString = nowTs.toString();
	
	todayFromTs = UtilDateTime.getDayStart(nowTs);
	todayToTs = UtilDateTime.getDayEnd(nowTs);
	
	if(UtilValidate.isNotEmpty(todayFromTs) && UtilValidate.isNotEmpty(todayToTs))
	{
	
	    // Summary
	    ecl = EntityCondition.makeCondition([
	        EntityCondition.makeCondition("statusId", EntityOperator.IN, includedOrderStatusList),
	        EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER"),
	        EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, todayFromTs),
	        EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN_EQUAL_TO, todayToTs)
	    ],
	    EntityOperator.AND);
	    context.todayOrderCount = BigDecimal.ZERO;
	    context.todayTotalRevenue= BigDecimal.ZERO;
	
	    List allHeaders = delegator.findList("OrderHeader", ecl, null, null, null, false);
	    if (UtilValidate.isNotEmpty(allHeaders))
	    {
	        BigDecimal orderCount = allHeaders.size();
	        context.todayOrderCount = orderCount;
	
	        BigDecimal totalRevenue = calcItemTotal(allHeaders);
	        context.todayTotalRevenue = totalRevenue;
	    }
	}
}
