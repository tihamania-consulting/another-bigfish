package admin;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.apache.ofbiz.party.contact.ContactHelper;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilValidate;
import javolution.util.FastList;
import javolution.util.FastMap;
import java.sql.Date;
import java.sql.Timestamp;
userLogin = session.getAttribute("userLogin");
requestParams = UtilHttp.getParameterMap(request);
context.userLogin=userLogin;
enumeration="";
enumId="";

enumId = requestParams.get("enumId");
if (!enumId) 
{
    enumId = parameters.enumId;
}

if(UtilValidate.isNotEmpty(enumId))
{
    enumeration = delegator.findOne("Enumeration",UtilMisc.toMap("enumId", enumId), false);
}
context.enum = enumeration;
context.enumId = enumId;
 