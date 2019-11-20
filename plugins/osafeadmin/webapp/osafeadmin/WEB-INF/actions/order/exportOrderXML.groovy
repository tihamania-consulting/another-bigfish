import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.base.util.string.*;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.GenericValue;

List orderList = new ArrayList();

if(parameters.orderId || parameters.exportId)
{
    if(parameters.orderId)
    {
        orderList.add(parameters.orderId);
    }
    if(parameters.exportId)
    {
        orderList.add(parameters.exportId);
    }
    context.exportId = parameters.orderId;
}
else
{
    session = context.session;
    svcCtx = session.getAttribute("orderPDFMap");
    if (UtilValidate.isEmpty(svcCtx)) {
        Map<String, Object> svcCtx = new HashMap();
    }
    if (UtilValidate.isNotEmpty(svcCtx)) {
        svcCtx.put("viewSize",  Integer.valueOf("1000"));
        Map<String, Object> svcRes = dispatcher.runSync("searchOrders", svcCtx);
        List<GenericValue> orderXMLList = UtilGenerics.checkList(svcRes.get("completeOrderList"), GenericValue.class);
        for(GenericValue order : orderXMLList)
        {
            orderList.add(order.orderId);
        }
    }
}
if(UtilValidate.isNotEmpty(orderList))
{
    context.exportIdList = orderList
}
