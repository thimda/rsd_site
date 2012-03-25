<html>
<head>
<base href="/portal/">
</head>
<body>

<style>

.floatmenu {
	font-size:12px;
	color:#0086b2;
	line-height:28px;
	list-style:none;
}

.floatmenu ul{
	/*padding-left:10px;*/
	list-style: none outside none;
}

.floatmenu ul li a{
	color:#0992C1;
	cursor:pointer;
}

#menuitems{
	padding-left:10px;
	overflow-y:auto;
}

#menuitems li{
	list-style:none;
}

#menuitems li a{
	font-size:16px;
	color:#0992C1;
	cursor:pointer;
	display:inline;
}
 

</style>

<table id="tasklist_body" style="position:relative;left:0px;">
	<tr align="left" style="height:32px;">
			<td class="td_top" align="center">
				<span class="floatmenu">常用流程</span>
			</td>
		<#list LINK_ROOT as child>
			<td class="td_top" align="center">
				<span class="floatmenu"><#if child.title ??>${child.title}<#else>未命名</#if></span>
			</td>
		</#list>
	</tr>
	<tr align="left">
			<td width="150" valign="top" class="td_style">
				<div class="floatmenu" style="margin-top:18px;" menu="t"  title="常用流程">
					<ul>
						<li>xxx</li>
					</ul>
				</div>
			</td>
		<#list LINK_ROOT as child>
			<td width="150" valign="top" class="td_style">
				<div class="floatmenu" style="margin-top:18px;" menu="t"  title="<#if child.title ??>${child.title}<#else>未命名</#if>">
				<ul>
					<#list child.nodes as node>
						<li title="${node.title}"  menu="t" >
							<a id="${node.pk_menuitem}" <#if node.funnode.url ??> isFun="1" href = "<#if node.funnode.url ?? && node.funnode.url?length != 0><#if node.funnode.url?index_of("?") != -1>${node.funnode.url}&nodecode=${node.funnode.id}<#else>${node.funnode.url}?nodecode=${node.funnode.id}</#if></#if>" <#else>style="text-decoration:none;color:#666666;cursor:default;"</#if>  ><b>${node.title}</b></a>
								<div class="floatmenuitem" id="${node.pk_menuitem}_child">
									<ul>
										<#list node.childrenMenu as childnode>
											<li>
												<a id="${childnode.pk_menuitem}" <#if childnode.pk_funnode ??> isFun="1" href = "<#if childnode.funnode.url ?? && childnode.funnode.url?length != 0><#if childnode.funnode.url?index_of("?") != -1 >${childnode.funnode.url}&nodecode=${childnode.funnode.id}<#else>${childnode.funnode.url}?nodecode=${childnode.funnode.id}</#if></#if>" <#else>style="text-decoration:none;color:#666666;cursor:default;"</#if>  title="${childnode.title}" >${childnode.title}</a>
											</li>
										</#list>
									</ul>
								</div>
						</li>
					</#list>
				</ul>
				</div>
			</td>
		</#list>
	</tr>
	<tr style="height:33px;">
		<td class="td_style"></td>
		<#list LINK_ROOT as child>
			<td class="td_style"></td>
		</#list>
	</tr>
</table>
</body>
</html>