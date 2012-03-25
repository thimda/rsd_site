package nc.uap.portal.user.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwSsoUtil;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.util.PortalDsnameFetcher;
/**
 * Portal单点登陆默认实现
 * @author licza
 *
 */
public class PortalSSOLoginHandler extends PortalLoginHandler{
	private static final String DATASOURCE = "datasource";

	@Override
	public AuthenticationUserVO getAuthenticateVO()	throws LoginInterruptedException {
		try {
			String serverIP=LfwSsoUtil.getServerIP();
			String tokenID=LfwSsoUtil.getTokenID();
			String ds = LfwSsoUtil.getDsId();
			LfwTokenVO token = LfwSsoUtil.getRmtToken(serverIP, tokenID,ds);
			if(token==null||token.getTokenid()==null)
				return null;
//				throw new NullPointerException();
			return LfwSsoUtil.getAuthUserVO(token);
		} catch (Exception e) {
			LfwLogger.error("===DefaultLfwSsoServiceImpl===转换单点登陆信息失败", e);
			throw new LfwRuntimeException("转换单点登陆信息失败");
		}
	}

	@Override
	public PtSessionBean doAuthenticate(AuthenticationUserVO userInfo)
			throws LoginInterruptedException {
		String targetDs = null;

		String oldDs = PortalDsnameFetcher.getPortalDsName();
		// portalDataSource = "portal";
		if (oldDs != null && oldDs.trim().length() > 0) {
			if (Logger.isDebugEnabled())
				Logger.debug("设置当前Portal数据源为:" + oldDs);
			LfwRuntimeEnvironment.setDatasource(oldDs);
		}
		else
			throw new LoginInterruptedException("没有指定Portal使用的数据源");

		
		Map<String, String> extMap = (Map<String, String> )userInfo.getExtInfo();
		if(extMap != null && extMap.containsKey(DATASOURCE)){
			targetDs = extMap.get(DATASOURCE);
		}
		Logger.debug("datasource=" + targetDs);
		boolean sameDs = targetDs == null || StringUtils.equals(oldDs, targetDs);
		
		if(!sameDs)
			InvocationInfoProxy.getInstance().setUserDataSource(targetDs);

		try {
			return auth(userInfo);
		} catch (Exception e) {
			if(e instanceof LoginInterruptedException)
				throw (LoginInterruptedException)e;
			else
				throw new LoginInterruptedException(e.getMessage());
		}finally{
			if(!sameDs)
				InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
	}
	
}
