package customer;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.apache.ofbiz.party.contact.ContactHelper;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.party.contact.ContactMechWorker;
import org.apache.ofbiz.base.util.*;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilDateTime;
import com.osafe.util.OsafeAdminUtil;

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

import org.apache.ofbiz.base.util.ObjectType;

partyId = parameters.partyId;
userLogin = session.getAttribute("userLogin");

session = context.session;
svcCtx = session.getAttribute("customerPDFMap");
if (UtilValidate.isEmpty(svcCtx)) 
{
    svcCtx = new HashMap();
}

if (UtilValidate.isNotEmpty(partyId))
{
    svcCtx.put("partyId", partyId);
}

if (UtilValidate.isNotEmpty(svcCtx)) 
{
	svcCtx.put("VIEW_SIZE", "10000");
	svcCtx.put("lookupFlag", "Y");
	svcCtx.put("showAll", "N");
	svcCtx.put("roleTypeId", "ANY");
	svcCtx.put("partyTypeId", "ANY");
	svcCtx.put("statusId", "ANY");
	svcCtx.put("extInfo", "N");
	svcCtx.put("partyTypeId", "PERSON");

    Map<String, Object> svcRes;
    svcRes = dispatcher.runSync("findParty", svcCtx);
    List<GenericValue> customerPDFList =  UtilGenerics.checkList(svcRes.get("completePartyList"), GenericValue.class);
    customerPDFList = EntityUtil.orderBy(customerPDFList, ["partyId"]);
    context.customerList = customerPDFList;
    if (UtilValidate.isNotEmpty(customerPDFList)) 
    {
        if (UtilValidate.isNotEmpty(customerPDFName)) 
        {
            customerPDFName = customerPDFName+(OsafeAdminUtil.convertDateTimeFormat(UtilDateTime.nowTimestamp(), "yyyy-MM-dd-HHmm"));
            response.setHeader("Content-Disposition","attachment; filename=\"" + UtilValidate.stripWhitespace(customerPDFName) + ".pdf" + "\";");
        }
        
        Map<String, Object> upPartyCtx = new HashMap();
        upPartyCtx.put("userLogin",userLogin);
        upPartyCtx.put("isDownloaded","Y");
        upPartyCtx.put("datetimeDownloaded",UtilDateTime.nowTimestamp());

        for(GenericValue party : customerPDFList)
        {
            person = delegator.findOne("Person",["partyId":party.partyId], false);
            
            partyAttrIsDownload = delegator.findOne("PartyAttribute", ["partyId" : party.partyId, "attrName" : "IS_DOWNLOADED"], false);
            Map<String, Object> isDownloadedPartyAttrCtx = new HashMap();
            isDownloadedPartyAttrCtx.put("partyId", party.partyId);
            isDownloadedPartyAttrCtx.put("userLogin",userLogin);
            isDownloadedPartyAttrCtx.put("attrName","IS_DOWNLOADED");
            isDownloadedPartyAttrCtx.put("attrValue","Y");
            Map<String, Object> isDownloadedPartyAttrMap = null;
            if (UtilValidate.isNotEmpty(partyAttrIsDownload)) 
            {
                isDownloadedPartyAttrMap = dispatcher.runSync("updatePartyAttribute", isDownloadedPartyAttrCtx);
            } else 
            {
                isDownloadedPartyAttrMap = dispatcher.runSync("createPartyAttribute", isDownloadedPartyAttrCtx);
            }
            
            partyAttrDateTimeDownload = delegator.findOne("PartyAttribute", ["partyId" : party.partyId, "attrName" : "DATETIME_DOWNLOADED"], false);
            Map<String, Object> dateTimeDownloadedPartyAttrCtx = new HashMap();
            dateTimeDownloadedPartyAttrCtx.put("partyId", party.partyId);
            dateTimeDownloadedPartyAttrCtx.put("userLogin",userLogin);
            dateTimeDownloadedPartyAttrCtx.put("attrName","DATETIME_DOWNLOADED");
            dateTimeDownloadedPartyAttrCtx.put("attrValue",UtilDateTime.nowTimestamp().toString());
            Map<String, Object> dateTimeDownloadedPartyAttrMap = null;
            if (UtilValidate.isNotEmpty(partyAttrDateTimeDownload)) 
            {
                dateTimeDownloadedPartyAttrMap = dispatcher.runSync("updatePartyAttribute", dateTimeDownloadedPartyAttrCtx);
            } else 
            {
                dateTimeDownloadedPartyAttrMap = dispatcher.runSync("createPartyAttribute", dateTimeDownloadedPartyAttrCtx);
            }
 
        }
        
    }
}





