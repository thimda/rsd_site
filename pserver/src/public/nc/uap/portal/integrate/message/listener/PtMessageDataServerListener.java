package nc.uap.portal.integrate.message.listener;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.MessageQueryParam;
import nc.uap.portal.integrate.message.vo.PtMessageVO;

/**
 * Portal消息加载类
 * @author licza
 *
 */
public class PtMessageDataServerListener extends DefaultDatasetServerListener{

	public PtMessageDataServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		Dataset ds = se.getSource();
		PaginationInfo  pg = ds.getCurrentRowSet().getPaginationInfo();
		MessageQueryParam param = MessageCenter.getMessageQueryParam();
		if(param == null)
			return;
		PtMessageVO[] vos = MessageCenter.query(param, pg);
		if(vos != null){
			new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
			//ds.setCurrentKey(Dataset.MASTER_KEY);
			postProcessRowSelect(ds);
		}
	}

}
