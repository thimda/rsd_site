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

</head>
<body>

     <!--导航开始-->
    <div>
		<#import "../../../comm/navigation.ftl" as nav>
		<@nav.menu pages=userPages currPage=page></@nav.menu>
    </div>
    <!--导航结束-->
    
    <!--布局开始-->
    <div>
		<div id="l1" tp="layout"  pid="l1">
<style>
.bluePortlet { width:100%;   position:relative; overflow:hidden; }
.bluePortlet .header {background:url(/portal/images/portlet-middle-bg.gif) left top repeat-x; height:27px;}
.bluePortlet .header .pTitle {float:left;display:inline;height:27px; padding-left:6px; min-width:100px; background:url(/portal/images/portlet-left.gif) left top no-repeat;font:bold 12px/27px "SimSun"; color:#fff;}
.bluePortlet .header .pTitle img{ border:0; vertical-align:middle;}
 .bluePortlet .header ul {float:right;display:inline;height:27px; padding-right:15px; background:url(/portal/images/portlet-right.gif) right top no-repeat; }
.bluePortlet .header ul li{ list-style:none; float:left; vertical-align:middle; height:27px;}
.bluePortlet .header ul li a{color:#ECFCC0; font:12px/27px "SimSun"; line-height:24px; }
.bluePortlet .header ul li a img { vertical-align:middle;}
.bluePortlet .roundDiv {position:relative;height:0;}
.bluePortlet .roundDiv div {position:absolute;height:8px; width:8px; overflow:hidden; background-image:url(/portal/images/bg2.gif); background-repeat:no-repeat;}

.bluePortlet .roundDiv .divTL {top:0; left:0;background-position:0 0;}
.bluePortlet .roundDiv .divTR {top:0; right:0;background-position:-8px 0;}
.bluePortlet .roundDiv .divBL {bottom:0; left:0;background-position:0 -8px;}
.bluePortlet .roundDiv .divBR {bottom:0; right:0;background-position:-8px -8px;}

.bluePortlet .content {border:2px solid #0868a8;background:#ecfafd; _height:10px; _overflow:visible; min-height:10px;
background: -webkit-gradient(linear, left top, left bottom, from(#fefeff), to(#ecfafd)); /* for webkit browsers */
background: -moz-linear-gradient(top,  #fefeff,  #ecfafd); /* for firefox 3.6+ */
filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fefeff', endColorstr='#ecfafd'); /* for IE */}

</style><div class="bluePortlet margeach"  tp="portlet"  pid="p3" id="pmng_ipad_pmng_MgrContentPortlet">
  <div class="header" tp="pHead">
    <span class="pTitle" tp="pTitle">内容</span>
    <ul class="pPart" tp="pPart">
     <li tp="pHander">
      </ll>
    </ul>
  </div>
  <div class="mainbody">
        <div class="roundDiv"><div class="divTL"></div><div class="divTR"></div></div>
        <div class="content"  tp="pBody">
<iframe name="portlet" allowtransparency="true"  style="border:0px; padding:0;"  width="100%" height="200" id="pmng_ipad_pmng_MgrContentPortlet_iframe" frameborder="0"  vspace="0" hspace="0" src="${url}">
</iframe>

<script>

	$("#pmng_ipad_pmng_MgrContentPortlet_iframe").load(function(){
		initIframeEnv.call(this);
 
	});
	initIframeArea('pmng_ipad_pmng_MgrContentPortlet_iframe',0);

	$(function(){
		$(window).resize(function(){
			 resizeIframe('pmng_ipad_pmng_MgrContentPortlet_iframe',0);
		});
	})

</script><script>getContainer("#pmng_ipad_pmng_MgrContentPortlet").setSupportModes(['VIEW']);</script><script>getContainer("#pmng_ipad_pmng_MgrContentPortlet").addDoMaxResize(['normal']);</script>			<div class="clear"></div>
        </div>
        <div class="roundDiv"><div class="divBL"></div><div class="divBR"></div></div>
  </div>
</div>	             
</div>
    </div>
    <!--布局结束-->
    	
    <!--页脚开始-->
    <div tp="foot">
		<#include "../../../comm/foot.ftl">
    </div>
    <!--页脚结束-->
    <!--Portal工具条-->
	<#include "../../../comm/tipspanel.ftl">
	<!-- pserver -->
</body>
</html>