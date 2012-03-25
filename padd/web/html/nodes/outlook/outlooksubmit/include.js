function setTabItemURL(tabId, itemIndex, url,name) {
	var widget=pageUI.getWidget("main");
	var tab_view=widget.getTab(tabId)
	var iframe=document.createElement("iframe");
	iframe.src=url;
	iframe.id=name;
	iframe.width="100%";
	iframe.height="100%";
	iframe.style.overflowY="auto";
	var itms=tab_view.getTabItems();
	var old= itms[itemIndex].getObjHtml().childNodes[0].childNodes[0];
  	if(old!=null){
       itms[itemIndex].getObjHtml().childNodes[0].removeChild(old);
  	}
	itms[itemIndex].getObjHtml().childNodes[0].appendChild(iframe);
}
function addAttachFileDs(pk,name,filesize,filetype,uploadtime,pk_user,username){	
	 var widget=pageUI.getWidget('main');
	 var dsUploader=widget.getDataset('ds_attachfile');
	 var row=dsUploader.getEmptyRow();
	 row.setCellValue(dsUploader.nameToIndex("pk_file"),pk);
	 row.setCellValue(dsUploader.nameToIndex("filename"),name);
	 row.setCellValue(dsUploader.nameToIndex("filesize"),filesize);
	 row.setCellValue(dsUploader.nameToIndex("uploadtime"),uploadtime);
	 row.setCellValue(dsUploader.nameToIndex("creator"),pk_user);
	 row.setCellValue(dsUploader.nameToIndex("creator_username"),username);
	 dsUploader.addRow(row);
	 var proxy = new ServerProxy(null,null,true);
	 proxy.addParam("listener_class", "nc.portal.pwfm.listener.AddAttachFileRowListener");
	 proxy.addParam("pk_file",pk);
	 proxy.addParam("filename",name);
	 var sbr = new SubmitRule();  
	 proxy.setSubmitRule(sbr); 
	 proxy.execute();
}

function EleNameRender() {
};
EleNameRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "right";
	cell.innerHTML = "<img src='" + window.globalPath + "/pt/file/down?id=" + value + "'/>";
};
function submit(){
	var address = window.location+"";
	var content = pageUI.getWidget('main').getComponent('iframe_formdata').frame.contentWindow;
	/*
	if(address.lastIndexOf(".html") == address.length-5){
		return getSessionAttribute('submitUrl');
	}
	
	if(content.collectData != undefined){
		//content.submit();
	}
	*/
	var pageUniqueId = content.getSessionAttribute("pageUniqueId");
	var url = getSessionAttribute('submitUrl');//+ "&otherPageUniqueId="+pageUniqueId;
	openWindowInCenter(url, '审核', '600', '430');
}
function reject(){
	var address = window.location+"";
	var content = pageUI.getWidget('main').getComponent('iframe_formdata').frame.contentWindow;
	if(address.lastIndexOf(".html") == address.length-5){
		return getSessionAttribute('rejectUrl');
	}
	if(content.collectData != undefined){
		//content.submit();
	}
	var pageUniqueId = content.getSessionAttribute("pageUniqueId");
	var url = getSessionAttribute('rejectUrl')+ "&otherPageUniqueId="+pageUniqueId;
	openWindowInCenter(url, '退回', '600', '430');
}
function collectData(){
	var content = pageUI.getWidget('main').getComponent('iframe_formdata').frame.contentWindow;
	if(content.collectData != undefined){
		return content.collectData();
	}
	
}
function sendData(flwDataString){
	window.setTimeout(function(){delaySend(flwDataString)},500);
}
/**
 * 保存正文,取得正文的主键,新建文档返回空
 */
function getMasterWordPk(){
	var wordFrameComp = pageUI.getWidget('main').getComponent('iframe_word');
	if(wordFrameComp == null)
		return "";
	var wordFrame = wordFrameComp.frame.contentWindow;
	if(wordFrame == null)
		return "";
	if(wordFrame.saveFileToLocal == undefined)
		return "";
	if(wordFrame.getMasterWordPk == undefined)
		return "";
	wordFrame.saveFileToLocal();
	return wordFrame.getMasterWordPk();
}
/**
 * 延迟发送数据
 * @param {string} flwDataString 流程数据
 */
function delaySend(flwDataString){
	var flwData = null;
	try{
		eval("flwData="+flwDataString);
	}catch(e){
	}
	flwData.formVO = collectData();
	var masterWordPk = getMasterWordPk();
	var serverContext = getSessionAttribute('serverContext');
	var path = serverContext+"/outlookservice";
	var divObj = document.createElement("div");
	divObj.style.position="absolute";
	divObj.style.left=0;
	divObj.style.top=0;
	divObj.style.width=1;
	divObj.style.height=1;
	var formObj = document.createElement("form");
	formObj.action=path;
	formObj.method="post";
	divObj.appendChild(formObj);
	var cmdObj = document.createElement("<input name='cmd' type='hidden' value='submit'>");
	formObj.appendChild(cmdObj);
	
	var frmdefpkObj = document.createElement("<input name='frmdefpk' type='hidden'>");
	formObj.appendChild(frmdefpkObj);
	frmdefpkObj.value = window.$paramsMap.get('frmdefpk');
	
	var wordpkObj = document.createElement("<input name='wordpk' type='hidden'>");
	formObj.appendChild(wordpkObj);
	wordpkObj.value = masterWordPk;
	
	var dataObj = document.createElement("<input name='data' type='hidden'>");
	formObj.appendChild(dataObj);
	dataObj.value=toJSON(flwData);
	document.body.appendChild(divObj);
	formObj.submit();
}