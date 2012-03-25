<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>全文检索</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">

		<style type="text/css">
/*BODY {MARGIN-TOP: 5px;FONT-SIZE: 85%;MARGIN-LEFT: 10px;FONT-FAMILY: Arial;BACKGROUND-COLOR: #ffffff}*/
BODY{margin:0;border:0;padding:0;}
A:link {COLOR: #3737c8}
A:active {COLOR: #f00}
A:visited {COLOR: #639}
A.result_attr:link {FONT-SIZE: 90%;COLOR: #008000}


#search_menu{padding:0;margin:0;border:0;height:31px;width:100%;min-width:600px;overflow:hidden;}
#search_menu_mid{padding:0;margin:0;border:0;height:31px;overflow:hidden;float:left right;background-image:url("${href("/search/img/menu.png")}");}
#search_menu_toleft{padding:0;margin:0;border:0;height:31px;float:left;width:28px;background-image:url("${href("/search/img/menu.png")}");margin-right:-3px;}
#search_menu_leftcorner{padding:0;margin:0;border:0;height:31px;width:10px;background-image:url("${href("/search/img/left_up.png")}");background-repeat:no-repeat;background-position:left;float:left;margin-right:-3px;}
#search_menu_rightcorner{padding:0;margin:0;border:0;height:31px;width:10px;background-image:url("${href("/search/img/right_up.png")}");background-repeat:no-repeat;background-position:right;float:right;margin-left:-3px;}
#search_menu_toleft .left{padding:0;margin:0;border:0;height:31px;width:13px;background-image:url("${href("/search/img/to_light.png")}");background-repeat:no-repeat;background-position:left center;float:left;}
#search_menu_toleft .right{padding:0;margin:0;border:0;height:31px;width:5px;background-image:url("${href("/search/img/left_line.png")}");background-repeat:no-repeat;background-position:right;float:left;}
#search_menu_toright{padding:0;margin:0;border:0;height:31px;float:right;width:28px;text-align:center;background-image:url("${href("/search/img/menu.png")}");margin-left:-3px;}
#search_menu_toright .left{padding:0;margin:0;border:0;height:31px;width:10px;background-image:url("${href("/search/img/right_line.png")}");background-repeat:no-repeat;background0position:left top;float:left;}
#search_menu_toright .right{padding:0;margin:0;border:0;height:31px;width:17px;background-image:url("${href("/search/img/to_right.png")}");background-repeat:no-repeat;background-position:center;float:left;}
#search_menu_toright .line{padding:0;margin:0;border:0;height:17px;margin-top:7px;width:1px;background-color:#7bcdf8;float:left;}
#nextPage_menu{padding:0;margin:0;border:0;height:31px;float:right;width:18px;text-align:center;background-image:url("${href("/search/img/menu.png")}");margin-left:-3px;}
#nextPage_menu .content{padding:0;margin:0;border:0;height:31px;width:18px;text-align:center;background-image:url("${href("/search/img/news_indicator_white.png")}");background-position:center right;background-repeat:no-repeat;}

.search_menu_item{padding:0;margin:0;margin-left:10px;margin-right:10px;border:0;text-align:left;float:left;height:31px;width:auto;}
	 
.search_menu_item .text{padding:0;margin:0;border:0;font-size:12px;color:#FFF;height:20px;width:auto;margin-top:11px;margin-left:5px;margin-right:5px;float:left;text-align:center;background-repeat:no-repeat;background-position:center bottom;}
#selected_text{background-image:url("${href("/search/img/current.png")}");}
.search_menu_item .light{padding:0;margin:0;border:0;margin-left:5px;min-width:50px;width:auto;height:31px;margin-top:0px;margin-left:0px;float:left;}
.search_menu_item .light .left{padding:0;margin:0;border:0;margin-top:9px;height:22px;float:left;width:auto;}
.search_menu_item .light .right{padding:0;margin:0;border:0;margin-top:9px;height:22px;float:left;width:auto}
.search_menu_item .light .middle_blue{padding:0;margin:0;border:0;margin-top:9px;height:22px;font-size:12px;line-height:18px;color:#FFF;background-image:url("${href("/search/img/bule_mid.png")}");background-repeat:repeat-x;width:auto;float:left;}
.search_menu_item .light .middle_green{padding:0;margin:0;border:0;margin-top:9px;height:22px;font-size:12px;line-height:18px;color:#FFF;background-image:url("${href("/search/img/green_mid.png")}");background-repeat:repeat-x;float:left;}
.current_select{padding:0;margin:0;border:0;background-image:url("${href("/search/img/current.png")}");background-repeat:no-repeat;margin-top:4px;height:5px;background-position: center top;}
	

#search_body { MARGIN: 10px;PADDING: 10px;FONT-FAMILY:"宋体";FONT-SIZE:12px;min-width:600px;}
#search_innerBody{padding:0;margin:0;border-style:solid;border-width:1px;border-color:#b8cfd7;border-top:0;min-width:600px;/*height:941px;overflow-y:scroll;overflow-x:hidden;*/}
.clear{clear:both;}
#search_frm{padding:0;margin:0;border:0;height:70px;width:100%;min-width:600px;overflow:hidden;}
#sfrm{padding:0;margin:0;border:0;}
#search_input{padding:0;margin:0;border:0;margin-top:24px;margin-left:30px;background-image:url("${href("/search/img/search.png")}");background-repeat:no-repeat;width: 290px;height:22px;float:left;padding-left:5px;float:left;line-height:22px;}
#search_btn{height:24px;width:80px;margin-top:24px;margin-left:20px;float:left;}
#search_time{padding:0;margin:0;border:0;float:right;font-size:12px;margin-top:24px;margin-right:5px;}
#dot{border-top:1px;border-top-style:dotted;border-color:#999;margin:0;}
#search_content{padding:0;margin:0;border:0;margin-top:20px;margin-left:30px;width:600px;height:auto;}
.search_content_item{padding:0;padding-bottom:24px;margin:0;border:0;width:600px;height:auto;}
.search_content_item a{padding:0;margin-top:24px;margin-bottom:0;border:0;font-family:"宋体";font-size:12px;font-weight:bold;text-decoration:underline;color:#4b98c4;}
.search_content_item p{padding:0;margin-top:11px;margin-bottom:0;line-height:150%;border:0;font-family:"宋体";font-size:12px;font-weight:normal;color:#999999;width:600px;height:auto;
}
#turnto_page{margin:0;padding:0;border:0;height:40px;overflow:hidden;}
#turntopage_left{margin:0;padding:0;border:0;background-image:url("${href("/search/img/light_down.png")}");background-repeat:no-repeat;height:40px;width:9px;float:left;margin-right:-3px;}
#turntopage_mid{margin:0;padding:0;border:0;border-style:solid;border-width:1px;border-top:0;border-left:0;border-right:0;border-color:#b8cfd7;text-align:center;line-height:39px;height:39px;margin-left:6px;margin-right:6px;float:left right;background-image:url("${href("/search/img/bottom_background.png")}");}
#turntopage_right{margin:0;padding:0;border:0;background-image:url("${href("/search/img/right_down.png")}");background-repeat:no-repeat;background-position:right;height:40px;width:9px;float:right;margin-left:-3px;}


</style>


	
	
<script src="${href("/js/jquery-1.4.2.min.js")}"></script>
<script type="text/javascript">


var left_hid = 0;  //左边隐藏菜单数目
var right_hid = 0;		//右边隐藏菜单数目
var length;			//当前菜单栏长度
var current_select; //当前被选中的菜单
function getRight_hid(){
	length = document.getElementById("search_menu_mid").children.length;
	var mid_top = document.getElementById("search_menu_mid").getBoundingClientRect().top;
	var top;
	for(var i = 0;i<length;i++){
		top = document.getElementById("search_menu_mid").children.item(i).getBoundingClientRect().top;
		if(top != mid_top && top != 0){
			right_hid = length-i;
			break;
		}
		right_hid = 0;
	}
	return right_hid;
}
function toleft(){
	if(left_hid==0)return;
	left_hid--;
	document.getElementById("search_menu_mid").children.item(left_hid).style.display="";
	
	
	//判断两按钮是否显示
	if(left_hid > 0)
		document.getElementById("search_menu_toleft").style.display="";
	else
		document.getElementById("search_menu_toleft").style.display="none";
	if(getRight_hid() > 0)
		document.getElementById("search_menu_toright").style.display="";
	else 
		document.getElementById("search_menu_toright").style.display="none";
}
function toright(){
	var right = getRight_hid();
	while(right == getRight_hid()){
		if(getRight_hid()==0)break;
		document.getElementById("search_menu_mid").children.item(left_hid).style.display="none";
		left_hid++;
	}
	
	if(left_hid > 0)
		document.getElementById("search_menu_toleft").style.display="";
	else
		document.getElementById("search_menu_toleft").style.display="none";
	if(getRight_hid() > 0)
		document.getElementById("search_menu_toright").style.display="";
	else 
		document.getElementById("search_menu_toright").style.display="none";

}


	
	function pop_win(urls) {
	//alert(urls);
	if (urls.length == 0)
		return;
	var start = 0;
	//alert(start);
	var end = urls.indexOf('\t');
	var len = end - start;
	while (end > 0 && end < urls.length) {
		//alert(urls.substring(start,end));
		window.open(urls.substring(start, end));
		start = end + 1;
		end = urls.indexOf('\t', start);
	}
}

	function checksubmit(formE){
		if(formE.queryString == ""){
			alert("请填写查询条件！");
			return false;
		}
		return true;
	}
	function changeCategory(value){
		//alert(value);
		$("#category").val(value);
		$("#sfrm").submit();
		return false;
	}
	
	function turnToPage(page){
		$("#pageTurn").val(page);
		$("#sfrm").submit();
		return false;
	}
	
	
	window.onload=function(){
		//document.getElementById("search_menu_toleft").style.display="none";
		//document.getElementById("search_menu_toright").style.display="none";
		document.getElementById("nextPage_menu").style.display="none";

		if(left_hid > 0){
			document.getElementById("search_menu_toleft").style.display="";
		}
		else
			document.getElementById("search_menu_toleft").style.display="none";
		if(getRight_hid() > 0)
			document.getElementById("search_menu_toright").style.display="";
		else 
			document.getElementById("search_menu_toright").style.display="none";
	}	

	

	function selected(e){
		if(current_select==null)current_select=document.getElementById("first_menu_item");
		current_select.children.item(0).style.backgroundImage="none";
		current_select.children.item(0).innerHTML=current_select.children.item(0).innerHTML.replace("strong","span").replace("STRONG","SPAN");
		current_select.children.item(1).children.item(0).innerHTML="<img src='${href("/search/img/bule_light.png")}'>";
		current_select.children.item(1).children.item(1).style.backgroundImage="url(${href("/search/img/bule_mid.png")})";
		current_select.children.item(1).children.item(2).innerHTML="<img src='${href("/search/img/bule_right.png")}'>";
	
		e.children.item(0).style.backgroundImage="url(${href("/search/img/current.png")})"
		e.children.item(0).innerHTML=e.children.item(0).innerHTML.replace("span","strong").replace("SPAN","STRONG");
		e.children.item(1).children.item(0).innerHTML="<img src='${href("/search/img/green_light.png")}'>";
		e.children.item(1).children.item(1).style.backgroundImage="url(${href("/search/img/green_mid.png")})";
		e.children.item(1).children.item(2).innerHTML="<img src='${href("/search/img/green_right.png")}'>";
		current_select = e;	
		//为IE bug   强制刷新div
		document.getElementById("search_menu").style.display="none";
		document.getElementById("search_menu").style.display="";	
	}
	
	
	
	
	
	
	window.onresize=function(){  
		//当窗口大小变化时   为IE bug   强制刷新div
		document.getElementById("search_menu").style.display="none";
		document.getElementById("search_menu").style.display="";
	}
	
	
	

</script>
	</head>

	<body>
	
	
	<#if ispad>
		
		<script type="text/javascript">
			window.onload=function(){
				document.getElementById("Ipad_pull").style.display="none";
				//document.getElementById("Ipad_sfrm").style.display="none";
				//document.getElementById("Ipad_pull_btn").style.display="none";
			}
		
		
			var ispull=false;
			function pull(){
				if(ispull==false){
					document.getElementById("Ipad_pull").style.display="";
					ispull=true;
				}
				else{
					document.getElementById("Ipad_pull").style.display="none";
					ispull=false;
				}
			}
		</script>
		
		
		<style type="text/css">
#Ipad_main{margin:0;border:0;padding:0;width:100%;height:100%;overflow:hidden;background-color:white;}		
#Ipad_navigation{margin:0;border:0;padding:0;height:50px;width:100%;background-image:url("${href("/search/img/Ipad_navigation.png")}");background-repeat:no-repeat;}
#Ipad_navigation_btn1{margin:0;border:0;padding:0;margin-left:7px;margin-top:7px;height:30px;width:87px;float:left;}
#Ipad_navigation_btn1 .left{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_back_light.png")}");background-repeat:no-repeat;float:left;width:18px;height:30px;}
#Ipad_navigation_btn1 .mid{margin:0;border:0;padding:0;padding-left:1px;background-image:url("${href("/search/img/Ipad_back_center.png")}");background-repeat:repeat-x;height:30px;width:63px;line-height:30px;float:left;font-size:14px;font-family:"΢ź";font-weight:bold;color:#ffffff;}
#Ipad_navigation_btn1 .right{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_back_right.png")}");background-repeat:no-repeat;background-position:right top;float:left;width:5px;height:30px;}
#Ipad_navigation_btn2{margin:0;border:0;padding:0;width:72px;height:30px;margin-top:7px;margin-left:400px;float:left;}
#Ipad_navigation_btn2 .left{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_btn_press_light.png")}");background-repeat:no-repeat;height:30px;width:3px;float:left;}
#Ipad_navigation_btn2 .mid{margin:0;border:0;padding:0;width:66px;height:30px;background-image:url("${href("/search/img/Ipad_btn_press_center.png")}");background-repeat:repeat-x;font-family:"΢ź";font-size:14px;color:#ffffff;font-weight:bold;text-align:center;float:left;line-height:30px;}
#Ipad_navigation_btn2 .right{margin:0;border:0;padding:0;height:30px;width:3px;background-image:url("${href("/search/img/Ipad_btn_press_right.png")}");background-repeat:no-repeat;background-position:right top;float:left;}




#Ipad_pull{margin:0;border:0;padding:0;width:262px;position:absolute;left:62px;top:114px;overflow:hidden;}
#Ipad_pull .top{margin:0;border:0;padding:0;width:262px;height:14px;overflow:hidden;}
#Ipad_pull .top .left_top{margin:0;border:0;padding:0;width:22px;height:14px;background-image:url("${href("/search/img/Ipad_light_up.png")}");background-repeat:no-repeat;float:left;}
#Ipad_pull .top .right_top{margin:0;border:0;padding:0;width:18px;height:14px;background-image:url("${href("/search/img/Ipad_right_up.png")}");background-position:right top;background-repeat:no-repeat;float:left;}
#Ipad_pull .top .mid_top{margin:0;border:0;padding:0;margin-top:2px;height:12px;width:222px;background-image:url("${href("/search/img/Ipad_mid_up.png")}");background-repeat:repeat-x;float:left;}

#Ipad_pull .first_item{margin:0;padding:0;font-family:"";font-size:16px;color:#1e95cb;width:252px;height:30px;padding-top:5px;border-width:2px;border-style:solid;border-top:0;border-color:#b3ddef;margin-left:3px;background-image:url("${href("/search/img/Ipad_point.png")}");}
#Ipad_pull .item{margin:0;padding:0;font-family:"";font-size:16px;color:#1e95cb;width:252px;height:30px;border-width:2px;border-style:solid;border-top:0;border-color:#b3ddef;padding-top:14px;margin-left:3px;background-image:url("${href("/search/img/Ipad_point.png")}");}
#Ipad_pull .last_item{margin:0;padding:0;font-family:"";font-size:16px;color:#1e95cb;width:252px;height:20px;border-width:2px;border-style:solid;border-top:0;border-bottom:0px;border-color:#b3ddef;padding-top:14px;margin-left:3px;background-image:url("${href("/search/img/Ipad_point.png")}");}
#Ipad_pull .bottom{margin:0;border:0;padding:0;width:262px;height:14px;overflow:hidden;}
#Ipad_pull .bottom .left_bottom{margin:0;border:0;padding:0;width:20px;height:14px;background-image:url("${href("/search/img/Ipad_light_down.png")}");background-repeat:no-repeat;background-position:left bottom;float:left;}
#Ipad_pull .bottom .mid_bottom{margin:0;border:0;padding:0;width:222px;height:14px;background-image:url("${href("/search/img/Ipad_mid_down.png")}");background-repeat:repeat-x;background-position:bottom;float:left;}
#Ipad_pull .bottom .right_bottom{margin:0;border:0;padding:0;width:20px;height:14px;background-image:url("${href("/search/img/Ipad_right_down.png")}");background-repeat:no-repeat;background-position:right bottom;float:left;}



#Ipad_search_frm{margin:0;padding:0;border:0;height:72px;overflow:hidden;clear:right;}
#Ipad_search_input{margin:0;border:0;padding:0;margin-top:21px;margin-left:43px;width:288px;height:30px;overflow:hidden;float:left;}
#Ipad_search_input .left{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_search_light.png")}");background-repeat:no-repeat;width:15px;height:30px;float:left;}
#Ipad_search_input .mid{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_search_mid.png")}");background-repeat:repeat-x;width:258px;height:30px;float:left;}
#Ipad_search_input .right{margin:0;border:0;padding:0;background-image:url("${href("/search/img/Ipad_search_right.png")}");background-repeat:no-repeat;width:15px;height:30px;float:left;}

#Ipad_mic{margin:0;border:0;padding:0;margin-top:21px;width:50px;height:30px;float:left;}
#Ipad_search_frm span{margin:0;border:0;padding:0;font-size:12px;margin-top:24px;margin-right:5px;line-height:30px;float:right;}

#Ipad_content{margin:0;border:0;padding:0;width:700px;margin-top:38px;margin-left:43px;}
#Ipad_content .item{margin:0;border:0;padding:0;margin-bottom:32px;width:700px;}
#Ipad_content .item .title{margin:0;border:0;padding:0;font-size:18px;font-family:"黑体";color:#367ca4;width:700px;}
#Ipad_content .item .img_link{margin:0;border:0;padding:0;margin-top:10px;font-size:14px;font-family:"微软雅黑";font-weight:bold;color:#4ea55c;width:700px;}
#Ipad_content .item p{margin:0;border:0;padding:0;margin-top:11px;font-size:16px;font-family:"黑体";color:#999999;width:700px;}
#Ipad_search_input .mid #sfrm{margin:0;border:0;padding:0;width:258px;height:30px;float:left;}
#Ipad_input {margin 0;border:0;padding:0; float:left;width:258px;height:28px;margin-top:1px;}	
#Ipad_turntopage{margin:0;padding:0;border-style:solid;border-width:1px;border-left:0;border-right:0;border-color:#b8cfd7;text-align:center;line-height:40px;height:40px;}
		</style>
		
		
	<div id="Ipad_main">
		
		
		<div class="clear"></div>
		
		<div id="Ipad_search_frm">
					
					
			<script>
            	function sub(){
            		document.getElementById("search_input").setAttribute("value",document.getElementById("Ipad_search_text").innerHTML);
					Ipad_sfrm.submit();
            	}
            </script>	
					
				<div id="Ipad_search_input">
					<div class="left">
					</div>
					<div class="mid">						
						<form name=search id="sfrm" action="doSearch" method="post">
                			<input type="hidden" name="currentPage" id="currentPage" value="${currentPage}">
							<input type="hidden" name="pageTurn" id="pageTurn" value="">
							<input type="hidden" name="pageName" id="pageName"/>
							<input type="hidden" name="pageModule" id="pageModule"/>
                			<input id="Ipad_input" type="text" name="queryString" value="${queryString}">
    					</form>
    					<script>
							$("#pageName")[0].value = CUR_PAGE_NAME;$("#pageModule")[0].value = CUR_PPAGE_MODULE;
						</script>
					</div>
					<div class="right">
					</div>
					
				</div>
				<div id="Ipad_mic"><a href="/voice/voiceSch"><img src="${href('/search/img/Ipad_blue_mic.png')}"/></a>
				</div>
					<#if queryString!="">
   			        			<span style="display:''">搜索 <font color="#FF0000">${queryString}</font>
							&nbsp; 共发现 <B>${numFound}</B> 个结果[用时${queryTime}秒]&nbsp;&nbsp; </span>
							<#else>
								<span style="display:none">搜索 <font color="#FF0000">${queryString}</font>
							&nbsp; 共发现 <B>${numFound}</B> 个结果[用时${queryTime}秒]&nbsp;&nbsp; </span>
					</#if>
				<div class="clear"></div>
		</div>
		
		<script>
 			function vs(str){
  				document.location.href = "/portal/pt/link/search?qryString="+ encodeURIComponent(encodeURIComponent(str)) +"&pageName="+CUR_PAGE_NAME+"&pageModule="+CUR_PPAGE_MODULE;
 			}
 		</script>
		
		
		<div id=dot></div>
		
		<div></div>   <!--IE BUG:  没有它IE下相邻元素margin-top失效-->

		<div id="Ipad_content">
		
		
		
			<#if indexVOList?exists>
				<#list indexVOList as indexVO>
					<div class="item">
						<#if indexVO.url ?exists>
						
							<div class="title">
								<A class="result_url" onclick="document.location.href='${indexVO.url}' "  >${indexVO.title}</A>
							</div>
						<#else>
							<div class="title">
								${indexVO.title}
							</div>
						</#if>
							<div class="img_link">
								${indexVO.url?default("")}
							</div>
							<p>${indexVO.simplebriefInfo}</p>
						</div>
				</#list>
			</#if>

        </div>
        
        
        
        <#if (totalPage > 1) >
              	<div id="Ipad_turntopage">
              			<#if (currentPage>1) >
							<a href="#" onclick="return turnToPage('front')">前一页</a>&nbsp;&nbsp;
						</#if>
			
						<#assign begin = currentPage - 5 />
						<#if (begin < 1) >
							<#assign begin = 1 />
						</#if>
						<#assign end = currentPage + 5 />
						<#if (end > totalPage) >
							<#assign end = totalPage />
						</#if>
					
						
						<#if (begin <= end) >
							<#list begin..end as page>
								<#if page == currentPage>
									<b>${page}</b>&nbsp;
								<#else>	
									<a href="#" onclick="return turnToPage('${page}')">${page}</a>&nbsp;&nbsp;
								</#if>
							</#list>
						</#if>
						
						<#if (currentPage < totalPage) >
							<a href="#" onclick="return turnToPage('next')">下一页</a>
						</#if>
				</div>
		</#if>
        
        
        
	</div>
	
	<#else>
	
	
	
	
	
		<TABLE class=result_page cellSpacing=0 cellPadding=0 width="985"
			border=0 height="58">
			<TBODY>
				<TR>
					<TD width=135 height=58 align="center" vAlign=middle>
						<a href="#"><img
								src="${href("/search/img/oalogo.png")}" width="200"
								height="30" align="left" border=0> </a>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		


	<div id="search_body">
        	<div id="search_menu">
        	        
                <div id="search_menu_leftcorner">
                </div>
               <div id="search_menu_rightcorner">
                </div>
            
             	<div id="search_menu_toleft" onClick="toleft()">
                    <div class="left">
                    </div>
                    <div class="right">
                    </div> 
                </div>
                
                 <div id="nextPage_menu">
                	<div class="content">
                    </div>
                </div>
                
                <div id="search_menu_toright" onClick="toright()">          	
                 	<div class="left">
                    </div>
                    <div class="right">
                    </div>
                    <div class="line">
                 	</div>   
                </div>
               
                
                <div id="search_menu_mid">
                    <div class="search_menu_item" id="first_menu_item" onclick=selected(this)>
                		<div class="text" id="selected_text"><strong>所有</strong>
                        </div>
                		<div class="light">
                    		<div class="left"><img src="${href("/search/img/green_light.png")}">
                       		</div>
							<div class="middle_green">${numFound}
                        	</div>
                        	<div class="right"><img src="${href("/search/img/green_right.png")}">
                        	</div>
                   		</div>
           			 </div>
            		<div class="search_menu_item" onclick=selected(this)>
                		<div class="text"><span>BI报表</span>
                        </div>
                		<div class="light">
                    		<div class="left"><img src="${href("/search/img/bule_light.png")}">
                       		</div>
							<div class="middle_blue">${numFound}
                        	</div>
                        	<div class="right"><img src="${href("/search/img/bule_right.png")}">
                        	</div>
                    	</div>
            		</div>      		
             	</div>        
            </div>
            
            
            
            
            <div id="search_innerBody">
            	<div id="search_frm">
                	<form name=search id="sfrm" action="doSearch" method="post">
                			<input type="hidden" name="currentPage" id="currentPage" value="${currentPage}">
							<input type="hidden" name="pageTurn" id="pageTurn" value="">
                			<input id="search_input" type="text" name="queryString" value="${queryString}">
   			        		<input id="search_btn"  type="image" name="submit" src="${href("/search/img/btn.png")}">
   			        		<#if queryString!="">
   			        			<span id="search_time" style="display:''">搜索 <font color="#FF0000">${queryString}</font>
							&nbsp; 共发现 <B>${numFound}</B> 个结果[用时${queryTime}秒]&nbsp;&nbsp; </span>
							<#else>
								<span id="search_time" style="display:none">搜索 <font color="#FF0000">${queryString}</font>
							&nbsp; 共发现 <B>${numFound}</B> 个结果[用时${queryTime}秒]&nbsp;&nbsp; </span>
							</#if>
    				</form>
                </div>
                <div id="dot">
                </div>
                <div id="search_content">
    				
        	<#if indexVOList?exists>
				<#list indexVOList as indexVO>
					<div class="search_content_item">
						<#if indexVO.url ?exists>
							<#if ispad>
								<A class="result_url" onclick="document.location.href='${indexVO.url}' "  >${indexVO.title}</A>
							<#else>
								<A class="result_url" href="${indexVO.url}" target=_blank rel=nofollow>${indexVO.title}</A>
							</#if>
							<#else>
								${indexVO.title}
							</#if>
						
							<p>${indexVO.simplebriefInfo}</p>
						</div>
				</#list>
			</#if>   		
                </div>
            	
            </div> 
            <div class="clear"></div>
            <#if (totalPage > 1) >
              	<div id="turnto_page">
              		<div id="turntopage_left">
                	</div>
                	<div id="turntopage_right">
                	</div>
                	<div id="turntopage_mid">
                	
              		
              				<#if (currentPage>1) >
								<a href="#" onclick="return turnToPage('front')">前一页</a>&nbsp;&nbsp;
							</#if>
			
						<#assign begin = currentPage - 5 />
						<#if (begin < 1) >
							<#assign begin = 1 />
						</#if>
						<#assign end = currentPage + 5 />
						<#if (end > totalPage) >
							<#assign end = totalPage />
						</#if>
					
						
						<#if (begin <= end) >
							<#list begin..end as page>
								<#if page == currentPage>
									<b>${page}</b>&nbsp;
								<#else>	
									<a href="#" onclick="return turnToPage('${page}')">${page}</a>&nbsp;&nbsp;
								</#if>
							</#list>
						</#if>
						
						<#if (currentPage < totalPage) >
							<a href="#" onclick="return turnToPage('next')">下一页</a>
						</#if>
					
					</div>
            	</div>
		</#if>
          
          
            
        </div>
    </#if>    
	</body>
</html>
