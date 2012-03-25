package nc.uap.portal.integrate.message.listener;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.MessageQueryParam;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;

/**
 * 消息中心导航数据集监听
 * 
 * @author licza
 * 
 */
public class NavTreeDsListener extends DefaultDatasetServerListener {

	public NavTreeDsListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		Dataset ds = se.getSource();
		PtMessagecategoryVO[] vos = MessageCenter.getNavData(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
		new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
	}

	@Override
	public void onAfterRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		Dataset msgds = getCurrentContext().getWidget().getViewModels().getDataset("msgds");
		Row row = ds.getSelectedRow();
		String syscode = row.getString(ds.nameToIndex("syscode"));
		String parentId = row.getString(ds.nameToIndex("parentid"));
		String pluginid = row.getString(ds.nameToIndex("pluginid"));
		Integer type = row.getInt(ds.nameToIndex("type"));
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		MessageQueryParam param = new MessageQueryParam(syscode,parentId,type,new String[]{"0","1"},pk_user,pluginid);
		MessageCenter.processMessageQry(param, msgds, true);
		String[] tile = MessageCenter.getTile(param);
		if(tile != null && tile.length == 2){
			getGlobalContext().addExecScript("showTab('"+tile[0]+"');");
			getGlobalContext().addExecScript("setSidleTitle('"+tile[1]+"');");
		}
	}

}
