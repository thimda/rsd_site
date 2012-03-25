function beforeCallServer(proxy, listenerId, eventName, eleId){
	   var iframe = getPopParent().parent.document.getElementById('iframe_word');
	   if(iframe != null&&listenerId=='pwfmlistener'){
            iframe.contentWindow.saveFileToURL();
       }
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
    var ds=mainWidget.getDataset("ds_prehumact");
	var rowId = ds.getRow(rowIndex).rowId;
	if(value=='true'){
	   cell.innerHTML = "<input type='button' value='选人' style='width:80px' onclick=assign(\'"+rowId+"\')>";
	}else{
	   cell.innerHTML = "<input type='button' value='选人' style='width:80px'  disabled='disabled'>";
	}	
};

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
	var dsNextHumAct=mainWidget.getDataset("ds_prehumact");
	
	var assignWidget=window.pageUI.getWidget('assign');
	var dsSelected=assignWidget.getDataset('ds_selecteds');
	var focusRowIndex = dsNextHumAct.getFocusRowIndex();
	var nextHumActrow = dsNextHumAct.getRow(focusRowIndex);
	var selectRowData = dsSelected.getCurrentRowData();
	if (nextHumActrow == null || selectRowData == null || selectRowData.getRows() == null || selectRowData.getRows().length == 0) {
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
	if(address.lastIndexOf(".html") == address.length-5){
		var parentWin = window.getPopParent();
		if(parentWin != null && parentWin.sendData != undefined){
			var data = collectData();
			parentWin.sendData(toJSON(data));
		}
		window.close();
		return;
	}
}

/**
 * 收集数据
 * @return {JSON} json数据对象
 */
function collectData(){

	var mainWidget=window.pageUI.getWidget('main');
	var dsFlwinfo = mainWidget.getDataset("ds_flwinfo");
			
	var meta = dsFlwinfo.metadata;
	var rowFlwinfo = dsFlwinfo.getSelectedRow();
	var flwinfoObj = {};
	for(var i = 0; i < meta.length; i++){
		flwinfoObj[meta[i].key] = rowFlwinfo.getCellValue(i)
	}
	flwinfoObj.operator = window.$paramsMap.get('operator');
	var dsPrehumact = mainWidget.getDataset("ds_prehumact");
			
	meta = dsPrehumact.metadata;
	var row = dsPrehumact.getSelectedRow();
	if(row == null){
		alert("请选择回退步");
		return;
	}
	var rejectAct = {};
	for(var j = 0; j < meta.length; j++){
		rejectAct[meta[j].key] = row.getCellValue(j)
	}
	var flowObj = {ds_flwinfo:flwinfoObj,ds_prehumact:nextacts};
	var data = {flowCxt:flowObj};
	data.taskpk = window.$paramsMap.get('taskpk');
    data.frmdefpk = window.$paramsMap.get('frmdefpk');
    data.pproinspk = window.$paramsMap.get('pproinspk');
    data.operator = window.$paramsMap.get('operator');
    data.outlookToken = getSessionAttribute('outlookToken');
	return data;
}