<html>
<head>
	<title>Debug Console</title>
</head>

<body>
	<table>
			<tr>
				<td>				<form action="/portal/pt/M0dUlE/crc" target="chkcrc">
				Module Name: <input name="id" />
						 <button type="submit"  name="sbt" >CRC</button>
					</form>
				 </td>
			</tr>
			<tr>
				<td>
					 <iframe scrolling="no" id="chkcrc" name="chkcrc" width="400" height="120"  style="border:none;"vspace="0" hspace="0">
					 
					 </iframe>
				</td>
			</tr>
	</table>

	<table border="1" cellpadding="0" cellspacing="0" >
		<tr>
			<td width="100">Module Name</td>
			<td width="100">Health</td>
			<td width="80">CRC</td>
			<td width="80">ReBuild</td>
			<td width="80">ReDeploy</td>
		</tr>
		
		<#list nodes as node>
			<tr>
				<td width="100">${node.name}</td>
				<td width="100">${node.health}</td>
				<td width="80"><a href="/portal/pt/M0dUlE/crc?id=${node.id}" target="chkcrc">CRC</a></td>
				<td width="80"><a href="/portal/pt/M0dUlE/rebuild?id=${node.id}" target="chkcrc">ReBuild</a></td>
				<td width="80"><a href="/portal/pt/M0dUlE/redeploy?id=${node.id}" target="chkcrc">ReDeploy</a></td>
			</tr>
		</#list>
	</table>
</body>
</html>