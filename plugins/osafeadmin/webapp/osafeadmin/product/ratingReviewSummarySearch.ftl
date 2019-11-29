<!-- start searchBox -->
<#assign nowTimestamp=Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp()>
<#assign weekStart=Static["org.apache.ofbiz.base.util.UtilDateTime"].addDaysToTimestamp(nowTimestamp,-6)>
<#assign monthStart=Static["org.apache.ofbiz.base.util.UtilDateTime"].addDaysToTimestamp(nowTimestamp,-30)>
<#assign productionDate=Static["org.apache.ofbiz.base.util.UtilProperties"].getPropertyValue('osafeAdmin','production-date')>
<#if productionDate?has_content>
   <#assign productDateStart=Static["org.apache.ofbiz.base.util.UtilDateTime"].toTimestamp(productionDate)>
<#else>
   <#assign productDateStart=Static["org.apache.ofbiz.base.util.UtilDateTime"].toTimestamp('06/02/2011 00:00:00')>
</#if>
<div class="entryRow">
  <div class="entry daterange">
    <label>${uiLabelMap.PeriodCaption}</label>
    <div class="entryInput from">
      <input class="dateEntry" type="text" name="dateFrom" maxlength="40" value="${parameters.dateFrom!weekStart?string('dd/MM/yyyy')!""}"/>
    </div>
    <label class="tolabel">${uiLabelMap.ToCaption}</label>
    <div class="entryInput to">
      <input class="dateEntry" type="text" name="dateTo" maxlength="40" value="${parameters.dateTo!nowTimestamp?string('dd/MM/yyyy')!""}"/>
    </div>
  </div>
  <div class="dateSelectButtons">
    <input type="button" class="standardBtn dateSelect" name="TodayBtn" value="${uiLabelMap.TodayBtn}" onClick="setDateRange('${nowTimestamp?string('dd/MM/yyyy')}','${nowTimestamp?string('dd/MM/yyyy')}',${searchFormName!""});"/>
    <input type="button" class="standardBtn dateSelect" name="ThisWeekBtn" value="${uiLabelMap.LastSevenDaysBtn}" onClick="setDateRange('${weekStart?string('dd/MM/yyyy')}','${nowTimestamp?string('dd/MM/yyyy')}',${searchFormName!""});"/>
    <input type="button" class="standardBtn dateSelect" name="MonthToDateBtn" value="${uiLabelMap.LastThirtyDaysBtn}" onClick="setDateRange('${monthStart?string('dd/MM/yyyy')}','${nowTimestamp?string('dd/MM/yyyy')}',${searchFormName!""});"/>
    <input type="button" class="standardBtn dateSelect" name="DateAllBtn" value="${uiLabelMap.DateAllBtn}" onClick="setDateRange('${productDateStart?string('dd/MM/yyyy')}','${nowTimestamp?string('dd/MM/yyyy')}',${searchFormName!""});"/>
</div>
</div>
<!-- end searchBox -->