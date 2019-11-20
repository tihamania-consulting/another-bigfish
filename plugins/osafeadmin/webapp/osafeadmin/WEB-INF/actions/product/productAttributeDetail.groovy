package product;

import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.product.catalog.*;
import org.apache.ofbiz.product.store.*;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.commons.lang.StringUtils;

if (UtilValidate.isNotEmpty(parameters.productId)) 
{
	
    product = delegator.findOne("Product",["productId":parameters.productId], false);
    context.product = product;
    // get the product content wrapper
    if("Y".equals(product.getString("isVariant")))
	 {
		GenericValue parent = ProductWorker.getParentProduct(product.productId, delegator);
		if (UtilValidate.isNotEmpty(parent))
		 {
			productContentWrapper = new ProductContentWrapper(parent, request);
		 }
	 }
    else
     {
        productContentWrapper = new ProductContentWrapper(product, request);
     }
    String productDetailHeading = "";
    if (UtilValidate.isNotEmpty(productContentWrapper))
    {
        productDetailHeading = StringEscapeUtils.unescapeHtml(productContentWrapper.get("PRODUCT_NAME").toString());
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
    productAttrList = delegator.findByAnd("ProductAttribute", UtilMisc.toMap("productId", parameters.productId));
    
    context.productAttrList = productAttrList;

}