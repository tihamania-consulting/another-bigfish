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
    // get the product price and content wrapper
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
    productAttr = delegator.findByAnd("ProductAttribute", UtilMisc.toMap("productId", parameters.productId));
    productAttrMap = new HashMap();
    if (UtilValidate.isNotEmpty(productAttr))
    {
        attrlIter = productAttr.iterator();
        while (attrlIter.hasNext()) {
            attr = (GenericValue) attrlIter.next();
            productAttrMap.put(attr.getString("attrName"),attr.getString("attrValue"));
        }
    }

    //Set Meta title, Description and Keywords
    String productName = productContentWrapper.get("PRODUCT_NAME", "string");
    if (UtilValidate.isEmpty(productName)) 
    {
        productName = gvProduct.productName;
    }
    if(UtilValidate.isNotEmpty(productName)) 
    {
        context.defaultTitle = productName;
    }
    if(UtilValidate.isNotEmpty(productContentWrapper.get("DESCRIPTION", "string")))
    {
        context.defaultMetaKeywords = productContentWrapper.get("DESCRIPTION", "string");
    }
    if(UtilValidate.isNotEmpty(productContentWrapper.get("LONG_DESCRIPTION", "string")))
    {
        context.defaultMetaDescription = productContentWrapper.get("LONG_DESCRIPTION", "string");
    }
}