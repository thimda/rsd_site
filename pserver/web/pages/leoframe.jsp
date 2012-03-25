<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="javax.portlet.PortletSession" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://portals.apache.org/pluto/portlet-el_2_0" prefix="portlet-el" %>
<portlet:defineObjects/>
<%
//PortletSession ps = renderRequest.getPortletSession();
String height = (String)renderRequest.getAttribute("if_height");
String iframeId = windowId.replaceAll("\\.","_") + "_iframe";
%>
<iframe name="portlet" allowtransparency="true"  style="border:0px; padding:0;"  width="100%" height="200" id="<%= iframeId%>" frameborder="0"  vspace="0" hspace="0" <%= renderRequest.getAttribute("if_src_type")%>="<%=renderRequest.getAttribute("if_src")%>">
</iframe>

<script>
	$("#<%= iframeId%>").load(function(){
		initIframeEnv.call(this);
		var frameDoc = document.getElementById("<%= iframeId%>").contentWindow.document;
 		var img = frameDoc.createElement("IMG");
		img.style.position="absolute";
		img.src="/portal/images/inarrow.png";
		img.style.right="8px";
		img.style.top="8px"; 
		img.style.cursor="pointer";
		addEventHandler(img,"click",function(){
			var param = {};
			param[ACTION_NAME]="doMaxAction";
			getContainer("#<%= windowId %>").doAction(param);
		})
		frameDoc.body.appendChild(img);
	});
	initIframeArea('<%= iframeId%>',<%=height%>);

	$(function(){
		$(window).resize(function(){
			 resizeIframe('<%= iframeId%>',<%=height%>);
		});
	});
	function addEventHandler(oTarget, sEventType, fnHandler) {
		if (oTarget.addEventListener) {  // 用于支持DOM的浏览器
			oTarget.addEventListener(sEventType, fnHandler, true);
		} else if (oTarget.attachEvent) {  // 用于IE浏览器
			oTarget.attachEvent("on" + sEventType, fnHandler);
		} else {  // 用于其它浏览器
			oTarget["on" + sEventType] = fnHandler;
		}
	}
	function addResizeHanlder2FrmPortlet(ctnId){
		var frameDoc = $("#" + ctnId).find("iframe")[0].contentWindow.document;
		var rizHdl = frameDoc.getElementById(ctnId + "_RizHdl");
		if(rizHdl == null){
			var rizHdl = frameDoc.createElement("IMG");
			rizHdl.id = ctnId + "_RizHdl";
			rizHdl.style.position="absolute";
			rizHdl.src="/portal/images/outarror.png";
			rizHdl.style.left="8px";
			rizHdl.style.top="8px"; 
			rizHdl.style.cursor="pointer";
			addEventHandler(rizHdl,"click",function(event){
				var img = this;
				if(img.id == null){
					img = event.srcElement;
				}
				var param = {};
				param[ACTION_NAME]="doMaxAction";
				getContainer("#" + (img.id.replace("_RizHdl", ""))).doReSize();
				img.style.display="none";
			})
			frameDoc.body.appendChild(rizHdl); 
		}else{
			rizHdl.style.display="";
		}
		
		
	}
	
</script>