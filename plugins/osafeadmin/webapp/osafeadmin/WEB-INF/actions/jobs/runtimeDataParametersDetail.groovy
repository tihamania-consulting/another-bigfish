package jobs;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.base.util.string.*;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

Map<String, Object> svcCtx = new HashMap();
userLogin = session.getAttribute("userLogin");
svcCtx.put("userLogin", userLogin);

runtimeDataId = StringUtils.trimToEmpty(parameters.runtimeDataId);
context.runtimeDataId = runtimeDataId;
if (UtilValidate.isNotEmpty(runtimeDataId))
{
	runtimeData = delegator.findOne("RuntimeData", [runtimeDataId : runtimeDataId]);
	context.runtimeData = runtimeData;
}


jobId = StringUtils.trimToEmpty(parameters.jobId);
context.jobId = jobId;
if (UtilValidate.isNotEmpty(jobId))
{
	schedJob = delegator.findOne("JobSandbox", [jobId : jobId]);
	context.schedJob = schedJob;
	detailInfoBoxHeading = FlexibleStringExpander.expandString(UtilProperties.getMessage("OSafeAdminUiLabels","RuntimeDataParametersHeading",locale), context);
	
	context.detailInfoBoxHeading  = detailInfoBoxHeading;
}