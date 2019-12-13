package common;

import org.apache.ofbiz.base.util.UtilValidate;
import java.text.NumberFormat;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.product.category.CategoryContentWrapper;
import org.apache.ofbiz.product.catalog.CatalogWorker;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartEvents;
import javolution.util.FastList;
import com.osafe.util.Util;
import com.osafe.services.OsafeManageXml;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilMisc;
import com.osafe.services.InventoryServices;
import org.apache.ofbiz.party.content.PartyContentWrapper;
import com.osafe.control.SeoUrlHelper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilNumber;
import org.apache.ofbiz.base.util.UtilFormatOut;
import org.apache.ofbiz.base.util.Debug;
import java.util.LinkedHashMap;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.order.order.OrderReadHelper;
import com.osafe.events.WishListEvents;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;


wishListItem = request.getAttribute("wishListItem");
rowNo = request.getAttribute("rowNo");
cartLineIndex = rowNo;



wishListSize = 0;
wishListId = WishListEvents.getWishListId(request, false);
if (UtilValidate.isNotEmpty(wishListId))
{
	wishList = delegator.findByAnd("ShoppingListItem", [shoppingListId : wishListId]);
	wishListSize = wishList.size();
}


urlProductId = wishListItem.productId;
productId = wishListItem.productId;
product = delegator.findOne("Product", UtilMisc.toMap("productId",productId), true);
productPrice = dispatcher.runSync("calculateProductPrice", UtilMisc.toMap("product", product, "userLogin", userLogin));
totalPrice = (productPrice.basePrice)*(wishListItem.quantity);
quantity = wishListItem.quantity;



productCategoryId = product.primaryProductCategoryId;
virtualProduct="";
if(UtilValidate.isEmpty(productCategoryId))
{
	productCategoryMemberList = product.getRelated("ProductCategoryMember");
	productCategoryMemberList = EntityUtil.filterByDate(productCategoryMemberList,true);
	productCategoryMemberList = EntityUtil.orderBy(productCategoryMemberList, UtilMisc.toList('sequenceNum'));
	if(UtilValidate.isNotEmpty(productCategoryMemberList))
	{
		productCategoryMember = EntityUtil.getFirst(productCategoryMemberList);
		productCategoryId = productCategoryMember.productCategoryId;
	}
}

inventoryLevelMap = InventoryServices.getProductInventoryLevel(productId, request);
inventoryLevel = inventoryLevelMap.get("inventoryLevel");
inventoryInStockFrom = inventoryLevelMap.get("inventoryLevelInStockFrom");
inventoryOutOfStockTo = inventoryLevelMap.get("inventoryLevelOutOfStockTo");
if (inventoryLevel <= inventoryOutOfStockTo)
{
	stockInfo = uiLabelMap.OutOfStockLabel;
	inStock = false
}
else
{
	inStock = true;
	if (inventoryLevel >= inventoryInStockFrom)
	{
		stockInfo = uiLabelMap.InStockLabel;
	}
	else
	{
		stockInfo = uiLabelMap.LowStockLabel;
	}
}





if(UtilValidate.isNotEmpty(product.isVariant) && "Y".equals(product.isVariant))
{
	virtualProduct = ProductWorker.getParentProduct(productId, delegator);
	urlProductId = virtualProduct.productId;
	if(UtilValidate.isEmpty(productCategoryId))
	{
		productCategoryMemberList = virtualProduct.getRelated("ProductCategoryMember");
		productCategoryMemberList = EntityUtil.filterByDate(productCategoryMemberList,true);
		productCategoryMemberList = EntityUtil.orderBy(productCategoryMemberList, UtilMisc.toList('sequenceNum'));
		if(UtilValidate.isNotEmpty(productCategoryMemberList))
		{
			productCategoryMember = EntityUtil.getFirst(productCategoryMemberList);
			productCategoryId = productCategoryMember.productCategoryId;
		}
	}
}

//Product Image URL
productImageUrl = ProductContentWrapper.getProductContentAsText(product, "SMALL_IMAGE_URL", locale, dispatcher, "string");
if(UtilValidate.isEmpty(productImageUrl) && UtilValidate.isNotEmpty(virtualProduct))
{
	productImageUrl = ProductContentWrapper.getProductContentAsText(virtualProduct, "SMALL_IMAGE_URL", locale, dispatcher, "string");
}
//If the string is a literal "null" make it an "" empty string then all normal logic can stay the same
if(UtilValidate.isNotEmpty(productImageUrl) && "null".equals(productImageUrl))
{
	productImageUrl = "";
}

//Product Alt Image URL
productImageAltUrl = ProductContentWrapper.getProductContentAsText(product, "SMALL_IMAGE_ALT_URL", locale, dispatcher, "string");
if(UtilValidate.isEmpty(productImageAltUrl) && UtilValidate.isNotEmpty(virtualProduct))
{
	productImageAltUrl = ProductContentWrapper.getProductContentAsText(virtualProduct, "SMALL_IMAGE_ALT_URL", locale, dispatcher, "string");
}
//If the string is a literal "null" make it an "" empty string then all normal logic can stay the same
if(UtilValidate.isNotEmpty(productImageAltUrl) && "null".equals(productImageAltUrl))
{
	productImageAltUrl = "";
}

//Product Name
productName = ProductContentWrapper.getProductContentAsText(product, "PRODUCT_NAME", locale, dispatcher);
if(UtilValidate.isEmpty(productName) && UtilValidate.isNotEmpty(virtualProduct))
{
	productName = ProductContentWrapper.getProductContentAsText(virtualProduct, "PRODUCT_NAME", locale, dispatcher);
}

/*
price = orderItem.unitPrice;
displayPrice = orderItem.unitPrice;
offerPrice = "";
if(UtilValidate.isNotEmpty(orderHeader))
{
	orderReadHelper = new OrderReadHelper(orderHeader);
	if(UtilValidate.isNotEmpty(orderReadHelper))
	{
		cartItemAdjustment = orderReadHelper.getOrderItemAdjustmentsTotal(orderItem);
	}
	
	if(UtilValidate.isNotEmpty(orderItem))
	{
		itemSubTotal = orderReadHelper.getOrderItemSubTotal(orderItem,orderReadHelper.getAdjustments());
	}
}


if (cartItemAdjustment < 0)
{
	offerPrice = orderItem.unitPrice + (cartItemAdjustment/orderItem.quantity);
}

if ("Y".equals(orderItem.isPromo))
{
	price = orderItem.unitPrice;
}
else 
{ 
	if (orderItem.selectedAmount > 0)
	{
		price =  orderItem.unitPrice / orderItem.selectedAmount;
	}
	else
	{
		price =  orderItem.unitPrice;
	}
}
*/

//Get currency
CURRENCY_UOM_DEFAULT = Util.getProductStoreParm(request,"CURRENCY_UOM_DEFAULT");
currencyUom = CURRENCY_UOM_DEFAULT;

//BUILD CONTEXT MAP FOR PRODUCT_FEATURE_TYPE_ID and DESCRIPTION(EITHER FROM PRODUCT_FEATURE_GROUP OR PRODUCT_FEATURE_TYPE)
Map productFeatureTypesMap = new HashMap();
productFeatureTypesList = delegator.findList("ProductFeatureType", null, null, null, null, true);

//get the whole list of ProductFeatureGroup and ProductFeatureGroupAndAppl
productFeatureGroupList = delegator.findList("ProductFeatureGroup", null, null, null, null, true);
productFeatureGroupAndApplList = delegator.findList("ProductFeatureGroupAndAppl", null, null, null, null, true);
productFeatureGroupAndApplList = EntityUtil.filterByDate(productFeatureGroupAndApplList);

if(UtilValidate.isNotEmpty(productFeatureTypesList))
{
	for (GenericValue productFeatureType : productFeatureTypesList)
	{
		//filter the ProductFeatureGroupAndAppl list based on productFeatureTypeId to get the ProductFeatureGroupId
		productFeatureGroupAndAppls = EntityUtil.filterByAnd(productFeatureGroupAndApplList, UtilMisc.toMap("productFeatureTypeId", productFeatureType.productFeatureTypeId));
		description = "";
		if(UtilValidate.isNotEmpty(productFeatureGroupAndAppls))
		{
			productFeatureGroupAndAppl = EntityUtil.getFirst(productFeatureGroupAndAppls);
			productFeatureGroups = EntityUtil.filterByAnd(productFeatureGroupList, UtilMisc.toMap("productFeatureGroupId", productFeatureGroupAndAppl.productFeatureGroupId));
			productFeatureGroup = EntityUtil.getFirst(productFeatureGroups);
			description = productFeatureGroup.description;
		}
		else
		{
			description = productFeatureType.description;
		}
		productFeatureTypesMap.put(productFeatureType.productFeatureTypeId,description);
	}
	
}

//product features : STANDARD FEATURES 
//Issue 38934, 38916 - Check for duplicate feature descriptions
productFeatureAndAppls = new ArrayList();
Map standardFeatureExistsMap = new HashMap();
standardFeatures = delegator.findByAnd("ProductFeatureAndAppl", UtilMisc.toMap("productId", productId, "productFeatureApplTypeId", "STANDARD_FEATURE"), UtilMisc.toList("sequenceNum"));
standardFeatures = EntityUtil.filterByDate(standardFeatures,true);
standardFeatures = EntityUtil.orderBy(standardFeatures,UtilMisc.toList('sequenceNum'));

for (GenericValue standardFeature : standardFeatures)
{
    String featureDescription = standardFeature.description;
    if (UtilValidate.isNotEmpty(featureDescription)) 
    {
    	featureDescription = featureDescription.toUpperCase();
        if (!standardFeatureExistsMap.containsKey(featureDescription))
        {
        	productFeatureAndAppls.add(standardFeature);
        	standardFeatureExistsMap.put(featureDescription,featureDescription);
        }
    }
}

productFriendlyUrl = SeoUrlHelper.makeSeoFriendlyUrl(request,'eCommerceProductDetail?productId='+urlProductId+'&productCategoryId='+productCategoryId+'');

IMG_SIZE_WISHLIST_H = Util.getProductStoreParm(request,"IMG_SIZE_WISHLIST_H");
IMG_SIZE_WISHLIST_W = Util.getProductStoreParm(request,"IMG_SIZE_WISHLIST_W");

pdpQtyMinAttributeValue = "";
pdpQtyMaxAttributeValue = "";
if(UtilValidate.isNotEmpty(product))
{
	productAttrPdpQtyMin = delegator.findOne("ProductAttribute", UtilMisc.toMap("productId",productId,"attrName","PDP_QTY_MIN"), true);
	productAttrPdpQtyMax = delegator.findOne("ProductAttribute", UtilMisc.toMap("productId",productId,"attrName","PDP_QTY_MAX"), true);
	if(UtilValidate.isNotEmpty(productAttrPdpQtyMin) && UtilValidate.isNotEmpty(productAttrPdpQtyMax))
	{
		pdpQtyMinAttributeValue = productAttrPdpQtyMin.attrValue;
		pdpQtyMaxAttributeValue = productAttrPdpQtyMax.attrValue;
	}
}

qtyInCart = 0;
shoppingCart = ShoppingCartEvents.getCartObject(request);
if(UtilValidate.isNotEmpty(shoppingCart))
{
	shoppingCartItems = shoppingCart.items();
	if(UtilValidate.isNotEmpty(shoppingCartItems))
	{
		for (ShoppingCartItem shoppingCartItem : shoppingCartItems)
		{
			qtyInCart = shoppingCartItem.getQuantity();
		}
	}
}

//image 
context.productImageUrl = productImageUrl;
context.productImageAltUrl = productImageAltUrl;
context.IMG_SIZE_WISHLIST_H = IMG_SIZE_WISHLIST_H;
context.IMG_SIZE_WISHLIST_W = IMG_SIZE_WISHLIST_W;
//friendlyURL
context.productFriendlyUrl = productFriendlyUrl;
context.urlProductId = urlProductId;
context.productId = productId;
context.productCategoryId = productCategoryId;
//product Name
context.productName = productName;
if(UtilValidate.isNotEmpty(productName))
{
	context.wrappedProductName = StringUtil.wrapString(productName);
}
//product features 
context.productFeatureAndAppls = productFeatureAndAppls;
context.productFeatureTypesMap = productFeatureTypesMap;
context.displayPrice = productPrice.basePrice;
context.currencyUom = currencyUom;
//quantity
context.quantity = quantity;
context.qtyInCart = qtyInCart;
//item subtotal
context.itemSubTotal = totalPrice;
context.wishListSeqId= wishListItem.shoppingListItemSeqId;
context.rowNo = rowNo;
context.inStock = inStock;
context.wishListItem = wishListItem;
context.wishListSize = wishListSize;
context.cartLineIndex = cartLineIndex;
context.lineIndex = context.cartLineIndex;

//inventory
context.stockInfo = stockInfo;
context.inStock = inStock;
context.pdpQtyMinAttributeValue = pdpQtyMinAttributeValue;
context.pdpQtyMaxAttributeValue = pdpQtyMaxAttributeValue;

