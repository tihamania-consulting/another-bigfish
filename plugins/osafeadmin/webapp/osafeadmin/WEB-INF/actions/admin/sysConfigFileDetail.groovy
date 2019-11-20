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
         if (configFile.getName().equalsIgnoreCase(parameters.fileName))
            { 
              context.sysConfigFileName = parameters.fileName;
              context.sysConfigFileTextData = FileUtil.readTextFile(configFile, true);
              break;
            }
      }
}
