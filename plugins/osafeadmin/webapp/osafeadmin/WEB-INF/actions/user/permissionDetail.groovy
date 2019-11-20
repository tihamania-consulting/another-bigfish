package user;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.UtilValidate;

permissionId = StringUtils.trimToEmpty(parameters.permissionId);
context.permissionId = permissionId;
if (UtilValidate.isNotEmpty(permissionId))
{
	permissions = delegator.findOne("SecurityPermission", [permissionId : permissionId]);
	context.permissions = permissions;
}