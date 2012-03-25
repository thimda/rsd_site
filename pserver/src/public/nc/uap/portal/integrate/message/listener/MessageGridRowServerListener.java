package nc.uap.portal.integrate.message.listener;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.GridRowEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.GridRowServerListener;
import nc.uap.lfw.core.log.LfwLogger;

/**
 * 消息中心表格事件监听
 * @author licza
 *
 */
public class MessageGridRowServerListener extends GridRowServerListener{

	public MessageGridRowServerListener(LfwPageContext pagemeta,
			String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onRowDbClick(GridRowEvent e) {
		try {
			 
			Dataset ds = getCurrentContext().getWidget().getViewModels().getDataset( e.getSource().getDataset());
			Row row = ds.getFocusRow();
			if(row == null)
				return;
			String pk = row.getString(ds.nameToIndex("pk_message"));
			MCPageComm.getPortalMessagePlugin(getGlobalContext()).execute(new String[]{pk}, "view", getGlobalContext());
		} catch (CpbBusinessException e1) {
			LfwLogger.error(e1.getMessage(),e1);
		}
	}

}
