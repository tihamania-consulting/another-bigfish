package promotion;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.*;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.util.EntityFindOptions;


productStoreId=globalContext.productStoreId;
shipmentMethodTypeId=parameters.shipmentMethodTypeId;

//get shipment method info
if (UtilValidate.isNotEmpty(shipmentMethodTypeId))
{
    shipmentMethod = delegator.findOne("ShipmentMethodType", [shipmentMethodTypeId : shipmentMethodTypeId]);
    context.shipmentMethod = shipmentMethod;
    
}





