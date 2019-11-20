package admin;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.base.util.string.*;
import java.util.List;
import java.util.Map;

Map<String, Object> svcCtx = new HashMap();
userLogin = session.getAttribute("userLogin");
svcCtx.put("userLogin", userLogin);
svcRes = dispatcher.runSync("getSysConfigFiles", svcCtx);
configFileList = svcRes.sysConfigFileList;
fileList = new ArrayList();
if(UtilValidate.isNotEmpty(configFileList)) {
      for (configFile in configFileList) {
          sysConfigFile = new HashMap();
          sysConfigFile.put("fileName", configFile.getName());
          sysConfigFile.put("filePath", configFile);
          sysConfigFile.put("fileNameUpperCase", configFile.getName().toUpperCase());
          fileList.add(sysConfigFile);
      }
      context.configFileResultList = UtilMisc.sortMaps(fileList, UtilMisc.toList("fileNameUpperCase"));
}
