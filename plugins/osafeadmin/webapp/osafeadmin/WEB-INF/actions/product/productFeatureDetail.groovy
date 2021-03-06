package product;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.*;

if (UtilValidate.isNotEmpty(parameters.productId) && UtilValidate.isNotEmpty(parameters.productFeatureTypeId) ) 
{
    product = delegator.findOne("Product",["productId":parameters.productId], false);
    context.product = product;
    if (UtilValidate.isNotEmpty(product)) 
     {
        productContentWrapper = new ProductContentWrapper(product, request);
        String productDetailHeading = "";
        if(productContentWrapper)
        {
            productDetailHeading = productContentWrapper.get("PRODUCT_NAME", "string");
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
        context.productFeatureAndAppls = delegator.findByAnd("ProductFeatureAndAppl", [productId : parameters.productId, productFeatureTypeId : parameters.productFeatureTypeId], UtilMisc.toList("sequenceNum","productFeatureId"));
     }
}