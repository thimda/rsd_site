<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="javax.portlet.PortletSession" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://portals.apache.org/pluto/portlet-el_2_0" prefix="portlet-el" %>
<portlet:defineObjects/>
<%
String vxml = (String)renderRequest.getAttribute("vxml");
String vhtml5 = (String)renderRequest.getAttribute("vhtml5");

String width = (String)renderRequest.getAttribute("width");
String height = (String)renderRequest.getAttribute("height");

String userAgent = nc.uap.lfw.core.LfwRuntimeEnvironment.getWebContext().getRequest().getHeader("user-agent");
boolean isIpad = userAgent != null && userAgent.length() > 0 && ( userAgent.indexOf("iPad") != -1 ||  userAgent.indexOf("iPhone") != -1);
%>
<%if(isIpad){%>
	<video  id="player_<%= windowId %>" width="<%=width%>" height="<%= height%>"   controls="controls" preload="true" autoplay="true">
	   <%= vhtml5 %>" 
	</video>
<%}else{%>
<embed id="player_<%= windowId %>" src="/portal/swf/player.swf" width="<%=width%>" height="<%= height%>" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" wmode="transparent" allowScriptAccess="always" allowFullScreen="true"  menu="false" FlashVars="xml=<%=vxml%>"></embed>
<%}%>
<script>
function resizeVideoPlayer(wid){	  
	var player= $("#"+wid);
	if(player.width()==0){
		setTimeout("resizeVideoPlayer('"+wid+"')",300);
	}else{
		player.height(0.75*player.width());
	}
}
$(function(){
   $(window).resize(function(){
	 resizeVideoPlayer("player_<%= windowId %>");
	});
	resizeVideoPlayer("player_<%= windowId %>");
})
</script>
<script>
$(function(){
	getContainer('#<%=windowId%>').setExposed();
})
</script>