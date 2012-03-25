package nc.uap.portal.menu.listener;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpAppsNodeQry;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
/**
 * 树状菜单数据库监听类
 * @author licza
 *
 */
public class MenuTreeDsListener extends DefaultDatasetServerListener {

	public MenuTreeDsListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onAfterRowSelect(DatasetEvent e) {
		Dataset  ds = e.getSource();
		Row row = ds.getSelectedRow();
		if(row != null){
			String pk_funnode = row.getString(ds.nameToIndex("pk_funnode"));
			if(pk_funnode == null || pk_funnode.isEmpty())
				return;
			 try {
				 CpAppsNodeVO[] nodes = NCLocator.getInstance().lookup(ICpAppsNodeQry.class).getNodeByPks(new String[]{pk_funnode});
				 if(nodes == null || nodes.length == 0){
					 return;
				 }
				String execScript = "document.getContainer().doAction('"+ nodes[0].getUrl() +"');";
				getGlobalContext().addExecScript(execScript);
			 } catch (CpbBusinessException e1) {
			}
		}
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		WebContext ctx = LfwRuntimeEnvironment.getWebContext();
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		String category = (String)ctx.getWebSession().getAttribute("category");
		ICpMenuQry mq = NCLocator.getInstance().lookup(ICpMenuQry.class);
		try {
			MenuItemAdapterVO[] mis = mq.getMenuItemsWithPermission(category,pk_user,false,false,true);
			new SuperVO2DatasetSerializer().serialize(mis, se.getSource(), Row.STATE_NORMAL);
		} catch (CpbBusinessException e) {
			LfwLogger.error("===MenuTreeDsListener#onDataLoad===" + e.getMessage(),e.getMessage());
			throw new LfwRuntimeException("获取菜单时出现异常！" + e.getMessage());
		}
	}

}
