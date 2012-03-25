<html>
	<head>
		<title>设置</title>
		<style>
			a{font-size: 14px; }
			input{font-size: 14px; }
		</style>
	</head>
	<body>
		<form action="/portal/pt/setting/template" target="settingFrame">
			<a >布局皮肤</a>
			<select name="id">
				 <#if skins ??>
					<#list skins as skin>
						 <option value="${skin.id}">${skin.name}</option>
					</#list>
				</#if>
			</select>
			<script>
				document.write("<input name='pagename' type='hidden' value='" + parent.window.opener.CUR_PAGE_NAME + "'/>");
				document.write("<input name='pagemodule' type='hidden' value='" + parent.window.opener.CUR_PPAGE_MODULE + "'/>");
			</script>
			<input type="submit" value="确定" />
		</form>
		<iframe id="settingFrame" name="settingFrame" width="0" height="0" style="border: 0px;"></iframe>
 	</body>
</html>

 
  