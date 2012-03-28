
/**
 * 打开被集成的NC系统的某个节点
 * @param funcode 被打开节点的节点号
 */
function openNCNode(funcode, systemcode){
	//try
	{
		execNCAppletFunction("nc.ui.sm.webcall.OpenNCNode", "openNode", funcode, systemcode);
	}
	//catch(error)
	{
		
	}	
};

/**
 * 
 * @param argStr 调用类的参数String
 * @param isNcJob 是否是打开NC待办事务节点。
 */
function execNCAppletFunction(className, methodName, argStr, systemcode)
{
	//log("systemcode=" + systemcode);
	if(systemcode == null)
		systemcode = "NC";
   	var ncFrame = $("$NC_FRAME_ONLY_ONE_ID$" + systemcode); 
   	if(ncFrame == null)
   	{ 
   		var param = "systemcode=" + systemcode + "&screenWidth=" + window.screen.width + "&screenHeight=" + window.screen.height;
   		// 从Portal服务器抓取NC的页面源码
   		var pageSource = loadPage(window.globalPath + "/pt/nc5x/fetch", param, null, null, true, false);
   		if(pageSource.substring(0,6) == "ERROR")
   		{
   			pageSource = decodeURIComponent(pageSource);
   			alert(pageSource.substring(6, pageSource.length));
   			return;
   		}
   		ncFrame = initNCFrame(systemcode);
   		ncFrame.contentWindow.document.write(pageSource);
   	}
//   	showProgressPopup();
	waitLoadNCApplet(className, methodName, argStr, systemcode);
};

function waitLoadNCApplet(className, methodName, argStr, systemcode)
{
	if(window.$loadExecCount != null)
		window.$loadExecCount++;
	var applet = null;
	try {
		var ncFrame = $("$NC_FRAME_ONLY_ONE_ID$" + systemcode);
		if(ncFrame != null)
			applet = ncFrame.contentWindow.document.applets["NCApplet"];
		//log("ncFrame=" + ncFrame + " applet=" + applet);
	}
	catch(error){
		// 设置frame src后,src所指向的页面执行需要一定的时间,所以src页面设置的document.domain可能还没有执行,
		// 这时会报"拒绝访问错误",但此时并不需要返回,要给applet初始化一些时间
		if(window.$loadExecCount == null)
			window.$loadExecCount = 0;
		//hideProgressPopup();
		if(window.$loadExecCount > 10)
		{
			alert("get applet error:" + error.name + ":" + error.message);
			window.$loadExecCount = 0;
			return;
		}	
	}
	if(applet == null || !isNcLoaded(applet))
	{
		setTimeout("waitLoadNCApplet('"+ className +"','" + methodName + "','" + argStr + "','" + systemcode + "')", 100);
		return;
	}
	//if(window.$loadExecCount != null)
	//	log("method waitLoadNCApplet exec " + window.$loadExecCount + " count!");
	window.$loadExecCount = null;
	exec(className, methodName, argStr, systemcode);
}; 

function isNcLoaded(applet) {
	try{
		return applet.callNC("nc.ui.sm.webcall.OpenNCNode", "isDesktopShow", null);
	}
	catch(error) {
		return false;	
	}
}

function exec(className, methodName, argStr, systemcode)
{
	 try {
	 	var ncFrame = $("$NC_FRAME_ONLY_ONE_ID$" + systemcode);
    	var applet = ncFrame.contentWindow.document.applets["NCApplet"];
    	//hideProgressPopup();
    	applet.callNC(className, methodName, argStr);
    	/*
    	if(isNcJob)
    		applet.callNC("nc.client.portal.PortalInNCClient", "openMsgPanel", argStr);
    	else
    		applet.callNC("nc.ui.sm.webcall.OpenNCNode", "openNode", argStr);*/
    		
    } catch(error) {
    	//hideProgressPopup();
        alert(error.name + ":" + error);
    }
};

/**
 * 初始化加载nc applet的"iframe"
 */
function initNCFrame(systemcode)
{
	var frameID = "$NC_FRAME_ONLY_ONE_ID$" + systemcode;
	var frame = $(frameID);
	if(frame == null)
	{
		frame = document.createElement("iframe");
		frame.id = frameID;
		frame.style.position = "relative";
		frame.style.left = "0";
		frame.style.top = "0";
		frame.style.width = 1;
		frame.style.height = 0;
		frame.frameBorder = 0;
		frame.width = 0;
		frame.height = 0;
		frame.src = window.globalPath + "/setdomain.jsp";
		document.body.appendChild(frame);
	}
	return frame;
}; 

/**
 * 当portlet初次进入View时需要调用此方法将过时的iframe信息清楚掉。
 */
function clearNCFrame(systemcode)
{
	var frameID = "$NC_FRAME_ONLY_ONE_ID$" + systemcode;
	var ncFrame = $(frameID);
	if(ncFrame != null)
	{
		ncFrame.src = "";
		ncFrame.parentNode.removeChild(ncFrame);
	}
};


/**
* ajax请求封装
* @param path 请求url，不能为空
* @param queryStr 请求参数,如 a=1&b=2&c=3
* @param returnFunc 回调函数。ajax请求完毕后会调用此函数
* @param returnArgs 回调函数传入的参数
* @param method 请求方式，"GET" or "POST",默认POST
* @param asyn 是否异步，默认异步
* @param format 是否是格式化数据，如果是，返回xml，否则返回text.默认为text
*	
*/
function loadPage(pathOrg, queryStringOrg, returnFunction, returnArgs, method, asyn, format) 
{
	var pos = pathOrg.indexOf("?");
	var path = pathOrg;
	var queryString = "";
	if (pos != -1) 
	{
		path = pathOrg.substring(0, pos);
		queryString = pathOrg.substring(pos + 1, pathOrg.length);
	}
	if(queryString != "")
		queryString += "&";
	if(queryStringOrg != null && queryStringOrg != "")
		queryString += queryStringOrg;
	
	if(typeof(getStickString) != "undefined"){
	 	var stickStr = getStickString();
    	if(stickStr != null && stickStr != "")
    		queryString = mergeParameter(queryString, stickStr);
	}
	
	//是否异步，默认异步
	asyn = getBoolean(asyn, true);
	if (queryString == null) {
		queryString = "";
	}

	if (returnFunction == null) {
		returnFunction = function () {};
	}
	
	var xmlHttpReq;

	try {
		xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		 
	}
	catch (e) {
		try {
			xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			
		}
		catch (e) {
			try {
				xmlHttpReq = new XMLHttpRequest();
			}
			catch (e) {
			}
		}
	}
	try {
		if (method != null && method == false) {
			// 标明是ajax请求,应为ajax请求传递的中文参数在后台不需要转化编码会正确获得
			var urlParam = path + "?" + queryString + "&isAjax=1";
			if(queryString != "" && queryString[queryString.length - 1] != "&")
				urlParam += "&";
			urlParam += "tmprandid=" + Math.random();
			
			//由于get方法对同一个网址不会做第二次返回，所以为了请求正常进行，在网址后加随机参数
			xmlHttpReq.open("GET", urlParam , asyn);
			xmlHttpReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			xmlHttpReq.send(null);
			//同步，直接返回结果
			if(!asyn)
			{
				if(format)
					return xmlHttpReq.responseXML;
				return xmlHttpReq.responseText; 
			}
		}
		else {
			xmlHttpReq.open("POST", path, asyn);
			xmlHttpReq.setRequestHeader("Method", "POST " + path + " HTTP/1.1");
			xmlHttpReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			xmlHttpReq.send(queryString + "&isAjax=1");
			//同步，直接返回结果
			if(!asyn)
			{
				if(format)
					return xmlHttpReq.responseXML;
				return xmlHttpReq.responseText; 
			}
		}

		xmlHttpReq.onreadystatechange = function() {
				if (xmlHttpReq.readyState == 4) {
					if (xmlHttpReq.status == 200) {
						returnFunction(xmlHttpReq, returnArgs, null, [pathOrg, queryStringOrg, returnFunction, returnArgs, method, asyn, format]);
					}
					else if(xmlHttpReq.status == 306) 
					{
						returnFunction(xmlHttpReq, returnArgs, trans("ml_sessioninvalid"));
					}
					//为报销特殊处理的。这里需要提取出去
					else if(xmlHttpReq.status == 308){
						alert(trans("ml_requestvalid"));
						var bxtop = window;
						if(bxtop.bxmainpage == null)
							bxtop = bxtop.parent;
						bxtop.location.href = "core/main/main.jsp?pageId=main";
						return;
					}
					else{
						returnFunction(xmlHttpReq, returnArgs, "Error occurred,response status is:"+ xmlHttpReq.status); 
					}
				}
			};
	}
	catch (e) {
		if(IS_IE)
			alert(e.name + " " + e.message);
		else
			alert(e);
		
	}
};
function getBoolean(value, defaultValue)
{
	if(value == 'false')
		return false;
	else if(value == 'true')
		return true;
	else if(value != false && value != true)
		return defaultValue;
	else
		return value;
};