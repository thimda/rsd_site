<#--
具有内补丁的布局
@param layout page
-->
<#macro wrap layout page>
<div id="${layout.id}" tp="layout" pid="${layout.id}" style="padding:0 5px;">
<#nested>
<div class="clear"></div>
</div>
</#macro>