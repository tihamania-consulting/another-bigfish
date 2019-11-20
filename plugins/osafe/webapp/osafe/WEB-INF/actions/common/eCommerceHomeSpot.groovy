package content;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.entity.transaction.*

contentTypeId=context.contentTypeId;
productStore = ProductStoreWorker.getProductStore(request);
if(UtilValidate.isNotEmpty(contentTypeId) && UtilValidate.isNotEmpty(productStore))
{
    xContentXrefList = productStore.getRelated("XContentXref");
    xContentXrefList = EntityUtil.filterByAnd(xContentXrefList, UtilMisc.toMap("contentTypeId" , contentTypeId));
    xContentXrefList = EntityUtil.orderBy(xContentXrefList,UtilMisc.toList("bfContentId"));
    context.spotsList = xContentXrefList;
}

