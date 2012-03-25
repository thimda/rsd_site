<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<title>general data...</title>
</head>
<body>
<div>
<h1 id="tips">生成数据中....</h1>
<div style="algin:center">
<img id="loadingBar" src="<%= nc.uap.lfw.core.LfwRuntimeEnvironment.getRootPath() %>/images/process.gif"/>
</div>
</div>
<script>
function showMessage(msg){
	document.getElementById("tips").innerHTML = msg;
	document.getElementById("loadingBar").style.display = "none";
}
</script>
<iframe boder="0"  frameborder="0" vspace="0" hspace="0" width="0" src="<%= nc.uap.lfw.core.LfwRuntimeEnvironment.getRootPath() %>/pt/offline/client/down"></iframe>
</body>

</html>