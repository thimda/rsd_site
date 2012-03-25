package nc.uap.portal.inte;

import nc.bs.framework.common.NCLocator;
import nc.uap.portal.integrate.itf.IPtSsoConfigQryService;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.integrate.sso.itf.ISSOService;

/**
 * portal集成服务工厂
 * @author licza
 *
 */
public class PintServiceFactory {
	/***
	 * 得到SSO查询服务
	 * @return
	 */
	public static ISSOQueryService getSsoQryService(){
		return NCLocator.getInstance().lookup(ISSOQueryService.class);
	}
	/**
	 * 得到SSO服务
	 * @return
	 */
	public static ISSOService getSsoService(){
		return NCLocator.getInstance().lookup(ISSOService.class);
	}
	/**
	 * 获得sso配置信息查询服务
	 * @return
	 */
	public static IPtSsoConfigQryService getSsoConfigQryService(){
		return NCLocator.getInstance().lookup(IPtSsoConfigQryService.class);
	}
}
