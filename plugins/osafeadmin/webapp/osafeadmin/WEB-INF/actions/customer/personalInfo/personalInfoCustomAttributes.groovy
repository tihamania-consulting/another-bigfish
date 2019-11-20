package personalInfo;

import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import com.osafe.services.OsafeManageXml;
import javolution.util.FastMap;

customPartyAttributeList = null;
Map<String, Object> svcCtx = new HashMap();
svcCtx.put("useCache", "false");
partyCustomAttributeListRes = dispatcher.runSync("getPartyCustomAttributeList", svcCtx);
//CustomPartyAttributeServices.getPartyCustomAttributeList();

context.customPartyAttributeList = partyCustomAttributeListRes.get("customPartyAttributeList");

