<style> 
.fun_panel{
		
}
.fun_panel .hdl{
	display:none;
}
.fun_panel .item{
	height:100%;
	margin:0 auto;
	width:100%;
	 border-bottom: 1px dotted #333333;
    padding: 6px 0;
}

.fun_panel .item .icon{
	 float:left; width:40px; margin-left:10px;
}
.fun_panel .item dl{
	 margin:0 0 0 65px  !important; margin: 0 0 0 67px ; height:100%;
	 text-align:left;
}
.fun_panel .item dl dt a{
	font-size: 12px; font-weight: bold;line-height:20px;
}

.fun_panel .item dl dt span{
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
     margin: 2px 10px 0 0;
}

.fun_panel .item dl dd li{
	list-style:none;
	font-size: 12px;color: #808080;
	line-height:18px;
}

.fun_panel .item dl dt   .blue{
	background:url(${env().web}/images/indicator/blue.png);
}
.fun_panel .item dl dt   .green{
	background:url(${env().web}/images/indicator/green.png);
}
.fun_panel .item dl dt   .orange{
	background:url(${env().web}/images/indicator/orange.png);
}
.fun_panel .item dl dt   .blue3{
	background:url(${env().web}/images/indicator/blue_3.PNG);width:34px;
}
.fun_panel .item dl dt   .green3{
	background:url(${env().web}/images/indicator/green_3.PNG);width:34px;
}
.fun_panel .item dl dt   .orange3{
	background:url(${env().web}/images/indicator/orange_3.PNG);width:34px;
}

</style>
<div class="fun_panel">
<#if FUNS??>
	<#list FUNS as fun>
		<div class="item <#if 2 lt fun_index>hdl</#if>">
			<div class="icon"><img src="${fun.icon}" /></div>
			<dl>	<#assign cs = fun.stat>
					<dt><a>${fun.title}</a><span class="${numberDye(cs)}">${cs}</span></dt>
					<dd>
						<li><a>${fun.detail}</a></li>
					</dd>
			</dl>
			<div class="clear"></div>
		</div>
 	</#list>
</#if>
</div>
<script>
$(function() {
		registerMoreBar("${WINDOW_ID}","${FUNS?size - 3}",function(){$(".fun_panel .hdl").show(100);},function(){$(".fun_panel .hdl").hide(100);})
});
</script>