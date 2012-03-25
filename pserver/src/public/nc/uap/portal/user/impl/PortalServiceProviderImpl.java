package nc.uap.portal.user.impl;

import nc.uap.lfw.login.itf.ILoginHandler;
import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.portal.deploy.itf.IPtAfterPageDeployService;
import nc.uap.portal.deploy.itf.IPtAfterPortletDeployService;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.user.itf.IPortalPowerService;
import nc.uap.portal.user.itf.IPortalServiceProvider;
import nc.uap.portal.user.itf.IPtUserQryService;

/**
 * Portal用户服务提供者
 * 
 * @author licza
 * 
 */
public class PortalServiceProviderImpl implements IPortalServiceProvider {

	@Override
	public ILoginHandler<PtSessionBean> getLoginHandler() {
		return new PortalLoginHandler();
	}

	@Override
	public ILoginSsoService<PtSessionBean> getLoginSsoService() {
		return new PortalSSOServiceImpl();
	}

	@Override
	public ILoginHandler<PtSessionBean> getSSOLoginHandler() {
		return new PortalSSOLoginHandler();
	}

	@Override
	public IPtUserQryService getUserQry() {
		return null;
	}

 

	public IPtAfterPortletDeployService getAfterPortletDeploy() {
		return new PtAfterPortletDeployImpl();

	}

	@Override
	public IPtAfterPageDeployService getPageDeployService() {
		return new PtAfterPageDeployImpl();
	}

	@Override
	public IPtAfterPortletDeployService getPortletDeployService() {
		return new PtAfterPortletDeployImpl();
	}

	@Override
	public IPortalPowerService getPortalPowerService() {
		return new PtPortalPowerImpl();
	}
}
