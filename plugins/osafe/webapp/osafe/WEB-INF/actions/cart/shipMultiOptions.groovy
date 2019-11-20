package common;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ofbiz.base.util.UtilValidate;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.product.ProductWorker;
import com.osafe.util.Util;
import org.apache.ofbiz.base.util.UtilMisc;
import com.osafe.control.SeoUrlHelper;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.entity.Delegator;
import com.osafe.services.InventoryServices;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.product.catalog.CatalogWorker;



ShoppingCart shoppingCart = session.getAttribute("shoppingCart");
context.shoppingCart=shoppingCart;
context.shippingApplies=shoppingCart.shippingApplies();