// JavaScript Document
//Portlet链接地址前缀
var link_Pre = "P_U_R_L_";
var tipShowFlag = false;
var NCPortalSupportPortletMode = ["view","edit","help"]
var NCPortalSupportPortletModeName = {view:"查看",edit:"编辑",help:"帮助"}
var currentTipMeta = "";
$(function() {
	//处理Portlet链接地址
	disposeURL ();
	
	//初始化拖动
	initDragable();
	
	//初始化定时刷新Portlet
	initRefreshPortlet();
	
	//初始化Portlet容器工具条
	initTips();
	//解决IE多线程问题
	IECorrect();
});
/**Portlet刷新关系**/
var refresh_circle = {};
/**
 * 初始化Portlet容器工具条
 */
function initTips(){
	if(CUR_PPAGE_READONLY == "false"){
		//初始化双击隐藏事件
		$("[tp=pBody]").mouseover(function(){
			//var _pt =  $(this).parents("[tp=portlet]");
			if(tipShowFlag && currentTipMeta != "")
			getContainer("#"+currentTipMeta).hideTips();
		});
		//初始化工具条显示鼠标事件
		$("[tp='portlet']").find("[tp=pHead]").mouseover(function(){
			var _pt =  $(this).parents("[tp=portlet]");
		    if(!tipShowFlag || (currentTipMeta != "" && currentTipMeta != _pt.attr("id")))
			   getContainer(_pt).showTips();
		});
		//右边栏不触发工具条
		$("[tp='portlet']").find("[tp='pPart']").mouseover(function(){
			return false;
		});
	}
}


/**
 * 初始化定时刷新Portlet
 */
function initRefreshPortlet(){
	setInterval(checkPortletRefresh, 1000);
}

/**
 * 处理定时刷新Portlet
 */
function checkPortletRefresh(){
	for( xa in refresh_circle){
		 var ctn = getContainer("#"+xa);
		 if(!ctn.ModifiedSince || ctn.ModifiedSince < (new Date()).getTime()-refresh_circle[xa]*1000){
		 	ctn.doView();
		 	ctn.ModifiedSince = (new Date()).getTime();
		 }
	}
}


/**
初始化Iframe中的Portlet环境
**/
function initIframeEnv(){
		//为Iframe注册getContainer方法
	var container = $(this).parents("[tp='portlet']");
	//初始化Container  顺便取其ID
	var containerId = getContainer(container).id;
	
	try {
		//注册ContainerID
		$(this).get(0).contentWindow.document._pt_container_id=containerId;
		//注册getContainer()方法
		$(this).get(0).contentWindow.document.getContainer=getIframeContainer;
		$(this).get(0).contentWindow.document._pt_frame_id = $(this).get(0).id;
		if($(this).attr("flowMode") == "1"){
 			setTimeout("initFrameMiniHeight('#"+ $(this).get(0).id +"')",300);
		}
		
	} catch (e) {
		// 跨域 抛弃
	}
}

function initFrameMiniHeight(frameId){
	
	var hScorll = window.document.body.offsetHeight -	window.document.body.children[0].scrollHeight;
				if(hScorll > 0){
					var minHeight = $(frameId).height() + hScorll;
					$(frameId).attr("minHeight", minHeight);
					$(frameId).attr("fullHeight", minHeight);
					$(frameId).height(minHeight);
				}
				adjustIFramesHeightOnLoad($(frameId).get(0)); 				
}

function initParentIframeHeight(){
	try {
		 
		var frame =	parent.getParentsContainer(this._pt_frame_id);
		$(frame).height(window.document.body.scrollHeight);
	} catch (e) {
		 
	}
}

/**
初始化Iframe的高度、宽度
**/
function initIframeArea(iframeId, assignHeight){
	var thisFrame=$("#" + iframeId);
	if (assignHeight == -1) {
		thisFrame.attr("flowMode","1");
		
	} else if (assignHeight == 0) {
		// 适应容器
		$(function() {
			var footObj = $("[tp='foot']");
			var container = thisFrame.parents("[tp='portlet']");
			// 页面空隙
			var nheight = $(window).height() - footObj.offset().top
					- footObj.outerHeight(true);
			nheight = nheight + getContainer(container).getOuter().height()	- container.outerHeight(true);
			// 页面底部仍有空隙
			if (nheight <= 0) {
				nheight = container.innerHeight() - thisFrame.height() - 31;
				if(nheight <= 0){
					// 算出Layout底部与Foot顶的距离
					nheight = getContainer(container).getOuter().height()
						- container.outerHeight(true) ;
				}
				thisFrame.height(nheight + thisFrame.height() );
			} else if (nheight > 0) {
				// 如果距离过小 可能是浏览器多线程加载造成,要检查Layout底与Foot顶距离
				if (nheight < 5) {
					var _t_height = getContainer(container).getOuter().height()
							- container.outerHeight(true) ;
					if (_t_height > 0) {
						thisFrame.height(_t_height + thisFrame.height() );
					}
				} else {
					thisFrame.height(nheight + thisFrame.height() );
					// var mheight= getContainer(container).getOuter().height()
					// - container.outerHeight(true) -5;
					// if(mheight > nheight)
					// thisFrame.attr("height",mheight+thisFrame.height());
				}
			}
		})
	} else {
		//自定义
		thisFrame.attr("height", assignHeight);
	}
}

/**
 * 重画iframe
 * @param {} iframeId
 */
function resizeIframe(iframeId,assignHeight){
	var thisFrame=$("#" + iframeId);
	if(assignHeight == -1){
		
	}else if(assignHeight == 0){
		var top = thisFrame.offset().top;
		if(top < 0){
			top = 0;
		}
		var h = $(window).height() - top - 15;
		if(h > 0){
			thisFrame.height(h);
		}
	}else if(assignHeight > 0){
		thisFrame.height(assignHeight); 
	}
}

/**
获得当前Portlet
仅限Iframe内使用
**/
function getParentsContainer(containerId){
	return document.getElementById(containerId);
}

 
/**
Iframe中获得Container
**/
function getIframeContainer(){
	try {
		return	parent.getParentsContainer(this._pt_container_id);
	} catch (e) {
		return	window.getParentsContainer(this._pt_container_id);
	}
}


/**
解决IE多线程加载问题
**/
function IECorrect(){
  //if($.browser.msie){
	  $("iframe").each(function(index,ele){
			if($(ele).attr("src")==undefined||$(ele).attr("src")==""){
				$(ele).attr("src",$(ele).attr("scr"));	
			}
	  });
  //}
}
/**
*处理Portlet链接及表单
**/
function  disposeURL (){
	//处理A标签
 	var hrefObjs=[];
	try {
		hrefObjs=$("a[href*="+link_Pre+"]");
	} catch (e) {
		//跨域.抛弃异常
	}
	burnURL(hrefObjs);

//处理表单
	var formObjs=[];
 	try {
 		formObjs=    $("form[action*="+link_Pre+"]");
 	} catch (e) {
		//跨域.抛弃异常
	}
	burnFORM(formObjs);
	disposeFrameURL();
}
/**
*处理Iframe中的Portlet链接及表单
**/
function disposeFrameURL(){
	var fms = window.frames;
	if(fms == null || fms.length <1)
		return;
	try {
		for(var fi = 0; fi < fms.length; fi++){
			var fm = fms[fi];
			if(fm.name =="portlet"){
				$(fm).ready(function (){
					try{
						burnURL($(fm.contentWindow.document).contents().find("a[href*="+link_Pre+"]"));
						burnFORM($(fm.contentWindow.document).contents().find("form[action*="+link_Pre+"]"));
					} catch (e) {
						//跨域.抛弃异常
					}
				})
			}

		}
	} catch (e) {
		// 跨域 抛弃
	}
}


/**
 * 将Portlet链接地址替换为Portlet请求
 * @param hrefObjs 要处理的链接
 */
function burnURL(hrefObjs){
	$.each(hrefObjs,function(i,target){
		var _t_url=getTrueUrl ($(target).attr("href"));
		$(target).click(function(){
			 openPortlet(_t_url) ;
		})
		//去掉Href属性
		$(target).attr("href","javascript:void(0)");
	})
}

/**
 * 将PortletForm替换为Portlet请求
 * @param formObjs 要处理的Form
 */
function burnFORM(formObjs){
	$.each(formObjs,function(i,target){
		var _t_url=getTrueUrl ($(target).attr("action"));
		$(target).submit(function(){
			openPortlet(_t_url,$(target).serializeArray());
			//直接返回
			return false;
		})
		//去掉action以净化状态栏
		$(target).attr("action","");
	})
}


/**
得到真实的PortletURL
@param URL Tag生成的伪URL
**/
function getTrueUrl(fakeurl){
	var start=fakeurl.indexOf(link_Pre)+8;
	var _t_url=fakeurl.substr(start);
	return _t_url;
}
/**
处理Portlet请求
使用AJAX方式
@url 请求地址
@fdata 序列化的表单(可选参数)
**/
function openPortlet(url,fdata,fn){
$.ajax({
	type: "GET",
	url: url,
	data:fdata,
	cache:false,
    success: function(data){
	    if(data && typeof  data == "object"){
			if(data.err){
				alert(data.err);
				eval(data.exec);
			}else{
				$(data).each(function (index, el) {
					var _ctn = getContainer("#"+el.name);
					var protocol = el[RESPONSE_PROTOCOL];
					_ctn.setCurrentMode(el.mode);
					if(protocol == RESPONSE_MODE_SCRIPT){
						eval(el.content);				
					}else{
						_ctn.setContent(el.content);
					}
					if(el.title != ""){
						_ctn.setTitle(el.title);
					}
				 });
				//先加载
				disposeURL ();
				if(fn){
					 if(typeof fn == "string"){
						eval(fn);
					 }else if(typeof fn == "function"){
					 	fn.call(this);
					 }
				}
			}
		}
   }
}); 
}
/**
初始化拖放
**/
function initDragable(){
	//仅在页面非只读状态下初始化拖动
	if(CUR_PPAGE_READONLY != "true"){
		$.baseball({
			accepter:$("[tp='layout']").filter(function(index) {return $("[tp='layout']", this).length == 0;}).add($("[tp='portlet']").siblings("[tp='layout']").parent("[tp='layout']")),
			target:"[tp='portlet']",	
			handle:"[tp='pHead']"
			});
		}
	}

var $tabs;
var $dialog;

//系统编码与portlet对应关系
var portlet2SystemStore={};
//系统编码与页签对应关系
var portlet2TabStore={};

//Tab页签数量
var auth_tab_counter = 1;

/**
*弹出验证对话框
@param systemName 系统名称
@param systemCode 系统编号
@param portletId Portlet窗口ID
@param shareLevel 共享级别
*/
function showAuthDialog(systemName,systemCode,portletId,shareLevel){
	//未初始化
	if(!$tabs||!$dialog){
		initAuthDialog();
	}
	//如果在隐藏状态则弹出
	$("#authDialog" ).dialog();
	var portletArr=portlet2SystemStore[systemCode+shareLevel];
	//没有系统编码的记录
	if(!portletArr){
		$tabs.tabs( "add", "#tabs-"+auth_tab_counter, systemName);
		$("div#tabs-"+auth_tab_counter+".ui-tabs-panel").append('<iframe frameborder="0" width="100%" height="200" src="'+ ROOT_PATH +'/core/uimeta.jsp?pageId=credential&model=nc.portal.sso.pagemodel.CredentialEditPageModel&wmode=dialog&portletId='+portletId+'&systemCode='+systemCode+'&sharelevel='+shareLevel+'"> </iframe>');
		//记录systemCode+sharelevel与tab页签的关系
		portlet2TabStore[systemCode+shareLevel]=auth_tab_counter;
		//增加tab页签计数
		auth_tab_counter++;
		portlet2SystemStore[systemCode+shareLevel]=new Array(portletId);
	}else{
		//没有Portlet的记录
		if(jQuery.inArray(portletId, portletArr) == -1){
			portletArr.push(portletId);
		}
	}
}
/**
*初始化对话框
**/
function initAuthDialog(){
 	$(document.body).append('<div id="authDialog" title="帐户关联"><div id="authTabs"><ul></ul></div></div>');
	$tabs = $( "#authTabs").tabs({
			tabTemplate: "<li><a href='#{href}'>#{label}</a> </li>"
		});
		$dialog = $( "#authDialog" ).dialog({
			minWidth: 480 ,
			maxWidth: 480 ,
			minHeight: 320 ,
			maxHeight: 320 ,
			buttons: {
				完成: function() {
					$( this ).dialog( "close" );
				},
				取消: function() {
					$( this ).dialog( "close" );
				}
			},close: function(event, ui) {
				//关闭时销毁iFrame
				$( "#authDialog" ).find("iframe").attr("src","");
			}
		});
}
/**
 * SSO验证成功回调函数
 */
function authCorrect(portletWindId,systemcode,sharelevel){
	if(!$.isEmptyObject(portlet2SystemStore)){
		var pwindArr=portlet2SystemStore[systemcode+sharelevel];
		var exec="removeAuthForm('"+systemcode+"','"+sharelevel+"');";
		if(pwindArr!=null){
			for(var i = 0;i<pwindArr.length;i++){
				getContainer("#"+pwindArr[i]).doView(exec);
			}
		}
	}else{
			getContainer("#"+portletWindId).doView();
	}
}
/**
 * 移除验证表单
 */
function removeAuthForm(systemcode,sharelevel){
	 var tabIndex=portlet2TabStore[systemcode+sharelevel];
	 var tabId="tabs-"+tabIndex;
	 var tabItem=$("#authTabs").children("div");
	 //找出tabindex 移除
	 for(var i = 0;i<tabItem.length;i++){
	 	if(tabItem[i].id==tabId){
	 		$tabs.tabs( "remove",i);
	 	}
	 }
	 //最后一个 关闭对话框
	 if($("#authTabs").children("div").length==0){
	 	$dialog.dialog( "close" );
	 }
}
/**
* 隐藏
**/
function hideFrame(){
	$outer =  $("[tp=framelayout]" );
	$outer.show();
	if( $("#coverFrame_system" ).length > 0){
		$( "#coverFrame_system" ).find("iframe")[0].src="";
		$("#coverFrame_system").hide();
	}
}
/**
 * 打开节点
 * @param event 事件
 */
function openFrame(title,url){
	//得到框架布局
	$outer =  $("[tp=framelayout]" );
	if($outer.size()<=0){
		alert("未配置显示框架!请在页面中选择一个Layout并设置样式为框架布局!");
		return false;
	}
	$outer.hide();
	if( $("#coverFrame_system" ).length == 0){
		var frameHeight = $outer.height();
		$outer.parent().append('<div id="coverFrame_system"><iframe frameborder="0" width="100%" height="'+frameHeight+'"></iframe><div>');
	}
	$( "#coverFrame_system" ).find("[tp=pTitle]").html(title);
	var fm = $( "#coverFrame_system_iframe" );
	fm[0].src=url;
	$("#coverFrame_system").show();
	initIframeArea('coverFrame_system_iframe',0);
}
/**
 * 打开页面
 */
function showFrameDailog(title,w,h,url,closeable){
	if( $("#tsFrame_system" ).length == 0)
	$(document.body).append('<div id="tsFrame_system"><iframe scrolling="no" marginheight="0" marginwidth="0" style="border:0px; padding:0;" frameborder="0" width="100%" src=""></iframe><div>'); 
	$( "#tsFrame_system" ).dialog({
			title:title,
			width: w,
			height: h,
			modal: true,
			beforeclose: function(event, ui) {
				/**设置对话框是否可以手工关闭**/
				if(closeable == "undefined")
					return true;
				return closeable;
			},close: function(event, ui) {
				//关闭时销毁iFrame
				$( "#tsFrame_system" ).find("iframe")[0].src="";
			}
			
		});
	var fm = $( "#tsFrame_system" ).find("iframe");
	fm.height( $("#tsFrame_system" ).height() - 5);
	fm[0].src=url;
}
/**
 * 关闭当前的弹出对话框
 */
function closeFrameDailog(){
	$( "#tsFrame_system" ).dialog("close");
}
/**
 * 停止事件传递
 */
function stopDefault(e) {
	//prevetnDefault()是DOM事件的核心方法,用于阻止事件的默认行为
	if (e.preventDefault)
		e.preventDefault();
	else {
		//returnValue是IE中事件的默认属性,将其设置为false以取消事件的默认动做
		e.returnValue = false;
	}
};
/**
 * 调整Iframe高度
 */
function adjustIFramesHeightOnLoad(iframe) {
	if(iframe.contentWindow.window.document.body.children && iframe.contentWindow.window.document.body.children.length > 0){
		var eles = iframe.contentWindow.window.document.getElementsByName("flowv");
		var iframeHeight = iframe.contentWindow.window.document.body.children[0].scrollHeight;;
		if(eles != null){
			var maxHeight = getMaxEleHeight(eles);
			iframeHeight = iframeHeight < maxHeight ? maxHeight : iframeHeight;
		}
		if( $(iframe).attr("minHeight") < iframeHeight){
	       $(iframe).height(iframeHeight);
	    }
	}
 };
 
 function getMaxEleHeight(eles) {
 	var height = 0;
 	for(var i = 0; i < eles.length; i ++){
 		var tmpHeight = eles[i].offsetHeight;
 		if(tmpHeight > height)
 			height = tmpHeight;
 	}
 	return height;
 }
 /**
  * 打开一个公共的Portlet
  */
function openPublicPortlet(module,name,w,h,category){
	var frameName = module + "_" + name + "_" + "PF";
	var mockPortletName = CUR_PPAGE_MODULE + "_" + CUR_PAGE_NAME + "_" + module + '_' + name;
	if($("#" + frameName).length == 0)
		$(document.body).append('<div id="'+frameName+'"><div>'); 
	$("#" + frameName).append('<div tp="portlet" setTitle="setPublicPortletTitle"  pid="P13466617358" id="'+ mockPortletName +'"><div class="content"  tp="pBody"></div></div>');
	
	$("#" + frameName).dialog({
			title:"Loading...",
			width: w,
			height: h,
			modal: true
	});
    var param = getActionParam(mockPortletName);
    param.if_src_type = "src";
    param.category = category;
    param.h = h;
	var actURL = ROOT_PATH + ACTION_URL;
	openPortlet(actURL,param);
};
/**
 * 设置公共Portlet的标题
 */
function setPublicPortletTitle(title){
	var idCol = this.id.split("_");
	var frameName = idCol[2] + "_" + idCol[3] + "_" + "PF";
	$("#" + frameName).dialog({title:title});
};
/**
 * 增加事件
 */
function addEventHandler(oTarget, sEventType, fnHandler) {
	if (oTarget.addEventListener) {  // 用于支持DOM的浏览器
		oTarget.addEventListener(sEventType, fnHandler, true);
	} else if (oTarget.attachEvent) {  // 用于IE浏览器
		oTarget.attachEvent("on" + sEventType, fnHandler);
	} else {  // 用于其它浏览器
		oTarget["on" + sEventType] = fnHandler;
	}
}