package admin;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilMisc;

//manage Solr Facet Groups
adminToolsList = new ArrayList();
adminTool = new HashMap();
adminTool.put("toolType", uiLabelMap.ManageSOLRFacetGroupsHeading);
adminTool.put("toolDesc", uiLabelMap.ManageSOLRFacetGroupsInfo);
adminTool.put("toolDetail", "facetGroupList");
adminToolsList.add(adminTool);

//manage Solr Facet Values
adminTool = new HashMap();
adminTool.put("toolType", uiLabelMap.ManageSOLRFacetValueHeading);
adminTool.put("toolDesc", uiLabelMap.ManageSOLRFacetValueInfo);
adminTool.put("toolDetail", "facetValueList");
adminToolsList.add(adminTool);


context.resultList = UtilMisc.sortMaps(adminToolsList, UtilMisc.toList("toolType"));