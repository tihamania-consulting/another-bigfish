import java.math.BigDecimal;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartEvents
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.service.GenericServiceException

shoppingCart = ShoppingCartEvents.getCartObject(request);
userLogin = session.getAttribute("userLogin");
context.userLogin = userLogin;
partyId = null;
productStoreId = "";
partyStoreCreditBalance = BigDecimal.ZERO;
partyAppliedStoreCreditTotal = BigDecimal.ZERO;

if(UtilValidate.isEmpty(productStoreId))
{
    productStoreId = ProductStoreWorker.getProductStoreId(request);
}

if(UtilValidate.isNotEmpty(userLogin))
{
    partyId = userLogin.partyId;
}
if(UtilValidate.isNotEmpty(partyId))
{
    //Get party store credit
    processorResult = null;
    try
    {
        processorResult = dispatcher.runSync("balanceInquireStoreCredit", UtilMisc.toMap("partyId", partyId, "productStoreId", productStoreId, "userLogin", userLogin));
    }
    catch (GenericServiceException e)
    {
        processorResult = null;
    }
    
    if(UtilValidate.isNotEmpty(processorResult))
    {
        partyStoreCreditBalance = processorResult.get("balance");
    }

    //Get applied store credit payment
    try
    {
        processorResult = dispatcher.runSync("getStoreCreditPaymentTotal", UtilMisc.toMap("cart", shoppingCart, "userLogin", userLogin));
    }
    catch (GenericServiceException e)
    {
        processorResult = null;
    }
    
    if(UtilValidate.isNotEmpty(processorResult))
    {
        partyAppliedStoreCreditTotal = processorResult.get("total");
    }
}
context.partyStoreCreditBalance = partyStoreCreditBalance;
context.partyAppliedStoreCreditTotal = partyAppliedStoreCreditTotal;


