<#if USER_MENU??>
<#assign menuitems =  USER_MENU.items>
<#list menuitems as menuitem>
<#assign pageUrl = env().web+"/pt/home/view?pageModule="+menuitem.module+"&pageName="+menuitem.id >
  	  <li id="${menuitem.module}_${menuitem.id}" <#if menuitem.id == currPage.pagename&&menuitem.module==currPage.module> class="current"  </#if>><a class="mi_l"></a><a href="${pageUrl}"><span>${menuitem.title}</span></a><a class="mi_r"></a> <#if menuitem_index<menuitems?size-1><span class="mi_f"></span></#if></li>
</#list>
</#if>
 