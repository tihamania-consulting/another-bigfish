package common;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.product.catalog.CatalogWorker;
import org.apache.ofbiz.product.category.CategoryWorker;
import javolution.util.FastMap;
import org.apache.ofbiz.entity.util.EntityUtil;

productStoreCatalogList  = CatalogWorker.getStoreCatalogs(request);
if (UtilValidate.isNotEmpty(productStoreCatalogList))
{
	productStoreCatalogList  = EntityUtil.filterByDate(productStoreCatalogList, true);
	gvProductStoreCatalog = EntityUtil.getFirst(productStoreCatalogList);
	String prodCatalogId = gvProductStoreCatalog.prodCatalogId;
	String topCategoryId = CatalogWorker.getCatalogTopCategoryId(request, prodCatalogId);
	if(UtilValidate.isNotEmpty(topCategoryId))
	{
	    context.topCategoryId = topCategoryId;
	}
	CategoryWorker.getRelatedCategories(request, "topLevelList", topCategoryId, true);
}
