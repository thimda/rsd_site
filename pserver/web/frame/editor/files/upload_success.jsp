<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<%@page import="java.util.ArrayList"%>
<%@page import="nc.bs.portal.util.PortalUtil"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<title>
				文件上传
			</title>
	</head>
	<body topmargin="10" leftmargin="0">		
			<%
				String serverip = request.getParameter("serverip");
				if(serverip != null && !serverip.equals(""))
				{
					String[] ipPort = serverip.split("_");
					if(ipPort != null && ipPort.length == 2)
					{
						String ip = ipPort[0] + ":" + ipPort[1];
						String scheme = request.getScheme();
						
						String[] filenames = (String[])request.getAttribute("filenames");
						String[] fileKeys = (String[])request.getAttribute("fileKeys");
						String[] fileTypes = (String[])request.getAttribute("fileTypes");
						String fileName = "", fileKey = "", fileType = "";
						for(int i = 0; i < filenames.length; i ++)
						{
							fileName += filenames[i];
							fileKey += fileKeys[i];
							fileType += fileTypes[i];
							if(i != filenames.length - 1)
							{
								fileName += "_";
								fileKey += "_";
								fileType += "_";
							}
						}
			%>
					<script>
						if("<%=ip%>" != null)
							window.location.href = "<%=scheme%>://<%=ip%>" + "${ROOT_PATH}/frame/editor/files/upload_success.jsp?fileName=" + encodeURIComponent("<%=fileName%>") + "&fileKey=<%=fileKey%>&fileType=<%=fileType%>";
					</script>
			<%
					}
				}
			%>
		<c:if test="${not empty message}">
			文件上传失败
		</c:if>
		<c:if test="${empty message}">
			<script>
				parent.document.getElementById("divProcessing").style.display="none";
			</script>	
			文件上传成功
			<script>
				<%
					String fileName = PortalUtil.convertToCorrectEncoding(request.getParameter("fileName"));
					String fileKey = request.getParameter("fileKey");
					String fileType = request.getParameter("fileType");
					if(fileName != null && !fileName.trim().equals(""))
					{
						String[] fileNames = fileName.split("_");
						String[] fileKeys = fileKey.split("_");
						String[] fileTypes = fileType.split("_");
						for(int i = 0; i < fileNames.length; i++)
						{
				%>
							window.parent.UploadSaved("<%=fileNames[i]%>", "<%=fileKeys[i]%>", "<%=fileTypes[i]%>");
				<%
						}
					}else
					{
				%>	
						<c:forEach var="fileName" items="${filenames}" varStatus="s">
							window.parent.UploadSaved("${fileName}", "${fileKeys[s.index]}", "${fileTypes[s.index]}");
						</c:forEach>
				<%
					}
				%>
			</script>
		</c:if>
	</body>
</html>