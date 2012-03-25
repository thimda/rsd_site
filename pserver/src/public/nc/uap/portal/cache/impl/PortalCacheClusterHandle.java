package nc.uap.portal.cache.impl;

import org.apache.commons.lang.StringUtils;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.ClusterCacheMessage;
import nc.uap.portal.cache.IPortalCacheClusterHandle;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.service.PortalServiceUtil;

/**
 * Portal缓存更新处理
 * @author licza
 *
 */
public class PortalCacheClusterHandle implements IPortalCacheClusterHandle{

	@Override
	public void doIt(ClusterCacheMessage message) {
		String type = message.getType();
		String cacheKey = message.getCacheKey();
		String param = message.getParam();
		String cacheSpace = null;
		LfwLogger.debug("===PortalCacheClusterHandle#doIt=== Process Message : type:" + type +" ;cacheKey: " + cacheKey + ";param : " + param );
		if(StringUtils.equalsIgnoreCase(type, ClusterCacheMessage.TYPE_PAGE)){
			cacheSpace = CacheKeys.PORTTAL_PAGES_CACHE;
		}else if(StringUtils.equalsIgnoreCase(type, ClusterCacheMessage.TYPE_PORTLET)){
			cacheSpace = CacheKeys.PORTLETS_CACHE;
			/**
			 * 更新单个集团的Portlet缓存
			 */
			if(StringUtils.equals(cacheKey, CacheKeys.GROUP_PORTLETS_CACHE) && !StringUtils.isEmpty(param)){
				PortalServiceUtil.getPortletRegistryService().updateGroupCache(param, false);
				return;
			}
		}

		if(cacheSpace != null)
			reBuildCache(cacheSpace, cacheKey);
	}
	
	/**
	 * 重新建立缓存
	 * @param cacheSpace
	 * @param cacheKey
	 */
	public void reBuildCache(String cacheSpace,String cacheKey){
		final long timespan = System.currentTimeMillis();
		LfwLogger.debug("===PortalCacheClusterHandle#reBuildCache=== start cacheSpace:" + cacheSpace + "; cacheKey:" + cacheKey + " timespan :" + timespan); 
		PortalCacheManager.clearCache(cacheSpace, cacheKey);
		LfwLogger.debug("===PortalCacheClusterHandle#reBuildCache=== end cacheSpace:" + cacheSpace + "; cacheKey:" + cacheKey + " timespan :" + timespan); 
	}
}
