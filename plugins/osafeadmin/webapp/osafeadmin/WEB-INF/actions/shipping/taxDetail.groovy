package shipping;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.util.*;


productStoreId=globalContext.productStoreId;
taxAuthorityRateSeqId = parameters.taxAuthorityRateSeqId;

if (UtilValidate.isNotEmpty(taxAuthorityRateSeqId))
{
    taxAuthorityRateProduct = delegator.findOne("TaxAuthorityRateProduct", [taxAuthorityRateSeqId : taxAuthorityRateSeqId]);
    
    if(UtilValidate.isNotEmpty(taxAuthorityRateProduct)) 
    {
        context.taxAuthorityRateProduct = taxAuthorityRateProduct;
    }
}





