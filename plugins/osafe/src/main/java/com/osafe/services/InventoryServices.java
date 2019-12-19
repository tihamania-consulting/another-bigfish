package com.osafe.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import javolution.util.FastMap;

import org.apache.commons.collections.MapUtils;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityTypeUtil;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.ofbiz.product.inventory.InventoryWorker;
import org.apache.ofbiz.product.product.ProductContentWrapper;
import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.product.store.ProductStoreWorker;
import org.apache.ofbiz.service.*;

import com.osafe.util.Util;

public class InventoryServices {

    public static final String module = OrderServices.class.getName();
	public static final GenericDispatcherFactory genericDispatcherFactory = new GenericDispatcherFactory();

    public static Map<String, Object> getProductInventoryLevel(String productId, ServletRequest request) {
    	Delegator delegator = (Delegator)request.getAttribute("delegator");
    	Map<String, Object> ineventoryLevelMap = new HashMap<String, Object>();
    	
		try {
			List<GenericValue> inventoryProductAttributes =  delegator.findByAnd("ProductAttribute", UtilMisc.toMap("productId",productId), null, true);
			ineventoryLevelMap = getProductInventoryLevel(productId, inventoryProductAttributes,request);
			
		} catch (GenericEntityException ge) {
		    Debug.logError(ge, ge.getMessage(), module);
		}
    	return ineventoryLevelMap;
    }
    
    public static Map<String, Object> getProductInventoryLevel(String productId, List<GenericValue> productAttributes, ServletRequest request)
    {
    	return getProductInventoryLevel(productId, productAttributes, request, null);
    }
    
    public static Map<String, Object> getProductInventoryLevel(List<GenericValue> productAttributes, Map<String, Object> context) 
    {
    	return getProductInventoryLevel((String) productAttributes.get(0).get("productId"), productAttributes, null, context);
    }
    
    public static Map<String, Object> getProductInventoryLevel(String productId, List<GenericValue> productAttributes, ServletRequest request, Map<String, Object> context) {
    	String productStoreId = "";
    	Delegator delegator = null;
    	if(UtilValidate.isNotEmpty(request))
    	{
    		productStoreId = ProductStoreWorker.getProductStoreId(request);
        	delegator = (Delegator)request.getAttribute("delegator");
    	}
    	else
    	{
    		productStoreId = (String)context.get("productStoreId");
        	delegator = (Delegator)context.get("delegator");
    	}
    	Map<String, Object> ineventoryLevelMap = new HashMap<String, Object>();
    	if(UtilValidate.isEmpty(request) && UtilValidate.isEmpty(productStoreId))
    	{
    		Debug.logInfo("Could not get product inventory level.  Missing request and context.", "InventoryServices.java");
    		return ineventoryLevelMap;
    	}
    	
    	//get the Inventory Product Store Parameters
    	String inventoryMethod = Util.getProductStoreParm(productStoreId, "INVENTORY_METHOD");
    	
    	BigDecimal inventoryInStockFrom = new BigDecimal("-1");
    	BigDecimal inventoryOutOfStockTo = new BigDecimal("-1");
    	BigDecimal inventoryLevel = BigDecimal.ZERO;
    	BigDecimal inventoryWarehouseLevel = BigDecimal.ZERO;
		String inventoryInStockFromStr = Util.getProductStoreParm(productStoreId, "INVENTORY_IN_STOCK_FROM");
		String inventoryOutOfStockToStr = Util.getProductStoreParm(productStoreId, "INVENTORY_OUT_OF_STOCK_TO");
		try {
			inventoryInStockFrom = new BigDecimal(inventoryInStockFromStr);
		} catch (Exception e) {
			inventoryInStockFrom = new BigDecimal("-1");
		}

		try {
			inventoryOutOfStockTo = new BigDecimal(inventoryOutOfStockToStr);
		} catch (Exception e) {
			inventoryOutOfStockTo = new BigDecimal("-1");
		}

    	
    	if(inventoryMethod != null && inventoryMethod.equalsIgnoreCase("BIGFISH"))
    	{
    	    GenericValue totalInventoryProductAttribute = null;
    	    GenericValue whInventoryProductAttribute = null;
    	    
			if(UtilValidate.isNotEmpty(productAttributes)) {
			    List<GenericValue> totalInventoryProductAttributes = EntityUtil.filterByAnd(productAttributes, UtilMisc.toMap("attrName", "BF_INVENTORY_TOT"));
			    totalInventoryProductAttribute = EntityUtil.getFirst(totalInventoryProductAttributes);
			    
			    List<GenericValue> whInventoryProductAttributes = EntityUtil.filterByAnd(productAttributes, UtilMisc.toMap("attrName", "BF_INVENTORY_WHS"));
			    whInventoryProductAttribute = EntityUtil.getFirst(whInventoryProductAttributes);
			}
    		if(totalInventoryProductAttribute!=null)
    		{
    		    String bigfishInventory = (String)totalInventoryProductAttribute.get("attrValue");
    			try {
    			    inventoryLevel = new BigDecimal(bigfishInventory);
    			} catch (Exception e) {
    			    inventoryLevel = BigDecimal.ZERO;
				}
    		}
    		
    		if(whInventoryProductAttribute!=null)
    		{
    		    String bigfishWHInventory = (String)whInventoryProductAttribute.get("attrValue");
    			try {
    				inventoryWarehouseLevel = new BigDecimal(bigfishWHInventory);
    			} catch (Exception e) {
    				inventoryWarehouseLevel = BigDecimal.ZERO;
				}
    		}
    		

    	} else if (inventoryMethod != null && inventoryMethod.equalsIgnoreCase("OFBIZ"))
    	{
			LocalDispatcher dispatcher = genericDispatcherFactory.createLocalDispatcher("entity-" + delegator.getDelegatorName(), delegator);
			Map<String, Object> prodInventorySummary = null;
			try {
				prodInventorySummary = dispatcher.runSync("getProductInventoryAndFacilitySummary", UtilMisc.toMap("productId", productId, "facilityId", "BF_FAC"));
			} catch (GenericServiceException e) {
				prodInventorySummary = new HashMap<>();
			}
			if (prodInventorySummary.get("totalQuantityOnHand") != null) {
				inventoryLevel = (BigDecimal) prodInventorySummary.get("totalAvailableToPromise");
			}
			if (prodInventorySummary.get("totalAvailableToPromise") != null) {
				inventoryWarehouseLevel = (BigDecimal) prodInventorySummary.get("totalQuantityOnHand");
			}
		}
    	ineventoryLevelMap.put("inventoryLevel", inventoryLevel);
    	ineventoryLevelMap.put("inventoryWarehouseLevel", inventoryWarehouseLevel);
    	ineventoryLevelMap.put("inventoryLevelInStockFrom", inventoryInStockFrom);
    	ineventoryLevelMap.put("inventoryLevelOutOfStockTo", inventoryOutOfStockTo);
    	ineventoryLevelMap.put("inventoryMethod", inventoryMethod);
    	return ineventoryLevelMap;
    }
    
    
    public static void setProductInventoryLevel(ShoppingCart shoppingCart, ShoppingCartItem shoppingCartItem, String productStoreId, BigDecimal quantity, String deliveryOption) {

    	String productId = shoppingCartItem.getProductId();
    	Delegator delegator = DelegatorFactory.getDelegator(null);
    	LocalDispatcher dispatcher = genericDispatcherFactory.createLocalDispatcher("entity-" + delegator.getDelegatorName(), delegator);
    	
    	String inventoryMethod = Util.getProductStoreParm(productStoreId, "INVENTORY_METHOD");
    	GenericValue userLogin = null;
		try {
			userLogin = delegator.findOne("UserLogin",UtilMisc.toMap("userLoginId", "system"), true);
		} catch (GenericEntityException e1) {
			e1.printStackTrace();
		}
		if (inventoryMethod != null && inventoryMethod.equalsIgnoreCase("OFBIZ"))
		{
			String orderId = shoppingCart.getOrderId();
			String orderItemSeqId = shoppingCartItem.getOrderItemSeqId();
			try {
				GenericValue shipmentGroup = delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId)).get(0);
				String shipGroupSeqId  = shipmentGroup.getString("shipGroupSeqId");
				dispatcher.runSync("reserveProductInventory", UtilMisc.toMap("userLogin",userLogin, "orderId", orderId, "orderItemSeqId" ,
						orderItemSeqId, "productId", productId, "quantity", quantity, "requireInventory", "Y", "reserveOrderEnumId", "INVRO_FIFO_REC",
						"shipGroupSeqId" , shipGroupSeqId));
			} catch (GenericEntityException | GenericServiceException e) {
				e.printStackTrace();
			}
		}
    	else if(inventoryMethod != null && inventoryMethod.equalsIgnoreCase("BIGFISH"))
    	{
    		GenericValue totalInventoryProductAttribute = null;
    	    GenericValue whInventoryProductAttribute = null;
    	    BigDecimal currentInventoryLevel = BigDecimal.ZERO;
    	    BigDecimal currentInventoryWarehouseLevel = BigDecimal.ZERO;
    	    
    	    BigDecimal newInventoryLevel = BigDecimal.ZERO;
    	    BigDecimal newInventoryWarehouseLevel = BigDecimal.ZERO;
    	    
    		try {
    			List<GenericValue> inventoryProductAttributes =  delegator.findByAnd("ProductAttribute", UtilMisc.toMap("productId",productId), null, true);
    			if(UtilValidate.isNotEmpty(inventoryProductAttributes)) {
    			    List<GenericValue> totalInventoryProductAttributes = EntityUtil.filterByAnd(inventoryProductAttributes, UtilMisc.toMap("attrName", "BF_INVENTORY_TOT"));
    			    totalInventoryProductAttribute = EntityUtil.getFirst(totalInventoryProductAttributes);
    			    
    			    List<GenericValue> whInventoryProductAttributes = EntityUtil.filterByAnd(inventoryProductAttributes, UtilMisc.toMap("attrName", "BF_INVENTORY_WHS"));
    			    whInventoryProductAttribute = EntityUtil.getFirst(whInventoryProductAttributes);
    			}
    		} catch (GenericEntityException ge) {
    		    Debug.logError(ge, ge.getMessage(), module);
			}
    		
    		//Get the current inventory level attributes from Product Attribute
    		
    		if(totalInventoryProductAttribute != null)
    		{
    		    String bigfishInventory = (String)totalInventoryProductAttribute.get("attrValue");
    			try {
    			    currentInventoryLevel = new BigDecimal(bigfishInventory);
    			} catch (Exception nfe) {
    			    currentInventoryLevel = BigDecimal.ZERO;
				}
    		}
    		
    		
    		if(whInventoryProductAttribute != null)
    		{
    		    String bigfishWHInventory = (String)whInventoryProductAttribute.get("attrValue");
    			try {
    			    currentInventoryWarehouseLevel = new BigDecimal(bigfishWHInventory);
    			} catch (Exception nfe) {
    			    currentInventoryWarehouseLevel = BigDecimal.ZERO;
				}
    		}
    		
    		//Calculate the new inventory levels after Increase or Decrease the quantity with the current inventory levels
    		if(UtilValidate.isNotEmpty(quantity)) 
    		{	
    			newInventoryLevel = currentInventoryLevel.subtract(quantity);
    			newInventoryWarehouseLevel = currentInventoryWarehouseLevel.subtract(quantity);
    		}
    		//Update the Inventory Level Attributes to Product Attribute
    		Map updateProductAttributeCtx = new HashMap();
    		Map createProductAttributeCtx = new HashMap();
            try {
            	if(totalInventoryProductAttribute != null) {
        		    updateProductAttributeCtx.put("userLogin",userLogin);
        		    updateProductAttributeCtx.put("productId",productId);
        		    updateProductAttributeCtx.put("attrName","BF_INVENTORY_TOT");
        		    updateProductAttributeCtx.put("attrValue",Integer.valueOf(newInventoryLevel.intValue()).toString());
        		    dispatcher.runSync("updateProductAttribute", updateProductAttributeCtx);
        		}
            	else
            	{
            		createProductAttributeCtx.put("userLogin",userLogin);
        			createProductAttributeCtx.put("productId",productId);
        			createProductAttributeCtx.put("attrName","BF_INVENTORY_TOT");
        			createProductAttributeCtx.put("attrValue",Integer.valueOf(newInventoryLevel.intValue()).toString());
        			dispatcher.runSync("createProductAttribute", createProductAttributeCtx);
            	}
                
            } catch (GenericServiceException e) {
                Debug.logWarning(e, module);
            }
            
            if(UtilValidate.isNotEmpty(deliveryOption) && (deliveryOption.equals("SHIP_TO") || deliveryOption.equals("SHIP_TO_MULTI"))) {
        		
                try {
                	if(whInventoryProductAttribute != null) {
                		updateProductAttributeCtx = UtilMisc.toMap("userLogin",userLogin);
                		updateProductAttributeCtx.put("productId",productId);
                		updateProductAttributeCtx.put("attrName","BF_INVENTORY_WHS");
                		updateProductAttributeCtx.put("attrValue",Integer.valueOf(newInventoryWarehouseLevel.intValue()).toString());
                		dispatcher.runSync("updateProductAttribute", updateProductAttributeCtx);
                	}
                	else
                	{
                		createProductAttributeCtx = UtilMisc.toMap("userLogin",userLogin);
            			createProductAttributeCtx.put("productId",productId);
            			createProductAttributeCtx.put("attrName","BF_INVENTORY_WHS");
            			createProductAttributeCtx.put("attrValue",Integer.valueOf(newInventoryWarehouseLevel.intValue()).toString());
            			dispatcher.runSync("createProductAttribute", createProductAttributeCtx);
                	}
                    
                } catch (GenericServiceException e) {
                    Debug.logWarning(e, module);
                }
            }
    	}
    }
    
    public static Map<String, Object> checkProductInventoryLevel(String productId, BigDecimal orderedQty, ServletRequest request, HttpServletRequest servletRequest) {
    	Delegator delegator = (Delegator)request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Locale locale = UtilHttp.getLocale(servletRequest);
    	Map<String, Object> checkInventoryLevelMap = new HashMap<String, Object>();
    	checkInventoryLevelMap.put("stockMessage","");
    	checkInventoryLevelMap.put("inStock","Y");

    	
		try {
			Map<String,Object> inventoryLevelMap = InventoryServices.getProductInventoryLevel(productId, request);
            if (UtilValidate.isNotEmpty(inventoryLevelMap))
            {
            	String inventoryMethod =(String) inventoryLevelMap.get("inventoryMethod"); 
            	if(UtilValidate.isNotEmpty(inventoryMethod) && !inventoryMethod.equalsIgnoreCase("NONE"))
            	{
                	BigDecimal inventoryInStockFrom = (BigDecimal) inventoryLevelMap.get("inventoryLevelInStockFrom");
                	BigDecimal inventoryOutOfStockTo = (BigDecimal) inventoryLevelMap.get("inventoryLevelOutOfStockTo");
                	BigDecimal inventoryLevel = (BigDecimal) inventoryLevelMap.get("inventoryLevel");
                	BigDecimal inventoryWarehouseLevel = (BigDecimal) inventoryLevelMap.get("inventoryWarehouseLevel");
     			    if ((inventoryLevel.doubleValue() < inventoryOutOfStockTo.doubleValue()) || (inventoryLevel.doubleValue() == inventoryOutOfStockTo.doubleValue()) || (orderedQty.doubleValue() > inventoryLevel.doubleValue()))
    			    {
                        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                    	String productName = ProductContentWrapper.getProductContentAsText(product, "PRODUCT_NAME", locale, dispatcher, "html");
                    	if(UtilValidate.isEmpty(productName))
                    	{
                    		GenericValue virtualProduct = ProductWorker.getParentProduct(productId, delegator);
                    		if(UtilValidate.isNotEmpty(virtualProduct))
                        	{
                    			productName = ProductContentWrapper.getProductContentAsText(virtualProduct, "PRODUCT_NAME", locale, dispatcher, "html");
                        	}
                    	}
                    	MessageString tmpMessage=null;
     	 			    if ((orderedQty.doubleValue() > inventoryLevel.doubleValue()))
     	 			    {
     	                	String productAvailable= null;
     	                	if (inventoryLevel.intValue() != 1)
     	                	{
     	                		productAvailable = inventoryLevel.intValue() + " items";
     	                	}
     	                	else
     	                	{
     	                		productAvailable = inventoryLevel.intValue() + " item";
     	                		
     	                	}
     	                	Map<String, String> messageMap = UtilMisc.toMap("productName", productName,"productAvailable",productAvailable );
     	                	tmpMessage = new MessageString(UtilProperties.getMessage("OSafeUiLabels", "OutOfStockOrderedProductInfo",messageMap, locale),"",true);
	
     	 			    }
     	 			    else
     	 			    {
                        	Map<String, String> messageMap = UtilMisc.toMap("productName", productName);
                        	tmpMessage = new MessageString(UtilProperties.getMessage("OSafeUiLabels", "OutOfStockProductInfo",messageMap, locale),"",true);
	
     	 			    }
                    	
                    	checkInventoryLevelMap.put("stockMessage",tmpMessage);
                    	checkInventoryLevelMap.put("inStock","N");
    			    }
     			    checkInventoryLevelMap.putAll(inventoryLevelMap);            		
            	}

            }
			
		} catch (GenericEntityException ge) {
		    Debug.logError(ge, ge.getMessage(), module);
		}
    	return checkInventoryLevelMap;
    }
    
    
    
}
