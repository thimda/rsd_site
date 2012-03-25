<div class="cleanPortlet"   tp="portlet"  pid="${portlet.id}" id="${lookID(page,portlet)}">
    <div class="header" tp="pHead">
       <span tp="pTitle" class="pTitle">${portlet.title}</span>
       <ul tp="pPart" class="pPart">
       </ul>
    </div>
	<div class="content"  tp="pBody">
	    <@portletWarper portletName=portlet.name pageModule=page.module pageName=page.pagename ></@portletWarper>
		<div class="clear"></div>
	</div>
 </div>