<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#--系统渲染组件 可继承或者重写-->
<#import "../../../comm/render.ftl" as render>
<#--系统渲染组件 可继承或者重写-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${href("/css.css")}" rel="stylesheet" type="text/css">
<script src="${href("/js/jquery-1.4.2.min.js")}"></script>
<script src="${href("/js/portal_util.js")}"></script>
<script src="${href("/js/popbaseball.js")}"></script>
<script src="${href("/js/portal.js")}"></script>
<link rel="stylesheet" href="${href("/css/ui-lightness/jquery-ui-1.8.7.custom.css")}">
<script src="${href("/js/jquery-ui-1.8.7.custom.min.js")}"></script>
<#--引入常量-->
 <@render.importConstant constants=ptconstants/>
<title>${env().title}</title>
<base href="${env().ctx}" />
		<style>
			body{
				background:url(${RES_PATH}/images/bg.png) repeat-x scroll 0 0 #addcff; 
				text-align:center;
				margin:0px;
			}
			a{
				cursor:pointer;
			}
			table{border:none;border-collapse:collapse;border-spacing:0;}
			th,td{padding:0;}
			.userinfo{}
			.userinfo span{	display:block:;color:#4FB0E3;font-size:15;font-weight:bold;	}
			.userinfo div{
				display:block;
			}
			.userinfo div a{
				display:inline-block;font-size:13;color:#727784;
			}
		</style>
	</head>
	<body>
	 <div style="width:100%;max-width:1200px;margin-left:auto;margin-right:auto;">
		<div tp="nagigation">
			<!-- 顶部 -->
			<div style="width:100%">
				<table  width="100%"  >
					<tr>
						<td height="10" colspan="3"></td>
					</tr>
					<tr id="head_top">
						<td height="30" width="240"><img src="${env().web}/images/oalogo.png"></td>
						<td>　</td>
						<td width="200">
							<table>
								<tr>
									<td width="30">&nbsp;</td>
									<td width="40"><a onclick="openPublicPortlet('pint','MsgCenterPopupPortlet',800,480)"><img src="${RES_PATH}/images/h_mgs.png"></a></td>
									<td width="50"><img src="${RES_PATH}/images/user_icon.png"></td>
									<td class="userinfo">
										<span>${USER_INFO.username}</span>
										<div>
											<a onclick="window.open ('${env().web}/pages/setting.jsp', 'newwindow', 'height=250, width=420, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');"   style="cursor: pointer;" >设置</a>
											<a>|</a>
											<a href="${env().web}/pt/home/logout">注销</a>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height="10" colspan="3"></td>
					</tr>
				</table>
			</div>
			<!-- 导航栏 -->
			<#import "../../../comm/navigation.ftl" as nav>
     		<@nav.menu pages=userPages currPage=page></@nav.menu>
		</div>
	    <!--布局开始-->
	    <div style="background-color:#fff;border:1px solid #bec3c7;">
	      <@render.layout pageLayout=page.layout page=page/>
	    </div>
	    <!--布局结束-->
	    
	    <!--页脚开始-->
	    <div tp="foot">
	    </div>
	    <!--页脚结束-->
	    <!--Portal工具条-->
		<#include "../../../comm/tipspanel.ftl">
	</div>
 
</body>
</html>