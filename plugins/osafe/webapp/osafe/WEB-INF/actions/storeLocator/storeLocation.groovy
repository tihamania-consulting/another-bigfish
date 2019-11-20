package common;

import org.apache.ofbiz.base.util.UtilValidate;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import com.osafe.util.Util;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.entity.Delegator;

storeRow = request.getAttribute("storeRow");
storeRowIndex = request.getAttribute("storeRowIndex");
rowNum = request.getAttribute("rowNum");
rowClass = request.getAttribute("rowClass");
storeContentSpot = request.getAttribute("storeContentSpot");
pickupStoreButtonVisible = request.getAttribute("pickupStoreButtonVisible");
userLocation = request.getAttribute("userLocation");
if (UtilValidate.isNotEmpty(storeRow))
{

	context.storeRow = storeRow;	
	context.storeRowIndex = storeRowIndex; 
	context.rowNum = rowNum;	
	context.rowClass = rowClass;	
	context.storeContentSpot = storeContentSpot;	
	context.pickupStoreButtonVisible = pickupStoreButtonVisible;	
	context.userLocation = userLocation;	
}
















