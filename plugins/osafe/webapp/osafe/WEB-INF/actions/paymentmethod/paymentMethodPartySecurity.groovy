package paymentmethod;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.util.EntityUtil;
import javolution.util.FastList;
import javolution.util.FastMap;
import com.osafe.util.Util;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.commons.lang.StringUtils;

userLogin = session.getAttribute("userLogin");
paymentMethodId = StringUtils.trimToEmpty(requestParameters.get("paymentMethodId"));

partySecurityCheck="N";
if(UtilValidate.isNotEmpty(paymentMethodId))
{
	if(UtilValidate.isNotEmpty(userLogin))
	{
	    partyId = userLogin.partyId;
	}
	if(UtilValidate.isNotEmpty(partyId))
	{
		fieldMap = UtilMisc.toMap("partyId", partyId,"paymentMethodId",paymentMethodId);
		if(UtilValidate.isNotEmpty(context.paymentMethodType))
		{
    		fieldMap.put("paymentMethodTypeId",context.paymentMethodType);
		}
	    paymentMethods = delegator.findByAnd("PaymentMethod", fieldMap, UtilMisc.toList("lastUpdatedStamp"));
	    if(UtilValidate.isNotEmpty(paymentMethods))
	    {
	    	partySecurityCheck="Y";
	    }
	}
	
}

context.partySecurityCheck = partySecurityCheck;



