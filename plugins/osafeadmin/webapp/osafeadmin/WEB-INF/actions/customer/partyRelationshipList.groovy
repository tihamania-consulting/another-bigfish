package customer;

import java.util.List;
import java.util.Map;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilMisc;

session = context.session;
party = request.getAttribute("Party");

initializedCB = StringUtils.trimToEmpty(parameters.initializedCB);
preRetrieved = StringUtils.trimToEmpty(parameters.preRetrieved);


if (UtilValidate.isNotEmpty(preRetrieved))
{
   context.preRetrieved=preRetrieved;
}
else
{
   preRetrieved = context.preRetrieved;
}

if (UtilValidate.isNotEmpty(initializedCB))
{
   context.initializedCB=initializedCB;
}

List<String> roleTypeIds = new ArrayList();
roleTypeIds.add("CUSTOMER");
List<GenericValue> partyList = new ArrayList();
if(UtilValidate.isNotEmpty(party))
{
	partyRelationships = party.getRelated("FromPartyRelationship");
	partyRelationships = EntityUtil.filterByCondition(partyRelationships, EntityCondition.makeCondition("roleTypeIdTo", EntityOperator.IN, roleTypeIds));
	for(GenericValue partyRelationship : partyRelationships)
	{
		partyTo = partyRelationship.getRelatedOne("ToParty");
		partyList.add(partyTo);
	}
	changed = request.removeAttribute("party");
}

Map roleTypesDescMap = new HashMap();
List<GenericValue> roleTypesDesc = delegator.findByAnd("RoleType", UtilMisc.toMap());
for(GenericValue roleTypeDesc : roleTypesDesc)
{
	roleTypesDescMap.put(roleTypeDesc.getString("roleTypeId"), roleTypeDesc.getString("description"));
}

context.roleTypesDescMap = roleTypesDescMap;
context.resultList = partyList;
context.pagingListSize = partyList.size();
