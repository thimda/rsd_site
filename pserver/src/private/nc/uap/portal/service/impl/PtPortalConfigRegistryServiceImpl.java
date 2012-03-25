package nc.uap.portal.service.impl;

import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.service.itf.IPtPortalConfigRegistryService;

/**
 * Portal配置信息注册实现
 * @author licza
 *
 */
public class PtPortalConfigRegistryServiceImpl implements IPtPortalConfigRegistryService {

	@Override
	public void init() {
		PortalCacheManager.notify(CacheKeys.PORTAL_CONFIG_CACHE, CacheKeys.PORTAL_CONFIG_CACHE_ITEM);
	}

	@Override
	public String get(String key) {
		return PortalCacheManager.getPortalConfig().get(key);
	}
}
