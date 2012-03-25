<#--
获取Portlet模板路径
@param portlet
@param page
-->
<#function lookUp page portlet>
<#assign portalPath=portletLookUp(portlet.name?default(""),portlet.theme?default(""),page.skin?default(""),page.module?default(""))>
  <#return portalPath>
</#function>

<#function lookID page portlet>
<#assign portalPath=getPortletId(page.module?default(""),page.pagename?default(""),portlet.name?default(""))>
  <#return portalPath>
</#function>