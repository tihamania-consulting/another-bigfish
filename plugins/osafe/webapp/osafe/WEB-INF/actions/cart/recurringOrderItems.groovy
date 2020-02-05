package cart;

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
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import com.osafe.control.SeoUrlHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;

rowClass = request.getAttribute("rowClass");
lineIndex = request.getAttribute("lineIndex");
if (UtilValidate.isEmpty(lineIndex))
{
	lineIndex = 0;
}

shoppingList = request.getAttribute("shoppingList");
shoppingListId = "";
if (UtilValidate.isEmpty(shoppingList))
{
	shoppingListId = StringUtils.trimToEmpty(requestParameters.get("shoppingListId"));
	if (UtilValidate.isEmpty(shoppingListId))
	{
		shoppingListId = session.getAttribute("shoppingListId");
		
	}
	if (UtilValidate.isNotEmpty(shoppingListId))
	{
		shoppingListOne = delegator.findOne("ShoppingList",UtilMisc.toMap("shoppingListId",shoppingListId), true);
		if (UtilValidate.isNotEmpty(shoppingListOne))
		{
			userLogin = session.getAttribute("userLogin");
			if (UtilValidate.isNotEmpty(userLogin))
			{
				userLoginPartyId = userLogin.partyId;
				if (UtilValidate.isNotEmpty(userLoginPartyId))
				{
					if (userLoginPartyId == shoppingListOne.partyId)
					{
						shoppingList=shoppingListOne;
					    session.setAttribute("shoppingListId",shoppingListId);	
					}
				}

				
			}
		}
	}
}

productImageUrl = "";
productId = "";
categoryId = "";
productFriendlyUrl = "";
productImageAltUrl = "";
productName = "";
virtualProduct = "";
quantity = "";
shipDate = "";
displayPrice = "";
recurrenceFreq = "";
recurrenceItem = "N";
contactMechId = "";
paymentMethodId = "";
pdpQtyMinAttributeValue = "";
pdpQtyMaxAttributeValue = "";
status = "Y";
currencyUom ="";

Map productFeatureTypesMap = new HashMap();
List productFeatureAndAppls = new ArrayList();
partyShippingLocations = new ArrayList();

if (UtilValidate.isNotEmpty(shoppingList))
{
	lastOrderDate = shoppingList.lastOrderedDate;
	active = shoppingList.isActive;
	currencyUom = shoppingList.currencyUom;
	if (UtilValidate.isNotEmpty(active))
	{
		status = active;
	}
	recurrenceInfoList = shoppingList.getRelated("RecurrenceInfo");
	if (UtilValidate.isNotEmpty(recurrenceInfoList))
	{
		recurrenceInfo = EntityUtil.getFirst(recurrenceInfoList);
		recurrenceRuleList = recurrenceInfo.getRelated("RecurrenceRule");
		if (UtilValidate.isNotEmpty(recurrenceRuleList))
		{
			recurrenceRule = EntityUtil.getFirst(recurrenceRuleList);
			recurrenceFreq = recurrenceRule.intervalNumber;
			int intervalNumber = (int)recurrenceFreq;
			if (UtilValidate.isNotEmpty(intervalNumber) && UtilValidate.isNotEmpty(lastOrderDate) && status =="Y")
			{
				shipDate = Util.getDaysForwardTimestamp(lastOrderDate, intervalNumber);
			}
		}
	}
	
	shoppingListId = shoppingList.shoppingListId;
	shoppingListItems = shoppingList.getRelated("ShoppingListItem")
	if (UtilValidate.isNotEmpty(shoppingListItems))
	{
		shoppingListItem = EntityUtil.getFirst(shoppingListItems);
		quantity = shoppingListItem.quantity;
		productId = shoppingListItem.productId;
		ssProductId = productId;
		displayPrice = shoppingListItem.modifiedPrice;
		if(UtilValidate.isNotEmpty(productId))
		{
			product = delegator.findOne("Product", [productId : productId], true);
			if(UtilValidate.isNotEmpty(product))
			{
				  //CHECK WE HAVE A DEFAULT PRODUCT CATEGORY THE PRODUCT IS MEMBER OF
				  productCategoryId = product.primaryProductCategoryId;
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
				  if(UtilValidate.isNotEmpty(product.isVariant) && "Y".equals(product.isVariant))
				  {
					  virtualProduct = ProductWorker.getParentProduct(productId, delegator);
					  urlProductId = virtualProduct.productId;
					  if(UtilValidate.isEmpty(productCategoryId) && UtilValidate.isNotEmpty(virtualProduct))
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
				  productFriendlyUrl = SeoUrlHelper.makeSeoFriendlyUrl(request,'eCommerceProductDetail?productId='+productId+'&productCategoryId='+productCategoryId);
				
				  //product features : STANDARD FEATURES
	 			 //Issue 38934, 38916 - Check for duplicate feature descriptions
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
				  
				  
				  //BUILD CONTEXT MAP FOR PRODUCT_FEATURE_TYPE_ID and DESCRIPTION(EITHER FROM PRODUCT_FEATURE_GROUP OR PRODUCT_FEATURE_TYPE)
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
				  
				  //Product Image URL
				  productImageUrl = ProductContentWrapper.getProductContentAsText(product, "SMALL_IMAGE_URL", locale, dispatcher , "string");
				  if(UtilValidate.isEmpty(productImageUrl) && UtilValidate.isNotEmpty(virtualProduct))
				  {
					  productImageUrl = ProductContentWrapper.getProductContentAsText(virtualProduct, "SMALL_IMAGE_URL", locale, dispatcher , "string", "urstringl");
				  }
				  //If the string is a literal "null" make it an "" empty string then all normal logic can stay the same
				  if(UtilValidate.isNotEmpty(productImageUrl) && "null".equals(productImageUrl))
				  {
					  productImageUrl = "";
				  }
				  //Product Alt Image URL
				  productImageAltUrl = ProductContentWrapper.getProductContentAsText(product, "SMALL_IMAGE_ALT_URL", locale, dispatcher , "string");
				  if(UtilValidate.isEmpty(productImageAltUrl) && UtilValidate.isNotEmpty(virtualProduct))
				  {
					  productImageAltUrl = ProductContentWrapper.getProductContentAsText(virtualProduct, "SMALL_IMAGE_ALT_URL", locale, dispatcher , "string");
				  }
				  //If the string is a literal "null" make it an "" empty string then all normal logic can stay the same
				  if(UtilValidate.isNotEmpty(productImageAltUrl) && "null".equals(productImageAltUrl))
				  {
					  productImageAltUrl = "";
				  }
				  
				  //Product Name
				  productName = ProductContentWrapper.getProductContentAsText(product, "PRODUCT_NAME", locale, dispatcher , "string");
				  if(UtilValidate.isEmpty(productName) && UtilValidate.isNotEmpty(virtualProduct))
				  {
					  productName = ProductContentWrapper.getProductContentAsText(virtualProduct, "PRODUCT_NAME", locale, dispatcher , "string");
				  }
				  
				  
				  
				  
				  	paymentMethodId = shoppingList.paymentMethodId;
					contactMechId = shoppingList.contactMechId
					partyId = shoppingList.partyId;
					if (UtilValidate.isNotEmpty(partyId))
					{
						party = delegator.findByPrimaryKeyCache("Party", [partyId : partyId]);
						if (UtilValidate.isNotEmpty(party))
						{
							partyContactMechPurpose = party.getRelated("PartyContactMechPurpose");
							partyContactMechPurpose = EntityUtil.filterByDate(partyContactMechPurpose,true);
							partyContactMechPurpose = EntityUtil.orderBy(partyContactMechPurpose,UtilMisc.toList("-fromDate"));
				
							partyShippingLocations = EntityUtil.filterByAnd(partyContactMechPurpose, UtilMisc.toMap("contactMechPurposeTypeId", "SHIPPING_LOCATION"));
							partyShippingLocations = EntityUtil.getRelated("PartyContactMech", partyShippingLocations);
							partyShippingLocations = EntityUtil.filterByDate(partyShippingLocations,true);
							partyShippingLocations = EntityUtil.orderBy(partyShippingLocations, UtilMisc.toList("fromDate DESC"));
						}
					}
					
					List<GenericValue> productAttributes = productAttributes = product.getRelated("ProductAttribute");
					if(UtilValidate.isNotEmpty(productAttributes))
					{
						List<GenericValue> productAttrPdpQtyMinAttributes = EntityUtil.filterByAnd(productAttributes, UtilMisc.toMap("productId",productId,"attrName","PDP_QTY_MIN"));
						productAttrPdpQtyMin = EntityUtil.getFirst(productAttrPdpQtyMinAttributes);
						
						List<GenericValue> productAttrPdpQtyMaxAttributes = EntityUtil.filterByAnd(productAttributes, UtilMisc.toMap("productId",productId,"attrName","PDP_QTY_MAX"));
						productAttrPdpQtyMax = EntityUtil.getFirst(productAttrPdpQtyMaxAttributes);
						
						if(UtilValidate.isNotEmpty(productAttrPdpQtyMin) && UtilValidate.isNotEmpty(productAttrPdpQtyMax))
						{
							pdpQtyMinAttributeValue = productAttrPdpQtyMin.attrValue;
							pdpQtyMaxAttributeValue = productAttrPdpQtyMax.attrValue;
						}
					}
					
					if(UtilValidate.isEmpty(pdpQtyMinAttributeValue) && UtilValidate.isEmpty(pdpQtyMaxAttributeValue))
					{
						pdpQtyMinAttributeValue = Util.getProductStoreParm(request,"PDP_QTY_MIN");
						pdpQtyMaxAttributeValue = Util.getProductStoreParm(request,"PDP_QTY_MAX");
					}
					
					if(UtilValidate.isEmpty(pdpQtyMinAttributeValue) && UtilValidate.isEmpty(pdpQtyMaxAttributeValue))
					{
						pdpQtyMinAttributeValue = 1;
						pdpQtyMaxAttributeValue = 99;
					}
				  
				  
				  	if (UtilValidate.isNotEmpty(shoppingList.shoppingListTypeId) && "SLT_AUTO_REODR".equals(shoppingList.shoppingListTypeId))
					{
					    	recurrenceItem = "Y";
					}
			}
		}
	}
	
	
	
	
	
}

context.shoppingList = shoppingList;
context.shoppingListId = shoppingListId;
context.productImageUrl = productImageUrl;
context.productImageAltUrl = productImageAltUrl;
context.productFriendlyUrl = productFriendlyUrl;
context.urlProductId = productId;
context.wrappedProductName = StringUtil.wrapString(productName);
context.rowClass = rowClass;
context.lineIndex = lineIndex;
context.cartLineIndex = lineIndex;
context.productFeatureAndAppls = productFeatureAndAppls;
context.productFeatureTypesMap = productFeatureTypesMap;
context.quantity = quantity;
context.shipDate = shipDate;
context.displayPrice = displayPrice;
context.itemSubTotal = displayPrice;
context.recurrenceFreq = (String)recurrenceFreq;
context.recurrenceItem = recurrenceItem;
context.partyShippingLocations = partyShippingLocations;
context.contactMechId = contactMechId;
context.paymentMethodId = paymentMethodId;
context.productQtyMin = pdpQtyMinAttributeValue;
context.productQtyMax = pdpQtyMaxAttributeValue;
context.status = status;
context.currencyUom = currencyUom;