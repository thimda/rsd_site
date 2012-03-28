package nc.uap.portal.mng.page;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalPageQryService;
import nc.uap.portal.service.itf.IPtPortalPageRegistryService;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtPageVO;
import nc.uap.portal.vo.PtPortletVO;
import nc.vo.pub.lang.UFBoolean;

import org.xml.sax.SAXException;
public class PageManagerMainViewController implements IController {
  private static final long serialVersionUID=2511687051958509067L;
  public void pageAddEvent(  MouseEvent<MenuItem> mouseEvent){
    String source = mouseEvent.getSource().getId();
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		String groupid = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("orgPk");
		String url = rootPath+ "/html/bin-release/portletdesign.jsp";
		if(groupid != null)
			url += "?groupid=" + groupid;
		if (source.startsWith("new")) {
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.popOuterWindow(url, "Portal布局设计", "1024", "750");
		}
  }
  public void pageEditEvent(  MouseEvent<MenuItem> mouseEvent){
    String source = mouseEvent.getSource().getId();
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		String groupid = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("orgPk");
		
		if (source.equals("layoutedit")) {
			Dataset ds = AppLifeCycleContext.current().getViewContext()
					.getView().getViewModels().getDataset("pageds");
			Row row = ds.getSelectedRow();

			if (row == null) {
				throw new LfwRuntimeException("请选中数据行");
			}
			String pk = row.getString(ds.nameToIndex("pk_portalpage"));
			String url = rootPath + "/html/bin-release/portletdesign.jsp?newpage=false&pk="+ pk;
			if(groupid != null)
				url += "&groupid=" + groupid;
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.popOuterWindow( url, "Portal布局设计", "1024", "750");
		}
  }
  public void pageUpdateEvent(  MouseEvent<MenuItem> mouseEvent){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("pageds");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LfwRuntimeException("请选中要操作的行");
		String version = row.getString(ds.nameToIndex("version"));
		String newversion = row.getString(ds.nameToIndex("newversion"));
		IPtPortalPageRegistryService prs = PortalServiceUtil
				.getRegistryService();

		String pk = row.getString(ds.nameToIndex("pk_portalpage"));
		String module = row.getString(ds.nameToIndex("module"));
		String pagename = row.getString(ds.nameToIndex("pagename"));

		PtPageVO recentPageVO = prs.getPreUpdatePageFromCache(module, pagename);
		if (PtUtil.isNumbic(newversion) && Integer.parseInt(newversion) > 0) {
			if (PtUtil.isNumbic(version)
					&& Integer.parseInt(newversion) <= Integer
							.parseInt(version))
				throw new LfwRuntimeException("当前版本无需更新!");
		}
		try {
			// 最新版本的VO
			if (recentPageVO == null)
				throw new LfwRuntimeException("当前版本无需更新!");
			// 当前版本VO
			PtPageVO currentPageVO = PortalServiceUtil.getPageQryService()
					.getPageByPk(pk);
			currentPageVO.setSettings(recentPageVO.getSettings());
			currentPageVO.setTitle(recentPageVO.getTitle());
			currentPageVO.setIsdefault(recentPageVO.getIsdefault());
			currentPageVO.setLevela(recentPageVO.getLevela());
			currentPageVO.setVersion(recentPageVO.getVersion());
			// 更新到数据库
			PortalServiceUtil.getPageService().update(currentPageVO);
			// 更新集团缓存
			// prs.updateGroupCache(currentPageVO.getPk_group(),Boolean.TRUE);
			row.setValue(ds.nameToIndex("version"), newversion);
		} catch (PortalServiceException e2) {
			LfwLogger.error(e2.getMessage(), e2);
		}
  }
  public void pageApplyEvent(  MouseEvent<MenuItem> mouseEvent){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("pageds");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LfwRuntimeException("请选中要操作的行");
		InteractionUtil.showConfirmDialog("警告", "此操作会将集团下的所有用户的布局更新为当前布局!");
		if (!InteractionUtil.getConfirmDialogResult())
			return;
		String pk = row.getString(ds.nameToIndex("pk_portalpage"));
		try {
			// 更新数据库
			PortalServiceUtil.getPageService().applyPageLayout(pk);
			InteractionUtil.showMessageDialog("应用成功");
		} catch (PortalServiceException e1) {
			throw new LfwRuntimeException("操作失败!");
		}
  }
  public void pageDeleteEvent(  MouseEvent<MenuItem> mouseEvent){
    String[] messages = new String[] { "请选中要删除的行", "警告",
				"此操作会删除集团下的所有用户的布局!", "删除失败!" };
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("pageds");
		Row row = ds.getSelectedRow();
		/**
		 * 未选中行
		 */
		if (row == null)
			throw new LfwRuntimeException(messages[0]);
		/**
		 * 确认删除
		 */
		InteractionUtil.showConfirmDialog(messages[1], messages[2]);
		boolean b = InteractionUtil.getConfirmDialogResult();
		if (!b)
			return;

		String pk = row.getString(ds.nameToIndex(PtPageVO.PK_PORTALPAGE));
		String module = row.getString(ds.nameToIndex(PtPageVO.MODULE));
		String pagename = row.getString(ds.nameToIndex(PtPageVO.PAGENAME));
		String key = PortalPageDataWrap.modModuleName(module, pagename);

		try {
			PtPageVO currentPageVO = PortalServiceUtil.getPageQryService().getPageByPk(pk);
			currentPageVO.setActiveflag(new UFBoolean(false));
			/**
			 * 从数据库中删除
			 */
			LfwLogger.debug("===" + this.getClass().getName()
					+ "===开始从数据库中禁用Portal页面:" + key);

			PortalServiceUtil.getPageService().update(currentPageVO);//delete(pk);

			LfwLogger.debug("===" + this.getClass().getName()
					+ "===从数据库中禁用Portal页面:" + key + "完成");
			/**
			 * 从数据集中删除
			 */
			ds.removeRow(row);
		} catch (PortalServiceException e1) {
			throw new LfwRuntimeException(messages[3]);
		}
  }
  public void adPageSetEvent(  MouseEvent<MenuItem> mouseEvent){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("pageds");
		if(ds.getSelectedIndex() < 0){
			throw new LfwRuntimeException("请选择数据!");
		}
		Row row = ds.getSelectedRow();
		String fatherid = (String) row.getValue(ds.nameToIndex("fatherid"));
		String pk = (String) row.getValue(ds.nameToIndex("pk_portalpage"));
		if(fatherid != null){
			String module = (String) row.getValue(ds.nameToIndex("module"));
			String pagename = (String) row.getValue(ds.nameToIndex("pagename"));
			String url = LfwRuntimeEnvironment.getRootPath() + "/app/mockapp/portletsetting?model=nc.uap.portal.mng.portlet.PortletSettingPageModel&module="+ module + "&pagename="+pagename+"&pk_portlet=" + pk;
			AppLifeCycleContext.current().getApplicationContext().showModalDialog(url, "高级设置", DialogConstant.DEFAULT_WIDTH, DialogConstant.FIVE_ELE_HEIGHT, "ast", true, true	, null);
//			AppLifeCycleContext.current().getApplicationContext().popOuterWindow(url, "高级设置", "400", "300");
			
		}
		else{
			LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("pk_portalpage", pk);
			AppLifeCycleContext.current().getWindowContext().popView("popup", DialogConstant.WITH_MENU_HEIGHT, "300", "高级设置");
		}
  }
  public void pageRefeshEvent(  MouseEvent<MenuItem> mouseEvent){
	  onDataLoad_pageds(null);
  }
  public void onDataLoad_pageds(  DataLoadEvent dataLoadEvent){
    Dataset ds =  AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("pageds");
    
    String pk_group = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
    String orgPk = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("orgPk");
    if(orgPk != null)
		ds.setLastCondition("pk_group = '"+orgPk+"' and fk_pageuser = '*'");
    else
    	ds.setLastCondition("pk_group = '~' and fk_pageuser = '*'");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
		
		Row[] rows = ds.getCurrentRowData().getRows();
		if(rows == null || rows.length == 0)
			return;
		for(int i = 0; i < rows.length; i++){
			Row r = rows[i];
			String pk = (String) r.getValue(ds.nameToIndex("pk_portalpage"));
			String pagename = (String) r.getValue(ds.nameToIndex("pagename"));
			PtPortletVO[] portletVos = getPortlet(pk, pk_group);
			if(portletVos == null || portletVos.length == 0)
				continue;
			for(int j = 0; j < portletVos.length; j++){
				PtPortletVO portletVo = portletVos[j];
				Row row = ds.getEmptyRow();
				row.setValue(ds.nameToIndex("pk_portalpage"), portletVo.getPk_portlet());
				row.setValue(ds.nameToIndex("title"), portletVo.getDisplayname());
				row.setValue(ds.nameToIndex("module"), portletVo.getModule());
				row.setValue(ds.nameToIndex("pagename"), pagename);
				row.setValue(ds.nameToIndex("version"), portletVo.getVersion());
				row.setValue(ds.nameToIndex("newversion"), portletVo.getNewversion());
				row.setValue(ds.nameToIndex("fatherid"), pk);
				ds.addRow(row);
			}
			
		}
//		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
		
		ButtonStateManager.updateButtons();
  }
  private PtPortletVO[] getPortlet(  String pk,  String pk_group){
    IPtPortalPageQryService pageQry = NCLocator.getInstance().lookup(
				IPtPortalPageQryService.class);
	  IPtPortletQryService portletQry = NCLocator.getInstance().lookup(
				IPtPortletQryService.class);
	  PtPortletVO[] portletVos = null;
	  
		try {
			PtPageVO pagevo;
			pagevo = pageQry.getPortalPageVOByPK(pk);
			Page page = PortalPageDataWrap.parsePage(pagevo);
			String[][] portletNames = page.getAllPortletNames();
			if(portletNames != null && portletNames.length > 0){
				portletVos = portletQry.getGroupPortlets(pk_group, portletNames);
			}
		} catch (PortalServiceException e1) {
			LfwLogger.error(e1);
			throw new LfwRuntimeException("查询数据库失败", e1);
		} catch (IOException e2) {
			LfwLogger.error(e2);
			throw new LfwRuntimeException("PML配置解析失败", e2);
		} catch (SAXException e3) {
			LfwLogger.error(e3);
			throw new LfwRuntimeException("PML配置解析失败", e3);
		}
		 
	return portletVos;
  }
  public void onAfterRowSelect_pageds(  DatasetEvent datasetEvent){
	  
	  Dataset currDs = datasetEvent.getSource();
	  Row row = currDs.getSelectedRow();
	  String fatherId = (String) row.getValue(currDs.nameToIndex("fatherid"));
	  if(fatherId != null){
		 LfwWidget widget = AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getViewContext("main").getView();
		 MenubarComp[] menubas = widget.getViewMenus().getMenuBars();
		 if(menubas != null && menubas.length > 0){
			 for(int i = 0; i < menubas.length; i++){
				 MenubarComp menu = menubas[i];
				 List<MenuItem> itemList = menu.getMenuList();
				 if(itemList != null && itemList.size() > 0){
					 MenuItem item;
					for(int j = 0; j < itemList.size(); j++){
						item = itemList.get(j);
						if(!(item.getText().equals("高级设置") || item.getText().equals("分配") || item.getText().equals("刷新"))){
							item.setEnabled(false);
						}
						else 
							item.setEnabled(true);
					}
				 }
			 }
		 }
	  }
	  else
		  ButtonStateManager.updateButtons();
//    Dataset currDs = datasetEvent.getSource();
//		Dataset portletDS = AppLifeCycleContext.current().getViewContext()
//				.getView().getViewModels().getDataset("portletds");
//		Row row = currDs.getSelectedRow();
//		if (row == null) {
//			// 被选中的是集团
//			return;
//		}
//		String pk = (String) row.getValue(currDs.nameToIndex("pk_portalpage"));
//		AppLifeCycleContext.current().getApplicationContext().addAppAttribute("pk_page", pk);
//		String groupPk = (String) row.getValue(currDs.nameToIndex("pk_group"));
//		IPtPortalPageQryService pageQry = NCLocator.getInstance().lookup(
//				IPtPortalPageQryService.class);
//		IPtPortletQryService portletQry = NCLocator.getInstance().lookup(
//				IPtPortletQryService.class);
//		try {
//			PtPageVO pagevo = pageQry.getPortalPageVOByPK(pk);
//			if(pagevo == null)
//				return;
//			Page page = PortalPageDataWrap.parsePage(pagevo);
//			String[][] portletNames = page.getAllPortletNames();
//			PtPortletVO[] portletVos = null;
//			if(portletNames != null && portletNames.length > 0){
//				portletVos = portletQry.getGroupPortlets(groupPk, portletNames);
//			}
//			new SuperVO2DatasetSerializer().serialize(portletVos, portletDS, Row.STATE_NORMAL);
//		} catch (PortalServiceException e1) {
//			LfwLogger.error(e1);
//			throw new LfwRuntimeException("查询数据库失败", e1);
//		} catch (IOException e2) {
//			LfwLogger.error(e2);
//			throw new LfwRuntimeException("PML配置解析失败", e2);
//		} catch (SAXException e3) {
//			LfwLogger.error(e3);
//			throw new LfwRuntimeException("PML配置解析失败", e3);
//		}
//		new UifUpdateOperatorState(currDs, AppLifeCycleContext.current().getViewContext().getView()).execute();
		
  }
  public void pluginsimplequery_plugin(  Map<Object,Object> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys.get("whereSql");
	  String wheresql = whereSql.getWhere();
	  QueryCmd cmd = new QueryCmd("main", "pageds", wheresql){
		  protected String buildWhere(String whereSql){
			 return whereSql + "and pk_group = '"+LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit()+"' and fk_pageuser = '*' and levela = 1";
		  }
	  };
	  cmd.excute();
  }
  public void pageAssignEvent(  MouseEvent<MenuItem> mouseEvent){
	  AppLifeCycleContext.current().getApplicationContext().navgateTo("cp_templateassign", "模板分配", "800", "600");
	  
  }
  public void pluginorg_plugin(Map<Object, Object> paramMap){
//	  System.out.println(paramMap.get("orgValue"));
	  String value = (String) paramMap.get("orgValue");
	  AppLifeCycleContext.current().getApplicationContext().addAppAttribute("orgPk", value);
	  this.pageRefeshEvent(null);
  }
}
