package product;

import org.apache.ofbiz.base.util.UtilProperties;

messageMap=[:];
if(attachNo)
{
    messageMap.put("attachNo", attachNo);
    if(attachNo == '1')
    {
        context.pageTitle = UtilProperties.getMessage("OSafeAdminUiLabels","ProductAttachmentsTitle", locale )
    }
    else
    {
        context.pageTitle = null;
    }
    context.detailInfoBoxHeading = UtilProperties.getMessage("OSafeAdminUiLabels","ProductAttachmentHeading",messageMap, locale )
}