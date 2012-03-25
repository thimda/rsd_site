package nc.uap.portal.portlets;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.WebKeys;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.inte.Constants;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.PortletSessionUtil;
import nc.uap.portlet.iframe.BaseIframePortlet;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * <b>系统集成Portlet</b> 制作登陆凭证<br/>
 * @author licza
 * @since NC 6.0
 */
public class WebApplicationPortlet extends BaseIframePortlet {
	protected String systemCode = null;
	// 本portlet的ID
	protected String portletId;
	// 共享级别
	protected String sharelevel = null;
	protected IWebAppLoginService loginService;
	protected String funcode = null;
	
	/**验证页面**/
	protected static final String AUTH_PAGE= "/core/uimeta.jsp?pageId=credential&model=nc.portal.sso.pagemodel.CredentialEditPageModel";
 
	/**
	 * 初始化参数
	 * @param request
	 */
	public void init(PortletRequest request) {
		PortletPreferences preference = request.getPreferences();
		sharelevel = preference.getValue("share_level", String.valueOf(WebKeys.PORTLET_SHARE_APPLICATION));
		portletId = request.getWindowID();
		systemCode  = preference.getValue("system_code", null);
		funcode = preference.getValue("funcode", null);
		if (Logger.isDebugEnabled())
			Logger.debug("===WebApplicationPortlet类init方法:WebApplicationPortlet启动,systemCode=" + systemCode + ",sharelevel=" + sharelevel + ",portletId="
					+ portletId);
		// 获得gateUrl
		if (systemCode == null) {
			Logger.warn("===WebApplicationPortlet类init方法:portletId=" + portletId + ",systemCode is null!");
			throw new NullPointerException("portlet配置信息里没有找到配置的系统编码!");
		}
	}
	@Override
	protected void doDispatch(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		init(request);
		super.doDispatch(request, response);
	}
	@Override
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			Logger.debug("===WebApplicationPortlet类doEdit方法:进入WebApplicationPortlet的doEdit方法,进入用户信息录入页!");
			// 获得当前portal登录用户
			portletId = request.getWindowID();
			SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(getSystemCode());
			if(provider == null){
				LfwLogger.error("没有配置" + getSystemCode());
				response.getWriter().write("没有配置" + getSystemCode());
				return;
			}
			HttpServletRequest httpRequest = LfwRuntimeEnvironment.getWebContext().getRequest();
			request.setAttribute(HEIGHT_PARAM, "0");
			// 转向验证表单页面
			String contextPath=httpRequest.getContextPath();
			request.setAttribute(SRC_PARAM, PortletSessionUtil.makeAnchor(contextPath + AUTH_PAGE + "&portletId="+portletId+"&systemCode="+getSystemCode()+"&sharelevel=" + sharelevel,portletId ));
			include(request, response);
		} catch (Exception e) {
			Logger.error("===WebApplicationPortlet类doEdit方法:系统未正确配置或中间件没有启动!", e);
			throw new PortletException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "WebApplicationPortlet-000000", null,
					new String[] { e.getMessage() })/* 第三方集成系统未正确配置或中间件没有启动:{0} */);
		}
	}

	@Override
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PortletSession portletSession = request.getPortletSession();
		try {
			portletId = request.getWindowID();
			Logger.debug("===WebApplicationPortlet类doView方法:进入WebApplicationPortlet的doView方法!");
			HttpServletRequest httpReq = LfwRuntimeEnvironment.getWebContext().getRequest();
			HttpServletResponse httpResp = LfwRuntimeEnvironment.getWebContext().getResponse();
			PtCredentialVO credential = getCredentialVO(httpReq);
			String params = request.getParameter("_param");
			if (credential == null) {
				Logger.debug("===WebApplicationPortlet类doView方法:没有获得该用户的凭证信息,进入制造凭证页面.");
				// 如果没有凭证,转向EDIT模式,让用户即可输入信息以获得凭证
				doEdit(request, response);
			} else {
				Logger.debug("===WebApplicationPortlet类doView方法:成功获取用户已有凭证,利用该凭证获取登录URL信息.");
				request.setAttribute("SYSTEM_CODE", getSystemCode());
				request.setAttribute("SYSTEM_NAME", getSystemName());
				SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(getSystemCode());
				// 将当前页面分辨率设置到request属性中
				httpReq.setAttribute("screenWidth", httpReq.getSession().getAttribute("screenWidth"));
				httpReq.setAttribute("screenHeight", httpReq.getSession().getAttribute("screenHeight"));
				httpReq.setAttribute("$portletId", portletId);
				
				String gateUrl = null;
				if(funcode == null || funcode.length() == 0)
					gateUrl = getAuthService().getGateUrl(httpReq, httpResp, credential, provider);
				else
					gateUrl = getAuthService().getNodeGateUrl(httpReq, httpResp,funcode, credential, provider);
				Logger.debug("===WebApplicationPortlet类doView方法:获取该用户的登录系统gateURL=" + gateUrl);
				request.setAttribute(WebKeys.APPLICATION_PORTLET_URL_KEY, gateUrl);
				request.getPortletSession().setAttribute("portletWindowState", request.getWindowState().toString());
				request.setAttribute(SRC_PARAM, gateUrl + (params == null ? "" : params) );
				request.setAttribute(HEIGHT_PARAM, "0");
				request.setAttribute(SRC_TYPE, "scr");
				include(request, response);
			}
		} catch (Exception e) {
			Logger.error("请确保所访问系统正确配置且启动!", e);
			portletSession.setAttribute(Constants.CREDENTIAL_PROCESS_ERROR, "登录发生错误,数据源配置更改或者用户信息更改了!");
			doEdit(request, response);
		}
	}

	protected PtCredentialVO getCredentialVO(HttpServletRequest req) throws PortalServiceException {
		// 获得当前portal登录用户
		IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		String userId = userVO.getUserid();
		Logger.debug("===WebApplicationPortlet类getCredentialVO方法:当前登录用户=" + userId);
		// view前判断用户是否有登录这个应用的凭证
		PtCredentialVO credential = PintServiceFactory.getSsoQryService().getCredentials(userId, portletId, getSystemCode(), new Integer(sharelevel));
		if (Logger.isDebugEnabled()) {
			if (credential == null)
				Logger.debug("===WebApplicationPortlet类getCredentialVO方法:credential为null!!!userId=" + userId + ",portletId=" + portletId + ",systemCode="
						+ getSystemCode());
		}
		return credential;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		super.processAction(request, response);
	}

	protected String getSystemCode() {
		return systemCode;
	}

	/**
	 * 获取认证服务类
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	protected IWebAppLoginService getAuthService() throws PortalServiceException {
			loginService = ProviderFetcher.getInstance().getAuthService(getSystemCode());
		return loginService;
	}

	protected String getSystemName() {
		return getProvider().getSystemName();
	}

	protected SSOProviderVO getProvider() {
		return ProviderFetcher.getInstance().getProvider(getSystemCode());
	}
	/**
	 * 重载include方法
	 */
	protected void include(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PortletContext context = getPortletContext();
		if(request.getAttribute(SRC_TYPE) == null)
			request.setAttribute(SRC_TYPE, "src");
		PortletRequestDispatcher requestDispatcher = context.getRequestDispatcher(getFramePage());
		requestDispatcher.include(request, response);
	}
	
}
