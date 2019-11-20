package admin;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilMisc;
import com.osafe.services.OsafeManageXml;
import org.apache.commons.lang.StringUtils;
import javolution.util.FastList;
import javolution.util.FastMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

XmlFilePath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("osafeAdmin.properties", "image-location-preference-file"), context);

imageLocationPrefList =  OsafeManageXml.getListMapsFromXmlFile(XmlFilePath);
pagingListSize=imageLocationPrefList.size();
context.pagingListSize=pagingListSize;
context.imageLocationPrefList = imageLocationPrefList;
