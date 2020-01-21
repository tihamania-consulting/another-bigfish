package common;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.party.content.PartyContentWrapper;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionBuilder;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityOperator;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.product.ProductWorker;
import com.osafe.util.Util;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.catalog.CatalogWorker;
import org.apache.ofbiz.product.category.CategoryWorker;

paramsExpr = new ArrayList();
exprBldr =  new EntityConditionBuilder();
cart = session.getAttribute("shoppingCart");

String manufacturerPartyId = parameters.manufacturerPartyId;
if (UtilValidate.isNotEmpty(manufacturerPartyId))
 {
    GenericValue gvParty =  delegator.findOne("Party", UtilMisc.toMap("partyId",manufacturerPartyId), true);
 
    if (UtilValidate.isNotEmpty(gvParty)) 
    {
          context.manufacturerPartyId = gvParty.partyId;
          PartyContentWrapper partyContentWrapper = new PartyContentWrapper(gvParty, request);
          context.partyContentWrapper = partyContentWrapper;
		  
		  context.description = partyContentWrapper.get("LONG_DESCRIPTION", "string");
		  context.profileImageUrl = partyContentWrapper.get("PROFILE_IMAGE_URL", "string");
		  context.profileName = partyContentWrapper.get("PROFILE_NAME", "string");
		  context.IMG_SIZE_PROF_MFG_H = Util.getProductStoreParm(request,"IMG_SIZE_PROF_MFG_H");
		  context.IMG_SIZE_PROF_MFG_W = Util.getProductStoreParm(request,"IMG_SIZE_PROF_MFG_W");
		  
		  

          paramsExpr.add(EntityCondition.makeCondition(exprBldr.EQUALS(manufacturerPartyId: manufacturerPartyId)));
	      productSearchList = [];
		  orderBy = ["productId"];
          paramCond=null;
      	  prodCond = EntityCondition.makeCondition(paramsExpr, EntityOperator.AND);
          paramCond = EntityCondition.makeCondition([prodCond], EntityOperator.AND);
		  
		  productSearchList = delegator.findList("Product",paramCond, null, orderBy, null, true);
          productList = new ArrayList();
		  if (UtilValidate.isNotEmpty(productSearchList))
		  {
              currentCategories = [];
	 		  productStoreCatalogList  = CatalogWorker.getStoreCatalogs(request);
			  if (UtilValidate.isNotEmpty(productStoreCatalogList))
			  {
					productStoreCatalogList  = EntityUtil.filterByDate(productStoreCatalogList, true);
					gvProductStoreCatalog = EntityUtil.getFirst(productStoreCatalogList);
					String prodCatalogId = gvProductStoreCatalog.prodCatalogId;
					String topCategoryId = CatalogWorker.getCatalogTopCategoryId(request, prodCatalogId);
					if(UtilValidate.isNotEmpty(topCategoryId))
					{
	   				    currentCategories = CategoryWorker.getRelatedCategoriesRet(request, "topLevelList", topCategoryId, true, true, true);
					}
			  }
		  
               for (GenericValue product: productSearchList)
               {
                   String isVariant = product.getString("isVariant");
                   if (UtilValidate.isEmpty(isVariant)) 
                   {
                     isVariant = "N";
                   }
                  if ("N".equals(isVariant) && ProductWorker.isSellable(product))
                  {
                  
					 if (UtilValidate.isNotEmpty(currentCategories))
					 {
				        boolean productNotInCateogry = true;
				        for (GenericValue currentCategory  : currentCategories)
				        {
				          if (CategoryWorker.isProductInCategory(delegator,product.productId,currentCategory.productCategoryId))
				          {
				            productNotInCateogry = false;
				            break;
				          }
				        }
				        if (productNotInCateogry)
				        {
				          continue;
				        }
					 }
                  
					 ProductContentWrapper productContentWrapper = new ProductContentWrapper(product, request);
					 Map manufacturerProductItems = new HashMap();
					 manufacturerProductItems.put("productId",product.productId);
					 manufacturerProductItems.put("primaryProductCategoryId",product.primaryProductCategoryId);
					 manufacturerProductItems.put("name",productContentWrapper.get("PRODUCT_NAME", "string").toString());
					 manufacturerProductItems.put("productImageSmallUrl",productContentWrapper.get("PRODUCT_NAME", "string"));
					 manufacturerProductItems.productImageSmallUrl = productContentWrapper.get("SMALL_IMAGE_URL", "string");
					 //set default and list price
					 virtualProductPrices = delegator.findByAnd("ProductPrice", UtilMisc.toMap("productId", product.productId, "currencyUomId", cart.getCurrency(), "productStoreGroupId", "_NA_"), UtilMisc.toList("-fromDate"));
					 virtualProductPrices = EntityUtil.filterByDate(virtualProductPrices, true);
					 if (UtilValidate.isNotEmpty(virtualProductPrices))
					 {
						 for(GenericValue virtualProductPrice : virtualProductPrices)
						 {
						   if (virtualProductPrice.productPriceTypeId == "DEFAULT_PRICE")
						   {
								 manufacturerProductItems.put("price",virtualProductPrice.price);
								 continue;
						   }
						   if (virtualProductPrice.productPriceTypeId == "LIST_PRICE")
						   {
								 manufacturerProductItems.put("listPrice",virtualProductPrice.price);
								 continue;
						   }
						 }
						 
					 }
					 
					 manufacturerProductItems.put("internalName",product.internalName);
					 manufacturerProductItems.put("productImageSmallAlt",productContentWrapper.get("SMALL_IMAGE_ALT_URL", "string"));
					 manufacturerProductItems.put("productImageSmallAltUrl",productContentWrapper.get("SMALL_IMAGE_ALT_URL", "string"));
                     productList.add(manufacturerProductItems);
                     
                  }
               }
		     
		  }
          context.manufacturerProductList = productList;
          pagingListSize=productList.size();
          context.pagingListSize=pagingListSize;
    	  context.pagingList =productList;         
    }
 }
 




