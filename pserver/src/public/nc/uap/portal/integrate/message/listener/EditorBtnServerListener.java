package nc.uap.portal.integrate.message.listener;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.listener.UifMouseListener;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.vo.pub.lang.UFDateTime;
/**
 * 编辑页鼠标点击事件
 * @author licza
 *
 */
public class EditorBtnServerListener extends UifMouseListener<ButtonComp>{

	public EditorBtnServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onclick(MouseEvent<ButtonComp> e) {
		/**
		 * 提交
		 */
		if("submitbtn".equals(e.getSource().getId())){
			Dataset ds = getCurrentContext().getWidget().getViewModels().getDataset("msgds");
			Row row = ds.getSelectedRow();
			String pk_user = row.getString(ds.nameToIndex("pk_user"));
			String content = row.getString(ds.nameToIndex("content"));
			String username = row.getString(ds.nameToIndex("username"));
			String title = row.getString(ds.nameToIndex("title"));
			Object priorityVal = row.getValue(ds.nameToIndex("priority"));
			int priority = priorityVal == null ? 0 : row.getInt(ds.nameToIndex("priority"));
			
			if(pk_user == null || pk_user.length() <= 0 || username == null || username.length() <= 0){
				throw new LfwRuntimeException("请选择收件人!");
			}
			
			List<PtMessageVO> list = new ArrayList<PtMessageVO>();
			
			String[] pks = pk_user.split(",");
			String[] names = username.split(",");
			if(pks.length != names.length)
				throw new LfwRuntimeException("请选择收件人!");
			for(int i = 0 ; i < pks.length ; i++){
				PtMessageVO msg = new PtMessageVO();
				msg.setPk_user(pks[i]);
				msg.doSetContent(content);
				msg.setTitle(title);
				msg.setUsername(names[i]);
				msg.setPriority(priority);
				PtSessionBean ses = (PtSessionBean)LfwRuntimeEnvironment.getLfwSessionBean();
				if(ses == null)
					return;
				msg.setPk_sender(ses.getPk_user());
				msg.setSendername(ses.getUser_name());
				msg.setSystemcode(MCConstant.PERSON_MESSAGE);
				msg.setMsgtype(0);
				msg.setOwnerstate(0);
				msg.setState("0");
				msg.setSendtime(new UFDateTime());
				list.add(msg);
			}
			try {
				PortalServiceUtil.getMessageService().add(list.toArray(new PtMessageVO[0]));
				InteractionUtil.showMessageDialog("消息发送成功!");
			} catch (PortalServiceException e1) {
				throw new LfwRuntimeException(e1);
			}
		}
		/**
		 * 取消
		 */
		else{
			getGlobalContext().closeWindow();
		}
	}

}
