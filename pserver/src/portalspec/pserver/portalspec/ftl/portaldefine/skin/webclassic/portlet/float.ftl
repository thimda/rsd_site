<div class="blankPortlet margeach"   tp="portlet"  pid="${portlet.id}" id="${lookID(page,portlet)}" style="position:static">
    <div class="header" tp="pHead" style="display:none">
       <span tp="pTitle" class="pTitle"></span>
       <ul tp="pPart" class="pPart">
       </ul>
    </div>
	<div class="content"  tp="pBody">
	    <@portletWarper portletName=portlet.name pageModule=page.module pageName=page.pagename ></@portletWarper>
		<div class="clear"></div>
	</div>
 </div>