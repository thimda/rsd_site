package nc.uap.portal.integrate.itf;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.WebKeys;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.plugins.impl.DynamicalPluginImpl;
import nc.uap.portal.user.entity.IUserVO;

/**
 * 集成接口实现
 * 
 * <pre>
 * 此类是集成接口默认的实现
 * </pre>
 * 
 * @author licza
 * 
 */
public abstract class AbstractIntegrate extends DynamicalPluginImpl implements IBaseIntegrate {
	 
 

	@Override
	public String getSystemCode() {
		return getId();
	}

	@Override
	public String getSystemName() {
		return getTitle();
	}
	/**
	 * 获取sso查询服务
	 * 
	 * @return
	 */
	protected ISSOQueryService getSsoQryService() {
		return NCLocator.getInstance().lookup(ISSOQueryService.class);
	}

	/**
	 * 获取认证服务类
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	protected IWebAppLoginService getAuthService() throws PortalServiceException {
		IWebAppLoginService loginService = ProviderFetcher.getInstance()
				.getAuthService(getSystemCode());
		return loginService;
	}

	/**
	 * 获取SSOProviderVO
	 * 
	 * @return
	 */
	protected SSOProviderVO getProvider() {
		return ProviderFetcher.getInstance().getProvider(getSystemCode());
	}

	/**
	 * 获取用户凭证
	 * 
	 * @param req
	 * @return
	 * @throws PortalServiceException
	 */
	public PtCredentialVO getCredentialVO()  {
		// 获得当前portal登录用户
		IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		String userId = userVO.getUserid();
		Logger.debug("当前登录用户=" + userId);
		// 判断用户是否有登录这个应用的凭证
		PtCredentialVO credential = null;
		try {
			credential = getSsoQryService().getCredentials(userId,this.getClass().getName(), getSystemCode(), getSharelevel());
		} catch (PortalServiceException e) {
			throw new LfwRuntimeException("获得credential出现异常:" + e.getMessage() , e);
		}
		if (credential == null) {
			Logger.debug("获得credential为null! Userid"	+ userId);
		}
		return credential;
	}

	/**
	 * 单点登陆失败,弹出验证窗口
	 * @param portletWindId 要刷新的Portlet窗口
	 * @return
	 */
	public String printAuthFailTemplet(String portletWindId) {
		StringBuffer content = new StringBuffer();
		if(!isGiveUp()){
			content.append("<script>$(function(){");
			content.append("showAuthDialog('");
			content.append(getSystemName());
			content.append("','");
			content.append(getSystemCode());
			content.append("','");
			content.append(portletWindId);
			content.append("','");
			content.append(getSharelevel());
			content.append("');");
			content.append("})</script>");
		}
		return content.toString();
	}


	@Override
	public Integer getSharelevel() {
		return WebKeys.PORTLET_SHARE_APPLICATION;
	}
	
	@Override
	public boolean isIntegrateSystem() {
		return false;
	}
	
	@Override
	public boolean isGiveUp(){
		try {
			PtCredentialVO cd = getCredentialVO();
			/**
			 * 用户自行取消此系统的关联,条件为:
			 * 1.非集成系统
			 * 2.凭据不为空 
			 * 3.凭据的主键为空
			 */
			return !isIntegrateSystem() && cd != null && cd.getPk_credential() == null;
		} catch (Exception e) {
			LfwLogger.warn(e.getMessage());
		}
		return false;
	}
}
