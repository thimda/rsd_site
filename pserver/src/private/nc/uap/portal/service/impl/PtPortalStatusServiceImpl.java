package nc.uap.portal.service.impl;

import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.portal.deploy.PortalDeployMessage;
import nc.uap.portal.service.itf.IPtPortalStatusService;

/**
 * Portal状态服务实现类
 * 
 * @author licza
 * 
 */
public class PtPortalStatusServiceImpl implements IPtPortalStatusService {
	private static final String DEPLOY_MSG = "deploy_msg";
	private static final String START_COMPLET = "start_complet";
	private static final String START_COMPLET_SIGN = "portal_core_start_complet";

	@Override
	public boolean isPortalCoreStartComplete() {
		ILfwCache cache = getStatusCache();
		Object o = cache.get(START_COMPLET);
		return (o != null && o.equals("1"));
	}

	private ILfwCache getStatusCache() {
		return LfwCacheManager.getStrongCache(START_COMPLET_SIGN, null);
	}

	@Override
	public void signPortalCoreStartComplete(PortalDeployMessage msg) {
		ILfwCache cache = getStatusCache();
		cache.put(START_COMPLET, "1");
		cache.put(DEPLOY_MSG, msg);
	}

	@Override
	public PortalDeployMessage getDeployMessage() {
		ILfwCache cache = getStatusCache();
		Object o = cache.get(DEPLOY_MSG);
		return o == null ? null : (PortalDeployMessage) o;
	}

}
