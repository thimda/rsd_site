<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<html>
	<head>
		<title>设置</title>
		<style>
			a{font-size: 14px; }
			input{font-size: 14px; }
		</style>
	</head>
	<body>
		<form action="/portal/pt/setting/lang" target="settingFrame">
			<a >语言</a>
			<select name="langId">
				<option value="simpchn">简体中文</option>
			</select>
			<input type="submit" value="确定" />
		</form>
		<iframe id="settingFrame" name="settingFrame" width="0" height="0" style="border: 0px;"></iframe>
 	</body>
</html>