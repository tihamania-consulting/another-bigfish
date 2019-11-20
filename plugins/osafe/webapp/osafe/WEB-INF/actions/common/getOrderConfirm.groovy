package common;

import org.apache.ofbiz.base.util.UtilValidate;

viewName="orderHistory";
orderId = parameters.orderId;
if(UtilValidate.isNotEmpty(orderId))
{
	viewName="orderConfirm";
}
    	
return viewName;