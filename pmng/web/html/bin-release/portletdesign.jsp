<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="history/history.css" />
<!--
<%
  String playerpath = request.getContextPath()+"/core/pt/swf/down";
%>
-->
<title></title>
<script>
	var webContextPath = '<%=request.getContextPath()%>';
	var isIE  = (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;
	//var isWin = (navigator.appVersion.toLowerCase().indexOf("win") != -1) ? true : false;
	//var isOpera = (navigator.userAgent.indexOf("Opera") != -1) ? true : false;
	var browser = isIE==true?'IE':notIE
	var playerpath = '<%=request.getContextPath()%>'+"/core/pt/swf/down?browser="+browser;
</script>
<script src="AC_OETags.js" language="javascript"></script>
<script src="portlet.js" language="javascript"></script>
<script src="history/history.js" language="javascript"></script>
<style>
body { margin: 0px; overflow:hidden }
</style>
<script language="JavaScript" type="text/javascript">
<!--
var requiredMajorVersion = 9;
var requiredMinorVersion = 0;
var requiredRevision = 124;
// -->
</script>
</head>

<body scroll="no">
<script language="JavaScript" type="text/javascript">
<!--
var hasProductInstall = DetectFlashVer(6, 0, 65);
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
if ( hasProductInstall && !hasRequestedVersion ) {
	var alternateContent = 'Your Flash Player is too old '
  	+ 'This content requires the higher version Adobe Flash Player. '
   	+ '<a href='+playerpath+'>Get new version Flash</a>';
    document.write(alternateContent);  
} else if(!hasProductInstall){ 
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href='+playerpath+'>Get Flash</a>';
    document.write(alternateContent);  
  }
  else {
	AC_FL_RunContent(
			"src", "portletdesign",
			"width", "100%",
			"height", "100%",
			"align", "middle",
			"id", "portletdesign",
			"quality", "high",
			"bgcolor", "#ffffff",
			"name", "portletdesign",
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
  } 
// -->
</script>
<noscript>
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="portletdesign" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="portletdesign.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="portletdesign.swf" quality="high" bgcolor="#ffffff"
				width="100%" height="100%" name="portletdesign" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</noscript>
</body>
</html>
