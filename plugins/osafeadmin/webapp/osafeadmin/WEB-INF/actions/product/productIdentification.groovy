package product;

import javolution.util.FastList;
import org.apache.ofbiz.base.util.UtilValidate;
import com.osafe.util.Util;
import org.apache.ofbiz.base.util.StringUtil;

List goodIdentificationTypesList = new ArrayList();
List goodIdentificationTypes = new ArrayList();
String goodIdentificationTypeTxt = Util.getProductStoreParm(request,"PRODUCT_INC_GOOD_ID");
if (UtilValidate.isNotEmpty(goodIdentificationTypeTxt))
{
   goodIdentificationTypes = StringUtil.split(goodIdentificationTypeTxt, ",");
   for(String goodIdentificationTypeId : goodIdentificationTypes)
   {
	   goodIdentificationTypeId = goodIdentificationTypeId.trim();
       goodIdentificationType = delegator.findOne("GoodIdentificationType", ["goodIdentificationTypeId" : goodIdentificationTypeId], false);
       if(UtilValidate.isNotEmpty(goodIdentificationType))
       {
    	   goodIdentificationTypesList.add(goodIdentificationTypeId);
       }
   }
   context.goodIdentificationTypesList	= goodIdentificationTypesList;
}

