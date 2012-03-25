package nc.uap.portal.login;

import nc.uap.portal.user.entity.IUserVO;

/**
 * 匿名登陆配置信息
 * 
 * @author licza
 * 
 */
public class AnnoymousLoginConfig {

	/**
	 * Portal是否支持匿名用户
	 * 
	 * @return
	 */
	public static boolean isSupportAnnoymous() {
		IUserVO user = getAnnoymous();
		return (user != null) && (user.getPk_user() != null);
	}

	/**
	 * 获得匿名用户
	 * 
	 * @return
	 */
	public static IUserVO getAnnoymous() {
//		INotifyAbleCache na = (INotifyAbleCache)LfwClassUtil.newInstance("nc.portal.anonymousmgr.AnnoyMousCache");
//		return PortalCacheProxy.newIns().get(na,IUserVO.class);
		return null;
	}
}
