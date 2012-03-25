package nc.uap.portal.portlets.integration;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.impl.IntegrationLoginWithForm;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.portlets.WebApplicationPortlet;

/**
 * 利用第三方系统登录表单单点集成第三方系统的通用Portlet
 * @author gd 2009-10-16
 * @version NC5.6
 * @since NC5.6
 *
 */
public class IntegrationPortletWithForm extends WebApplicationPortlet 
{
	private IWebAppLoginService loginService;
	@Override
	protected IWebAppLoginService getAuthService() {
		try {
				loginService = ProviderFetcher.getInstance().getAuthService(getSystemCode());
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		if (loginService == null)
			loginService = LfwClassUtil.newInstance(IntegrationLoginWithForm.class);
		return loginService;
	}
 }
