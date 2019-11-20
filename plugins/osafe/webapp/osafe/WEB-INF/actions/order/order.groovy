package common;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ofbiz.base.util.UtilValidate;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import com.osafe.util.Util;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;

orderHeader = request.getAttribute("orderHeader");
status = orderHeader.getRelatedOne("StatusItem");
orderItemShipGroups =  orderHeader.getRelated("OrderItemShipGroup", UtilMisc.toList("shipGroupSeqId"));
orderItemShipGroupSize = orderItemShipGroups.size();
rowClass = request.getAttribute("rowClass");
lineIndex = request.getAttribute("lineIndex");
trackingURL = "";
trackingNumber = "";
if (UtilValidate.isNotEmpty(orderItemShipGroups) && orderItemShipGroupSize == 1)
 {
    for (GenericValue shipGroup : orderItemShipGroups)
    {
        trackingNumber = shipGroup.trackingNumber;
        findCarrierShipmentMethodMap = UtilMisc.toMap("shipmentMethodTypeId", shipGroup.shipmentMethodTypeId, "partyId", shipGroup.carrierPartyId,"roleTypeId" ,"CARRIER");
        carrierShipmentMethod = delegator.findByPrimaryKeyCache("CarrierShipmentMethod", findCarrierShipmentMethodMap);
        carrierDescription = "";
        if (UtilValidate.isNotEmpty(carrierShipmentMethod))
        {
            shipmentMethodType = carrierShipmentMethod.getRelatedOne("ShipmentMethodType");
            if (UtilValidate.isNotEmpty(shipmentMethodType))
            {
              carrierDescription = shipmentMethodType.description;
            }
        	
        }
        carrierPartyGroupName = "";
        if (UtilValidate.isNotEmpty(shipGroup.carrierPartyId) && shipGroup.carrierPartyId != "_NA_")
        {
            carrierParty = carrierShipmentMethod.getRelatedOne("Party");
            carrierPartyGroup = carrierParty.getRelatedOne("PartyGroup");
            carrierPartyGroupName = carrierPartyGroup.groupName;
            trackingURLPartyContents = delegator.findByAnd("PartyContent",UtilMisc.toMap("partyId",shipGroup.carrierPartyId,"partyContentTypeId","TRACKING_URL"));
            if (UtilValidate.isNotEmpty(trackingURLPartyContents))
            {
                trackingURLPartyContent = EntityUtil.getFirst(trackingURLPartyContents);
                if (UtilValidate.isNotEmpty(trackingURLPartyContent))
                {
                    content = trackingURLPartyContent.getRelatedOne("Content");
                    if (UtilValidate.isNotEmpty(content))
                    {
                        dataResource = content.getRelatedOne("DataResource");
                        if (UtilValidate.isNotEmpty(dataResource))
                        {
                            electronicText = dataResource.getRelatedOne("ElectronicText");
                            trackingURL = electronicText.textData;
                            if (UtilValidate.isNotEmpty(trackingURL))
                            {
                                trackingURL = FlexibleStringExpander.expandString(trackingURL, UtilMisc.toMap("TRACKING_NUMBER",trackingNumber));
                            }
                        }
                    }
                }
            }
        }
    }
 }




context.orderHeader = orderHeader;
context.status = status;
context.orderItemShipGroups = orderItemShipGroups;
context.orderItemShipGroupSize = orderItemShipGroupSize;
context.trackingURL = trackingURL;
context.trackingNumber = trackingNumber;
context.lineIndex = lineIndex;
context.rowClass = rowClass;



