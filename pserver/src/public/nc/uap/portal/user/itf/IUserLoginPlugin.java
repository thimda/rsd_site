package nc.uap.portal.user.itf;

import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.portal.deploy.vo.PtSessionBean;

/**
 * 用户登录插件
 * 
 * @author licza
 * 
 */
public interface IUserLoginPlugin {
	public static final String ID = "loginplugin";
	/**
	 * 用户登录前操作
	 * @param session
	 */
	void beforeLogin(AuthenticationUserVO userInfo);
	/**
	 * 用户登录后操作
	 * @param session
	 */
	void afterLogin(PtSessionBean session);
}
