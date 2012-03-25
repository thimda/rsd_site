package nc.uap.portal.integrate.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.cache.PortalCacheProxy;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.IWebAppFunNodesService;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.cache.SSOProviderCache;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * @update lkp 增加获取Provider实例及验证类的方法，使该类成为对外获取ssoProviderVO信息的唯一接口，减轻外部获取sso信息的代码量。
 * @author yzy
 */
public class ProviderFetcher {

	
	/*登录IuserProvider的实现类类名，如果为空，则认为是portaluserProvider*/
	//private String loginProvider; 
	
	private ProviderFetcher() {
		
	}
	
	/**
	 * 根据systemcode获取SSOProviderVO
	 * @param systemCode
	 * @return
	 */
	public SSOProviderVO getProvider(String systemCode){
		if(Logger.isDebugEnabled())
			Logger.debug("===ProviderFetcher类getProvider方法:获取systemCode=" + systemCode + "的SSOProviderVO信息。");
		
		SSOProviderVO provider = (SSOProviderVO)getProvidersPool().get(systemCode);
		if(provider == null)
		{
			if(Logger.isDebugEnabled())
				Logger.debug("===ProviderFetcher类getProvider方法:systemCode=" + systemCode + " 的SSOProviderVO不存在！");
			return null;
		}
		 
		if(!provider.isEnableMapping())
			return provider;
		
		provider = (SSOProviderVO)provider.clone();
		return SsoProviderMappingUtil.mapping(provider);
	}
	
	/**
	 * 获取当前系统配置的所有ProviderVO列表
	 * @return
	 */
	public SSOProviderVO[] listProvider()
	{
		ArrayList<SSOProviderVO> al = new ArrayList<SSOProviderVO>(getProvidersPool().values());
		Logger.debug("===ProviderFetcher类listProvider方法:获取当前所有的SSOProviderVO列表!");
		
		List<SSOProviderVO> enableMappingList = new ArrayList<SSOProviderVO>();
		List<SSOProviderVO> noEnableMappingList = new ArrayList<SSOProviderVO>();
		
		for(int i = 0; i < al.size(); i++)
		{
			if(al.get(i).isEnableMapping())
				enableMappingList.add((SSOProviderVO)al.get(i).clone());
			else
				noEnableMappingList.add(al.get(i));
		}
		
		SSOProviderVO[] processedVOs = SsoProviderMappingUtil.mapping(enableMappingList.toArray(new SSOProviderVO[0]));
		if(processedVOs != null)
			for(int i = 0;i < processedVOs.length; i++)
				noEnableMappingList.add(processedVOs[i]);
		
		return (SSOProviderVO[])noEnableMappingList.toArray(new SSOProviderVO[0]);
	}
	
	/**
	 * 获取所有需要制作凭证的provider对象
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public SSOProviderVO[] listCredentialProvider() 
	{
		SSOProviderVO[] providers = listProvider();
		List<SSOProviderVO> list = new ArrayList<SSOProviderVO>();
		if(providers != null && providers.length != 0)
		{
			String authClass = null;
			Class authClazz = null;
			for(int i = 0; i < providers.length; i++)
			{
				authClass = providers[i].getAuthClass();
				if(authClass != null)
				{
					try {
						authClazz = Class.forName(authClass);
						// 如果是继承自该抽象类,则不需要制造凭证,在用户管理页签也不需要显示
					    if(!SimpleWebAppLoginService.class.isAssignableFrom(authClazz))
					    	list.add(providers[i]);
					} catch(ClassNotFoundException e) {
						 Logger.error("===ProviderFetcher类listCredentialProvider方法:在获取所有需要制造凭证的ssoProviderVO时出现认证类无法找到错误:" + e);
					}
				}
			}
		}
		sortCredentialProvider(list);
		// log出需要制作凭证的所有系统
		if(Logger.isDebugEnabled())
		{	
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < list.size(); i++)
				sb.append(list.get(i).getSystemCode()).append(",");
			Logger.debug("===ProviderFetcher类listCredentialProvider方法:需要制作凭证的系统=" + sb.toString());
		}	
		return list.toArray(new SSOProviderVO[0]);
	}
	
	private void sortCredentialProvider(List<SSOProviderVO> list)
	{
		Collections.sort(list, new Comparator<SSOProviderVO>()
		{
			public int compare(SSOProviderVO vo1, SSOProviderVO vo2)
			{
				if(vo1.getSystemCode().equals("NC") && !vo2.getSystemCode().equals("NC"))
					return -1; 
				if(vo2.getSystemCode().equals("NC") && !vo1.getSystemCode().equals("NC"))
					return 1;
				return vo1.getSystemCode().compareTo(vo2.getSystemCode());
			}
		});
	}
	
	
//	private String getUserProviderClazz() 
//	{
//		return PortalServiceFacility.getClusterCatheService().getUserProvider();
//	}
	
	/**
	 * 获取指定集成系统的登录类实例
	 * @param systemCode
	 * @return
	 * @throws PortalServiceException
	 */
	public IWebAppLoginService getAuthService(String systemCode) throws PortalServiceException 
	{
		try {
			Logger.debug("===ProviderFetcher类getAuthService方法:获取systemCode=" + systemCode + ",对应的IWebAppLoginService的实现类。");
			SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
			if (provider == null) return null;
			String className = provider.getAuthClass();
			Logger.debug("===ProviderFetcher类getAuthService方法:其实现类名称为=" + className);
			IWebAppLoginService webAppService = (IWebAppLoginService)Class.forName(className).newInstance();
			return webAppService;
		} catch (ClassNotFoundException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的认证服务class未找到",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000000")/*应用系统注册的认证服务class未找到，无法在portal中打开该系统*/);
		} catch (InstantiationException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的认证服务class无法实例化",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000001")/*应用系统注册的认证服务class无法实例化，无法在portal中打开该系统*/);
		} catch (IllegalAccessException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的class无法实例化",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000001")/*应用系统注册的认证服务class无法实例化，无法在portal中打开该系统*/);
		}
	}
	/**
	 * 获取指定集成系统的功能节点实例
	 * @param systemCode
	 * @return
	 * @throws PortalServiceException
	 */
	public IWebAppFunNodesService getFunNodeService(String systemCode) throws PortalServiceException 
	{
		try {
			Logger.debug("===ProviderFetcher类getAuthService方法:获取systemCode=" + systemCode + ",对应的IWebAppLoginService的实现类。");
			SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
			if (provider == null) return null;
			String className = provider.getAuthClass();
			Logger.debug("===ProviderFetcher类getAuthService方法:其实现类名称为=" + className);
			IWebAppFunNodesService webAppService = (IWebAppFunNodesService)Class.forName(className).newInstance();
			return webAppService;
		} catch (ClassNotFoundException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的认证服务class未找到",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000000")/*应用系统注册的认证服务class未找到，无法在portal中打开该系统*/);
		} catch (InstantiationException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的认证服务class无法实例化",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000001")/*应用系统注册的认证服务class无法实例化，无法在portal中打开该系统*/);
		} catch (IllegalAccessException e) {
			Logger.error("===ProviderFetcher类getAuthService方法:应用系统注册的class无法实例化",e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "ProviderFetcher-000001")/*应用系统注册的认证服务class无法实例化，无法在portal中打开该系统*/);
		}
	} 
	
	public static ProviderFetcher getInstance() 
	{
		return new ProviderFetcher();
	}
	
	public Map<String, SSOProviderVO> getProvidersPool() 
	{
		return (Map<String, SSOProviderVO>) PortalCacheProxy.newIns().get(new SSOProviderCache());
	}
}
