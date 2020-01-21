package product;

import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.product.catalog.*;
import org.apache.ofbiz.product.category.*;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;

Delegator = request.getAttribute("delegator");

String productCategoryId = parameters.productCategoryId;

GenericValue gvProductCategory =  delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId",productCategoryId), false);
if (gvProductCategory) 
{
    CategoryContentWrapper currentProductCategoryContentWrapper = new CategoryContentWrapper(gvProductCategory, request);
    context.productCategory = gvProductCategory;
    context.currentProductCategoryContentWrapper = currentProductCategoryContentWrapper;
    context.productCategoryName = gvProductCategory.categoryName?gvProductCategory.categoryName:parameters.productCategoryId;
    context.title = "";
    String categoryName = currentProductCategoryContentWrapper.get("CATEGORY_NAME", "string");
    if (UtilValidate.isEmpty(categoryName)) 
    {
        categoryName = gvProductCategory.categoryName;
    }
    if(UtilValidate.isNotEmpty(categoryName)) 
    {
        context.defaultTitle = categoryName;
    }
    if(UtilValidate.isNotEmpty(currentProductCategoryContentWrapper.get("DESCRIPTION", "string")))
    {
        context.defaultMetaKeywords = currentProductCategoryContentWrapper.get("DESCRIPTION", "string");
    }
    if(UtilValidate.isNotEmpty(currentProductCategoryContentWrapper.get("LONG_DESCRIPTION", "string")))
    {
        context.defaultMetaDescription = currentProductCategoryContentWrapper.get("LONG_DESCRIPTION", "string");
    }
}