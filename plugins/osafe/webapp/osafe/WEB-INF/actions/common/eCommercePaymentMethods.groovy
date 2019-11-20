import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.util.EntityUtil;
import javolution.util.FastList;
import javolution.util.FastMap;
import com.osafe.util.Util;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;

userLogin = session.getAttribute("userLogin");
context.userLogin = userLogin;
savedPaymentMethodValueMaps = new ArrayList();
savedPaymentMethodEftValueMaps = new ArrayList();
partyId = null;
partyProfileDefault = null;
productStoreId = "";
if(UtilValidate.isEmpty(productStoreId))
{
	productStoreId = ProductStoreWorker.getProductStoreId(request);
	productStore = ProductStoreWorker.getProductStore(request);
	if(UtilValidate.isNotEmpty(productStore))
	{
		context.productStore = productStore;
	}
}
ShoppingCart shoppingCart = session.getAttribute("shoppingCart");
//Get currency
currencyUom = Util.getProductStoreParm(request,"CURRENCY_UOM_DEFAULT");
if(UtilValidate.isEmpty(currencyUom))
{
	if(UtilValidate.isNotEmpty(shoppingCart))
	{
		currencyUom = shoppingCart.getCurrency();
	}
}
context.currencyUom = currencyUom;
if(UtilValidate.isNotEmpty(userLogin))
{
    partyId = userLogin.partyId;
}
if(UtilValidate.isNotEmpty(partyId))
{
	partyProfileDefault = delegator.findOne("PartyProfileDefault", UtilMisc.toMap("partyId", partyId, "productStoreId", productStoreId), true);
}
if(Util.isProductStoreParmTrue(request,"CHECKOUT_KEEP_PAYMENT_METHODS"))
{
    if(UtilValidate.isNotEmpty(partyId))
    {
        paymentMethods = delegator.findByAnd("PaymentMethod", UtilMisc.toMap("partyId", partyId), UtilMisc.toList("lastUpdatedStamp"));
        paymentMethodValueMaps = new ArrayList();
        paymentMethodEftValueMaps = new ArrayList();
        if(UtilValidate.isNotEmpty(paymentMethods))
        {
            paymentMethods = EntityUtil.filterByDate(paymentMethods, true);
            for (GenericValue paymentMethod : paymentMethods) 
            {
                if ("CREDIT_CARD".equals(paymentMethod.getString("paymentMethodTypeId"))) 
                {
                    valueMap = new HashMap();
                    paymentMethodValueMaps.add(valueMap);
                    valueMap.put("paymentMethod", paymentMethod);
                    GenericValue creditCard = paymentMethod.getRelatedOne("CreditCard");
                    if (UtilValidate.isNotEmpty(creditCard))
                    {
                        valueMap.put("creditCard", creditCard);
                        
                    }
                }
                else if ("EFT_ACCOUNT".equals(paymentMethod.getString("paymentMethodTypeId")))
                {
                    eftValueMap = new HashMap();
                    paymentMethodEftValueMaps.add(eftValueMap);
                    eftValueMap.put("paymentMethod", paymentMethod);
                    GenericValue eftAccount = paymentMethod.getRelatedOne("EftAccount");
                    if (UtilValidate.isNotEmpty(eftAccount))
                    {
                        eftValueMap.put("eftAccount", eftAccount);
                    }
                }
            }
            savedPaymentMethodValueMaps= paymentMethodValueMaps;
            savedPaymentMethodEftValueMaps = paymentMethodEftValueMaps; 
        }
    }
    context.savedPaymentMethodValueMaps = savedPaymentMethodValueMaps;
    context.savedPaymentMethodEftValueMaps = savedPaymentMethodEftValueMaps;
}
context.partyProfileDefault = partyProfileDefault;
