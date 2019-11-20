package order;

import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import javolution.util.FastList;
import javolution.util.FastMap;


orderItemShipGroup = request.getAttribute("orderItemShipGroup");
orderItemShipGroupAssoc = orderItemShipGroup.getRelated("OrderItemShipGroupAssoc");

context.orderItemShipGroup = orderItemShipGroup;
context.orderItemShipGroupAssoc = orderItemShipGroupAssoc;
