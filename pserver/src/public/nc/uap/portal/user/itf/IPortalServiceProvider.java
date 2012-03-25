package nc.uap.portal.user.itf;

import nc.uap.lfw.login.itf.ILoginHandler;
import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.portal.deploy.itf.IPtAfterPageDeployService;
import nc.uap.portal.deploy.itf.IPtAfterPortletDeployService;
import nc.uap.portal.deploy.vo.PtSessionBean;

/**
 * Portal用户服务提供者
 * 
 * @author licza
 * 
 */
public interface IPortalServiceProvider {
	/**
	 * 获取Portal用户查询服务
	 * 
	 * @return
	 */
	IPtUserQryService getUserQry();

	/**
	 * 获得登录处理程序
	 * 
	 * @return
	 */
	ILoginHandler<PtSessionBean> getLoginHandler();

	/**
	 * 获得单点登录处理程序
	 * 
	 * @return
	 */
	ILoginHandler<PtSessionBean> getSSOLoginHandler();

	/**
	 * 获得单点登陆服务
	 * 
	 * @return
	 */
	ILoginSsoService<PtSessionBean> getLoginSsoService();


	IPtAfterPageDeployService getPageDeployService();

	IPtAfterPortletDeployService getPortletDeployService();

	IPortalPowerService getPortalPowerService();
}
