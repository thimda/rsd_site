package nc.uap.portal.integrate.message.itf;

import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Portal通知消息交换插件
 * 
 * @author licza
 * 
 */
public interface IPortalNoticeMessageExchange extends IPortalMessage {
	/**
	 * 扩展名
	 */
	public static final String PLUGIN_ID = "PortalNoticeMessage";

	/**
	 * 读信息
	 * 
	 * @param pk
	 */
	public void read(String pk);

	/**
	 * 创建信息
	 * 
	 * @param ctx
	 */
	public void compose(LfwPageContext ctx);

	/**
	 * 答复信息
	 */
	public void reply(LfwPageContext ctx);

	/**
	 * 转发信息
	 */
	public void fwd(LfwPageContext ctx);

	/**
	 * 删除收件箱消息
	 * 
	 * @param pks
	 */
	public void delInbox(String[] pks);

	/**
	 * 删除发件箱信息
	 * 
	 * @param sentpks
	 */
	public void delSent(String[] sentpks);
	
	/**
	 * 查看消息
	 * @param pk
	 * @param ctx
	 */
	public void view(String pk ,LfwPageContext ctx);
}
