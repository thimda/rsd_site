<#--
导航宏
@param pages 用户有权限的page集合
-->
<#macro menu pages currPage>
<style>
.menu{
	list-style: none;
	margin:0 0 0 14px;
	padding:0;
}
.menu li{
	float:left;
}
.menu  a{
	height:39px;
	line-height:39px;
	vertical-align:top;
	display:inline-block;
	font-size:13px;
	color:#FFF;
	font-family:微软雅黑;
}
.menu .mi_f{
	height:20px;
	background-color:#73bfd9;
	width:1px;
	background-image:none;
	margin:12px 15px 0;
	display:inline-block;
}
.menu .current{

}
.menu .current .mi_l{
	display:inline-block;
	width:9px;
	background-image:url(${RES_PATH}/images/menu_b_l.png);
	background-repeat:no-repeat;
}
.menu .current .mi_r{
	display:inline-block;
	width:9px;
	background-image:url(${RES_PATH}/images/menu_b_r.png);
}

.menu .current a{
	background-position: 0 4px;
	background-repeat:repeat-x;
	background-image:url(${RES_PATH}/images/menu_b_m.png);
	color:#088DC5;
	font-weight:bold;
}
.menu .current a span{
	margin:0 6px;

}
.nav_b_lr{
	width:10px;
	height:39px;
}
.nav_b_r{
	background-image:url(${RES_PATH}/images/nav_b_r.png);
}
.nav_b_l{
	background-image:url(${RES_PATH}/images/nav_b_l.png);
}
.nav_b_m{
	background-image:url(${RES_PATH}/images/nav_b_m.png);
}
.nav_ctrl{

}
.nav_ctrl img{
	margin: 0 6px;
}

.more_arrow_image{
	background-image:url(${RES_PATH}/images/more_arrow.png);
	width:10px;
	height:20px;
	float:right;
	margin-right:10px;
	background-position:center center;
	background-repeat:no-repeat;
}
.so_image{
	background-image:url(${RES_PATH}/images/so.png);
	width:20px;
	height:20px;
	float:right;
	margin-right:10px;
}
.so_search_image{
	background-image:url(${RES_PATH}/images/ref_m.png);
	height:20px;
	display:none;
	float:right;
	margin-right:10px;
}
.submit_image{
	background-image:url(${RES_PATH}/images/so.png);
	width:20px;
	height:20px;
}

.search_image{
	background-image:url(${RES_PATH}/images/search.png);
	width:20px;
	height:20px;
	background-color:#48B1E0;
}

.search_more{
	background-image:url(${RES_PATH}/images/search_more.png);
	background-position:center center;
	background-repeat:no-repeat;
	width:9px;
	height:7px;
}

.set_image{
	background-image:url(${RES_PATH}/images/set.png);
	width:20px;
	height:20px;
	float:right;
	margin-right:10px;
}
.dmax_image{
	background-image:url(${RES_PATH}/images/dmax.png);
	width:20px;
	height:20px;
	float:right;
	margin-right:10px;
}
.dmin_image{
	background-image:url(${RES_PATH}/images/dmin.png);
	width:20px;
	height:20px;
	display:none;
	float:right;
	margin-right:10px;
}

.table_item_body{
	border:none;
 }

.table_item_body .table_left_top{
	background:url(${RES_PATH}/images/pw_round_left_top.png) no-repeat;
	width:18px;
	height:18px;
}
.table_item_body .table_right_top{
	background:url(${RES_PATH}/images/pw_arrow_right_top.png) no-repeat;
	width:18px;
	height:18px;
}
.table_item_body .table_top_center{
	background:url(${RES_PATH}/images/pw_arrow_top_center.png) repeat-x;
	height:18px;
}
.table_item_body .table_left_center{
	background:url(${RES_PATH}/images/pw_arrow_left_center.png) repeat-y;
	width:18px;
}
.table_item_body .table_center{
	background:url(${RES_PATH}/images/pw_arrow_center.png) repeat;
}
.table_item_body .table_right_center{
	background:url(${RES_PATH}/images/pw_arrow_right_center.png) repeat-y;
	width:18px;
}
.table_item_body .table_left_bottom{
	background:url(${RES_PATH}/images/pw_angle_left_bottom.png) no-repeat;
	width:18px;
	height:18px;
}
.table_item_body .table_bottom_center{
	background:url(${RES_PATH}/images/pw_arrow_bottom_center.png) repeat-x;
	height:18px;
}
.table_item_body .table_right_bottom{
	background:url(${RES_PATH}/images/pw_round_right_bottom.png) no-repeat;
	width:18px;
	height:18px;
}

.search_item_body{
	border:none;
 }

.search_item_body .table_left_top{
	background:url(${RES_PATH}/images/pw_left_top.png) no-repeat;
	width:9px;
	height:9px;
	float:left;
}
.search_item_body .table_right_top{
	background:url(${RES_PATH}/images/pw_right_top.png) no-repeat;
	width:9px;
	height:9px;
	float:left;
}
.search_item_body .table_top_center{
	background:url(${RES_PATH}/images/pw_top_center.png) repeat-x;
	height:9px;
	float:left;
}
.search_item_body .table_left_center{
	background:url(${RES_PATH}/images/pw_left_center.png) repeat-y;
	width:9px;
	float:left;
}
.search_item_body .table_center{
	background:url(${RES_PATH}/images/pw_center.png) repeat;
	float:left;
}
.search_item_body .table_right_center{
	background:url(${RES_PATH}/images/pw_right_center.png) repeat-y;
	width:9px;
	float:left;
}
.search_item_body .table_left_bottom{
	background:url(${RES_PATH}/images/pw_left_bottom.png) no-repeat;
	width:9px;
	height:9px;
	float:left;
}
.search_item_body .table_bottom_center{
	background:url(${RES_PATH}/images/pw_bottom_center.png) repeat-x;
	height:9px;
	float:left;
}
.search_item_body .table_right_bottom{
	background:url(${RES_PATH}/images/pw_right_bottom.png) no-repeat;
	width:9px;
	height:9px;
	float:left;
}

.search_item_body .input_normal_left_bg {
	background:url(${RES_PATH}/images/input_normal_left.png) no-repeat scroll 0 0 transparent;
	float:left;
	height:22px;
	width:3px;
}

.search_item_body .input_normal_center_bg {
	background:url(${RES_PATH}/images/input_normal_center.png) repeat-x scroll 0 0 transparent;
	float:left;
	height:22px;
}

.search_item_body .input_normal_right_bg {
	background:url(${RES_PATH}/images/input_normal_right.png) no-repeat scroll 0 0 transparent;
	float:left;
	height:22px;
	width:3px;
}

#moreMenu a:link{color:#808080;  text-decoration:none;}
#moreMenu a:visited{color:#808080;  text-decoration:none;}
</style>
<script>
function hideTopHandler(){
	var ohh = $("#head_top").height();
	$("#head_top").attr("oriHeight",$("#head_top").height());
	$("#head_top").hide();
	$("#hideTop").hide();
	$("#showTop").show();
	var ifms = $("iframe");
	for(var i = 0; i< ifms.length; i++){
		var hh = $(ifms[i]).height();
		$(ifms[i]).height(hh + ohh);
	}
}

function showTopHandler(){
	$("#head_top").show();
	$("#hideTop").show();
	$("#showTop").hide();
	var ifms = $("iframe");
	var ohh = $("#head_top").attr("oriHeight");
	for(var i = 0; i< ifms.length; i++){
		var hh = $(ifms[i]).height();
		$(ifms[i]).height(hh - ohh);
	}
}

function showSrCtn(o){
 		 
}

$(function(){
	var sum = 14;
	var width = $("#menutd").width();
	$("#moreMenu").empty();
	$("#menutd").find(".menu").children().each(function(i){
		sum = sum + $(this).width();
		if(sum > width){
			var clone = $(this).clone(true);
			clone.show();
			$(this).hide();
			$("#moreMenu").append(clone);
		}
	});
	if($("#moreMenu").children().length > 0){
		$("#more").show();
	}else{
		$("#more").hide();
	}
	$("#searchMessage").attr("placein","0");

$("#search1").click(function () {
	if($("#searchMessage").attr("placein") == "0"){
		$("#searchMessage").attr("placein","1");
		$("#searchMessage").hide();
		var top = $(this).position().top + 20;
		var left = $(this).position().left - 288 + 20 - 6;
		$("#searchMessage").css("left",left + "px");
		$("#searchMessage").css("top",top + "px");
		$("#searchMessage").show();
	}else if($("#searchMessage").attr("placein") == "1"){
		$("#searchMessage").attr("placein","0");
		$("#searchMessage").hide();
	}
});

$("#more").mouseover(function(){
	$("#moreMessage").attr("placein","1");
	$("#moreMessage").hide();

	var top = $("#more").position().top + 9;
	var left = $("#more").position().left - 105;
	$("#moreMessage").css("left",left + "px");
	$("#moreMessage").css("top",top + "px");
	$("#moreMessage").show();
});

$("#moreMessage").mouseout(function(){
	$("#moreMessage").attr("placein","0");
	setTimeout(function(){
		if($("#moreMessage").attr("placein") == "0"){
			$("#moreMessage").hide();
		}
	},2000);
});

$("#moreMessage").mouseover(function () {
	$("#moreMessage").attr("placein","1");
	$("#moreMessage").hide();

	var top = $("#more").position().top + 9;
	var left = $("#more").position().left - 105;
	$("#moreMessage").css("left",left + "px");
	$("#moreMessage").css("top",top + "px");
	$("#moreMessage").show();
});

});

function debug(log){
			if(console){
				console.info(log);
			}
		};
</script>
<link rel="stylesheet" href="/lfw/frame/themes/webclassic/styles/button.css">
<div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="nav_b_lr nav_b_l"></td>
			<td id="menutd" class="nav_b_m">
				<ul class="menu">
					<#include "pagemenu.ftl">
				</ul>
			</td>
			<td id="navtd" class="nav_b_m" width="110">
				<div class="nav_ctrl">
					<div id="hideTop" onclick="hideTopHandler()" class="dmax_image"></div>
					<div id="showTop" onclick="showTopHandler()" class="dmin_image"></div>
					<!-- <div class="set_image"></div> -->
					<div id="search1" class="so_image"></div>
					<div id="search2" class="so_search_image">
						<form action="/portal/pt/search/doSearch" method="post"  target= "_blank">
							<input name="queryString" type="text" style="border:none;border:0;width:230px;float:left;"/>
							<input type="submit" value="" class="submit_image" style="width:20px;border:none;border:0;display:inline;float:left;"/>
						</form>
					</div>
					<div id="more" class="more_arrow_image">
						<div id="moreMessage" style="width:120px;height:180px;position:absolute;display:none;z-index:10000;">
							<table id="tableMessage" class="table_item_body" border="0" border="none" width="100%" height="100%">
								<tr height="18">
									<td class="table_left_top"></td>
									<td class="table_top_center"></td>
									<td class="table_right_top"></td>
								</tr>
								<tr height="144">
									<td width="18" class="table_left_center"></td>
									<td width="84" class="table_center" align="left" valign="top"><ul id="moreMenu" style="margin-left:6px;line-height:20px;"></ul></td>
									<td width="18" class="table_right_center"></td>
								</tr>
								<tr height="18">
									<td class="table_left_bottom"></td>
									<td class="table_bottom_center"></td>
									<td class="table_right_bottom"></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</td>
			<td class="nav_b_lr nav_b_r"  width="10"></td>
		</tr>
	</table>
</div>
<div style="width: 288px; height: 85px; position: absolute; display: none; z-index: 10000;" class="search_item_body" id="searchMessage">
	<div class="table_left_top" style="width: 9px; height: 9px;"></div>
	<div class="table_top_center" style="width: 270px; height: 9px;"></div>
	<div class="table_right_top" style="width: 9px; height: 9px;"></div>
	
	<div class="table_left_center" style="width: 9px; height: 67px;"></div>
	<div style="width: 270px; height: 67px;" class="table_center">
	    <div style="margin: 3px 4px 5px; width: 262px; height: 57px;">
			<div style="margin-bottom: 10px; float: left; width: 100%; color: rgb(0, 134, 178);">
				<div style="float: left; margin-right: 20px; font-weight: bold;">所有</div>
				<div style="float: left; margin-right: 20px;">BI</div>
				<div style="float: left; margin-right: 20px;">供应链</div>
				<div style="float: left; margin-right: 20px;">制造</div>
				<div style="float: left; margin-right: 20px;">财务</div>
				<div class="search_more" style="float: right; margin-top: 2px;"></div>
			</div>
			<div style="float: left; height: 1px; width: 100%; border-bottom: 1px solid rgb(229, 243, 247);"></div>
			 
			<div style="margin-top: 10px; float: left; height: 22px; width: 100%;">
				<div class="input_normal_left_bg"></div>
				<div class="input_normal_center_bg" style="width: 256px;">
					<form target="_blank" method="post" action="/portal/pt/search/doSearch">
						<input type="text" class="input_normal_center_bg" style="border: 0pt none; height: 22px; width: 230px; float: left;" name="queryString">
						<input type="submit" style="margin-top: 2px;width: 20px;height:18px; border: 0pt none; display: inline; float: right;" class="search_image" value="">
					</form>
				</div>
				<div class="input_normal_right_bg"></div>
			</div>
		</div>
	</div>
	<div class="table_right_center" style="width: 9px; height: 67px;"></div>
	
	<div class="table_left_bottom" style="width: 9px; height: 9px;"></div>
	<div class="table_bottom_center" style="width: 270px; height: 9px;"></div>
	<div class="table_right_bottom" style="width: 9px; height: 9px;"></div>
</div>
</#macro>
