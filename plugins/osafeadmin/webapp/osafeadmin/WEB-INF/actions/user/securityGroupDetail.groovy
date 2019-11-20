package user;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilValidate;

//get entity for UserLogin --> securityGroupInfo
groupId = StringUtils.trimToEmpty(parameters.groupId);
context.groupId = groupId;
if (UtilValidate.isNotEmpty(groupId))
{
    securityGroupInfo = delegator.findOne("SecurityGroup", [groupId : groupId]);
    context.securityGroupInfo = securityGroupInfo;	
}