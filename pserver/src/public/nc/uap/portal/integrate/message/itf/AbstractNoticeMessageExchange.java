package nc.uap.portal.integrate.message.itf;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.portal.integrate.message.MessageCenter;

/**
 * 通知消息交换基类
 * @version 6.0
 * @author licza
 */
public abstract class AbstractNoticeMessageExchange extends AbstractPortalMessageExchage implements IPortalNoticeMessageExchange{
	@Override
	public void execute(String[] pk, String cmd, LfwPageContext ctx) {
		MessageCenter.noticeMessageCmdExec(this, pk, cmd, ctx);
	}
}
