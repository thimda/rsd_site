package nc.uap.portal.integrate.message.itf;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.portal.integrate.message.MessageCenter;

/**
 * 集成系统信息交换基类
 * @since 6.0
 * @version 6.1
 * @author licza
 *
 */
public abstract class AbstractIntergrateNoticeMessageExchage extends AbstractIntergrateMessageExchage implements IPortalNoticeMessageExchange{
	@Override
	public void execute(String[] pk, String cmd, LfwPageContext ctx) {
		MessageCenter.noticeMessageCmdExec(this, pk, cmd, ctx);
	}
}
