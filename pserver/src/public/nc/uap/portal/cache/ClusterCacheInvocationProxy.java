package nc.uap.portal.cache;

import nc.bs.framework.server.ServerConfiguration;
import nc.uap.portal.service.PortalServiceUtil;

/**
 * 簇缓存通知
 * 
 * @author licza
 * 
 */
public class ClusterCacheInvocationProxy {

	/**
	 * 通知簇更新缓存
	 * 
	 * @param key
	 * @param pluginid
	 * @param value
	 */
	public static void fire(ClusterCacheMessage ccm) {
		ccm.setServer(getLocalServerName());
		PortalServiceUtil.getClusterSender().sendMessage(ccm);
	}

	/**
	 * 获得当前服务器名
	 * 
	 * @return
	 */
	private static final String getLocalServerName() {
		ServerConfiguration cfg = ServerConfiguration.getServerConfiguration();
		return cfg.getServerName();
	}

}
