<%@ page contentType="text/html;charset=UTF-8" %>
<%
  
  response.setHeader("Pragma","No-cache");   
  response.setHeader("Cache-Control","no-cache");   
  response.setDateHeader("Expires",   0);   

%>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<% 
		String portalDataSource = nc.uap.portal.util.PortalDsnameFetcher.getPortalDsName();
		nc.uap.lfw.core.LfwRuntimeEnvironment.setDatasource(portalDataSource);
		nc.uap.portal.service.itf.IPtPortalConfigRegistryService portalConfig = nc.uap.portal.service.PortalServiceUtil.getConfigRegistryService();
		//显示验证码
		String showRanImg = "false";//portalConfig.get("login.randomimage.enabled");
		//打开方式
		String openMode = portalConfig.get("portal.openmode");
		
		pageContext.setAttribute("showRanImg", showRanImg);
%>
<html>
	<head>
		<title>企业门户</title>		
		<lfw:base/>		
		<lfw:head/>		
		<lfw:import />
		<style>
			body{
				margin:0px;
				padding:0px;
				overflow:hidden;
			}
			.ctn{
				width:100%;
				height:100%;
				margin:0px;
				padding:0px;
				background-image : url(/portal/html/nodes/login/images/rigologin-bg.png) no-repeat bottom center fixed;
 			}
			.ctn .hd{
			}
			.ctn .hd a{
				color:#FFF;
				font-size:13;			
			}
			.ctn .content{
			}
			.ctn .foot{
				background-color:#F0FF0F;
 			}
 			.label_div{
 				z-index:99999;
 			}
 			.inputc{
 				
 			}
 			.inputc .ipt_l{
 				width:11px;
 				height:55px;
 			}
 			.inputc .ipt_m{
 				background:url("/portal/html/nodes/login/images/input_normal.png");
 				height:42px;
 				width:227px;
 				margin-left:3px;
 				margin-top:4px;
 			}
 			.inputc .ipt_m1{
				height:48px;
 				width:233px;
 				padding-top:1px;
 			}
 			.inputc .ipt_r{
 				width:11px;
 				height:55px;
 			}
 			.inputc input{
 				border:none;
 				height:23px;
 			}
 			.inputc .label{
 				color:#acb6c8;
 				font-size:13pt;
 			}
 			.text_div{
 				border:none;
 				background-image:url("/portal/html/nodes/login/images/ipt_input_bg.png");
 			}
 			.text_div .text_input{
 				 height:22px;
				 width: 1px;
 				 background:url('/portal/html/nodes/login/images/input_normal__center.png') repeat-x;
 				 float:left;
 			}
 			.inputf{
 			
 			}
 			.inputf .label{
 				color:#acb6c8;
 				font-size:13pt;
 			}
 			.inputf input{
 				border:none;
 			}
 			.inputf .ipt_l{
 				background:url("/portal/html/nodes/login/images/ipt_f_l.png");
 			}
 			.inputf .ipt_m1{
 				background:url("/portal/html/nodes/login/images/input_higlight.png");
 				height:48px;
 				width:233px;
 				padding-top:1px;
 			}
 			.inputf .ipt_m{
 				background:url("/portal/html/nodes/login/images/input_normal.png");
 				height:42px;
 				width:227px;
 				margin-left:3px;
 				margin-top:4px;
 			}
 			.inputf .ipt_r{
 				background:url("/portal/html/nodes/login/images/ipt_f_r.png");
 			}
 			
 			.login_button_div .btn_off {
				border-style:none;
				background: url(/portal/html/nodes/login/images/login-1.png) no-repeat;
				/**_background-image :none;
				 _FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=/portal/html/nodes/login/images/login-1.png);
				 **/
				border: 0px;
				height:36px;
				font-size: 12pt;
				color: #492500;
				font-weight: bold;
				background-color: transparent;
				cursor: pointer;
			} 
			.login_button_div .btn_on {
				border-style:none;
				background: url(/portal/html/nodes/login/images/login.png) no-repeat;
				/**
				_background-image :none;
				_FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=/portal/html/nodes/login/images/login.png);
				**/
				border: 0px;
				height:36px;
				font-size: 12pt;
				color: #492500;
				font-weight: bold;
				background-color: transparent;
				cursor: pointer;
			}
			.login_button_div .btn_down {
				border-style:none;
				background: url(/portal/html/nodes/login/images/login_press.png) no-repeat;
				/**
					_background-image :none;
					_FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=/portal/html/nodes/login/images/login_press.png);
				**/
				border: 0px;
				height:36px;
				font-size: 12pt;
				color: #492500;
				font-weight: bold;
				background-color: transparent;
				cursor: pointer;
			}
			.login_status_ck{
				background:url(/portal/html/nodes/login/images/bg.png) no-repeat;
				width:14px;
				height:14px;
			}
			.login_status_ck_checked{
				background:url(/portal/html/nodes/login/images/check.png) no-repeat;
				width:14px;
				height:14px;
			}
			.login_bg{
				filter:progid:dximagetransform.microsoft.alphaimageloader(src=${ROOT_PATH}/${NODE_PATH}/images/rigologin-bg.png, sizingmethod=scale);
				background-image : url(/portal/html/nodes/login/images/rigologin-bg.png) no-repeat bottom center fixed;
				z-index:-99999999;
				position:absolute;
				width:100%;
				height:100%;
				left:0px;
				top:0px;
			}
 		</style>
		<script>
			var showRanImg = <%= showRanImg %>;
			function iptFocus(obj){
				var ipt = findIptBox(obj);
				var className = ipt.className;
				ipt.className = className + " inputf";
				if(obj.id == 'userid'){
					document.getElementById('usernamelab').innerHTML = '';
				}else if(obj.id == 'password'){
					document.getElementById('passwordlab').innerHTML = '';
				}
			}
			function iptBlur(obj){
				var ipt = findIptBox(obj);
				var className = ipt.className;
				ipt.className = className.replace("inputf", "");
				if(obj.id == 'userid' && obj.children[0].children[0].value.length == 0){
					document.getElementById('usernamelab').innerHTML = '用户名';
				}else if(obj.id == 'password' && obj.children[0].children[0].value.length == 0){
					document.getElementById('passwordlab').innerHTML = '密码';
				}
			}
			
			function findIptBox(obj){
				var t = obj;
				for(var i = 0 ;i< 15; i++){
					t = t.parentNode;
					if(t.tagName == "TABLE"){
						return t;
					}
				}
				return false;
			}
			function changeLoginStatusCK(){
				var ck = document.getElementById('loginStatusCK');
				if(ck.className == ''){
					ck.className = 'login_status_ck_checked';
				}else{
					ck.className = '';
				}
			}
		</script>
		<title>Login</title>
	</head>
	<body>	
	<div class="login_bg"> </div>
	<lfw:pageModel>			
		<lfw:widget id="main">
				<div class="ctn">
					<div class="hd">
						<div style="height:40px;">
							<table width="100%" height="40" cellpadding="0" cellspacing="0" border="0" >
								<tr>
									<td align="left" width="50%"><div style="margin-top: 22px; margin-left: 20px;"><img src="/portal/html/nodes/login/images/ufidanc.png"></div></td>
									<td width="50%" align="right"><div style="width:200px;margin-top:25px;color:#ffffff;font-size:12pt;"><a>简体中文</a> <a>|</a> <a style="width: 60px; display: inline-block; text-align: left;">帮助</a></div></td></tr>
							</table>
						</div>
					</div>
					
					<div class="content">
						<table cellpadding="0" cellspacing="0" border="0"  width="100%">
							<tr>
								<td  width="100%" height="500" align="middle" valign="center">
									<table cellpadding="0" cellspacing="0" border="0" height="70">
										<tr>
											<td colspan="3" align="middle" valign="top" height="160">
												<div style="position: relative; width: 100%; height: 100%;">
												<img src="/portal/html/nodes/login/images/zhxt_logo.png">
												<div style="position: absolute; right: -33px; bottom: 63px; width: 243px; height: 249px;"><img src="/portal/html/nodes/login/images/light.png"></div>
												</div>
											</td>
										</tr>
										<tr>
											<td colspan="3" <c:if test="${showRanImg eq 'false'}">width="626" background="/portal/html/nodes/login/images/panel.png"</c:if><c:if test="${showRanImg eq 'true'}">width="685" background="/portal/html/nodes/login/images/panel1.png"</c:if> >
												<table  cellpadding="0" cellspacing="0" border="0" class="inputc">
													<tr>
														<td width="20px"></td>
														<td height="70">
															 <table cellpadding="0" cellspacing="0" border="0" class="inputc">
															 	<tr>
																 	<td>
																 		<div class="ipt_m1">
																	 		<div class="ipt_m">
																			 	<div style="padding-left:17px;padding-top:10px;">
																			 		<div id="usernamelab" style="position:absolute;z-index:99999;float:left;" class="label">用户名</div>
																			 		<lfw:textcomp id="userid" widget='main' width="190"/>
																			 	</div>
																		 	</div>
																	 	</div>
																 	</td>
															 	</tr>
															 </table>
														</td>
														<td  height="70">
															 <table cellpadding="0" cellspacing="0" border="0" class="inputc">
															 	<tr>
																 	<td>
																 		<div class="ipt_m1">
																	 		<div class="ipt_m">
																			 	<div style="padding-left:17px;padding-top:10px;">
																			 		<div id="passwordlab" style="position:absolute;z-index:99999;float:left;" class="label">密码</div>
																			 		<lfw:textcomp id="password" widget='main' width="190"/>
																			 	</div>
																		 	</div>
																	 	</div>
																 	</td>
															 	</tr>
															 </table>
														</td>
														<c:if test="${showRanImg eq 'true'}">
														<td  height="70">
															 <table cellpadding="0" cellspacing="0" border="0" class="inputc">
															 	<tr>
																 	<td>
																 		<lfw:textcomp id="randimg" widget='main' width="64"/>
																 		<img id="randimage" style="margin-top:4px;" src="./randimg" onclick="this.src='./randimg?d='+ new Date().getTime()" />
																 	</td>
															 	</tr>
															 </table>
														</td>
														</c:if>
														<td align="center">
															<lfw:button id="submitBtn" width="116" height="36" className="login_button_div"></lfw:button>
														</td>	 
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td colspan="3" height="15px;">&nbsp;</td>
										</tr>
										<tr>
											<td align="right" width="40px">
												<div class="login_status_ck" onclick="changeLoginStatusCK()" style="position: relative;">
													<div id="loginStatusCK" class="">
													</div>
												</div>
											</td>
											<td>
												<div style="position: relative;margin-left:10px;">
													<a style="color:#4d4d4d;cursor:pointer;">保持登录</a>
													<span style="margin-left:150px;cursor:default;"></span>
													<a style="color:#4d4d4d;cursor:pointer;" href="">忘记密码?</a>
												</div>
											</td>
											<td align="center" width="190px">
												<lfw:label id="tiplabel"></lfw:label>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
					<div class="foot" >
					</div>
				</div>
		</lfw:widget>
	</lfw:pageModel>	
	</body>
</html>