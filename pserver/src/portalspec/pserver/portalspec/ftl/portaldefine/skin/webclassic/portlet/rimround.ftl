<div class="rimround"  tp="portlet"  pid="${portlet.id}" id="${lookID(page,portlet)}">
	<div class="head"  tp="pHead">
		<div class="leftpad"></div>
		<div class="leftpad"></div>
		<div class="title"  tp="pTitle" >
			<img src="${RES_PATH}/images/dropdown.png">
			<a>${portlet.title}</a>
		</div>
		<div class="rightpad"></div>
	</div>
	
	<div class="pbody"   tp="pBody">
	 	    <@portletWarper portletName=portlet.name pageModule=page.module pageName=page.pagename ></@portletWarper>
			<div class="clear"></div>
	</div>
	
	<div class="foot">
		<div class="leftpad"></div>
		<div class="leftpad"></div>
		<div class="title">
			<a tp="hidMoreBar" onclick="_hidMoreBar('${lookID(page,portlet)}')"><span tp="count">0</span> more <img src="${RES_PATH}/images/titlebar_bottom/more_arrow.png"></a>
			<a tp="showMoreBar" style="display:none"  onclick="_showMoreBar('${lookID(page,portlet)}')">hide <img src="${RES_PATH}/images/titlebar_bottom/more_arrow.png"></a>
			
		</div>
		<div class="rightpad"></div>
	</div>
	
</div>