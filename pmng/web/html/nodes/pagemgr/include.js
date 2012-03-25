/**
 * portlet高级设置
 */
 
 function portletSettingsRender(){}
 portletSettingsRender.render = function(rowIndex, colIndex, value, header, cell){
 	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	cell.style.textAlign = "center";
	cell.innerHTML = "<a onclick='openPortletSetting("+rowIndex+")' style='cursor:pointer;'>设置</a>";
 };
 
 function openPortletSetting(rowIndex){
 	var pageds = pageUI.getWidget("main").getDataset("pageds");
 	var moduleIndex = pageds.nameToIndex("module");
 	var pagenameIndex = pageds.nameToIndex("pagename");
 	var selectedRow = pageds.getSelectedRow();
 	var moduleVal = selectedRow.getCellValue(moduleIndex);
 	var pagenameVal = selectedRow.getCellValue(pagenameIndex);
 	
 	var portletds = pageUI.getWidget("main").getDataset("portletds");
 	var pk_portletIndex = portletds.nameToIndex("pk_portlet");
 	selectedRow = portletds.getRow(rowIndex);
 	var pk_portletVal = selectedRow.getCellValue(pk_portletIndex);
 	var appUniqueId = getAppUniqueId();
 	
 	var url = "/portal/app/mockapp/portletsetting?model=nc.uap.portal.mng.portlet.PortletSettingPageModel&module="+moduleVal+
 	"&pagename="+pagenameVal+"&pk_portlet="+pk_portletVal+"&appUniqueId="+appUniqueId;
 	showDialog(url,"高级设置","480px","320px","",true,true);
 	/*
 	var proxy = new ServerProxy(null,null,false);
	proxy.addParam('clc', 'nc.uap.portal.mng.page.PageManagerMainViewController');
	proxy.addParam('m_n', 'popupview');
	proxy.addParam('rowIndex', rowIndex);
	proxy.execute();*/
 };
  