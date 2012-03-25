<#--
ÆÕÍ¨layout
@param layout page
-->
<#macro wrap layout page>
<div id="${layout.id}" tp="layout"  pid="${layout.id}">
	<#nested>
	<div class="clear"></div>
</div>
</#macro>