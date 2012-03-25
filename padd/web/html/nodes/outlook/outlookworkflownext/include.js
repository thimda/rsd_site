/**
 * 在服务器调用前的事件处理
 * @param {ServerProxy} proxy 服务器代理对象
 * @param {String} listenerId 监听器ID
 * @param {String} eventName 事件名
 * @param {String} eleId 元素ID
 */
function beforeCallServer(proxy, listenerId, eventName, eleId){
	   //var iframe = getPopParent().parent.document.getElementById('iframe_word');
		  // if(iframe != null&&listenerId=='pwfmlistener'){
	      //iframe.contentWindow.saveFileToURL();
	  // }
}

/**
 * 初始化下一步活动的dataset
 */
function initNextHumActDataset(dataset){
	dataset.initialize();
	var jsonString = getSessionAttribute('dsdata_ds_nexthumact');
	if(jsonString != null){
		var json;
	    try {
			eval("json = " + jsonString);
	    } catch(e) {
			return;
	    }
		dataset.setJsonData(json,dataset.currentKey,0);
	}
}

/**
 * 选中下一步活动时的事件处理
 */
function afterSelectNextHumAct(dataset){
	var nextHumActRow = dataset.getFocusRow();
	var msgType = nextHumActRow.getCellValue(dataset.nameToIndex("msgtype"));
	
	var mainWidget=window.pageUI.getWidget('main');
	var msgTypeComp = mainWidget.getComponent("text_msgtype");
	if (msgTypeComp == null) {
		return;
	}
	if (msgType == null || msgType == "") {
		msgTypeComp.setValue(" ");
		msgTypeComp.setActive(false);
		return;
	}
	var msgTypes = ","+msgType+",";
	if (msgTypes.indexOf(",taskcreatedphone,") >= 0) {
		msgTypeComp.checkboxs[0].setActive(true);
	}
	if (msgTypes.indexOf(",taskcreatedemail,") >= 0) {
		msgTypeComp.checkboxs[1].setActive(true);
	}
	msgTypeComp.setValue(msgType);
}


/**
 * 表格Cell渲染其定义
 */
function EleNameRender() {};

/**
 * 自定义表格Cell渲染方法
 * @param {int} rowIndex 行索引
 * @param {int} colIndex 列索引
 * @param {String} value 真实值
 * @param {Column} header 表头对象
 * @param {Cell} cell 表格对象
 */
EleNameRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "right";
    var mainWidget=window.pageUI.getWidget('main');
    var ds=mainWidget.getDataset("ds_nexthumact");
	if(value=='true'){
	   cell.innerHTML = "<input type='button' value='选人' style='width:80px' onclick=assign("+rowIndex+")>";
	}else{
	   cell.innerHTML = "<input type='button' value='选人' style='width:80px'  disabled='disabled'>";
	}	
}

/**
 * 切换活动数据时,更新对应的指派人
 * @param {int} rowIndex 行索引
 */
function refreshDsAssign(rowIndex){
	
    var mainWidget=window.pageUI.getWidget('main');
    var grid = mainWidget.getComponent("grid_nexthumact");
    var dsNextHumAct=mainWidget.getDataset("ds_nexthumact");
    dsNextHumAct.setFocusRowIndex(rowIndex);
	//grid.setFocusIndex(rowIndex);
	var humActId = dsNextHumAct.getValueAt(rowIndex,0);
    var assignWidget=window.pageUI.getWidget('assign');
    assignWidget.setVisible(true);
    var dsAllActors=assignWidget.getDataset('ds_allactors');
    var jsonString = getSessionAttribute('dsdata_ds_assignactors');
    var jsons = null;
	if(jsonString != null){
	    try {
			eval("jsons = " + jsonString);
	    } catch(e) {
			return;
	    }
	}    
    if(dsAllActors!=null){
    	if(jsons != null && jsons.length >0){
	    	for(var i = 0; i < jsons.length; i++){
	    		dsAllActors.setJsonData(jsons[i],jsons[i].keyValue,0);
	    	}
	    }
		dsAllActors.setCurrentPage(humActId);
    }
    var dsSelected=assignWidget.getDataset('ds_selecteds');
    if(dsSelected!=null){
	   if(jsons != null && jsons.length >0){
	    	for(var i = 0; i < jsons.length; i++){
	    		jsons[i].size = 0;
	    		jsons[i].total = 0;
	    		jsons[i].meta = [];
	    		jsons[i].data = [];
	    		dsSelected.setJsonData(jsons[i],jsons[i].keyValue,0);
	    	}
	    }
		dsSelected.setCurrentPage(humActId);
    }
}

/**
 * 添加选中的指派人
 */
function assignLeftOne(){
	var assignWidget=window.pageUI.getWidget('assign');
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	
	var selectedRow = dsSelected.getSelectedRow();
	var selectedIndex = dsSelected.getSelectedIndex();
	if (selectedRow == null) {
		return;
	}
	var dsAllActors=assignWidget.getDataset('ds_allactors');
	dsSelected.removeRow(selectedIndex);
	dsAllActors.addRow(selectedRow);
}

/**
 * 添加所有的可指派人
 */
function assignLeftAll(){
	var assignWidget=window.pageUI.getWidget('assign');
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	var dsAllActors=assignWidget.getDataset('ds_allactors');
	
	var rows = dsSelected.getCurrentRowData().getRows();
	if (rows == null || rows.length == 0) {
		return;
	}
	for (var i = 0; i < rows.length;) {
		var row = rows[0];
		dsSelected.removeRow(0);
		dsAllActors.addRow(row);
	}			
}

/**
 * 删除选中的已指派人
 */
function assignRightOne(){
	var assignWidget=window.pageUI.getWidget('assign');
	var dsAllActors=assignWidget.getDataset('ds_allactors');
	
	var selectedRow = dsAllActors.getSelectedRow();
	var selectedIndex = dsAllActors.getSelectedIndex();
	if (selectedRow == null) {
		return;
	}
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	dsAllActors.removeRow(selectedIndex);
	dsSelected.addRow(selectedRow);
}

/**
 * 删除所有的已指派人
 */
function assignRightAll(){
	var assignWidget=window.pageUI.getWidget('assign');
	var dsAllActors=assignWidget.getDataset('ds_allactors');
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	
	var rows = dsAllActors.getCurrentRowData().getRows();
	if (rows == null || rows.length == 0) {
		return;
	}
	for (var i = 0; i < rows.length;) {
		var row = rows[0];
		dsAllActors.removeRow(0);
		dsSelected.addRow(row);
	}
}

/**
 * 指派界面的确定按钮事件处理
 */
function assignOk(){
	var mainWidget=window.pageUI.getWidget('main');
	var dsNextHumAct=mainWidget.getDataset("ds_nexthumact");
	
	var assignWidget=window.pageUI.getWidget('assign');
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	var focusRowIndex = dsNextHumAct.getFocusRowIndex();
	var nextHumActRow = dsNextHumAct.getRow(focusRowIndex);
	var selectRowData = dsSelected.getCurrentRowData();
	if (nextHumActRow == null || selectRowData == null || selectRowData.getRows() == null || selectRowData.getRows().length == 0) {
		dsNextHumAct.setValueAt(focusRowIndex,dsNextHumAct.nameToIndex("usernames"), "");
		dsNextHumAct.setValueAt(focusRowIndex,dsNextHumAct.nameToIndex("userpks"), "");
		assignWidget.setVisible(false);
		return;
	}
	var selectedRows = selectRowData.getRows();
	var userNames = [];
	var userPks = [];
	for (var i = 0; i < selectedRows.length; i++) {
		var tmpRow = selectedRows[i];
		var userName = tmpRow.getCellValue(dsSelected.nameToIndex("username"));
		var userPk = tmpRow.getCellValue(dsSelected.nameToIndex("pk_user"));
		if (userPk != null && userPk != "" && userName != null && userName != "") {
			userNames.push(userName);
			userPks.push(userPk);
		}
	}
	dsNextHumAct.setValueAt(focusRowIndex,dsNextHumAct.nameToIndex("usernames"), userNames.join(","));
	dsNextHumAct.setValueAt(focusRowIndex,dsNextHumAct.nameToIndex("userpks"), userPks.join(","));
	assignWidget.setVisible(false);
}

/**
 * 指派界面的取消按钮时间处理
 */
function assignCancel(){
	var assignWidget=window.pageUI.getWidget('assign');
    assignWidget.setVisible(false);
}

/**
 * 提交按钮的时间处理
 * @param {Button} btn 按钮对象
 */
function onSubmit(btn){
	var address = window.location+"";
	if(".html" != address.substring(address.length - 5,address.length))
		return;
	var parentWin = window.getPopParent();
	if(parentWin != null && parentWin.sendData != undefined){
		var data = collectData();
		if(data == null)
			return;
		parentWin.sendData(toJSON(data));
	}	
	window.close();
}
/**
 * 收集数据
 * @return {JSON} json数据对象
 */
function collectData(){

	var mainWidget=window.pageUI.getWidget('main');
	var opinion = mainWidget.getComponent("text_opinion").getValue();
	if (opinion == null) {
		opinion="";
	}
	var scratchpad = mainWidget.getComponent("text_pad").getValue();
	if (scratchpad == null) {
		scratchpad = "";
	}

	var action = mainWidget.getComponent("text_action").getValue();
	if (action == null) {
		action = "";
	}
	var text_msgtype = mainWidget.getComponent("text_msgtype").getValue();
	if (text_msgtype == null) {
		text_msgtype = "";
	}
	var flwinfoObj = {text_opinion:opinion,text_pad:scratchpad,text_action:action,text_msgtype:text_msgtype};
	flwinfoObj.operator = window.$paramsMap.get('operator');
	var dsNexthumact = mainWidget.getDataset("ds_nexthumact");
			
	var meta = dsNexthumact.metadata;
	var rows = dsNexthumact.getSelectedRows();
	var nextacts = [];
	if(rows == null || rows.length == 0){
		alert("请选择下一步活动!");
		return;
	}
	
	for(var i = 0; i < rows.length; i++){
		var nextactsObj = {};
		for(var j = 0; j < meta.length; j++){
			nextactsObj[meta[j].key] = rows[i].getCellValue(j)
		}
		nextacts.push(nextactsObj);
	}
	
	var flowObj = {ds_flwinfo:flwinfoObj,ds_nexthumact:nextacts};
	var data = {flowCxt:flowObj};
	data.taskpk = window.$paramsMap.get('taskpk');
    data.frmdefpk = window.$paramsMap.get('frmdefpk');
    data.pproinspk = window.$paramsMap.get('pproinspk');
    data.operator = window.$paramsMap.get('operator');
    data.outlookToken = getSessionAttribute('outlookToken');
	return data;
}