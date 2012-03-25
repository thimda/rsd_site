package nc.uap.portal.service.itf;



/**
 * Portal配置信息注册服务
 * @author licza
 *
 */
public interface IPtPortalConfigRegistryService {
	/**
	 * 初始化Portal配置信息单点缓存
	 */
	public void init();
	
	/**
	 * 获得Portal配置信息
	 * @param key
	 * @return
	 */
	public String get(String key);
 
 }
