package order;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import javolution.util.FastList;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.party.contact.ContactHelper;
import org.apache.ofbiz.entity.GenericValue;

userLogin = session.getAttribute("userLogin");
orderId = StringUtils.trimToEmpty(parameters.orderId);

orderHeader = null;
OrderNotes = null;
shippingApplies = true;
boolean allItemsApproved = false;
boolean allItemsCompleted = false;
List<String> approvedList = new ArrayList();
List<String> completedList = new ArrayList();
List<GenericValue> orderItems = new ArrayList();
List<GenericValue> shipGroups = new ArrayList();
if (UtilValidate.isNotEmpty(orderId)) 
{
	orderHeader = delegator.findOne("OrderHeader", [orderId : orderId]);
	if(UtilValidate.isNotEmpty(orderHeader))
	{
		
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
		
		notes = orderHeader.getRelated("OrderHeaderNote", null, null);
		if(UtilValidate.isNotEmpty(notes))
		{
			context.orderNotes = notes;
			orderNotes = notes;
			context.notesCount = orderNotes.size();
		}

		showNoteHeadingOnPDF = false;
		if (UtilValidate.isNotEmpty(notes) && EntityUtil.filterByCondition(notes, EntityCondition.makeCondition("internalNote", EntityOperator.EQUALS, "N")).size() > 0) 
		{
			showNoteHeadingOnPDF = true;
		}
		context.showNoteHeadingOnPDF = showNoteHeadingOnPDF;
		
		
		// note these are overridden in the OrderViewWebSecure.groovy script if run
		context.hasPermission = true;
		context.canViewInternalDetails = true;

		orderReadHelper = new OrderReadHelper(orderHeader);
		orderTerms = orderHeader.getRelated("OrderTerm");
		
		if (UtilValidate.isNotEmpty(orderReadHelper))
		{
			//shipping applies check
			shippingApplies = orderReadHelper.shippingApplies();
			
			orderItems = orderReadHelper.getOrderItems();
			
			placingParty = orderReadHelper.getPlacingParty();
			if(UtilValidate.isNotEmpty(placingParty))
			{
				context.partyId = placingParty.partyId;
				context.shippingContactMechList = ContactHelper.getContactMech(placingParty, "SHIPPING_LOCATION", "POSTAL_ADDRESS", false);
			}
		    if (UtilValidate.isNotEmpty(orderProductStore)) 
		    {
		        context.destinationFacilityId = ProductStoreWorker.determineSingleFacilityForStore(delegator, orderProductStore.productStoreId);
		        context.toPartyId = orderProductStore.payToPartyId;
		        
		        if (orderProductStore.reqReturnInventoryReceive) 
			    {
			        context.needsInventoryReceive = orderProductStore.reqReturnInventoryReceive;
			    } 
			    else 
			    {
			        context.needsInventoryReceive = "Y";
			    }
		    }
		    context.orderReadHelper = orderReadHelper;
		}

		if(UtilValidate.isNotEmpty(orderItems))
		{
			orderItems.each { orderItem ->
		    	if("ITEM_APPROVED".equals(orderItem.get("statusId"))) 
		    	{
		    		approvedList.add(orderItem.orderItemSeqId);
		    	}
		    	if("ITEM_COMPLETED".equals(orderItem.get("statusId"))) 
		    	{
		    		completedList.add(orderItem.orderItemSeqId);
		    	}
		    }
		
		    if(approvedList.size() == orderItems.size())
		    {
		    	allItemsApproved = true;
		    }
		    if(completedList.size() == orderItems.size())
		    {
		    	allItemsCompleted = true;
		    }
			
			pagingListSize=orderItems.size();
			context.pagingListSize = pagingListSize;
			context.pagingList = orderItems;
		}
		
	    if ("SALES_ORDER".equals(orderHeader.orderTypeId)) 
	    {
	        context.returnHeaderTypeId = "CUSTOMER_RETURN";
	    }
	    
	    ecl = EntityCondition.makeCondition([
	     									EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId),
	     									EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_CANCELLED")],
	     								EntityOperator.AND);
	    orderPaymentPreferences = delegator.findList("OrderPaymentPreference", ecl, null, null, null, false);
	    orderPaymentPreference = EntityUtil.getFirst(orderPaymentPreferences);
	    context.orderPaymentPreference = orderPaymentPreference;
	    
	    if(security.hasEntityPermission('SPER_ORDER_MGMT', '_VIEW', session))
	    {
	        messageMap=[:];
	        messageMap.put("orderId", orderId);

	        context.pageTitle = UtilProperties.getMessage("OSafeAdminUiLabels","OrderStatusDetailTitle",messageMap, locale )
	        context.generalInfoBoxHeading = UtilProperties.getMessage("OSafeAdminUiLabels","OrderDetailInfoHeading",messageMap, locale )
	    }
	    conditions = new ArrayList();
	    conditions.add(EntityCondition.makeCondition("statusTypeId", EntityOperator.EQUALS, "ORDER_STATUS"));
	    conditions.add(EntityCondition.makeCondition("statusId", EntityOperator.IN, ["ORDER_APPROVED", "ORDER_CANCELLED","ORDER_COMPLETED"]));
	    mainCond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
	    statusItems = delegator.findList("StatusItem", mainCond, null, ["sequenceId"], null, false);
	    if(UtilValidate.isNotEmpty(statusItems))
	    {
	    	context.statusItems = statusItems;
	    }

	    //is it a store pickup?
	    storeId = "";
	    orderDeliveryOptionAttr = orderHeader.getRelated("OrderAttribute", [attrName : "DELIVERY_OPTION"], null);
	    orderDeliveryOptionAttr = EntityUtil.getFirst(orderDeliveryOptionAttr);
	    if (UtilValidate.isNotEmpty(orderDeliveryOptionAttr) && orderDeliveryOptionAttr.attrValue == "STORE_PICKUP") 
	    {
	    	context.isStorePickup = "Y";
	    	orderStoreLocationAttr = orderHeader.getRelated("OrderAttribute", [attrName : "STORE_LOCATION"]);
	    	orderStoreLocationAttr = EntityUtil.getFirst(orderStoreLocationAttr);
	    	if (UtilValidate.isNotEmpty(orderStoreLocationAttr)) 
	    	{
	    		storeId = orderStoreLocationAttr.attrValue;
	    	}
	    }

	    returnReasons = delegator.findList("ReturnReason", null, null, ["sequenceId"], null, false);
	    context.returnReasons = returnReasons;

	    // ship groups
	    shipGroups = orderHeader.getRelated("OrderItemShipGroup", null, ["shipGroupSeqId"]);
	    context.orderHeader = orderHeader;
	}
	context.orderId = orderId;
}

context.shipGroups = shipGroups;
context.shippingApplies = shippingApplies;
context.orderItems = orderItems;
context.allItemsCompleted = allItemsCompleted;
context.allItemsApproved = allItemsApproved;