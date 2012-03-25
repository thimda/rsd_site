package nc.portal.portlet.listener;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.SuperVO;
/**
 * Portal页管理页面Portal页数据加载类
 *
 */
public class PortalPageDatasetServerListener extends DefaultDatasetServerListener {

	public PortalPageDatasetServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		Dataset ds = se.getSource();
		PtPageVO vo = new PtPageVO();
		vo.setAttributeValue("fk_pageuser", "*");
		vo.setPk_group(LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit());
		SuperVO[] vos = null;
		try {
			vos = queryVOs(ds.getCurrentRowSet().getPaginationInfo(), vo, null);
			new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
			postProcessRowSelect(ds);
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

	@Override
	protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
		vo.setAttributeValue("fk_pageuser", "*");
		return super.postProcessQueryVO(vo, ds);
	}
}
