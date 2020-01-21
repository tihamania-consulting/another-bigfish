package product;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.*;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilProperties;

if (UtilValidate.isNotEmpty(parameters.productId)) 
{
	
    product = delegator.findOne("Product",["productId":parameters.productId], false);
    context.product = product;
    if (UtilValidate.isNotEmpty(product)) 
     {
        productContentWrapper = new ProductContentWrapper(product, request);
        String productDetailHeading = "";
        if (UtilValidate.isNotEmpty(productContentWrapper))
        {
            productDetailHeading = StringEscapeUtils.unescapeHtml(productContentWrapper.get("PRODUCT_NAME", "string").toString());
            if (UtilValidate.isEmpty(productDetailHeading)) 
            {
                productDetailHeading = product.get("productName");
            }
            if (UtilValidate.isEmpty(productDetailHeading)) 
            {
                productDetailHeading = product.get("internalName");
            }
            context.productDetailHeading = productDetailHeading;
            context.productContentWrapper = productContentWrapper;
        }
        ecl = EntityCondition.makeCondition([
          EntityCondition.makeCondition("productId", EntityOperator.EQUALS, product.productId),
          EntityCondition.makeCondition("primaryParentCategoryId", EntityOperator.NOT_EQUAL, null),
         ],
        EntityOperator.AND);

        resultList = delegator.findList("ProductCategoryAndMember", ecl, null, null, null, false);
        context.resultList = EntityUtil.filterByDate(resultList,true);
     }
}