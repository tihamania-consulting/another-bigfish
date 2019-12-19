package common;

import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.party.contact.*;
import org.apache.ofbiz.product.store.*;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import javolution.util.FastList;
import java.math.BigDecimal;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.condition.*;
import org.apache.ofbiz.accounting.payment.*;
import org.apache.ofbiz.order.order.*;
import org.apache.ofbiz.product.catalog.*;
import javolution.util.FastMap;
import com.osafe.util.Util;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.base.util.UtilNumber;

chosenShippingMethodDescription="";
shippingInstructions = "";
deliveryOption = "";
currencyUom = Util.getProductStoreParm(request,"CURRENCY_UOM_DEFAULT");
offerPriceVisible = "";


party = context.party;
partyId = context.partyId;
if (UtilValidate.isEmpty(partyId)) 
{
    if (UtilValidate.isNotEmpty(userLogin)) 
    {
        party = userLogin.getRelatedOne("Party");
        partyId = party.partyId;
    }
} 
else 
{
    party = delegator.findOne("Party", [partyId : partyId], true);
}

shippingContactMechList = ContactHelper.getContactMech(party, "SHIPPING_LOCATION", "POSTAL_ADDRESS", false);
shippingContactMechPhoneMap = [:];
for (GenericValue contactMech : shippingContactMechList)
{
    phoneNumberMap = [:];
    if(UtilValidate.isNotEmpty(contactMech))
    {
        contactMechIdFrom = contactMech.contactMechId;
        contactMechLinkList = delegator.findByAnd("ContactMechLink", UtilMisc.toMap("contactMechIdFrom", contactMechIdFrom))

        for (GenericValue link: contactMechLinkList)
        {
            contactMechIdTo = link.contactMechIdTo
            contactMech = delegator.findByPrimaryKeyCache("ContactMech", [contactMechId : contactMechIdTo]);
            phonePurposeList  = EntityUtil.filterByDate(contactMech.getRelated("PartyContactMechPurpose"), true);
            partyContactMechPurpose = EntityUtil.getFirst(phonePurposeList)

            if(partyContactMechPurpose) 
            {
                telecomNumber = partyContactMechPurpose.getRelatedOne("TelecomNumber");
                phoneNumberMap[partyContactMechPurpose.contactMechPurposeTypeId]=telecomNumber;
            }
        }
    }
    shippingContactMechPhoneMap[contactMechIdFrom] = phoneNumberMap;
}
context.shippingContactMechPhoneMap = shippingContactMechPhoneMap;

creditCardTypes = delegator.findByAnd("Enumeration", [enumTypeId : "CREDIT_CARD_TYPE"], ["sequenceId"]);
creditCardTypesMap = [:];
for (GenericValue creditCardType :  creditCardTypes) 
{
    creditCardTypesMap[creditCardType.enumCode] = creditCardType.description;
}

context.creditCardTypesMap = creditCardTypesMap;

/*
 * Ofbiz order status groovy (ecommerce) need to review and remove.
 */

orderId = parameters.orderId;
orderHeader = null;
// we have a special case here where for an anonymous order the user will already be logged out, but the userLogin will be in the request so we can still do a security check here
if (UtilValidate.isEmpty(userLogin)) 
{
    userLogin = parameters.temporaryAnonymousUserLogin;
    // This is another special case, when Order is placed by anonymous user from ecommerce and then Order is completed by admin(or any user) from Order Manager
    // then userLogin is not found when Order Complete Mail is send to user.
    if (UtilValidate.isEmpty(userLogin)) 
    {
        if (UtilValidate.isNotEmpty(orderId)) 
        {
            orderHeader = delegator.findOne("OrderHeader", [orderId : orderId], true);
            orderStatuses = orderHeader.getRelatedOne("OrderStatus");
            filteredOrderStatusList = [];
            extOfflineModeExists = false;
            
            // Handled the case of OFFLINE payment method.
            orderPaymentPreferences = orderHeader.getRelated("OrderPaymentPreference", UtilMisc.toList("orderPaymentPreferenceId"));
            filteredOrderPaymentPreferences = EntityUtil.filterByCondition(orderPaymentPreferences, EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.IN, ["EXT_OFFLINE"]));
            if (UtilValidate.isNotEmpty(filteredOrderPaymentPreferences))
            {
                extOfflineModeExists = true;
            }
            if (extOfflineModeExists) 
            {
                filteredOrderStatusList = EntityUtil.filterByCondition(orderStatuses, EntityCondition.makeCondition("statusId", EntityOperator.IN, ["ORDER_COMPLETED", "ORDER_APPROVED", "ORDER_CREATED"]));
            } 
            else 
            {
                filteredOrderStatusList = EntityUtil.filterByCondition(orderStatuses, EntityCondition.makeCondition("statusId", EntityOperator.IN, ["ORDER_COMPLETED", "ORDER_APPROVED"]));
            }            
            if (UtilValidate.isNotEmpty(filteredOrderStatusList)) 
            {
                if (filteredOrderStatusList.size() < 2) 
                {
                    statusUserLogin = EntityUtil.getFirst(filteredOrderStatusList).statusUserLogin;
                    userLogin = delegator.findOne("UserLogin", [userLoginId : statusUserLogin], true);
                } 
                else 
                {
                    filteredOrderStatusList.each { orderStatus ->
                        if ("ORDER_COMPLETED".equals(orderStatus.statusId)) 
                        {
                            statusUserLogin = orderStatus.statusUserLogin;
                            userLogin = delegator.findOne("UserLogin", [userLoginId :statusUserLogin], true);
                        }
                    }
                }
            }
        }
    }
    context.userLogin = userLogin;
}

/* partyId = null;
if (userLogin) partyId = userLogin.partyId; */

partyId = context.partyId;
if (UtilValidate.isNotEmpty(orderId)) 
{
    if (UtilValidate.isEmpty(partyId)) 
    {
        partyId = userLogin.partyId;
    }
}


// can anybody view an anonymous order?  this is set in the screen widget and should only be turned on by an email confirmation screen
allowAnonymousView = context.allowAnonymousView;

isDemoStore = true;
shippingApplies = true;
if (UtilValidate.isNotEmpty(orderId)) 
{
    orderHeader = delegator.findByPrimaryKeyCache("OrderHeader", [orderId : orderId]);
	if (UtilValidate.isNotEmpty(orderHeader))
	{
	    if ("PURCHASE_ORDER".equals(orderHeader?.orderTypeId)) 
	    {
	        //drop shipper or supplier
	        roleTypeId = "SUPPLIER_AGENT";
	    } else 
	    {
	        //customer
	        roleTypeId = "PLACING_CUSTOMER";
	    }
	    context.roleTypeId = roleTypeId;
	    // check OrderRole to make sure the user can view this order.  This check must be done for any order which is not anonymously placed and
	    // any anonymous order when the allowAnonymousView security flag (see above) is not set to Y, to prevent peeking
	    if (UtilValidate.isNotEmpty(orderHeader) && (!"anonymous".equals(orderHeader.createdBy) || ("anonymous".equals(orderHeader.createdBy) && !"Y".equals(allowAnonymousView)))) 
	    {
	        orderRole = EntityUtil.getFirst(delegator.findByAnd("OrderRole", [orderId : orderId, partyId : partyId, roleTypeId : roleTypeId]));
	        if (UtilValidate.isEmpty(userLogin) || UtilValidate.isEmpty(orderRole)) 
	        {
	            context.remove("orderHeader");
	            orderHeader = null;
	            Debug.logWarning("Warning: in OrderStatus.groovy before getting order detail info: role not found or user not logged in; partyId=[" + partyId + "], userLoginId=[" + (userLogin == null ? "null" : userLogin.get("userLoginId")) + "]", "orderstatus");
	        }
	    }
		
		customerPoNumberSet="";
		productStore = orderHeader.getRelatedOne("ProductStore");
		orderReadHelper = new OrderReadHelper(orderHeader);
		currencyUom = orderReadHelper.getCurrency();
		orderItems = orderReadHelper.getOrderItems();
		orderAdjustments = orderReadHelper.getAdjustments();
		orderHeaderAdjustments = orderReadHelper.getOrderHeaderAdjustments();
		orderAdjustmentsPromotion = EntityUtil.filterByAnd(orderAdjustments, UtilMisc.toMap("orderAdjustmentTypeId", "PROMOTION_ADJUSTMENT"));
		orderAdjustmentsShippingCharge = EntityUtil.filterByAnd(orderAdjustments, UtilMisc.toMap("orderAdjustmentTypeId", "SHIPPING_CHARGES"));
		orderAdjustmentsSalesTax = EntityUtil.filterByAnd(orderAdjustments, UtilMisc.toMap("orderAdjustmentTypeId", "SALES_TAX"));
		orderAdjustmentsLoyalty = EntityUtil.filterByAnd(orderAdjustments, UtilMisc.toMap("orderAdjustmentTypeId", "LOYALTY_POINTS"));
		orderAdjustmentsDiscount = EntityUtil.filterByAnd(orderAdjustments, UtilMisc.toMap("orderAdjustmentTypeId", "DISCOUNT_ADJUSTMENT"));
		orderSubTotal = orderReadHelper.getOrderItemsSubTotal();
		orderItemShipGroups = orderReadHelper.getOrderItemShipGroups();
		shipGroupsSize = orderItemShipGroups.size();
		headerAdjustmentsToShow = orderReadHelper.getOrderHeaderAdjustmentsToShow();
		shippingApplies = orderReadHelper.shippingApplies();
		orderItemsTotalQty = orderReadHelper.getTotalOrderItemsQuantity();
	
		orderShippingTotal = OrderReadHelper.getAllOrderItemsAdjustmentsTotal(orderItems, orderAdjustments, false, false, true);
		orderShippingTotal = orderShippingTotal.add(OrderReadHelper.calcOrderAdjustments(orderHeaderAdjustments, orderSubTotal, false, false, true));
	
		orderTaxTotal = OrderReadHelper.getAllOrderItemsAdjustmentsTotal(orderItems, orderAdjustments, false, true, false);
		orderTaxTotal = orderTaxTotal.add(OrderReadHelper.calcOrderAdjustments(orderHeaderAdjustments, orderSubTotal, false, true, false));
		
		orderGrandTotal = OrderReadHelper.getOrderGrandTotal(orderItems, orderAdjustments);
	
	
		placingCustomerOrderRoles = delegator.findByAnd("OrderRole", [orderId : orderId, roleTypeId : roleTypeId]);
		placingCustomerOrderRole = EntityUtil.getFirst(placingCustomerOrderRoles);
		placingCustomerPerson = placingCustomerOrderRole == null ? null : delegator.findByPrimaryKeyCache("Person", [partyId : placingCustomerOrderRole.partyId]);
	
		billingAccount = orderHeader.getRelatedOne("BillingAccount");
	
		orderPaymentPreferences = EntityUtil.filterByAnd(orderHeader.getRelated("OrderPaymentPreference"), [EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_CANCELLED")]);
		paymentMethods = [];
		orderPaymentPreferences.each { opp ->
			paymentMethod = opp.getRelatedOne("PaymentMethod");
			if (UtilValidate.isNotEmpty(paymentMethod))
			{
				paymentMethods.add(paymentMethod);
			} else
			{
				paymentMethodType = opp.getRelatedOne("PaymentMethodType");
				if (UtilValidate.isNotEmpty(paymentMethodType))
				{
					context.paymentMethodType = paymentMethodType;
				}
			}
		}
	
		webSiteId = orderHeader.webSiteId ?: CatalogWorker.getWebSiteId(request);
	
		payToPartyId = productStore.payToPartyId;
		paymentAddress =  PaymentWorker.getPaymentAddress(delegator, payToPartyId);
		if (UtilValidate.isNotEmpty(paymentAddress))
		{
			context.paymentAddress = paymentAddress;
		}
	
		// get Shipment tracking info
		osisCond = EntityCondition.makeCondition([orderId : orderId], EntityOperator.AND);
		osisOrder = ["shipmentId", "shipmentRouteSegmentId", "shipmentPackageSeqId"];
		osisFields = ["shipmentId", "shipmentRouteSegmentId", "carrierPartyId", "shipmentMethodTypeId"] as Set;
		osisFields.add("shipmentPackageSeqId");
		osisFields.add("trackingCode");
		osisFields.add("boxNumber");
		osisFindOptions = new EntityFindOptions();
		osisFindOptions.setDistinct(true);
		orderShipmentInfoSummaryList = delegator.findList("OrderShipmentInfoSummary", osisCond, osisFields, osisOrder, osisFindOptions, true);
	
		// check if there are returnable items
		returned = 0.00;
		totalItems = 0.00;
		orderItems.each { oitem ->
			totalItems += oitem.quantity;
			ritems = oitem.getRelated("ReturnItem");
			ritems.each { ritem ->
				rh = ritem.getRelatedOne("ReturnHeader");
				if (!rh.statusId.equals("RETURN_CANCELLED"))
				{
					returned += ritem.returnQuantity;
				}
			}
		}
	
		if (totalItems > returned)
		{
			context.returnLink = "Y";
		}
		
		if(UtilValidate.isNotEmpty(orderItems))
		{
			for (GenericValue orderItem : orderItems)
			{
				cartItemAdjustment = orderReadHelper.getOrderItemAdjustmentsTotal(orderItem);
				if(cartItemAdjustment < 0)
				{
					offerPriceVisible= "Y";
					break;
				}
			}
		}
		
		//get Delivery Option
		deliveryOptionAttr = delegator.findOne("OrderAttribute", [orderId : orderHeader.orderId, attrName : "DELIVERY_OPTION"], true);
		if (UtilValidate.isNotEmpty(deliveryOptionAttr))
		{
			deliveryOption =deliveryOptionAttr.attrValue;
		}
		
		//get shipping method
		selectedStoreId = "";
		isStorePickUp = "N";
		if(UtilValidate.isNotEmpty(orderItemShipGroups))
		{
			for(GenericValue shipGroup : orderItemShipGroups)
			{
				shippingInstructions = shipGroup.shippingInstructions;
				if(UtilValidate.isNotEmpty(orderHeader))
				{
					orderAttrPickupStoreList = orderHeader.getRelated("OrderAttribute", UtilMisc.toMap("attrName", "STORE_LOCATION"), null, true);
					if(UtilValidate.isNotEmpty(orderAttrPickupStoreList))
					{
						orderAttrPickupStore = EntityUtil.getFirst(orderAttrPickupStoreList);
						selectedStoreId = orderAttrPickupStore.attrValue;
						chosenShippingMethodDescription = uiLabelMap.StorePickupLabel;
						isStorePickUp = "Y";
					}
					if(UtilValidate.isEmpty(selectedStoreId))
					{
						context.storePickupId=selectedStoreId;
						party = delegator.findOne("Party", [partyId : selectedStoreId], true);
						if (UtilValidate.isNotEmpty(party))
						{
							partyGroup = party.getRelatedOne("PartyGroup");
							if (UtilValidate.isNotEmpty(partyGroup))
							{
								context.storePickupName = partyGroup.groupName;
							}
	
							partyContactMechPurpose = party.getRelated("PartyContactMechPurpose");
							partyContactMechPurpose = EntityUtil.filterByDate(partyContactMechPurpose,true);
	
							partyGeneralLocations = EntityUtil.filterByAnd(partyContactMechPurpose, UtilMisc.toMap("contactMechPurposeTypeId", "GENERAL_LOCATION"));
							partyGeneralLocations = EntityUtil.getRelated("PartyContactMech", partyGeneralLocations);
							partyGeneralLocations = EntityUtil.filterByDate(partyGeneralLocations,true);
							partyGeneralLocations = EntityUtil.orderBy(partyGeneralLocations, UtilMisc.toList("fromDate DESC"));
							if (UtilValidate.isNotEmpty(partyGeneralLocations))
							{
								partyGeneralLocation = EntityUtil.getFirst(partyGeneralLocations);
								context.storePickupAddress = partyGeneralLocation.getRelatedOne("PostalAddress");
							}
	
							partyPrimaryPhones = EntityUtil.filterByAnd(partyContactMechPurpose, UtilMisc.toMap("contactMechPurposeTypeId", "PRIMARY_PHONE"));
							partyPrimaryPhones = EntityUtil.getRelated("PartyContactMech", partyPrimaryPhones);
							partyPrimaryPhones = EntityUtil.filterByDate(partyPrimaryPhones,true);
							partyPrimaryPhones = EntityUtil.orderBy(partyPrimaryPhones, UtilMisc.toList("fromDate DESC"));
							if (UtilValidate.isNotEmpty(partyPrimaryPhones))
							{
								partyPrimaryPhone = EntityUtil.getFirst(partyPrimaryPhones);
								context.storePickupPhone = partyPrimaryPhone.getRelatedOne("TelecomNumber");
							}
						}
						shipmentMethodType = shipGroup.getRelatedOne("ShipmentMethodType");
						carrierPartyId = shipGroup.carrierPartyId;
						if(UtilValidate.isNotEmpty(shipmentMethodType))
						{
							carrier =  delegator.findByPrimaryKeyCache("PartyGroup", UtilMisc.toMap("partyId", shipGroup.carrierPartyId));
							if(UtilValidate.isNotEmpty(carrier))
							{
								if(UtilValidate.isNotEmpty(carrier.groupName))
								{
									chosenShippingMethodDescription = carrier.groupName + " " + shipmentMethodType.description;
								}
								else
								{
									chosenShippingMethodDescription = carrier.partyId + " " + shipmentMethodType.description;
								}
								
							}
						}
					}
				}
			}
		}
		
		//get Adjustment Info
		appliedPromoList = new ArrayList();
		appliedLoyaltyPointsList = new ArrayList();
		if(UtilValidate.isNotEmpty(orderHeaderAdjustments) && orderHeaderAdjustments.size() > 0)
		{
			adjustments = orderHeaderAdjustments;
			for (GenericValue cartAdjustment : adjustments)
			{
				promoInfo = new HashMap();
				promoInfo.put("cartAdjustment", cartAdjustment);
				promoCodeText = "";
				adjustmentType = cartAdjustment.getRelatedOne("OrderAdjustmentType");
				adjustmentTypeDesc = adjustmentType.get("description",locale);
				//loyalty points
				if(adjustmentType.orderAdjustmentTypeId.equals("LOYALTY_POINTS"))
				{
					loyaltyPointsInfo = new HashMap();
					loyaltyPointsInfo.put("cartAdjustment", cartAdjustment);
					loyaltyPointsInfo.put("adjustmentTypeDesc", adjustmentTypeDesc);
					appliedLoyaltyPointsList.add(loyaltyPointsInfo);
				}
				productPromo = cartAdjustment.getRelatedOne("ProductPromo");
				if(UtilValidate.isNotEmpty(productPromo))
				{
					promoInfo.put("adjustmentTypeDesc", adjustmentTypeDesc);
					promoText = productPromo.promoText;
					promoInfo.put("promoText", promoText);
					productPromoCode = productPromo.getRelated("ProductPromoCode");
					if(UtilValidate.isNotEmpty(productPromoCode))
					{
						promoCodesEntered = orderReadHelper.getProductPromoCodesEntered();
						if(UtilValidate.isNotEmpty(promoCodesEntered))
						{
							for (String promoCodeEntered : promoCodesEntered)
							{
								if(UtilValidate.isNotEmpty(promoCodeEntered))
								{
									for (GenericValue promoCode : productPromoCode)
									{
										promoCodeEnteredId = promoCodeEntered;
										promoCodeId = promoCode.productPromoCodeId;
										if(UtilValidate.isNotEmpty(promoCodeEnteredId))
										{
											if(promoCodeId == promoCodeEnteredId)
											{
												promoCodeText = promoCode.productPromoCodeId;
												promoInfo.put("promoCodeText", promoCodeText);
											}
										}
									}
								}
							}
							
						}
					}
					appliedPromoList.add(promoInfo);
				}
			}
		}
		//Address Locations
		billingLocations = orderReadHelper.getBillingLocations();
		if (UtilValidate.isNotEmpty(billingLocations))
		{
		   context.billingAddress = EntityUtil.getFirst(billingLocations);
		}
		shippingLocations = orderReadHelper.getShippingLocations();
		if (UtilValidate.isNotEmpty(shippingLocations))
		{
		   context.shippingAddress = EntityUtil.getFirst(shippingLocations);
		}
		
	
		context.orderId = orderId;
		context.isStorePickUp = isStorePickUp;
		context.offerPriceVisible = offerPriceVisible;
		context.orderHeader = orderHeader;
		context.localOrderReadHelper = orderReadHelper;
		context.orderItems = orderItems;
		context.orderAdjustments = orderAdjustments;
		context.orderHeaderAdjustments = orderHeaderAdjustments;
		context.appliedPromoList = appliedPromoList;
		context.appliedLoyaltyPointsList = appliedLoyaltyPointsList;
		context.orderAdjustmentsPromotion = orderAdjustmentsPromotion;
		context.orderAdjustmentsShippingCharge = orderAdjustmentsShippingCharge;
		context.orderAdjustmentsSalesTax = orderAdjustmentsSalesTax;
		context.orderAdjustmentsLoyalty = orderAdjustmentsLoyalty;
		context.orderAdjustmentsDiscount = orderAdjustmentsDiscount;
		context.cartSubTotal = orderSubTotal;
		context.orderItemShipGroups = orderItemShipGroups;
		context.headerAdjustmentsToShow = headerAdjustmentsToShow;
		context.currencyUomId = orderReadHelper.getCurrency();
		context.shoppingCartTotalQuantity = orderItemsTotalQty;
	
		context.orderShippingTotal = orderShippingTotal;
		context.orderTaxTotal = orderTaxTotal;
		context.orderGrandTotal = orderGrandTotal;
		context.placingCustomerPerson = placingCustomerPerson;
	
		context.billingAccount = billingAccount;
		context.paymentMethods = paymentMethods;
	
		context.productStore = productStore;
		context.isDemoStore = isDemoStore;
	
		context.orderShipmentInfoSummaryList = orderShipmentInfoSummaryList;
		context.customerPoNumberSet = customerPoNumberSet;
	
		orderItemChangeReasons = delegator.findByAnd("Enumeration", [enumTypeId : "ODR_ITM_CH_REASON"], ["sequenceId"]);
		context.orderItemChangeReasons = orderItemChangeReasons;
		
		context.shippingApplies = shippingApplies;
		if(UtilValidate.isNotEmpty(chosenShippingMethodDescription))
		{
			context.chosenShippingMethodDescription = chosenShippingMethodDescription;
		}
		if(UtilValidate.isNotEmpty(shippingInstructions))
		{
			context.shippingInstructions = shippingInstructions;
		}
		context.deliveryOption = deliveryOption;
		context.currencyUom = currencyUom;
	}
}

