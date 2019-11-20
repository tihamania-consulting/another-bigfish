import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.entity.condition.*;

shoppingLists = delegator.findByAnd("ShoppingList", [partyId : userLogin.partyId, shoppingListTypeId : "SLT_AUTO_REODR"],UtilMisc.toList("isActive DESC"));
context.shoppingLists = shoppingLists;

