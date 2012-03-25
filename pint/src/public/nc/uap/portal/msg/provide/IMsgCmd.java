package nc.uap.portal.msg.provide;

/**
 * 
 * @author licza
 *
 */
public interface IMsgCmd {
	
	/**
	 * 
	 * 
	 * @param pk
	 *            选中的消息主键
	 * @param cmd
	 *            命令
	 * @param ctx
	 *            当前页面上下文
	 */
	public abstract void execute();
}
