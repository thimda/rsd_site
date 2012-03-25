package nc.uap.portal.view.menu;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.util.ToolKit;

public class OutLookMenuController implements IController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5385710370594829984L;

	public void dataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		String pk_menucategory = ds.getId();
		LfwSessionBean sb = LfwRuntimeEnvironment.getLfwSessionBean();
		String pk_user = "0000AA100000000027FF";
		if(sb != null)
			pk_user = sb.getPk_user();
		fetchMenu(ds, pk_menucategory);
	}
	
	public void fetchMenu(Dataset ds, String pk_menucategory){
		try {
			MenuItemAdapterVO[] mis = CpbServiceFacility.getMenuQry().getMenuItemsByParent(pk_menucategory);
			if(ToolKit.notNull(mis)){
				for(MenuItemAdapterVO mi : mis){
					Row row = ds.getEmptyRow();
					String url = mi.getFunnode().getUrl();
					String pk = mi.getPk_menuitem();
					row.setString(ds.nameToIndex(OutLookMenuPageModel.ID_FIELD), pk);
					row.setString(ds.nameToIndex(OutLookMenuPageModel.LABEL_FIELD), mi.getTitle());
					row.setString(ds.nameToIndex(OutLookMenuPageModel.URL_FIELD), url);
					row.setString(ds.nameToIndex(OutLookMenuPageModel.PID_FIELD), mi.getMenuitem().getPk_parent());
					ds.addRow(row);
					if(url == null || url.length() == 0){
						fetchMenu(ds, pk);
					}
				}
			}
		} catch (CpbBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
	
	public void onAfterRowSelect(DatasetEvent e){ 
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		String url = row .getString(ds.nameToIndex(OutLookMenuPageModel.URL_FIELD));
		String pk_menucategory = row .getString(ds.nameToIndex(OutLookMenuPageModel.ID_FIELD));
		if(url == null){
			 return;
		}else{
			AppLifeCycleContext.current().getApplicationContext().addExecScript("document.getContainer().doAction('" + url + "');");
		}
	}
	
}
