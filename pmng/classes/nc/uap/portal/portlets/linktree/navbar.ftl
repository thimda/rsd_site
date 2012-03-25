 <div style="padding-top:5px;padding-bottom:5px;">
	<table width="100%">
		<tr>
			<td width="6"  height="27" background="${RES_PATH}/images/btn_normal_left.png"></td>
			<td background="${RES_PATH}/images/btn_normal_center.png" style="text-align: left; padding-left: 10px;">
			<div style="font-size:12px;" tp="navbar">
			<#if title ??> 
				<#list title as tt>
				<!--  <#if url ??><#if tt_index + 1- title?size == 0>href="${url}"</#if></#if> -->
					<a>${tt}</a>
					<#if tt_index + 1- title?size != 0><a>&nbsp;>&nbsp;</a></#if>
				</#list>
			</#if>
			</div>
			</td>
			<td background="${RES_PATH}/images/btn_normal_center.png" width="54px">
				<div style="border-left:1px solid gray;color:#666666;font-size:12px;">帮助</div>
			</td>
			<td width="6" background="${RES_PATH}/images/btn_normal_right.png"></td>
		</tr>
	</table>
</div>
<script>
	function setPortalNavData(html){
		$("[tp=navbar]").html(html);
	}
</script>