<%@ page contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<title>异常信息显示</title>
	</head>	
	<body leftMargin="0" topmargin="0" scroll="no">
		<table width="100%" height="100%" bgcolor="#E8E8E8">
			<tr align="center" valign="middle" bgcolor="#E8E8E8">
				<td>
					<c:if test="${not empty exception}">
						<font color="red">${exception}</font>
					</c:if>
				</td>
			</tr>
		</table>
	</body>
</html>