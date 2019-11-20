package admin;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilMisc;

feedImportList = new ArrayList();

//FEEDS IMPORT LIST
//Customer Imports
feedsImport = new HashMap();
feedsImport.put("toolSeq","1");
feedsImport.put("toolType", uiLabelMap.CustomerSetupFeedLabel);
feedsImport.put("toolDesc", uiLabelMap.CustomerSetupFeedInfo);
feedsImport.put("toolDetail", "customerSetupFeedImport");
feedImportList.add(feedsImport);

//Order Status Change Imports
feedsImport = new HashMap();
feedsImport.put("toolSeq","1");
feedsImport.put("toolType", uiLabelMap.OrderStatusChangesLabel);
feedsImport.put("toolDesc", uiLabelMap.OrderStatusChangesInfo);
feedsImport.put("toolDetail", "orderStatusChangesImport");
feedImportList.add(feedsImport);

//Product Rating Imports
feedsImport = new HashMap();
feedsImport.put("toolSeq","1");
feedsImport.put("toolType", uiLabelMap.ProductRatingsLabel);
feedsImport.put("toolDesc", uiLabelMap.ProductRatingsInfo);
feedsImport.put("toolDetail", "productRatingsImport");
feedImportList.add(feedsImport);

//Stores Imports
feedsImport = new HashMap();
feedsImport.put("toolSeq","1");
feedsImport.put("toolType", uiLabelMap.StoresLabel);
feedsImport.put("toolDesc", uiLabelMap.StoresInfo);
feedsImport.put("toolDetail", "storesImport");
feedImportList.add(feedsImport);


//FEEDS EXPORT LIST

feedExportList = new ArrayList();

//Customers Export
feedsExport = new HashMap();
feedsExport.put("toolSeq","1");
feedsExport.put("toolType", uiLabelMap.CustomersExportLabel);
feedsExport.put("toolDesc", uiLabelMap.CustomersExportInfo);
feedsExport.put("toolDetail", "customersExport");
feedExportList.add(feedsExport);

//Orders Export
feedsExport = new HashMap();
feedsExport.put("toolSeq","1");
feedsExport.put("toolType", uiLabelMap.OrdersExportLabel);
feedsExport.put("toolDesc", uiLabelMap.OrdersExportInfo);
feedsExport.put("toolDetail", "ordersExport");
feedExportList.add(feedsExport);

//Contact Us Export
feedsExport = new HashMap();
feedsExport.put("toolSeq","1");
feedsExport.put("toolType", uiLabelMap.ContactUsExportLabel);
feedsExport.put("toolDesc", uiLabelMap.ContactUsExportInfo);
feedsExport.put("toolDetail", "contactUsExport");
feedExportList.add(feedsExport);

//Request Catalog Export
feedsExport = new HashMap();
feedsExport.put("toolSeq","1");
feedsExport.put("toolType", uiLabelMap.RequestCatalogExportLabel);
feedsExport.put("toolDesc", uiLabelMap.RequestCatalogExportInfo);
feedsExport.put("toolDetail", "requestCatalogExport");
feedExportList.add(feedsExport);

//Google Product Feed Export
feedsExport = new HashMap();
feedsExport.put("toolSeq","1");
feedsExport.put("toolType", uiLabelMap.GoogleProductFeedExportLabel);
feedsExport.put("toolDesc", uiLabelMap.GoogleProductFeedExportInfo);
feedsExport.put("toolDetail", "googleProductFeedExport");
feedExportList.add(feedsExport);


//BLUEDART FEEDS IMPORT LIST
List bluedartFeedsImportList = new ArrayList();

bluedartFeedsImport = new HashMap();
bluedartFeedsImport.put("toolSeq","1");
bluedartFeedsImport.put("toolType", uiLabelMap.PrepaidFileFeedLabel);
bluedartFeedsImport.put("toolDesc", uiLabelMap.PrepaidFileFeedInfo);
bluedartFeedsImport.put("toolDetail", "bluedartPrepaidFileFeedImport");
bluedartFeedsImportList.add(bluedartFeedsImport);

bluedartFeedsImport = new HashMap();
bluedartFeedsImport.put("toolSeq","1");
bluedartFeedsImport.put("toolType", uiLabelMap.CoDFileFeedLabel);
bluedartFeedsImport.put("toolDesc", uiLabelMap.CoDFileFeedInfo);
bluedartFeedsImport.put("toolDetail", "blueDartCodFileFeedImport");
bluedartFeedsImportList.add(bluedartFeedsImport);

//FEED CONVERTER LIST
List feedConverterList = new ArrayList();

feedConverter = new HashMap();
feedConverter.put("toolSeq","1");
feedConverter.put("toolType", uiLabelMap.ProductLoadFileConverterLabel);
feedConverter.put("toolDesc", uiLabelMap.ProductLoadFileConverterInfo);
feedConverter.put("toolDetail", "productLoadFeedConverter");
feedConverterList.add(feedConverter);

feedConverter = new HashMap();
feedConverter.put("toolSeq","2");
feedConverter.put("toolType", uiLabelMap.OrderStstusChangeFileConverterLabel);
feedConverter.put("toolDesc", uiLabelMap.OrderStstusChangeFileConverterInfo);
feedConverter.put("toolDetail", "orderStstusChangeFeedConverter");
feedConverterList.add(feedConverter);

feedConverter = new HashMap();
feedConverter.put("toolSeq","3");
feedConverter.put("toolType", uiLabelMap.ProductRatingsFileConverterLabel);
feedConverter.put("toolDesc", uiLabelMap.ProductRatingsFileConverterInfo);
feedConverter.put("toolDetail", "productRatingsFeedConverter");
feedConverterList.add(feedConverter);

feedConverter = new HashMap();
feedConverter.put("toolSeq","3");
feedConverter.put("toolType", uiLabelMap.StoresFileConverterLabel);
feedConverter.put("toolDesc", uiLabelMap.StoresFileConverterInfo);
feedConverter.put("toolDetail", "storesFeedConverter");
feedConverterList.add(feedConverter);

context.feedConverterList = UtilMisc.sortMaps(feedConverterList, UtilMisc.toList("toolSeq"));
context.bluedartFeedsImportList = UtilMisc.sortMaps(bluedartFeedsImportList, UtilMisc.toList("toolSeq"));
context.feedExportList = UtilMisc.sortMaps(feedExportList, UtilMisc.toList("toolSeq"));
context.feedImportList = UtilMisc.sortMaps(feedImportList, UtilMisc.toList("toolSeq"));