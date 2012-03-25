/**
默认的创建Part方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createPart(title,href).click(function (){}).mouserover(function(){})...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreatePart(title, href) {
    var  _pt_tmp_part = $('<li><a href="' + href + '">' + title + '</a></li>');
	_pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
    return  _pt_tmp_part;
}

/**
默认的创建Part分割符方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createSep(title,href).css("color","red");...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreateSep() {
//默认分隔符
var _pt_segment="|";
//自定义分隔符
if(arguments.length==1){
	_pt_segment=arguments[0];
}
var _pt_tmp_part = $('<li><a>' + _pt_segment + '</a></li>');
 
   _pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
 
    return _pt_tmp_part;
}

/**
默认的创建空Part方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createBlankPart(title,href).html("<span>some html here</span>")...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreateBlankPart() {
var _pt_tmp_part = $('<li></li>');
   _pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
    return _pt_tmp_part;
}

/**
默认的设置模式的方法
返回创建元素的Jquery对象,可链式调用
@param mode 模式
@return Mode的Jquery对象
**/
function _pt_defaultSetMode(mode){
	
}
/**
默认的设置标题的方法
返回创建元素的Jquery对象,可链式调用
@param title 标题
@return Mode的Jquery对象
**/
function _pt_defaultSetTitle(title){
		return $(this).find("[tp='pTitle']").html(title);
	}

/**
默认的设置内容的方法
返回创建元素的Jquery对象,可链式调用
@param content 内容
@return content的Jquery对象
**/
function _pt_defaultSetContent(content){
	return $(this).find("[tp='pBody']").html(content);
	}
/**
默认的设置不显示外框方法
@return Portlet容器

**/
function _pt_defaultSetExposed(){
	var jqObj=$(this);
	//防止多次操作导致原样式丢失
	if(this.clsName==undefined){
		this.clsName=jqObj.attr("class");
	}
	jqObj.attr("class","margeach");
	var phd = jqObj.find("[tp=pHead]");
	phd.children().hide();
	phd.attr("oheight",phd.height());
	phd.height(3);
	jqObj.find("[tp=intine]").parent().prevAll().hide();
	jqObj.find("[tp=intine]").parent().nextAll().hide();
	return this;
	}
/**
默认的恢复显示外框方法
@return Portlet容器

**/
function _pt_defaultSetUnExposed(){
	var jqObj=$(this);
	jqObj.attr("class",this.clsName);
	var phd = jqObj.find("[tp=pHead]");
	phd.children().show();
	phd.height(phd.attr("oheight"));
	jqObj.find("[tp=intine]").parent().prevAll().show();
	jqObj.find("[tp=intine]").parent().nextAll().show();
	return jqObj;
	}
/**
 * 默认的显示工具条方法
 */
function _pt_defaultShowTips(){
	var jqObj=$(this);
	//工具条
	var tp = $("#tipspanel");
	tp.attr("portletid",jqObj.attr("id"));
	var tipLeft  = jqObj.position().left-tp.width()+jqObj.width();
	var tipTop = 0;
	var headHeight = jqObj.find("[tp=pHead]").height();
	if(headHeight > tp.height()){
		tipTop = jqObj.position().top + (headHeight - tp.height())/2;
	}else{
		tipTop = jqObj.position().top - tp.height() + headHeight;
	}
	tp.css({left:tipLeft,top:tipTop});
	var _modes = tp.find("[tp=pmodes]");
	//先清除其中的所有子节点
	_modes.empty();
	//重建子节点
	var sms = this.getSupportModes();
	for(var sm_idx in sms){
		var sm = sms[sm_idx].toLowerCase();
		//如果是NCPortal支持的模式
		if(jQuery.inArray(sm, NCPortalSupportPortletMode) != -1 && this.getCurrentMode() != sm){
			var funName = "_toolbar_" + sm + "()";
			var showName = NCPortalSupportPortletModeName[sm];
			_modes.append(" <a href='javascript:" + funName + "'>"+ showName +"</a> ");
		}
	}
	//非只读模式下
	if(CUR_PPAGE_READONLY != "true"){
		_modes.append(" <a href='javascript:deletePortlet();'>删除</a>");
	}
	
	tp.show();
	tp.draggable();
	tipShowFlag = true;
	currentTipMeta = jqObj.attr("id");
}
/**
 * 默认的显示工具条隐藏方法
 */
function _pt_defaultHideTips(){
	var jqObj=$(this);
	//工具条
	var tp = $("#tipspanel");
	tp.hide();
//	tp.hide(2000);
	tipShowFlag = false;
	currentTipMeta = "";
}

/**
 * 工具条查看按钮事件监听方法
 */
function _toolbar_view(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doView(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
		
	}
	
}

/**
 * 工具条编辑按钮事件监听方法
 */
function _toolbar_edit(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doEdit(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
	}
}
/**
 * 工具条帮助按钮事件监听方法
 */
function _toolbar_help(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doHelp(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
	}
}
/**
 * 改变Portlet皮肤
 */
function changePortletTheme(){
	var ptid = $("#tipspanel").attr("portletid");
	var ctr = $("#"+ptid);
	var pid = ctr.attr("pid");
	var param= getActionParam(ptid);
	param["pid"] = pid;
	var url = "/portal/pt/setting/changePortletTheme?portlet=" + ptid + "&pid=" + pid
	
	showFrameDailog("设置Portlet皮肤",480,320,url,true);
}

/**
 * 删除Portlet
 */
function deletePortlet(){
	var ptid = $("#tipspanel").attr("portletid");
	var ctr = $("#"+ptid);
	var pid = ctr.attr("pid");
	var param= getActionParam(ptid);
	param["pid"] = pid;
	$.ajax({
	type: "GET",
	url: ROOT_PATH + "/pt/home/doDelPortlet",
	data:param,
	cache:false,
    success: function(data){
     	var d = eval(data)[0];
	    if(d && typeof  d == "object"){
	    	alert(d.msg);
			if(d.err == 0){
				document.location.reload();
			} 
		}
   }
}); 
}

/**
获得最外层Layout
**/
function _pt_getOuter(){
	return 	$("[tp='layout']").filter(function(index){return $(this).parents("[tp='layout']").length<1;});
	}

function _pt_getSelfLayout(){
	
	}
function _pt_getRow(){
	return $(this).parent("[tp='layout']");
	}	
/**
 * 默认的切换到"查看"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doView(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}

/**
 * 默认的切换到"编辑"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doEdit(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	param[PORTLET_MODE]="edit";
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}
/**
 * 默认的切换到"帮助"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doHelp(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	param[PORTLET_MODE]="help";
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}
/**
 * 默认刷新方法
 * @param timesamp 时间间隔
 */
function _pt_doRefresh(timesamp){
	var ptid=$(this).attr("id") ;
	refresh_circle[ptid] = timesamp;
}
/**
 * 设置Portlet支持模式
 * @param modes 模式
 */
function _pt_setSupportModes(modes){
	$(this).data("supportModes",modes);
}

/**
 * 获得Portlet支持模式
 * @return 支持的模式
 */
function _pt_getSupportModes(){
	return $(this).data("supportModes");
}

/**
 * 获得当前Portlet模式
 */
function _pt_getCurrentMode(){
	var cm = $(this).data("currentMode");
	if(cm && cm != "")
		return cm;
	else
		return "view";
}

/**
 * 设置当前Portlet模式
 */
function _pt_setCurrentMode(mode){
	return $(this).data("currentMode",mode);
}
/**
默认触发事件方法
**/
function _pt_doAction(){
	var ptid=$(this).attr("id").split("_");
	
	var pageModule= ptid[0];
	var pageName=ptid[1];
 	var portletModule=ptid[2];
	var portletName=ptid[3];
	
	var actURL=ROOT_PATH+ACTION_URL;
	var argCount=arguments.length;
	var param={};
	if(argCount==0){
		//无参数 触发默认Action  无默认事件则返回
		//是否有必要对Portlet Action进行感知？
		}else if(argCount==1){
		//一个参数 默认为参数名
			if($.isPlainObject(arguments[0])){
			//为Object
				param=arguments[0]; 
				if(!param[ACTION_NAME]){
					param[ACTION_NAME]="processAction";
				}
				param[PAGE_NAME]=pageName;
				param[PORTLET_MODULE]=portletModule;
				param[WINDOW_STATE]="normal";
				param[PORTLET_MODE]="view";
				param[PAGE_MODULE]=pageModule;
				param[PORTLET_NAME]=portletName;
			}else{
				param[ACTION_NAME]="processAction";
				param[PAGE_NAME]=pageName;
				param[PORTLET_MODULE]=portletModule;
				param[WINDOW_STATE]="normal";
				param[PORTLET_MODE]="view";
				param[PAGE_MODULE]=pageModule;
				param[PORTLET_NAME]=portletName;
				param.frameUrl=arguments[0];
			}
		}else {
			//只处理前两个参数
			param=arguments[1];
			param[ACTION_NAME]=arguments[0];
			param[PAGE_NAME]=pageName;
			param[PORTLET_MODULE]=portletModule;
			param[PAGE_MODULE]=pageModule;
			param[PORTLET_NAME]=portletName;
			if(param[WINDOW_STATE]==null){
				param[WINDOW_STATE]="normal";
			}
			if(param[PORTLET_MODE]==null){
				param[PORTLET_MODE]="view";
			}
		}
		openPortlet(actURL,param);
	}
/**
 * 获得默认的Portlet参数
 */
function getActionParam(portletWinId){
	var ptid=portletWinId.split("_");
	var pageModule= ptid[0];
	var pageName=ptid[1];
	var portletModule=ptid[2];
	var portletName=ptid[3];
	var param={};
	param[PAGE_NAME]=pageName;
	param[PORTLET_MODULE]=portletModule;
	param[PAGE_MODULE]=pageModule;
	param[PORTLET_NAME]=portletName;
	param[WINDOW_STATE]="normal";
	param[PORTLET_MODE]="view";
	return param;
}

/**
 * 默认最大化实现
 * @return
 */
function _pt_defaultdoMax(){
	var jqObj=$(this);
	var outer=this.getOuter();
	var outerHeight=outer.height();
	var outerWidth=outer.width() -10;
	if(!this.oldWidth)
	this.oldWidth=jqObj.width();
	if(!this.oldHeight)
		this.oldHeight=jqObj.height();
	var oldHeight=jqObj.height();
	var pBody=jqObj.find("[tp='pBody']");
	var bodyHeight=pBody.height();
	if(!pBody[0].oldHeight)
		pBody[0].oldHeight = bodyHeight;
	var portlets = outer.find("[tp=portlet]");
	for(var i = 0;i< portlets.length; i++){
		var ctn = getContainer(portlets[i]);
		ctn.doHide();
	}
	
	//pBody.height(bodyHeight +outerHeight-oldHeight); 
	//jqObj.height(outerHeight ); 
	jqObj.parents("td").siblings().hide();
	jqObj.parents("td").attr("_width",jqObj.parents("td").attr("width"));
	jqObj.parents("td").attr("width","100%");
//	jqObj.css("position","absolute");
	//jqObj.width(outerWidth); 
//	jqObj.width("100%"); 
	jqObj.show(500);
	jqObj.find("[tp='pHander']").find("a").toggle();
}

function _pt_defaultdoHide(){
	var jqObj=$(this);
	jqObj.hide();
}
/**
 * 默认重设窗口大小
 * @return
 */
function _pt_defaultdoReSize(){
	var jqObj=$(this);
	var outer=this.getOuter();
	//jqObj.width(this.oldWidth);
	//jqObj.height(this.oldHeight);
	var pBody=jqObj.find("[tp='pBody']");
	var bodyHeight=pBody[0].oldHeight;
	//pBody.height(bodyHeight);
	jqObj.parents("td").attr("width",jqObj.parents("td").attr("_width")),
	jqObj.parents("td").siblings().show();
	outer.find("[tp=portlet]").show(500);
	jqObj.find("[tp='pHander']").find("a").toggle();
}
/**
 * 默认的显示最大化按钮
 */
function _pt_defaultAddDoMaxResize(windowState){
	var jqObj=$(this);
	var portletWindowID = this.id;
	var windowStates = windowState.toString();
	var $ul = jqObj.find("[tp='pPart']");
	/*
	if(windowStates.indexOf('minimized')!=-1){
		$ul.append("<li tp=\"pHander\">" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doMax();\"><img src=\""+ROOT_PATH+"/images/10.gif\" style=\"border:none;cursor:pointer;\"/></a>" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doReSize();\" style=\"display:none\"><img src=\""+ROOT_PATH+"/images/11.gif\" style=\"border:none;cursor:pointer;\"/></a></li>");	
	}else
	*/ 
	if(windowStates.indexOf('maximized')!=-1){
		$ul.append("<li tp=\"pHander\">" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doMax();\" style=\"display:none\"><img src=\""+ROOT_PATH+"/images/10.gif\" style=\"border:none;cursor:pointer;\"/></a>" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doReSize();\"><img src=\""+ROOT_PATH+"/images/11.gif\" style=\"border:none;cursor:pointer;\"/></a></li>");
	}
}



/**
返回容器的DOM
@param objId container元素ID
@return 容器DOM对象
**/
function getContainer(objId) {
	//得到对象
	var _pt_tmp_container=$(objId);
	if(_pt_tmp_container==null){
		return null;
		}
	//得到DOM对象
	var _pt_tmp_container_dom_=_pt_tmp_container.get(0);
	
	//检查是否注册createPart函数
	if(typeof(_pt_tmp_container_dom_.createPart) != "function"){
		 var _pt_tmp_createPart =_pt_tmp_container.attr("createPart");
		 //是否自定义
        if (_pt_tmp_createPart != null) {
			//自定义
            _pt_tmp_container_dom_.createPart = eval(_pt_tmp_createPart);
        } else {
            _pt_tmp_container_dom_.createPart = _pt_defaultCreatePart;
        }
	}
		
	//检查是否注册createSep函数
	if(typeof(_pt_tmp_container_dom_.createSep) != "function"){
		 var _pt_tmp_createSep =_pt_tmp_container.attr("createSep");
		 //是否自定义
        if (_pt_tmp_createSep != null) {
			//自定义
            _pt_tmp_container_dom_.createSep = eval(_pt_tmp_createSep);
        } else {
            _pt_tmp_container_dom_.createSep = _pt_defaultCreateSep;
        }
	}
		
	//检查是否注册createBlankPart函数
	if(typeof(_pt_tmp_container_dom_.createBlankPart) != "function"){
		 var _pt_tmp_createBlankPart =_pt_tmp_container.attr("createBlankPart");
		 //是否自定义
        if (_pt_tmp_createBlankPart != null) {
			//自定义
            _pt_tmp_container_dom_.createBlankPart = eval(_pt_tmp_createBlankPart);
        } else {
            _pt_tmp_container_dom_.createBlankPart = _pt_defaultCreateBlankPart;
        }
	}

	//检查是否注册setTitle函数
	if(typeof(_pt_tmp_container_dom_.setTitle) != "function"){
		 var _pt_tmp_setTitle =_pt_tmp_container.attr("setTitle");
		 //是否自定义
        if (_pt_tmp_setTitle != null) {
			//自定义
            _pt_tmp_container_dom_.setTitle = window[_pt_tmp_setTitle]
        } else {
            _pt_tmp_container_dom_.setTitle = _pt_defaultSetTitle;
        }
	}
		
	//检查是否注册setMode函数
	if(typeof(_pt_tmp_container_dom_.setMode) != "function"){
		 var _pt_tmp_setMode =_pt_tmp_container.attr("setMode");
		 //是否自定义
        if (_pt_tmp_setMode != null) {
			//自定义
            _pt_tmp_container_dom_.setMode = eval(_pt_tmp_setMode);
        } else {
            _pt_tmp_container_dom_.setMode = _pt_defaultSetMode;
        }
	}

	//检查是否注册setContent函数
	if(typeof(_pt_tmp_container_dom_.setContent) != "function"){
		 var _pt_tmp_setContent =_pt_tmp_container.attr("setContent");
		 //是否自定义
        if (_pt_tmp_setContent != null) {
			//自定义
            _pt_tmp_container_dom_.setContent = eval(_pt_tmp_setContent);
        } else {
            _pt_tmp_container_dom_.setContent = _pt_defaultSetContent;
        }
	}
		
		
	//检查是否注册setExposed函数
	if(typeof(_pt_tmp_container_dom_.setExposed) != "function"){
		 var _pt_tmp_setExposed =_pt_tmp_container.attr("setExposed");
		 //是否自定义
        if (_pt_tmp_setExposed != null) {
			//自定义
            _pt_tmp_container_dom_.setExposed = eval(_pt_defaultSetExposed);
        } else {
            _pt_tmp_container_dom_.setExposed = _pt_defaultSetExposed;
        }
	}
		
	//检查是否注册setUnExposed函数
	if(typeof(_pt_tmp_container_dom_.setUnExposed) != "function"){
		 var _pt_tmp_setUnExposed =_pt_tmp_container.attr("setUnExposed");
		 //是否自定义
        if (_pt_tmp_setUnExposed != null) {
			//自定义
            _pt_tmp_container_dom_.setUnExposed = eval(_pt_tmp_setUnExposed);
        } else {
            _pt_tmp_container_dom_.setUnExposed = _pt_defaultSetUnExposed;
        }
	}
		
	if(typeof(_pt_tmp_container_dom_.doMax) != "function"){
		 var _pt_tmp_doMax=_pt_tmp_container.attr("doMax");
		 //是否自定义
		 if (_pt_tmp_doMax != null) {
		//自定义
			_pt_tmp_container_dom_.doMax = eval(_pt_tmp_doMax);
		} else {
			_pt_tmp_container_dom_.doMax = _pt_defaultdoMax;
		}
	}
	if(typeof(_pt_tmp_container_dom_.doHide) != "function"){
		 var _pt_tmp_doHide = _pt_tmp_container.attr("doHide");
		 //是否自定义
		 if (_pt_tmp_doHide != null) {
		//自定义
		 	_pt_tmp_container_dom_.doHide = eval(_pt_tmp_doHide);
		} else {
			_pt_tmp_container_dom_.doHide = _pt_defaultdoHide;
		}
	}	
	
	if(typeof(_pt_tmp_container_dom_.doReSize) != "function"){
			
			 var _pt_tmp_doReSize=_pt_tmp_container.attr("doReSize");
			 //是否自定义
			 if (_pt_tmp_doReSize != null) {
			//自定义
				_pt_tmp_container_dom_.doReSize = eval(_pt_tmp_doReSize);
			} else {
				_pt_tmp_container_dom_.doReSize = _pt_defaultdoReSize;
			}
	}
	//注册显示工具条
	if(typeof(_pt_tmp_container_dom_.showTips) != "function"){
			
			 var _pt_tmp_showTips=_pt_tmp_container.attr("showTips");
			 //是否自定义
			 if (_pt_tmp_showTips != null) {
			//自定义
				_pt_tmp_container_dom_.showTips = eval(_pt_tmp_showTips);
			} else {
				_pt_tmp_container_dom_.showTips = _pt_defaultShowTips;
			}
	}
	//注册隐藏工具条
	if(typeof(_pt_tmp_container_dom_.hideTips) != "function"){
			
			 var _pt_tmp_hideTips=_pt_tmp_container.attr("hideTips");
			 //是否自定义
			 if (_pt_tmp_hideTips != null) {
			//自定义
				_pt_tmp_container_dom_.hideTips = eval(_pt_tmp_hideTips);
			} else {
				_pt_tmp_container_dom_.hideTips = _pt_defaultHideTips;
			}
	}
	//注册增加最大/还原按钮
	if(typeof(_pt_tmp_container_dom_.addDoMaxResize) != "function"){
			
			 var _pt_tmp_addDoMaxResize=_pt_tmp_container.attr("addDoMaxResize");
			 //是否自定义
			 if (_pt_tmp_addDoMaxResize != null) {
			//自定义
				_pt_tmp_container_dom_.addDoMaxResize = eval(_pt_tmp_addDoMaxResize);
			} else {
				_pt_tmp_container_dom_.addDoMaxResize = _pt_defaultAddDoMaxResize;
			}
	}
	// --------------------------------系统方法(用户不可自定义)-----------------------------//
	//注册doView函数
	if(_pt_tmp_container_dom_.doView==null){
		_pt_tmp_container_dom_.doView=_pt_doView;
	}
	//注册doView函数
	if(_pt_tmp_container_dom_.doEdit==null){
		_pt_tmp_container_dom_.doEdit=_pt_doEdit;
	}
	//注册doView函数
	if(_pt_tmp_container_dom_.doHelp==null){
		_pt_tmp_container_dom_.doHelp=_pt_doHelp;
	}
	//注册doRefresh函数
	if(_pt_tmp_container_dom_.doRefresh==null){
		_pt_tmp_container_dom_.doRefresh=_pt_doRefresh;
	}
 		
	//注册doAction函数
	if(_pt_tmp_container_dom_.doAction==null){
		_pt_tmp_container_dom_.doAction=_pt_doAction;
	}
		//注册getOuter函数
	if(_pt_tmp_container_dom_.getOuter==null){
		_pt_tmp_container_dom_.getOuter=_pt_getOuter;
	}
	//注册getRow函数
	if(_pt_tmp_container_dom_.getRow==null){
		_pt_tmp_container_dom_.getRow=_pt_getRow;
	}
	if(_pt_tmp_container_dom_.setSupportModes==null){
		_pt_tmp_container_dom_.setSupportModes=_pt_setSupportModes;
	}
	if(_pt_tmp_container_dom_.getSupportModes==null){
		_pt_tmp_container_dom_.getSupportModes=_pt_getSupportModes;
	}
	if(_pt_tmp_container_dom_.setCurrentMode==null){
		_pt_tmp_container_dom_.setCurrentMode=_pt_setCurrentMode;
	}
	if(_pt_tmp_container_dom_.getCurrentMode==null){
		_pt_tmp_container_dom_.getCurrentMode=_pt_getCurrentMode;
	}
	
    return _pt_tmp_container_dom_;
}

/**
 * 单次加载函数
 * Portlet在拖放时,Dom内的Javascript会再次执行,如果不想再次执行,可以使用$.container(function(){ ... })
 * @author licza 
 */
(function($) {
	$.extend({
	container : function (fn){
		var fnHash=$.sha1(fn.toString()) ;
			if(document[fnHash]){
				//log('has done');
			}else{
				fn.call(this);
				document[fnHash]=-1;
			}
		}
	})
})(jQuery);

/**
 * SHA加密
 */
(function($) {
$.extend({
	sha1:function (s)
	{
	       return binb2hex(core_sha1(AlignSHA1(s)));
	} 
})

function core_sha1(blockArray)
{
  var x = blockArray;  //append padding
  var w = Array(80);
  var a =  1732584193;
  var b = -271733879;
  var c = -1732584194;
  var d =  271733878;
  var e = -1009589776;
 
  for(var i = 0; i < x.length; i += 16)  
  {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;
    var olde = e;
 
    for(var j = 0; j < 80; j++)  
    {
      if(j < 16) w[j] = x[i + j];
      else w[j] = rol(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
      
      var t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)),
                       safe_add(safe_add(e, w[j]), sha1_kt(j)));
      e = d;
      d = c;
      c = rol(b, 30);
      b = a;
      a = t;
    }
 
    a = safe_add(a, olda);
    b = safe_add(b, oldb);
    c = safe_add(c, oldc);
    d = safe_add(d, oldd);
    e = safe_add(e, olde);
  }
  return new Array(a, b, c, d, e);
 
}
function sha1_ft(t, b, c, d)
{
  if(t < 20) return (b & c) | ((~b) & d);
  if(t < 40) return b ^ c ^ d;
  if(t < 60) return (b & c) | (b & d) | (c & d);
  return b ^ c ^ d;  //t<80
}
 
 
function sha1_kt(t)
{
  return (t < 20) ?  1518500249 : (t < 40) ?  1859775393 :
         (t < 60) ? -1894007588 : -899497514;
}

/**
 * 将32位数拆成高16位和低16位分别进行相加，从而实现 MOD 2^32 的加法
 */
function safe_add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

/**
 * 32位二进制数循环左移
 */
function rol(num, cnt)
{
  return (num << cnt) | (num >>> (32 - cnt));
}

function AlignSHA1(str){
  var nblk=((str.length+8)>>6)+1, blks=new Array(nblk*16);
  for(var i=0;i<nblk*16;i++)blks[i]=0;
  for(i=0;i<str.length;i++)
    blks[i>>2]|=str.charCodeAt(i)<<(24-(i&3)*8);
  blks[i>>2]|=0x80<<(24-(i&3)*8);
  blks[nblk*16-1]=str.length*8;
  return blks;
}
/**
 * 16进制转换
 * @param 二进制数组
 */
function binb2hex(binarray)
{
  var hex_tab = 0 ? "0123456789ABCDEF" : "0123456789abcdef";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i++)
  {
    str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
           hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
  }
  return str;
}
})(jQuery);