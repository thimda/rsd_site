<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">	
<head>		
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="apple-touch-icon-precomposed" href="/portal/chart.png" >

	<title>企业门户</title>		
	<lfw:base/>		
	<lfw:head/>		
	<lfw:import />
	<% 
		String portalDataSource = nc.uap.portal.util.PortalDsnameFetcher.getPortalDsName();
		nc.uap.lfw.core.LfwRuntimeEnvironment.setDatasource(portalDataSource);
		nc.uap.portal.service.itf.IPtPortalConfigRegistryService portalConfig = nc.uap.portal.service.PortalServiceUtil.getConfigRegistryService();
		//显示验证码
		String showRanImg = portalConfig.get("login.randomimage.enabled");
		//打开方式
		String openMode = portalConfig.get("portal.openmode");
		
		pageContext.setAttribute("showRanImg", showRanImg);
	 %>
	<script>
	/**
	 *  是否显示验证码
	 **/
	var showRanImg = <%= showRanImg %>;
	/**
	 * 自定义加载提示条
	 */
	function showDefaultLoadingBar() {
//		if (window.loadingBar == null) {
			var innerHTML = "<div align='center' style='height:100%;width:100%;padding-top:250px'><img src='images/process.gif'/> </div>";
			window.loadingBar = new LoadingBarComp(document.body, "$loadingBar", 0, 0, "500", "500", null, "center", "center", 100000, null, true);
			window.loadingBar.setInnerHTML(innerHTML);
			//修改背景色 licza
			window.loadingBar.Div_gen.style.backgroundColor="transparent";
//		}
		window.loadingBar.show();
	};
	/**
	* 保证登录页在最上层页面中打开
	*/
	function loadInRealPage() {
				var realPage = window;
				while (realPage.parent != null && realPage.parent != realPage) {
					realPage = realPage.parent;
				}
				if (realPage != window)
					realPage.location.href = window.location.href;
	}

	loadInRealPage();
	</script>
	<style>
		body{
			filter:progid:dximagetransform.microsoft.alphaimageloader(src=${ROOT_PATH}/${NODE_PATH}/images/big-bg.png, sizingmethod=scale);
		}
		
	</style>
</head>	
<body>
	<lfw:pageModel>			
		<lfw:widget id="main">
		<lfw:flowvLayout id="flowv1">
		<lfw:flowvPanel>
		<lfw:flowhLayout>
		<lfw:flowhPanel>
		</lfw:flowhPanel>
		<lfw:flowhPanel width="415">
		<lfw:flowvLayout id="flowv3">
		<lfw:flowvPanel>
		</lfw:flowvPanel>
		<lfw:flowvPanel height="302">
		<lfw:flowvLayout id="flowv4">
		<lfw:flowvPanel height="235">
			<lfw:flowhLayout id="flowv5">
				<lfw:flowhPanel width="198">
					<lfw:flowvLayout id="flowv7">
						<lfw:flowvPanel height="60">
						</lfw:flowvPanel>
						<lfw:flowvPanel>
							<img class="pngFix" src="images/logo.png"/>
							<div class="pngFixDiv"></div>
						</lfw:flowvPanel>
					</lfw:flowvLayout>
				</lfw:flowhPanel>
				<lfw:flowhPanel width="22">
					<img class="pngFix"  src="${NODE_PATH}/images/line.png"/>
					<div class="pngFixDiv"></div>
				</lfw:flowhPanel>
				<lfw:flowhPanel>
					<lfw:flowvLayout id="flowv6">
						<lfw:flowvPanel height="60">
						</lfw:flowvPanel>
						<lfw:flowvPanel height="27">
							<lfw:textcomp id="userid" widget='main' width="190"/>
						</lfw:flowvPanel>
						<lfw:flowvPanel height="27">
							<lfw:textcomp id="password" widget='main' width="190"/>
						</lfw:flowvPanel>
						<c:if test="${showRanImg eq 'true'}">
							<lfw:flowvPanel height="27">
							<lfw:flowhLayout id="flowh19">
								<lfw:flowhPanel width="130">
									<lfw:textcomp id="randimg" widget='main' width="190"/>
								</lfw:flowhPanel>
								<lfw:flowhPanel>
									<img id="randimage" src="./randimg" onclick="this.src='./randimg?d='+ new Date().getTime()" />
								</lfw:flowhPanel>
							</lfw:flowhLayout>
							</lfw:flowvPanel>
						</c:if>
						<c:if test="${showRanImg eq 'false'}">
							<lfw:flowvPanel height="9">
							</lfw:flowvPanel>
						</c:if>
						<lfw:flowvPanel>
							<lfw:button id="submitBtn" width="105" height="51" className="login_button_div"></lfw:button>
						</lfw:flowvPanel>
						<lfw:flowvPanel>
							<lfw:label id="tiplabel"></lfw:label>
						</lfw:flowvPanel>
					</lfw:flowvLayout>     	
				</lfw:flowhPanel>
		 
			</lfw:flowhLayout>
		</lfw:flowvPanel>
		<lfw:flowvPanel height="67">
			
		</lfw:flowvPanel>
		</lfw:flowvLayout>
		</lfw:flowvPanel>
		<lfw:flowvPanel>
		</lfw:flowvPanel>
		</lfw:flowvLayout>
		</lfw:flowhPanel>
		<lfw:flowhPanel>
		</lfw:flowhPanel>
		</lfw:flowhLayout>
		</lfw:flowvPanel>
		<lfw:flowvPanel height="93">
		<lfw:flowvLayout id="flowv2">
		<lfw:flowvPanel height="21">
			<lfw:flowhLayout id="flowh9">
				<lfw:flowhPanel>
				</lfw:flowhPanel>
				<lfw:flowhPanel width="128">
					<img class="pngFix" src="${NODE_PATH}/images/nc-logo.png"/>
					<div class="pngFixDiv"></div>
				</lfw:flowhPanel>
				<lfw:flowhPanel width="35">
				</lfw:flowhPanel>
			</lfw:flowhLayout>
		</lfw:flowvPanel>
		<lfw:flowvPanel height="31">
		</lfw:flowvPanel>
		<lfw:flowvPanel height="41">
		<lfw:border id="border1" topColor="#FFFFFF" widget="main" showLeft="false" showRight="false" showTop="true" showBottom="false" width="1">
		<lfw:flowvLayout id="flowv10">
			<lfw:flowvPanel height="40">
				<div class="copyright_div">&copy; Copyright 2008-2011 UFIDA Software CO.LTD all rights reserved.</div>
				<div class="foot_opacity"></div>
			</lfw:flowvPanel>
		</lfw:flowvLayout>
		</lfw:border>
		</lfw:flowvPanel>
		</lfw:flowvLayout>
		</lfw:flowvPanel>
		</lfw:flowvLayout>
		</lfw:widget>
	</lfw:pageModel>	
</body>
</html>