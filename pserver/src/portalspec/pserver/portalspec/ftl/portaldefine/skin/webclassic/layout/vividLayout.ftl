<#--
ÆÕÍ¨layout
@param layout page
-->
<#macro wrap layout page>
<style>
.vividlayout{
	border-left:1px solid #CCDCE1;
	background-color:#e5f5ff;
 }

</style>
<div id="${layout.id}" tp="layout"  pid="${layout.id}">
	<script>
		$("#${layout.id}").parent("td").addClass("vividlayout");
	</script>
	<#nested>
	<div class="clear"></div>
</div>	

</#macro>