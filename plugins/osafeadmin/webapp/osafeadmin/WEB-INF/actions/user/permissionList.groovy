package user;

import javolution.util.FastList;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;

//securityGroup drop down values for the search section
securityGroups = delegator.findList("SecurityGroup", null, null, null, null, false);

if (UtilValidate.isNotEmpty(securityGroups))
{
	context.securityGroups =securityGroups;
}

//search filtering
session = context.session;
searchGroupId = StringUtils.trimToEmpty(parameters.searchGroupId);
exprs = new ArrayList();
mainCond=null;

// groupId
if (UtilValidate.isNotEmpty(searchGroupId))
{
	securityGroupPermission = delegator.findList("SecurityGroupPermission", EntityCondition.makeCondition([groupId : searchGroupId]), null, null, null, false);
	permissionIds = EntityUtil.getFieldListFromEntityList(securityGroupPermission, "permissionId", true);
	exprs.add(EntityCondition.makeCondition("permissionId", EntityOperator.IN, permissionIds));
	context.groupId=searchGroupId;
}

if (UtilValidate.isNotEmpty(exprs)) 
{
	prodCond=EntityCondition.makeCondition(exprs, EntityOperator.AND);
	mainCond=prodCond;
}

orderBy = ["permissionId"];

users=new ArrayList();
if(UtilValidate.isNotEmpty(preRetrieved) && preRetrieved != "N")
{
    users = delegator.findList("SecurityPermission",mainCond, null, orderBy, null, false);
}

pagingListSize=users.size();
context.pagingListSize=pagingListSize;
context.pagingList = users;