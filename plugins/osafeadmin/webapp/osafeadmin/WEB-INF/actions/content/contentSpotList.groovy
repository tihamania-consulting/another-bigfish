package content;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionBuilder;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.entity.transaction.*

import java.sql.Timestamp;

Timestamp now = UtilDateTime.nowTimestamp(); 
orderBy = ["contentId"];
// Paging variables
viewIndex = Integer.valueOf(parameters.viewIndex  ?: 1);
viewSize = Integer.valueOf(parameters.viewSize ?: UtilProperties.getPropertyValue("osafeAdmin", "default-view-size"));
context.viewIndex = viewIndex;
context.viewSize = viewSize;

Map<String, Object> svcCtx = new HashMap();
userLogin = session.getAttribute("userLogin");
svcCtx.put("userLogin", userLogin);

context.userLoginId = userLogin.userLoginId;
contentList = new ArrayList();
contentTypeId=context.contentTypeId;
if(UtilValidate.isNotEmpty(contentTypeId))
{
    List conds = new ArrayList();
    conds.add(EntityCondition.makeCondition([contentTypeId : contentTypeId]));
    conds.add(EntityCondition.makeCondition([productStoreId : productStoreId]));
    contentList = delegator.findList("XContentXref",EntityCondition.makeCondition(conds, EntityOperator.AND), null, orderBy, null, false);
    context.resultList = contentList;
}
context.resultList = contentList;
