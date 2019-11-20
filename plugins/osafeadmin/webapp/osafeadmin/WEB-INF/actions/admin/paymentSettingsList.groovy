package admin;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.UtilMisc;

//payment gateway settings
adminToolsList = new ArrayList();
adminTool = new HashMap();
adminTool.put("toolType", uiLabelMap.AdminPayGatewayHeading);
adminTool.put("toolDesc", uiLabelMap.AdminPayGatewayInfo);
adminTool.put("toolDetail", "paymentGatewayConfigList");
adminToolsList.add(adminTool);

//titles
adminTool = new HashMap();
adminTool.put("toolType", uiLabelMap.AdminPaymentSettingHeading);
adminTool.put("toolDesc", uiLabelMap.AdminPaymentSettingInfo);
adminTool.put("toolDetail", "paymentSettingManagement");
adminToolsList.add(adminTool);

//credit card type
adminTool = new HashMap();
adminTool.put("toolType", uiLabelMap.ManageCreditCardTypeHeading);
adminTool.put("toolDesc", uiLabelMap.ManageCreditCardTypeInfo);
adminTool.put("toolDetail", "creditCardTypeList");
adminToolsList.add(adminTool);

context.resultList = adminToolsList;