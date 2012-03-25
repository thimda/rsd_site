<style>
.msgpanel{
	border-top: 1px dotted #333;
}
 .msgpanel .hdl{
 	display:none;
 }
  .msgpanel .sdl{
 	display:block;
 }
 .msgpanel dl{
	
    padding: 12px 0; clear: both;display:block;
}
.msgpanel dt span{
	color: #808080;
    display: block;
    float: left;
    font-size: 12px;
    font-weight: bold;
    text-align:left;
    width:100%;
}
.msgpanel dt span img{margin: 0 8px 0 11px;}
.msgpanel dd { padding-left: 27px;}
.msgpanel dd li{
	margin-top:11px;
    list-style:none;
    float: left;
    font-size: 12px;
    text-align:left;
    width:100%;
    
}
.msgpanel dd li a{
	color: #4d4d4d;
	float:left;
}
.msgpanel dd li span{
	 float:right;
	 text-align: center;
	 display:block;
	 width:28px;
	 height:18px;
	 background:url(${env().web}/images/indicator/blue.png);
	 color: #FFFFFF;
     font-family: arial;
     font-size: 12px;
     font-weight: bold;
     line-height: 18px;
     text-align: center;
     margin: 0 10px 0 0;
}

 
.msgpanel dd li .blue{
	background:url(${env().web}/images/indicator/blue.png);
}
.msgpanel dd li .green{
	background:url(${env().web}/images/indicator/green.png);
}
.msgpanel dd li .orange{
	background:url(${env().web}/images/indicator/orange.png);
}
.msgpanel dd li .blue3{
	background:url(${env().web}/images/indicator/blue_3.PNG);width:34px;
}
.msgpanel dd li .green3{
	background:url(${env().web}/images/indicator/green_3.PNG);width:34px;
}
.msgpanel dd li .orange3{
	background:url(${env().web}/images/indicator/orange_3.PNG);width:34px;
}
</style>
<div class="msgpanel">

<#if CATEGORY??>
	<#list CATEGORY as cate>
		<div style="border-bottom:1px dotted #333;"  <#if 2 lt cate_index>class="hdl"</#if>>
		<dl>
			<dt><span><img src="${env().web}/images/node01.png" />${cate.title}</span></dt>
			<dd>
			<#if cate.child??>
				<#list cate.child as childen>
				<#assign cs = getNewMessageCount(childen.pluginid,childen.id)>
				<li><a onclick="openPublicPortlet('pint','MsgCenterPopupPortlet',800,480,'${childen.id}')" >${childen.title}</a><span class="${numberDye(cs)}">${cs}</span></li>
				</#list>
			</#if>
			<div class="clear"></div>
			</dd>
			 
		</dl>
		<div class="clear"></div>
		</div>
	</#list>
</#if>
</div>
<script>
$(function() {
		registerMoreBar("${WINDOW_ID}","${CATEGORY?size - 3}",function(){$(".msgpanel .hdl").show(100);},function(){$(".msgpanel .hdl").hide(100);})
});
</script>