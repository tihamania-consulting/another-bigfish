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
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart.CartShipInfo;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart.CartShipInfo.CartShipItemInfo;
import org.apache.ofbiz.order.shoppingcart.shipping.ShippingEstimateWrapper;



shipGroup = request.getAttribute("shipGroup");
shipGroupIndex = request.getAttribute("shipGroupIndex");

context.shipGroup=shipGroup;
context.shipGroupIndex=shipGroupIndex;
