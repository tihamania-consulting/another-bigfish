import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.condition.EntityFunction;
import org.apache.ofbiz.entity.condition.EntityOperator;
import javolution.util.FastList;
import javolution.util.FastMap

import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate;

productFeatureGroupId = StringUtils.trimToEmpty(parameters.facetGroupId);
productCategoryId = StringUtils.trimToEmpty(parameters.productCategoryId);
rearrangeSeq = false; 

if(UtilValidate.isNotEmpty(productCategoryId))
{
	// Create Facet group for category, price and rating if not exist
    createFeatureGroupCtx = new HashMap();
    createFeatureGroupCtx.put("userLogin", userLogin);

    facetGroupCategory = delegator.findOne("ProductFeatureGroup", UtilMisc.toMap("productFeatureGroupId","FACET_GROUP_CATEGORY"));
    if(UtilValidate.isEmpty(facetGroupCategory))
    {
        createFeatureGroupCtx.put("productFeatureGroupId", "FACET_GROUP_CATEGORY");
        createFeatureGroupCtx.put("description",  UtilProperties.getMessage("OsafeUiLabels","FacetSearchCategoryCaption", parameters.locale));
        createFeatureGroupRes = dispatcher.runSync("createFeatureGroup", createFeatureGroupCtx);
    }

    facetGroupPrice = delegator.findOne("ProductFeatureGroup", UtilMisc.toMap("productFeatureGroupId","FACET_GROUP_PRICE"));
    if(UtilValidate.isEmpty(facetGroupPrice))
    {
        createFeatureGroupCtx.put("productFeatureGroupId", "FACET_GROUP_PRICE");
        createFeatureGroupCtx.put("description", "Price");
        createFeatureGroupRes = dispatcher.runSync("createFeatureGroup", createFeatureGroupCtx);
    }

    facetGroupRating = delegator.findOne("ProductFeatureGroup", UtilMisc.toMap("productFeatureGroupId","FACET_GROUP_RATINGS"));
    if(UtilValidate.isEmpty(facetGroupRating))
    {
        createFeatureGroupCtx.put("productFeatureGroupId", "FACET_GROUP_RATINGS");
        createFeatureGroupCtx.put("description", "Customer Rating");
        createFeatureGroupRes = dispatcher.runSync("createFeatureGroup", createFeatureGroupCtx);
    }
    
    createProductFeatureCatGrpApplCtx = new HashMap();
    createProductFeatureCatGrpApplCtx.put("productCategoryId", productCategoryId);
    createProductFeatureCatGrpApplCtx.put("userLogin", userLogin);

    facetCatGroupCategory = delegator.findByAnd("ProductFeatureCatGrpAppl", UtilMisc.toMap("productCategoryId", productCategoryId, "productFeatureGroupId","FACET_GROUP_CATEGORY"));
    if(UtilValidate.isEmpty(facetCatGroupCategory))
    {
        rearrangeSeq = true
        createProductFeatureCatGrpApplCtx.put("productFeatureGroupId", "FACET_GROUP_CATEGORY");
        createProductFeatureCatGrpApplCtx.put("sequenceNum", 10L);
        createProductFeatureCatGrpApplRes = dispatcher.runSync("createProductFeatureCatGrpAppl", createProductFeatureCatGrpApplCtx);
    }

    facetCatGroupPrice = delegator.findByAnd("ProductFeatureCatGrpAppl", UtilMisc.toMap("productCategoryId", productCategoryId, "productFeatureGroupId","FACET_GROUP_PRICE"));
    if(UtilValidate.isEmpty(facetCatGroupPrice))
    {
        rearrangeSeq = true
        createProductFeatureCatGrpApplCtx.put("productFeatureGroupId", "FACET_GROUP_PRICE");
        createProductFeatureCatGrpApplCtx.put("sequenceNum", 20L);
        createProductFeatureCatGrpApplRes = dispatcher.runSync("createProductFeatureCatGrpAppl", createProductFeatureCatGrpApplCtx);
    }

    facetCatGroupRating = delegator.findByAnd("ProductFeatureCatGrpAppl", UtilMisc.toMap("productCategoryId", productCategoryId, "productFeatureGroupId","FACET_GROUP_RATINGS"));
    if(UtilValidate.isEmpty(facetCatGroupRating))
    {
        rearrangeSeq = true
        createProductFeatureCatGrpApplCtx.put("productFeatureGroupId", "FACET_GROUP_RATINGS");
        createProductFeatureCatGrpApplCtx.put("sequenceNum", 30L);
        createProductFeatureCatGrpApplRes = dispatcher.runSync("createProductFeatureCatGrpAppl", createProductFeatureCatGrpApplCtx);
    }
}

exprs = new ArrayList();
mainCond=null;

// productFeatureGroupId
if(UtilValidate.isNotEmpty(productFeatureGroupId))
{
    exprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("productFeatureGroupId"), EntityOperator.EQUALS, productFeatureGroupId.toUpperCase()));
}

// productCategoryId
if(UtilValidate.isNotEmpty(productCategoryId))
{
    exprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("productCategoryId"), EntityOperator.EQUALS, productCategoryId.toUpperCase()));
}

if(UtilValidate.isNotEmpty(exprs))
{
    mainCond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
}

orderBy = ["sequenceNum"];
productFeatureCatGrpApplList = delegator.findList("ProductFeatureCatGrpAppl", mainCond, null, orderBy, null, false);

if(UtilValidate.isNotEmpty(productFeatureCatGrpApplList))
{
	// rearrange Facet group sequence if category, price and rating facet group are not exist
	if(rearrangeSeq)
	{
		filteredProductFeatureCatGrpApplList = EntityUtil.filterByCondition(productFeatureCatGrpApplList, EntityCondition.makeCondition("productFeatureGroupId", EntityOperator.NOT_IN, ["FACET_GROUP_CATEGORY", "FACET_GROUP_PRICE", "FACET_GROUP_RATINGS"]))
		count = 4L;
        for (GenericValue productFeatureCatGrpAppl in filteredProductFeatureCatGrpApplList) {
			productFeatureCatGrpAppl.put("sequenceNum", 10L * count);
			count++;
			productFeatureCatGrpAppl.store();
		}
        productFeatureCatGrpApplList = EntityUtil.orderBy(productFeatureCatGrpApplList, orderBy);
	}

    productFeatureCatGrpAppl = EntityUtil.getFirst(productFeatureCatGrpApplList);
    context.productFeatureCatGrpAppl = productFeatureCatGrpAppl;
}

context.productFeatureCatGrpApplList = productFeatureCatGrpApplList;
context.resultList = productFeatureCatGrpApplList;