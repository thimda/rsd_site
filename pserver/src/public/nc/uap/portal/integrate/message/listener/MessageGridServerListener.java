package nc.uap.portal.integrate.message.listener;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.CellEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.GridCellServerListener;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.ViewComponents;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;
/**
 * 消息表格事件监听类
 * @author licza
 *
 */
public class MessageGridServerListener  extends GridCellServerListener{

	public MessageGridServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1020666660536454344L;

	@Override
	public void afterEdit(CellEvent e) {
		
	}

	@Override
	public void beforeEdit(CellEvent e) {
		
	}

	@Override
	public void cellEdit(CellEvent e) {
		
	}

	@Override
	public void onCellClick(CellEvent e) {
		ViewComponents comps = getCurrentContext().getWidget().getViewComponents();
		String dsName =  e.getSource().getDataset();
		Dataset ds = getCurrentContext().getWidget().getViewModels().getDataset(dsName);
		Row row = ds.getFocusRow();
		if(row == null)
			return;
		
		UFDateTime _sendtime = (UFDateTime)row.getValue(ds.nameToIndex("sendtime"));
		String systemcode = row.getString(ds.nameToIndex("systemcode"));
		
		String sendtime = (_sendtime == null ? new UFDate(): _sendtime ).toString();
		((LabelComp) comps.getComponent("view_sendtime")).setText(sendtime);
		copyData(ds, row, "view_sender", "sendername", false);
		copyData(ds, row, "view_sendtime", "sendtime", false);
		copyData(ds, row, "view_receiver", "username", false);
		copyData(ds, row, "view_subject", "title", false);
		
		String pk = row.getString(ds.nameToIndex("pk_message"));
		IFrameComp fc = (IFrameComp) comps.getComponent("content_frame");
		String pluginid = MCPageComm.getPluginID(getGlobalContext());
		//设置信息状态为已读
		try {
			String state = row.getString(ds.nameToIndex("state"));
			if( StringUtils.equals(state, "0")){
				MCPageComm.getPortalMessagePlugin(getGlobalContext()).execute(new String[] {pk}, "read", getGlobalContext());
				row.setString(ds.nameToIndex("state"), "1");
			}
		} catch (CpbBusinessException e1) {
			LfwLogger.error("设置信息状态为已读失败!",e1);
		}
		String url = LfwRuntimeEnvironment.getWebContext().getRequest().getContextPath()+"/pt/message/preview?pluginid="+ pluginid +"&systemcode="+systemcode+"&id=";
		fc.setSrc(url+pk);

	}
	/**
	 * 从dataset里面拷贝值填充到lable中
	 * 
	 * @param ds
	 * @param row
	 * @param labelName
	 * @param rowName
	 */
	private void copyData(Dataset ds, Row row, String labelName, String rowName, boolean isHtml) {
		Object value = row.getValue(ds.nameToIndex(rowName));
		if(value != null){
			LabelComp viewComp = (LabelComp) getCurrentContext().getWidget().getViewComponents().getComponent(labelName);
			if (isHtml)
				viewComp.setInnerHTML((String)value);
			else{
				viewComp.setText(value.toString());
			}
		}
	}
}
