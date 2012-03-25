<div class="rimsquare" tp="portlet"  pid="${portlet.id}" id="${lookID(page,portlet)}">
	<div style="width:100%;height:24px;line-height:24px;"  tp="pHead">
		<div class="dropdown" style="margin-right:5px;margin-top:6px;"></div>
		<div class="textlabel">${portlet.title}</div>
		<div class="more" style="margin-left:3px;margin-top:2px;"></div>
		<div class="textlabel1">more</div>
		<div class="pPart" tp="pPart" ></div>
	</div>
	<div class="line"></div>
	<div style="width:100%;" tp="pBody">
		<@portletWarper portletName=portlet.name pageModule=page.module pageName=page.pagename ></@portletWarper>
		<div class="clear"></div>
	</div>
</div>