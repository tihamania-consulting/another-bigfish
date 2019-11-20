import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilMisc;

if (UtilValidate.isNotEmpty(context.pageTrackingList))
{   
    pixelTrackingList = EntityUtil.filterByAnd(context.pageTrackingList, UtilMisc.toMap("pixelPagePosition", request.getAttribute("pixelPagePosition")));
    pixelTrackingList = EntityUtil.orderBy(pixelTrackingList,UtilMisc.toList("pixelSequenceNum"));
    context.pixelTrackingList = pixelTrackingList;
    newUserLogin = request.getSession().getAttribute("NEW_USER_LOGIN");
    if (UtilValidate.isNotEmpty(newUserLogin))
    {
    	context.newAccountCreated="Y";
    	request.getSession().removeAttribute("NEW_USER_LOGIN");
    }
    
}

