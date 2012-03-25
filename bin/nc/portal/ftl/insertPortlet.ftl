<html>
<head>
<script src="${href("/js/jquery-1.4.2.min.js")}"></script>
</head>
<body>
<style>
.favcmenu dt { height:27px; font:  12px/27px "SimSun"; color:#034D71; padding-left:20px;}
.favcmenu dt span {display:block; height:27px; background:url(/portal/images/3.gif) left center no-repeat; padding-left:20px;}
.favcmenu dt.active span{background:url(/portal/images/4.gif) left center no-repeat;}
.favcmenu dd { display:none;}
.favcmenu dd li {height:20px;font:normal 12px/20px "SimSun";list-style:none; background:#FFF;}
.favcmenu dd li a{color:#000; font-size:12px; color:#000;   overflow-x:hidden; }
.favcmenu dd li a img{ border:none; margin:0 6px 0 0;}
.favcmenu dd li.active, .content dd li.hover {background:#dfdfdf;background-image:url(/portal/images/grayArraw.png) ; background-repeat:no-repeat; background-position:90% 40%;}
 </style>
<dl class="favcmenu" tp="FoldeMenu">
<#if PortletDisplayCategory ??>
	<#list PortletDisplayCategory as cate>
		<dt><span>${cate.text}</span></dt>
	    <dd>
		    <ul>
		    <#if cate.portletDisplayList ??>
				<#list cate.portletDisplayList as display>
					<#if display.dynamic ?? && display.dynamic>
			      		<li><a href="javascript:void(0)" onClick="insertNewPortlet('${display.module}','${display.id}','${display.title}')"><img src="/portal/images/blackpoint.gif" border="0" />${display.title}</a></li>
			        </#if>
		        </#list>
			</#if>
		    </ul>
	   </dd>
	</#list>
</#if>
</dl>
<script>


 $(".favcmenu dt").each(function(i){
	$(this).click(function(){
		$(".favcmenu dt").eq(i).toggleClass("active").next("dd").toggle();		
	})
})
$(".favcmenu dd li").click(function(){
	$(this).addClass("active").siblings().removeClass("active");
}).hover(function(){
	$(this).addClass("hover")
},function(){
	$(this).removeClass("hover")
})
/**
* 插入新的Portlet
**/
function insertNewPortlet(portletModule,portletId,title){
	var cfm = window.confirm("${multiLang("portal","ftl.insertPortlet.var1")}"+title+"${multiLang("portal","ftl.insertPortlet.var2")}");
	if (cfm){
		$("#portletModule").val(portletModule);
		$("#portletId").val(portletId);
		$("#ajaxForm").submit();
	}else{
		return false;
	}
}
function insertOK(){
	parent.window.opener.document.location.reload();
}
</script>
<form id="ajaxForm" name="ajaxForm" method="get" target="ajaxFrame" action="${env().web}/pt/home/doInsertNewPortlet">
<input type="hidden" name="pageName" id="pageName" value="${pageName}">
<input type="hidden" name="pageModule" id="pageModule" value="${pageModule}">
	<input type="hidden" name="portletModule" id="portletModule">
	<input type="hidden" name="portletId" id="portletId">
</form>
<iframe id="ajaxFrame" name="ajaxFrame" frameborder="0" width="0" height="0" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</body>
</html>
