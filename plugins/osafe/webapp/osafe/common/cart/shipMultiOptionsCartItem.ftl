 <div class="multiAddressItems group group1">
   <ul class="displayList shipItemList multiShipOptions">
	<li class="image itemImage multiShipOptionsItemImage<#if lineIndex == 0> firstRow</#if>">
	  <div>
	      <img alt="${wrappedProductName}" src="${productImageUrl}" class="productCartListImage" height="${IMG_SIZE_CART_H!""}" width="${IMG_SIZE_CART_W!""}" <#if productImageAltUrl?has_content && productImageAltUrl != ''> onmouseover="src='${productImageAltUrl!""}'; jQuery(this).error(function(){onImgError(this, 'PLP-Thumb');});" onmouseout="src='${productImageUrl!""}'; jQuery(this).error(function(){onImgError(this, 'PLP-Thumb');});"</#if> onerror="onImgError(this, 'PLP-Thumb');">     
	  </div>
	</li>
   </ul>
  </div>           
 <div class="multiAddressItems group group2">
   <ul class="displayList shipItemList multiShipOptions">
	<li class="string itemName multiShipOptionsItemName<#if lineIndex == 0> firstRow</#if>">
	  <div>
          <label>${uiLabelMap.CartItemNameCaption}</label>
		  <span>${wrappedProductName}</span>
	  </div>
	</li>
    <li class="number itemQty multiShipOptionsItemQty<#if lineIndex == 0> firstRow</#if>">
      <#assign qtyInput= parameters.get("qtyInCart_${lineIndex}")!1/>
	  <div>
	    <label>${uiLabelMap.CartItemQuantityCaption}</label>
		  <span>${shipQty!}</span>
	  </div>
	</li>
	<li class="string itemDescription multiShipOptionsItemDescription<#if lineIndex == 0> firstRow</#if>">
	  <div>
	    <label>${uiLabelMap.CartItemDescriptionCaption}</label>
	    <span>
	      <ul class="displayList productFeature">
			<li class="string productFeature">
			 <div>
			  <#if productFeatureAndAppls?has_content>
			      <#list productFeatureAndAppls as productFeatureAndAppl>
				    <#assign productFeatureTypeLabel = ""/>
				    <#if productFeatureTypesMap?has_content>
				      <#assign productFeatureTypeLabel = productFeatureTypesMap.get(productFeatureAndAppl.productFeatureTypeId)!"" />
				    </#if>
				    <span>${productFeatureTypeLabel!}:${productFeatureAndAppl.description!}</span>
			      </#list>
			  </#if>
	         </div>
	       </li>
	      </ul>
	    </span>
	  </div>
	</li>   
   </ul>
 </div>
