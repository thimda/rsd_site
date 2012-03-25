package nc.uap.portal.mng.page;
import nc.portal.portlet.vo.PtPageDeptVO;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.portal.mng.page.itf.IPageManagerService;
import nc.uap.portal.mng.page.itf.IPageManagerQryService;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.ctrl.IController;
import java.util.HashMap;
import nc.uap.lfw.core.event.TabEvent;
import nc.portal.portlet.vo.PtPageUserVO;
import nc.portal.portlet.vo.PtPageRoleVO;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.model.plug.TranslatedRows;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.jsp.uimeta.UITabComp;
/** 
 * @author chouhl
 */
public class PageManagerRelationViewController implements IController {
  private static final long serialVersionUID=1L;
  public void pluginAssign_plugin(  Map<Object,Object> keys){
    String pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
    TranslatedRows trs = (TranslatedRows) keys.get("assignRow");
	  String[] rowKeys = trs.getKeys();
	  int count = trs.getValue("type").size();
	  if(count == 0)
		  return;
	  for(int k = 0; k < count; k ++){
		  Map<String, String> valueMap = new HashMap<String, String>();
		  for(int i = 0; i < rowKeys.length; i ++){
			  String key = rowKeys[i];
			  String value = (String) trs.getValue(key).get(k);
			  valueMap.put(key, value);
		  }
		  String type = valueMap.get("type");
		  
		  IPageManagerQryService qryService = NCLocator.getInstance().lookup(IPageManagerQryService.class);
		  IPageManagerService service = NCLocator.getInstance().lookup(IPageManagerService.class);
		  if("1".equals(type)){
			  
		  }
		  else if("2".equals(type)){
			  String sqlWhere = "pk_page = '" + pk + "' and pk_role = '" + valueMap.get("pk_primary")+"'";
			  PtPageRoleVO[] vos = qryService.getPageRoleVOByCondition(sqlWhere);
			  if(vos == null || vos.length == 0){
				  PtPageRoleVO vo = new PtPageRoleVO();
				  vo.setPk_page(pk);
				  vo.setPk_role(valueMap.get("pk_primary"));
				  service.createPtPageRoleVO(vo);
			  }
		  }
		  else if("3".equals(type)){
			  String sqlWhere = "pk_page = '" + pk + "' and pk_user ='" + valueMap.get("pk_primary") +"'";
			  PtPageUserVO[] vos = qryService.getPageUserVOByCondition(sqlWhere);
			  if(vos == null || vos.length == 0){
				  PtPageUserVO vo = new PtPageUserVO();
				  vo.setPk_page(pk);
				  vo.setPk_user(valueMap.get("pk_primary"));
				  service.createPtPageUserVO(vo);
			  }
		  }
		  else if("4".equals(type)){
			  String sqlWhere = "pk_page = '" + pk + "' and pk_dept ='" + valueMap.get("pk_primary")+"'";
			  PtPageDeptVO[] vos = qryService.getPageDeptVOByCondition(sqlWhere);
			  if(vos == null || vos.length == 0){
				  PtPageDeptVO vo = new PtPageDeptVO();
				  vo.setPk_page(pk);
				  vo.setPk_dept(valueMap.get("pk_primary"));
				  service.createPtPageDeptVO(vo);
			  }
		  }
	  }
//	  System.out.println("**************  " + pk);
//	  System.out.println("**************  " + keys.size());
  }
  private LfwWidget getCurrentWidget(){
    LfwWidget widget = AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getViewContext("relationView").getView();
	return widget;
  }
  public void onDataLoad_pageroleds(  DataLoadEvent dataLoadEvent){
    String pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
	    Dataset ds = dataLoadEvent.getSource();
	    if(pk != null)
	    	ds.setLastCondition("pk_page='" + pk + "'");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onDataLoad_pageuserds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    String pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
    if(pk != null)
    	ds.setLastCondition("pk_page='" + pk + "'");
	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onDataLoad_pagedeptds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    String pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
    if(pk != null)
    	 ds.setLastCondition("pk_page='" + pk + "'");
	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void pluginmain_plugin(  Map<Object,Object> keys){
    TranslatedRow tr = (TranslatedRow) keys.get("gridRow");
//	  String[] rowKeys = tr.getKeys();
	  String pk = (String) tr.getValue("pk_portalpage");
	  LfwWidget widget = getCurrentWidget();
	  Dataset pageUserDs = widget.getViewModels().getDataset("pageuserds");
	  Dataset pageRoleDs = widget.getViewModels().getDataset("pageroleds");
	  Dataset pageDeptDs = widget.getViewModels().getDataset("pagedeptds");
	  AppLifeCycleContext.current().getApplicationContext().addAppAttribute("pk_page", pk);
//	  pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
	  if(pk != null){
		  pageUserDs.setLastCondition("pk_page='" + pk + "'");
		  pageRoleDs.setLastCondition("pk_page='" + pk + "'");
		  pageDeptDs.setLastCondition("pk_page='" + pk + "'");
	  }
	  CmdInvoker.invoke(new UifDatasetLoadCmd(pageUserDs.getId()));
	  CmdInvoker.invoke(new UifDatasetLoadCmd(pageRoleDs.getId()));
	  CmdInvoker.invoke(new UifDatasetLoadCmd(pageDeptDs.getId()));
  }
  public void afterActivedTabItemChange(  TabEvent tabEvent){
	  UITabComp tab = tabEvent.getSource();
//	  System.out.println("*************" + tab.getCurrentItem());
	  LfwWidget widget = getCurrentWidget();
	  Dataset pageUserDs = widget.getViewModels().getDataset("pageuserds");
	  Dataset pageRoleDs = widget.getViewModels().getDataset("pageroleds");
	  Dataset pageDeptDs = widget.getViewModels().getDataset("pagedeptds");
	  String pk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("pk_page");
//	  System.out.println("*********" + pk);
	  if(pk != null){
		  pageUserDs.setLastCondition("pk_page='" + pk + "'");
		  pageRoleDs.setLastCondition("pk_page='" + pk + "'");
		  pageDeptDs.setLastCondition("pk_page='" + pk + "'");
	  }
	  
	  if(tab.getCurrentItem().equals("0")){
		  CmdInvoker.invoke(new UifDatasetLoadCmd(pageRoleDs.getId()));
	  }else if(tab.getCurrentItem().equals("1")){
		  CmdInvoker.invoke(new UifDatasetLoadCmd(pageUserDs.getId()));
	  }else if(tab.getCurrentItem().equals("2")){
		  CmdInvoker.invoke(new UifDatasetLoadCmd(pageDeptDs.getId()));
	  }
	 
  }
}
