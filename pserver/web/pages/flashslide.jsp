<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://portals.apache.org/pluto/portlet-el_2_0" prefix="portlet-el" %>
<portlet:defineObjects/>
<%
   String playerVar=(String)renderRequest.getAttribute("playvar");
%>
 
<script type="text/javascript" src="/portal/frame/script/myFocus/myfocus-1.2.4.full.js"></script>
<script type="text/javascript" src="/portal/frame/script/myFocus/mF_fscreen_tb.js"></script>
<link rel="stylesheet" href="/portal/frame/script/myFocus/mF_fscreen_tb.css" type="text/css" media="screen" />
	<div id="boxID" style="visibility:hidden;width:200px;height:200px;"> 
		<div class="loading"><span>请稍候...</span></div> 
		  <ul class="pic"> 
				<%= playerVar %>
		  </ul>
	</div>
<script type="text/javascript">

 $(function(){
 	getContainer('#<%=windowId%>').setExposed();
 })
 window.onload= function(){
 	var ctn = $("#pcms_index_pcms_FlashSlidePortlet");
 	var nWidth = ctn.width();
 	var nHeight = (ctn.width()*226)/409;
 	$("#boxID").width(nWidth);
 	$("#boxID").height(nHeight);
	 	myFocus.set({
		id:'boxID',
		time:3, 
	    trigger:'click', 
	    warp:false,
	    width:nWidth, 
	    height:nHeight, 
	    txtHeight:'default', 
		autoZoom:true,
		waiting:false,
		path:false
	},true);
	}
</script>
