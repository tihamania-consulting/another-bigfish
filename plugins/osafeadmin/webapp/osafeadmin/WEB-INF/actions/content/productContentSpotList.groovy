package content;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionBuilder;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.entity.transaction.*
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.commons.lang.StringEscapeUtils;


String productId = StringUtils.trimToEmpty(parameters.productId);
orderBy = ["productContentTypeId"];

productContentList = new ArrayList();
if(UtilValidate.isNotEmpty(productId))
{
	
	Map createProductTextContentCtx = new HashMap();
	createProductTextContentCtx.put("productId", parameters.productId);
	createProductTextContentCtx.put("userLogin", userLogin);
	
	productSpecificContentListFirst = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId","PDP_SPC_CONTENT_01"));
	if(UtilValidate.isEmpty(productSpecificContentListFirst))
	{
	    createProductTextContentCtx.put("productContentTypeId", "PDP_SPC_CONTENT_01");
	    createProductTextContentRes = dispatcher.runSync("createProductTextContent", createProductTextContentCtx);
	}
	
	productSpecificContentListSecond = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId","PDP_SPC_CONTENT_02"));
	if(UtilValidate.isEmpty(productSpecificContentListSecond))
	{
		createProductTextContentCtx.put("productContentTypeId", "PDP_SPC_CONTENT_02");
		createProductTextContentRes = dispatcher.runSync("createProductTextContent", createProductTextContentCtx);
	}
	
	productSpecificContentListThird = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId","PDP_SPC_CONTENT_03"));
	if(UtilValidate.isEmpty(productSpecificContentListThird))
	{
		createProductTextContentCtx.put("productContentTypeId", "PDP_SPC_CONTENT_03");
		createProductTextContentRes = dispatcher.runSync("createProductTextContent", createProductTextContentCtx);
	}
	
    conds = null
    conds = EntityCondition.makeCondition([productId : productId]);
    
    productContentTypeIdExpr= new ArrayList();
    productContentTypeIdCond = null;
    productContentTypeIdExpr.add(EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "PDP_SPC_CONTENT_01"));
    productContentTypeIdExpr.add(EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "PDP_SPC_CONTENT_02"));
    productContentTypeIdExpr.add(EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "PDP_SPC_CONTENT_03"));
    if (UtilValidate.isNotEmpty(productContentTypeIdExpr))
	{
    	productContentTypeIdCond = EntityCondition.makeCondition(productContentTypeIdExpr, EntityOperator.OR);
	}
    productContentList = delegator.findList("ProductContent",EntityCondition.makeCondition([conds, productContentTypeIdCond], EntityOperator.AND), null, orderBy, null, false);
    context.productId = productId;
    
    product = delegator.findOne("Product",["productId":parameters.productId], false);
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
    }
}
context.resultList = productContentList;
