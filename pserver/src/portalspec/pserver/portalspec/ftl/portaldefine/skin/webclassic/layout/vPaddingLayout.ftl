<#--
具有内补丁的布局
@param layout page
-->
<#macro wrap layout page>
<div id="${layout.id}" tp="layout" pid="${layout.id}" style="padding:5px 0px;">
<#nested>
<div class="clear"></div>
</div>
</#macro>