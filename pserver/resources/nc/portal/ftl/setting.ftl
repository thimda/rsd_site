<html>
<head>
<title>设置</title>
<script src="${href("/js/jquery-1.4.2.min.js")}"></script>
</head>
<body>
<style>
.favcmenu dt { height:27px; font:  12px/27px "SimSun"; color:#034D71; padding-left:20px;}
.favcmenu dt span {display:block; height:27px; background:url(/portal/images/3.gif) left center no-repeat; padding-left:20px;}
.favcmenu dt.active span{background:url(/portal/images/4.gif) left center no-repeat;}
.favcmenu dd { display:none;}
.scmenu {list-style:none};
.scmenu li { height:27px; font:  12px/27px "SimSun"; color:#034D71; padding-left:20px;}
.scmenu li a{display:block; height:27px; background:url(/portal/images/3.gif) left center no-repeat; padding-left:20px;}
.scmenu li.active, .content dd li.hover {background:#dfdfdf;background-image:url(/portal/images/grayArraw.png) ; background-repeat:no-repeat; background-position:90% 40%;}
.favcmenu dd li {height:20px;font:normal 12px/20px "SimSun";list-style:none; background:#FFF;}
.favcmenu dd li a{color:#000; font-size:12px; color:#000;   overflow-x:hidden; }
.favcmenu dd li a img{ border:none; margin:0 6px 0 0;}
.favcmenu dd li.active, .content dd li.hover {background:#dfdfdf;background-image:url(/portal/images/grayArraw.png) ; background-repeat:no-repeat; background-position:90% 40%;}

 </style>
<dl class="favcmenu" tp="FoldeMenu">
<#if cateMap ??>
	<#list cateMap as cate>
		<dt><span>${cate.title}</span></dt>
	    <dd>
		    <ul>
		    <#if settingMap[cate.id] ??>
				<#list settingMap[cate.id] as sett>
		      		<li><a href="${sett.url}"><img src="/portal/images/blackpoint.gif" border="0" />${sett.title}</a></li>
		        </#list>
			</#if>
		    </ul>
	   </dd>
	</#list>
</#if>
</dl>
<dl class="favcmenu">
<#if singleSetting ??>
	<#list singleSetting as sett>
  		<dt><a href="${sett.url}"><span>${sett.title}</span></a></dt>
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
</script>
 </body>
</html>
