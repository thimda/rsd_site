package nc.uap.portal.sso.setting;

import java.awt.Dialog;

import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifEditCmd;
import nc.uap.lfw.core.cmd.UifUpdateOperatorState;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.portal.cache.PortalCacheProxy;
import nc.uap.portal.integrate.cache.SSOProviderCache;
import nc.uap.portal.integrate.itf.IPtSsoConfigService;
import nc.uap.portal.vo.PtSsopropVO;

/**
 * @author chouhl
 */
public class MainViewController implements IController {
	private static final long serialVersionUID = 1L;

	public void onDataLoad_ssoSystemsDs(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
	}

	public void onAfterRowSelect_ssoSystemsDs(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		new UifUpdateOperatorState(ds, AppLifeCycleContext.current().getViewContext().getView()).execute();
		ButtonStateManager.updateButtons();
	}

	public void onclick_add(MouseEvent mouseEvent) {
//		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ssoSystemsDs");
		UifEditCmd cmd = new UifEditCmd("ssoView",DialogConstant.DEFAULT_WIDTH, DialogConstant.SIX_ELE_HEIGHT);
		AppLifeCycleContext.current().getWindowContext().addAppAttribute("operate", "add");
		cmd.execute();
//		Row row = ds.getEmptyRow();
//		ds.addRow(row);
//		ds.setRowSelectIndex(ds.getRowIndex(row));
//		ds.setEnabled(true);
//		new UifUpdateOperatorState(ds, AppLifeCycleContext.current().getViewContext().getView()).execute();
	}

	public void onclick_delete(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ssoSystemsDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中删除数据");
		Row row = ds.getSelectedRow();
		String pk_ssoprop = row.getString(ds.nameToIndex("pk_ssoprop"));
		PortalCacheProxy.newIns().locator(new SSOProviderCache(), IPtSsoConfigService.class).deleteByPK(pk_ssoprop);
		ds.removeRow(row);
	}

	public void onclick_modify(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ssoSystemsDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中修改数据");
		Row row = ds.getSelectedRow();
//		row.setState(Row.STATE_UPDATE);
		UifEditCmd cmd = new UifEditCmd("ssoView", DialogConstant.DEFAULT_WIDTH, DialogConstant.SIX_ELE_HEIGHT);
		AppLifeCycleContext.current().getWindowContext().addAppAttribute("operate", "edit");
		AppLifeCycleContext.current().getWindowContext().addAppAttribute("selectRow", row);
		cmd.execute();
		
		
//		ds.setEnabled(true);
//		new UifUpdateOperatorState(ds, AppLifeCycleContext.current().getViewContext().getView()).execute();
//		ButtonStateManager.updateButtons();
	}

	public void onclick_save(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ssoSystemsDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中保存数据");
		WebComponent comp = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getWidget().getViewComponents().getComponent("mainGrid");
		new DefaultDataValidator().validate(ds, (GridComp)comp);
		Row[] rows = ds.getSelectedRows();
		if(rows != null && rows.length > 0){
			for(Row row : rows){
				PtSsopropVO ssoProp = new PtSsopropVO();
				ssoProp.setPk_ssoprop(row.getString(ds.nameToIndex("pk_ssoprop")));
				ssoProp.setSystemcode(row.getString(ds.nameToIndex("systemCode")));
				ssoProp.setSystemname(row.getString(ds.nameToIndex("systemName")));
				ssoProp.setEnablemapping(row.getUFBoolean(ds.nameToIndex("enableMapping")));
				ssoProp.setAuthclass(row.getString(ds.nameToIndex("authClass")));
				ssoProp.setGateurl(row.getString(ds.nameToIndex("gateUrl")));
				switch(row.getState()){
					case Row.STATE_ADD:
						PortalCacheProxy.newIns().locator(new SSOProviderCache(), IPtSsoConfigService.class).add(ssoProp);
						break;
					case Row.STATE_UPDATE:
						PortalCacheProxy.newIns().locator(new SSOProviderCache(), IPtSsoConfigService.class).update(ssoProp);
						break;
					default:
						break;
				}
				row.setState(Row.STATE_NORMAL);
			}
		}
		ds.setEnabled(false);
	}

	public void onclick_setProperties(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ssoSystemsDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中修改数据");
		Row row = ds.getSelectedRow();
		String systemCode = row.getString(ds.nameToIndex("systemCode"));
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("systemCode", systemCode);
		AppLifeCycleContext.current().getWindowContext().popView("setPropView", "460", "400", "设置属性");
	}

	public void onclick_ipMapping(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ssoSystemsDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中修改数据");
		Row row = ds.getSelectedRow();
		String systemCode = row.getString(ds.nameToIndex("systemCode"));
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(
				"systemCode", systemCode);
		AppLifeCycleContext.current().getWindowContext().popView(
				"ipMappingView", "460", "400", "IP映射");
	}
	
}
