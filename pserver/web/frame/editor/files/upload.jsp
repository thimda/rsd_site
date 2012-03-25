<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>

<%@page import="nc.bs.portal.util.PortalProperties"%>
<%@page import="nc.bs.portal.util.PortalPropertiesConstant"%>

<%@page import="nc.bs.portal.util.SecurityUtil"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<title>
				文件上传
			</title>
	</head>
	<body topmargin="10" leftmargin="0" bgColor="#DCDCDC">		
		<%
		  	String identifier = request.getParameter("identifier");
		  	String style = request.getParameter("style");
		  	// 标示上传图片时是forum还是新闻上传图片
		  	String source = request.getParameter("source");
			request.setAttribute("identifier", identifier);
			request.setAttribute("style", style);
			if(source != null && !source.equals(""))
				request.setAttribute("source", source);
			String docServerIp = PortalProperties.get(PortalPropertiesConstant.DOC_SERVER_IP);
			if(docServerIp == null)
				docServerIp = "";
			request.setAttribute("docserverip", docServerIp);
			String pk_org = SecurityUtil.getPk_org();
			if(pk_org == null)
				pk_org = "";
			String serverip = request.getParameter("serverip");
		%>
		
		<form name="uploadForm" method="post" onsubmit="return CheckUploadForm();" action="${docserverip}${ROOT_PATH}/file?cmd=putFile&identifier=${identifier}&pk_org=<%=pk_org%>&serverip=<%=serverip%>&style=${style}&source=${source}&redirectpage=frame/editor/files/upload_success.jsp" enctype="multipart/form-data">
			<input type="file" name="uploadfile"><br>
			<input type="submit" name="ok" value="上传">
		</form>
		<script>
			Array.prototype.indexOf = function(obj)
			{
				for(var i = 0; i < this.length; i ++)
				{
					if(this[i] == obj)
						return i;
				}
				return -1;
			};
			
			var filters = ['gif','doc','xls','ppt','jpg','png','bmp','txt','pdf','rar','zip'];
			function CheckUploadForm()
			{	
				if(uploadForm.uploadfile.value == null || uploadForm.uploadfile.value == "")
				{	
					alert("文件不能为空");
					return false;
				}
				
				//验证文件的后缀是否是合法的后缀
				var path = uploadForm.uploadfile.value;
				var index = path.lastIndexOf(".");
				if(index == -1)
				{
					alert("必须是有效的文件格式!");
					return false;
				}
				var name = path.substring(index + 1);
				if(filters.indexOf(name.toLowerCase()) == -1)
				{
					alert("文件必须是有效的文件格式,请检查文件后缀的合法性");
					return false;
				}
				
				parent.document.getElementById("divProcessing").style.display="";
				return true;
			}
		</script>
	</body>
</html>
