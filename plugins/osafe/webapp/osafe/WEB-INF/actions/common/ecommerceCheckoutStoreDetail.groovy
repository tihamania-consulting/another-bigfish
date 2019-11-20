package common;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.party.contact.ContactMechWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import javolution.util.FastList;
import org.apache.ofbiz.base.util.Debug

storeId = parameters.storeId;
shoppingCart = session.getAttribute("shoppingCart");
if (UtilValidate.isEmpty(storeId)) 
{
    if (UtilValidate.isNotEmpty(shoppingCart))
    {
        storeId = shoppingCart.getOrderAttribute("STORE_LOCATION");
        context.shoppingCart = shoppingCart;
		context.shoppingCartStoreId = storeId;
    }
} 
else 
{
    context.shoppingCart = session.getAttribute("shoppingCart");
}

if (UtilValidate.isEmpty(storeId)) 
{
    orderId = parameters.orderId;
    if (UtilValidate.isNotEmpty(orderId)) 
    {
        orderAttrPickupStore = delegator.findOne("OrderAttribute", ["orderId" : orderId, "attrName" : "STORE_LOCATION"], true);
        if (UtilValidate.isNotEmpty(orderAttrPickupStore)) 
        {
            storeId = orderAttrPickupStore.attrValue;
			context.shoppingCartStoreId = storeId;
        }
    }
}


//get a list of all the stores
productStore = ProductStoreWorker.getProductStore(request);
productStoreId=productStore.getString("productStoreId");
openStores = new ArrayList();
allStores = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId,"roleTypeId", "STORE_LOCATION"));
if (UtilValidate.isNotEmpty(allStores))
{
	for(GenericValue store : allStores)
	{
		storeParty = store.getRelatedOne("Party");
		if(UtilValidate.isNotEmpty(storeParty.statusId) && "PARTY_ENABLED".equals(storeParty.statusId))
		{
			openStores.add(storeParty);
		}
	}
}
if (UtilValidate.isNotEmpty(openStores))
{
	if(openStores.size() == 1)
	{
		oneStoreOpen = openStores.getAt(0);
		storeId = oneStoreOpen.partyId;
		context.oneStoreOpenStoreId = storeId;
	}
}



if (UtilValidate.isNotEmpty(storeId)) 
{
    party = delegator.findOne("Party", [partyId : storeId], true);
    if (UtilValidate.isNotEmpty(party))
    {
        partyGroup = party.getRelatedOne("PartyGroup");
        if (UtilValidate.isNotEmpty(partyGroup)) 
        {
            context.storeInfo = partyGroup;
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
        	context.storeAddress = partyGeneralLocation.getRelatedOne("PostalAddress");
        }

        partyPrimaryPhones = EntityUtil.filterByAnd(partyContactMechPurpose, UtilMisc.toMap("contactMechPurposeTypeId", "PRIMARY_PHONE"));
        partyPrimaryPhones = EntityUtil.getRelated("PartyContactMech", partyPrimaryPhones);
        partyPrimaryPhones = EntityUtil.filterByDate(partyPrimaryPhones,true);
        partyPrimaryPhones = EntityUtil.orderBy(partyPrimaryPhones, UtilMisc.toList("fromDate DESC"));
        if (UtilValidate.isNotEmpty(partyPrimaryPhones)) 
        {
        	partyPrimaryPhone = EntityUtil.getFirst(partyPrimaryPhones);
        	context.storePhone = partyPrimaryPhone.getRelatedOne("TelecomNumber");
        }
    }
}







