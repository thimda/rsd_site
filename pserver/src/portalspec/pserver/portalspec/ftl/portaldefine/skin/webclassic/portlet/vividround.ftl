<div  class="vividround"   tp="portlet"  pid="${portlet.id}" id="${lookID(page,portlet)}">
	<div class="div_left_top"></div>
	<div class="div_right_top"></div>
		<div class="div_title">
			<table width="100%" height="100%">
				<tr>
					<td><span class="datetext" style="margin-left:10px;float:left;"  tp="pTitle">${portlet.title}</span></td>
					<td align="center"  tp="pPart" class="pPart">
 					</td>
					<td tp="pHander">
						<div class="close" style="margin-right:10px;"></div>
						<div class="maximize" style="margin-right:6px;"></div>
						<div class="minimize" style="margin-right:6px;"></div>
					</td>
				</tr>
			</table>
		</div>
		<div  class="div_content">
			<div style="right:0px;" class="div_border"></div>
			<div tp="pBody" style="width:100%">
	   			<@portletWarper portletName=portlet.name pageModule=page.module pageName=page.pagename ></@portletWarper>
	   			<div class="clear"></div>
			</div>
			<div class="div_border" style="left:0px;"></div>
		</div>
	<div class="div_left_bottom"></div>
	<div class="div_right_bottom"></div>
</div>