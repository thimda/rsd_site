package nc.uap.portal.integrate.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.INotifyAbleCache;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.integrate.itf.IPtSsoConfigQryService;
import nc.uap.portal.integrate.system.SSOProviderVO;

/**
 * µ•µ„µ«¬º≈‰÷√–≈œ¢ª∫¥Ê
 * @author licza
 *
 */
public class SSOProviderCache implements INotifyAbleCache{

	@Override
	public Object build() {
		Map<String, SSOProviderVO> SSOProviders = null;
			SSOProviders = new ConcurrentHashMap<String, SSOProviderVO>();
			try {
				List<SSOProviderVO> ps = NCLocator.getInstance().lookup(IPtSsoConfigQryService.class).getAllConfig();
				if(ps != null && !ps.isEmpty()){
					for(SSOProviderVO vo : ps){
						SSOProviders.put(vo.getSystemCode(), vo);
					}
				}
			} catch (Exception e) {
				LfwLogger.error("===PortalCacheManager#getSSOProviderVO===ªÒµ√SSO≈‰÷√¥ÌŒÛ:" + e.getMessage() ,e );
			}
		return SSOProviders;
	}

	@Override
	public String getKey() {
		return CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE;
	}

	@Override
	public String getNameSpace() {
		return CacheKeys.PORTAL_SSO_PROVIDER_CACHE;
	}

}
