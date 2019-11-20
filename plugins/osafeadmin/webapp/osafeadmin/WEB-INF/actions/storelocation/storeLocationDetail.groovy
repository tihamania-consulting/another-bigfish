package storelocation;


import javolution.util.FastMap;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.common.CommonWorkers;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.content.content.ContentWorker;
import org.apache.ofbiz.party.party.PartyHelper;
import org.apache.ofbiz.party.contact.*;

userLogin = session.getAttribute("userLogin");
partyId = StringUtils.trimToEmpty(parameters.storePartyId);
messageMap=[:];
messageMap.put("partyId", partyId);

context.partyId=partyId;

party = delegator.findOne("Party", [partyId : partyId]);
context.party = party;
storeName = "";
storeName = PartyHelper.getPartyName(party);
partyGroup = delegator.findOne("PartyGroup", [partyId : partyId], false);
if (UtilValidate.isNotEmpty(partyGroup)) 
{
    context.partyGroup = partyGroup;
    storeName = partyGroup.groupName;
}
context.storeName = storeName;
partyGeneralContactMechValueMap = new HashMap();
partyPrimaryPhoneContactMechValueMap = new HashMap();
partyPrimaryEmailContactMechValueMap = new HashMap();

contactMechs = ContactHelper.getContactMech(party, "GENERAL_LOCATION", "POSTAL_ADDRESS", false);
if (UtilValidate.isNotEmpty(contactMechs))
{
	     contactMech = EntityUtil.getFirst(contactMechs);
	     partyGeneralContactMechValueMap.put("contactMech",contactMech);
	     postalAddress = contactMech.getRelatedOne("PostalAddress"); 
	     partyGeneralContactMechValueMap.put("postalAddress",postalAddress);
	 
}

contactMechs = ContactHelper.getContactMech(party, "PRIMARY_PHONE", "TELECOM_NUMBER", false);
if (UtilValidate.isNotEmpty(contactMechs))
{
	     contactMech = EntityUtil.getFirst(contactMechs);
	     partyPrimaryPhoneContactMechValueMap.put("contactMech",contactMech);
	     telecomNumber = contactMech.getRelatedOne("TelecomNumber"); 
	     partyPrimaryPhoneContactMechValueMap.put("telecomNumber",telecomNumber);
}
context.partyGeneralContactMechValueMap = partyGeneralContactMechValueMap;
context.partyPrimaryPhoneContactMechValueMap = partyPrimaryPhoneContactMechValueMap;
context.partyPrimaryEmailContactMechValueMap = partyPrimaryEmailContactMechValueMap;


partyContent = EntityUtil.getFirst(delegator.findByAnd("PartyContent", [partyId : partyId, 	partyContentTypeId : "STORE_HOURS"]));
if (UtilValidate.isNotEmpty(partyContent)) 
{
    content = partyContent.getRelatedOne("Content");
    if (UtilValidate.isNotEmpty(content))
    {
       context.storeHoursContentId = content.contentId;
       dataResource = content.getRelatedOne("DataResource");
       if (UtilValidate.isNotEmpty(dataResource))
        {
           context.storeHoursDataResourceId = dataResource.dataResourceId;
        }
    }
}

partyContent = EntityUtil.getFirst(delegator.findByAnd("PartyContent", [partyId : partyId, 	partyContentTypeId : "STORE_NOTICE"]));
if (UtilValidate.isNotEmpty(partyContent)) 
{
    content = partyContent.getRelatedOne("Content");
    if (UtilValidate.isNotEmpty(content))
    {
       context.storeNoticeContentId = content.contentId;
       dataResource = content.getRelatedOne("DataResource");
       if (UtilValidate.isNotEmpty(dataResource))
       {
          context.storeNoticeDataResourceId = dataResource.dataResourceId;
       }
    }
}

partyContent = EntityUtil.getFirst(delegator.findByAnd("PartyContent", [partyId : partyId, 	partyContentTypeId : "STORE_CONTENT_SPOT"]));
if (UtilValidate.isNotEmpty(partyContent)) 
{
	content = partyContent.getRelatedOne("Content");
	if (UtilValidate.isNotEmpty(content))
	{
	   context.storeContentSpotContentId = content.contentId;
	   dataResource = content.getRelatedOne("DataResource");
	   if (UtilValidate.isNotEmpty(dataResource))
	   {
		  context.storeContentSpotDataResourceId = dataResource.dataResourceId;
	   }
	}
}

partyGeoPoint = EntityUtil.getFirst(delegator.findByAnd("PartyGeoPoint", [partyId : partyId]))
if (UtilValidate.isNotEmpty(partyGeoPoint)) 
{
    geoPoint = partyGeoPoint.getRelatedOne("GeoPoint");
    context.geoPoint = geoPoint;
}