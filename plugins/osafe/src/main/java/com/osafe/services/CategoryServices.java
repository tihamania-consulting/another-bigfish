/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.osafe.services;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastList;
import javolution.util.FastMap;


import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.collections.ResourceBundleMapWrapper;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.category.CategoryWorker;

/**
 * ControlServlet.java - Master servlet for the web application.
 */
@SuppressWarnings("serial")
public class CategoryServices extends HttpServlet {

    public static final String module = CategoryServices.class.getName();


    public CategoryServices() {
        super();
    }

    public static List<Map<String, Object>> getRelatedCategories(Delegator delegator, String parentId, List<String> categoryTrail, boolean limitView, boolean excludeEmpty, boolean recursive)
    {
    	return getRelatedCategories(delegator, parentId, categoryTrail, limitView, excludeEmpty, recursive, true);
    }
    
    public static List<Map<String, Object>> getRelatedCategories(Delegator delegator, String parentId, List<String> categoryTrail, boolean limitView, boolean excludeEmpty, boolean recursive, boolean useCache) 
    {
        List<Map<String, Object>> categories = new ArrayList();
        if (categoryTrail == null) {
            categoryTrail = new ArrayList();
        }
        categoryTrail.add(parentId);
        if (Debug.verboseOn())
        {
            Debug.logVerbose("getRelatedCategories for: " + parentId, module);
        	
        }

        List<GenericValue> rollups = null;

        try {
            rollups = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("parentProductCategoryId", parentId), UtilMisc.toList("sequenceNum"), useCache);

            if (limitView) {
                rollups = EntityUtil.filterByDate(rollups, true);
            }
        } catch (GenericEntityException e) {
            Debug.logWarning(e.getMessage(), module);
        }
        if (rollups != null) 
        {
            for (GenericValue parent : rollups) 
            {
                GenericValue cv = null;
                Map<String, Object> cvMap = new HashMap();

                try {
                    if (useCache)
                    {
                    	cv = parent.getRelatedOne("CurrentProductCategory");
                    }
                    else
                    {
                    	cv = parent.getRelatedOne("CurrentProductCategory");
                    	
                    }
                } catch (GenericEntityException e) {
                    Debug.logWarning(e.getMessage(), module);
                }
                if (cv != null) {

                    if (excludeEmpty) 
                    {
                        if (!CategoryWorker.isCategoryEmpty(cv)) 
                        {
                            cvMap.put("ProductCategory", cv);
                            cvMap.put("ProductCategoryRollup", parent);
                            categories.add(cvMap);
                            if (recursive) 
                            {
                                categories.addAll(getRelatedCategories(delegator, cv.getString("productCategoryId"), categoryTrail, limitView, excludeEmpty, recursive,useCache));
                            }
                            List<String> popList = new ArrayList();
                            popList.addAll(categoryTrail);
                            cvMap.put("categoryTrail", popList);
                            categoryTrail.remove(categoryTrail.size() - 1);
                        }
                    } 
                    else 
                    {
                        cvMap.put("ProductCategory", cv);
                        cvMap.put("ProductCategoryRollup", parent);
                        cvMap.put("parentProductCategoryId", parent.getString("parentProductCategoryId"));
                        categories.add(cvMap);
                        if (recursive) 
                        {
                            categories.addAll(getRelatedCategories(delegator, cv.getString("productCategoryId"), categoryTrail, limitView, excludeEmpty, recursive,useCache));
                        }
                        List<String> popList = new ArrayList();
                        popList.addAll(categoryTrail);
                        cvMap.put("categoryTrail", popList);
                        categoryTrail.remove(categoryTrail.size() - 1);
                    }
                }
            }
        }
        return categories;
    }
    
    
}
