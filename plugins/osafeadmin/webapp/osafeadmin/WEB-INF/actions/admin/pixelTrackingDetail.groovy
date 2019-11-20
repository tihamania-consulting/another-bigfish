package admin;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;


pixelId = parameters.pixelId;
productStoreId = parameters.productStoreId;
if (UtilValidate.isNotEmpty(pixelId) && UtilValidate.isNotEmpty(productStoreId)) {
    pixelTrack = delegator.findOne("XPixelTracking",UtilMisc.toMap("pixelId", pixelId, "productStoreId", productStoreId), false);
    if (UtilValidate.isNotEmpty(pixelTrack) && UtilValidate.isNotEmpty(pixelTrack.contentId)) 
    {
        pixelContent = delegator.findOne("Content",UtilMisc.toMap("contentId", pixelTrack.contentId), false);
        dataResource = pixelContent.getRelatedOne("DataResource");
        if (UtilValidate.isNotEmpty(dataResource))
         {
            electronicText = dataResource.getRelatedOne("ElectronicText");
            if (UtilValidate.isNotEmpty(electronicText))
            {
                context.eText = electronicText.textData;
            }
            else
            {
            	context.eText="";
            }
         }
        context.pixelContent = pixelContent;
    }
    context.pixelTrack = pixelTrack;
}
