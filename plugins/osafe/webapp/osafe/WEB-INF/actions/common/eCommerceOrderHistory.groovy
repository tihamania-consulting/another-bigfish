import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.entity.condition.*;

orderRoleCollection = delegator.findByAnd("OrderRole", [partyId : userLogin.partyId, roleTypeId : "PLACING_CUSTOMER"]);
orderHeaderList = EntityUtil.orderBy(EntityUtil.filterByAnd(EntityUtil.getRelated("OrderHeader", orderRoleCollection),
        [EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "ORDER_REJECTED")]), ["orderDate DESC"]);
context.orderHeaderList = orderHeaderList;

