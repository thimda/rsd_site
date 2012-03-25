package nc.portal.portlet.listener;

import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.vo.org.GroupVO;
import nc.vo.pub.SuperVO;
/**
 * Portal页管理页面集团数据加载类
 *
 */
public class GroupDatasetServerListener extends DefaultDatasetServerListener {

	public GroupDatasetServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		try {
			GroupVO[] vos = CRUDHelper.getCRUDService().queryVOs(" 1=1 ", nc.vo.org.GroupVO.class, null, null, null);
			new SuperVO2DatasetSerializer().serialize(vos, se.getSource(), Row.STATE_NORMAL);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	@Override
	protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
		String fk_pageuser = "*";
		vo.setAttributeValue("fk_pageuser", fk_pageuser);
		return super.postProcessRowSelectVO(vo, ds);
	}
}
