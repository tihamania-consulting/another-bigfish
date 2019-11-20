package common;

import org.apache.ofbiz.base.util.*;
import javolution.util.FastList;
import org.apache.ofbiz.content.content.ContentWorker;
import org.apache.ofbiz.product.store.ProductStoreWorker;

import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;

if (UtilValidate.isNotEmpty(context.contentId) && UtilValidate.isNotEmpty(context.productStoreId)) 
{
    xContentXref = delegator.findByPrimaryKeyCache("XContentXref", [bfContentId : context.contentId, productStoreId : context.productStoreId]);
    if (UtilValidate.isNotEmpty(xContentXref))
    {
        content = xContentXref.getRelatedOne("Content");
        context.content = content;
    }
    else
    {
    	context.content = "";
    }
}
