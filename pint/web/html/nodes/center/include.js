
function EleNameRender() {
};
EleNameRender.render = function(rowIndex, colIndex, value, header, cell) {
	
	cell.innerHTML = "<a style='text-decoration: underline;cursor: pointer;' onclick=\"doClickTitle(" + rowIndex + ",'"+this.id+"')\"/> " + value + "</a>";
};

function doClickTitle(rowIndex,gridname){
	var dsname = "sentds";
	if(gridname == "msggrid")
		dsname = "msgds";
	var ds = pageUI.getWidget("main").getDataset(dsname);
	var pkIdx = ds.nameToIndex("id");
 	var selectedRow = ds.getRow(rowIndex);
 	var pk = selectedRow.getCellValue(pkIdx);
	
	
	var proxy = new ServerProxy(null,null,true);
	proxy.addParam('clc', 'nc.uap.portal.msg.ctrl.MainController');
	proxy.addParam('el', '2');
	proxy.addParam('source_id', 'main');
	proxy.addParam('source_type', 'widget');
	proxy.addParam('rowIndex', rowIndex);
	proxy.addParam('m_n', 'onTitleClick');
 	proxy.addParam('pk', pk);
	proxy.execute();
}