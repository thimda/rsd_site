package nc.uap.portal.integrate.message.impl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;

/**
 * 协同任务消息
 * @author licza
 *
 */
public class WflNoticeMessageExchange extends PortalNoticeMessageExchange {

	@Override
	public List<PtMessagecategoryVO> getCategory() {
		PtMessagecategoryVO task = new PtMessagecategoryVO("taskmessage",
				MCConstant.TYPE_NOTICE.getSyscode(), "协同任务", "WorkFlowMessage",
				"wflnoticemessage");
		List<PtMessagecategoryVO> mcl = new ArrayList<PtMessagecategoryVO>();
		mcl.add(task);
		return mcl;
	}

	@Override
	public void compose(LfwPageContext ctx) {
		InteractionUtil.showMessageDialog("协同任务消息不支持此操作!");
	}

	@Override
	public void reply(LfwPageContext ctx) {
		InteractionUtil.showMessageDialog("协同任务消息不支持此操作!");

	}

	@Override
	public void fwd(LfwPageContext ctx) {
		InteractionUtil.showMessageDialog("协同任务消息不支持此操作!");
	}
}
