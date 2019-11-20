package admin;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.base.util.UtilProperties;
import com.osafe.services.OsafeManageXml;
import javolution.util.FastMap;
import java.util.Map;
import org.apache.ofbiz.base.util.FileUtil;

uiLabelKey = parameters.key;
if (UtilValidate.isNotEmpty(uiLabelKey)) {
    context.uiLabelEntry = OsafeManageXml.findByKeyFromXmlFile(XmlFilePath, "key", uiLabelKey);
}
addUiLabelKey = parameters.addKey;
if (UtilValidate.isNotEmpty(addUiLabelKey)) {
    tmpDir = FileUtil.getFile("runtime/tmp");
    uploadedFile = new File(tmpDir, context.uploadedFileName);
    context.uiLabelEntry = OsafeManageXml.findByKeyFromXmlFile(uploadedFile.getAbsolutePath(), "key", addUiLabelKey);
}