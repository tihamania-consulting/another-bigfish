package order;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.apache.ofbiz.party.contact.ContactHelper;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.party.contact.ContactMechWorker;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import javolution.util.FastList;
import javolution.util.FastMap;

userLogin = session.getAttribute("userLogin");
orderId = StringUtils.trimToEmpty(parameters.orderId);
context.orderId = orderId;

orderHeader = null;
orderItems = null;

orderSubTotal = 0;
if (UtilValidate.isNotEmpty(orderId))
{
	orderHeader = delegator.findOne("OrderHeader", [orderId : orderId]);
	
	orderProductStore = orderHeader.getRelatedOne("ProductStore");
	if (UtilValidate.isNotEmpty(orderProductStore.storeName))
	{
		productStoreName = orderProductStore.storeName;
	}
	else
	{
		productStoreName = orderHeader.productStoreId;
	}
	context.productStoreName = productStoreName;
	
	messageMap=[:];
	messageMap.put("orderId", orderId);

	context.orderId=orderId;
	context.pageTitle = UtilProperties.getMessage("OSafeAdminUiLabels","OrderManagementOrderDetailTitle",messageMap, locale )
	context.generalInfoBoxHeading = UtilProperties.getMessage("OSafeAdminUiLabels","OrderDetailInfoHeading",messageMap, locale )
}
if(UtilValidate.isNotEmpty(orderHeader))
{
	
	orderReadHelper = new OrderReadHelper(orderHeader);
	orderItems = orderReadHelper.getOrderItems();

	context.orderHeader = orderHeader;
	context.orderReadHelper = orderReadHelper;
	context.orderItems = orderItems;
	
	orderShipments = orderHeader.getRelated("PrimaryShipment");
	if(UtilValidate.isNotEmpty(orderShipments))
	{
		context.orderShipments = orderShipments;
	}
	notes = orderHeader.getRelatedOrderBy("OrderHeaderNoteView", ["-noteDateTime"]);
	context.notesCount = notes.size();
    context.orderItemShipGroups = orderReadHelper.getOrderItemShipGroups();
	
}





