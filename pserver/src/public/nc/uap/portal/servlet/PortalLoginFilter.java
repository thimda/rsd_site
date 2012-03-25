package nc.uap.portal.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.filter.AbstractLfwLoginFilter;
import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.login.AnnoymousLoginConfig;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.PortalDsnameFetcher;
import nc.vo.pub.BusinessException;

/**
 * Portal登陆过滤器
 * 
 * @author licza
 * 
 */
public class PortalLoginFilter extends AbstractLfwLoginFilter {

	private static final String N = "N";
	private static final String P_MAXWIN = "p_maxwin";
	public static final String PORTAL_SYS_TYPE = "pt"; 

	@Override
	protected ILoginSsoService<? extends LfwSessionBean> getLoginSsoService() {
		return PortalServiceUtil.getServiceProvider().getLoginSsoService();
	}

	@Override
	protected String getSysType() {
		return PORTAL_SYS_TYPE;
	}

	/**
	 * 判断用户是否已经登陆 加入匿名用户支持
	 */
	protected boolean isUserLogin(HttpServletRequest request,
			HttpServletResponse response) {
		boolean isLoginflag = hasLogin();
		/**
		 * 已经登陆
		 */
		if (isLoginflag)
			return isLoginflag;
		/**
		 * 设置ds
		 */
		setDataSouceName();

		try {
			boolean annoySupport = AnnoymousLoginConfig.isSupportAnnoymous();
			if (!annoySupport)
				return false;
			/**
			 * 尝试使用匿名用户登陆
			 */
			annoymousLogin();
			/**
			 * 是否登陆成功
			 */
			return hasLogin();
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 是否登陆
	 * 
	 * @return
	 */
	private boolean hasLogin() {
		return LfwRuntimeEnvironment.getLfwSessionBean() != null;
	}

	/**
	 * 匿名登陆
	 * 
	 * @throws BusinessException
	 * @throws LoginInterruptedException
	 */
	private void annoymousLogin() throws BusinessException,
			LoginInterruptedException {
		IUserVO user = AnnoymousLoginConfig.getAnnoymous();
		AuthenticationUserVO userVO = new AuthenticationUserVO();
		userVO.setUserID(user.getUserid());
		String ePassword = user.getPassword();
		String pw = PortalServiceUtil.getEncodeService().decode(ePassword);
		userVO.setPassword(pw);
		Map<String, String> o = new HashMap<String, String>();
		o.put(P_MAXWIN, N);
		userVO.setExtInfo(o);
		getLoginHelper().processLogin(userVO);
	}

	/**
	 * 设置数据源名称
	 */
	private void setDataSouceName() {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		if (dsName == null)
			dsName = PortalDsnameFetcher.getPortalDsName();
		LfwRuntimeEnvironment.setDatasource(dsName);
	}

	public LoginHelper<PtSessionBean> getLoginHelper() {
		return (LoginHelper<PtSessionBean>) LfwLoginFetcher.getGeneralInstance().getLoginHelper();
	}
}
