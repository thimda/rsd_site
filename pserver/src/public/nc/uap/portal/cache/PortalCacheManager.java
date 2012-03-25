package nc.uap.portal.cache;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.itf.IBaseIntegrate;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.itf.IPortalMessage;
import nc.uap.portal.integrate.message.itf.IPortalNoticeMessageExchange;
import nc.uap.portal.integrate.message.itf.IPortalTaskMessageExchange;
import nc.uap.portal.integrate.message.itf.IPortalWaringMessageExchange;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalModuleQryService;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PortalDsnameFetcher;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortletDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtDisplayVO;
import nc.uap.portal.vo.PtDisplaycateVO;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;
import nc.uap.portal.vo.PtProtalConfigVO;
import nc.vo.pub.SuperVO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Portal缓存管理
 * 
 * @author licza
 * 
 */
public class PortalCacheManager {
	
	/**
	 * 获得数据源名称
	 * 
	 * @return
	 */
	public static String getDs() {
		String ds = LfwRuntimeEnvironment.getDatasource();
		if(ds == null)
			ds = PortalDsnameFetcher.getPortalDsName();
		return null;
	}
	/**
	 * 根据模块名获得模块定义
	 * @param module
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ModuleApplication getModuleAppByModuleName(String module){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLETS_CACHE, getDs());
		Map<String,ModuleApplication> kma = (Map<String,ModuleApplication>) cache.get(CacheKeys.MODULE_DEFINITION_CACHE);
		if(kma == null)
		{
			kma = new ConcurrentHashMap<String, ModuleApplication>();
			cache.put(CacheKeys.MODULE_DEFINITION_CACHE,kma);
		}
		if(!kma.containsKey(module))
		{
			IPtPortalModuleQryService mq = PortalServiceUtil.getModuleQryService();
			ModuleApplication ma = mq.getModuleAppByModuleName(module);
			kma.put(module, ma);
		}
		return kma.get(module);
	}
	@SuppressWarnings("unchecked")
	public static ModuleApplication[] getModuleAppByModuleNs(String ns){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLETS_CACHE, getDs());
		Map<String,ModuleApplication[]> kma = (Map<String,ModuleApplication[]>) cache.get(CacheKeys.NS_MODULE_DEFINITION_CACHE);
		if(kma == null)
		{
			kma = new ConcurrentHashMap<String, ModuleApplication[]>();
			cache.put(CacheKeys.NS_MODULE_DEFINITION_CACHE,kma);
		}
		if(!kma.containsKey(ns))
		{
			IPtPortalModuleQryService mq = PortalServiceUtil.getModuleQryService();
			ModuleApplication[] ma = mq.getModuleAppByNs(ns);
			kma.put(ns, ma);
		}
		return kma.get(ns);
	}
	
 
 
	/**
	 * 获得用户自定义Portlet缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, PortletDefinition>> getUserDiyPortletsCache(){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLETS_CACHE, getDs());
		Map<String, Map<String, PortletDefinition>> usersPortlets = (Map<String, Map<String, PortletDefinition>>) cache.get(CacheKeys.USER_DIY_PORTLETS_CACHE);
		if (usersPortlets == null) {
			try {
				PtPortletVO[] userPortletVOs = PortalServiceUtil.getPortletQryService().getUserDiyPortlets();
				usersPortlets = PortletDataWrap.getUsersDiyPortlets(userPortletVOs);
				if(usersPortlets == null)
					usersPortlets = new ConcurrentHashMap<String, Map<String, PortletDefinition>>();
				cache.put(CacheKeys.USER_DIY_PORTLETS_CACHE, usersPortlets);
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
			}
		} 
		return usersPortlets;
	}
	
	/**
	 * 获得集团Portlet缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, PortletDefinition>>  getGroupPortletsCache(){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLETS_CACHE, getDs());
 		Map<String, Map<String, PortletDefinition>> groupsPortlets = (Map<String, Map<String, PortletDefinition>>) cache.get(CacheKeys.GROUP_PORTLETS_CACHE);
		if(groupsPortlets == null) {
			try {
				PtPortletVO[] groupPortletVOs = PortalServiceUtil.getPortletQryService().getGroupPortlets();
				groupsPortlets = PortletDataWrap.getGroupsPortlets(groupPortletVOs);
				if(groupsPortlets == null)
					groupsPortlets = new ConcurrentHashMap<String, Map<String, PortletDefinition>>();
				cache.put(CacheKeys.GROUP_PORTLETS_CACHE, groupsPortlets);
			} catch (Exception e) {
				LfwLogger.error("查询集团Portal时出错!",e);
				return null;
			}
		}
		return groupsPortlets;
	}
	
	/**
	 * 获得系统级Portlet缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, PortletDefinition> getSystemPortletsCache(){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLETS_CACHE, getDs());
		Map<String, PortletDefinition> systemPortlets = (Map<String, PortletDefinition>) cache.get(CacheKeys.SYSTEM_PORTLETS_CACHE);
		if (systemPortlets == null) {
			try {
				PtPortletVO[] systemPortletVOs = PortalServiceUtil.getPortletQryService().getSystemPortlet();
				systemPortlets = PortletDataWrap.getSystemPortlets(systemPortletVOs);
				if(systemPortlets == null)
					systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
				cache.put(CacheKeys.SYSTEM_PORTLETS_CACHE, systemPortlets);
			} catch (Exception e) {
				LfwLogger.error("获得缓存失败",e.getMessage());
				return null;
			}
		}
		return systemPortlets;
	}
	/**
	 * 清除缓存
	 * @param nameSpace
	 * @param key
	 */
	public static void clearCache(String nameSpace,String key){
		ILfwCache cache = LfwCacheManager.getStrongCache(nameSpace, getDs());
		cache.remove(key);
	}
	
	/**
	 * 通知更新缓存
	 * @param nameSpace
	 * @param key
	 */
	public static void notify(String nameSpace,String key){
		clearCache(nameSpace, key);
		/**
		 * 创建通知消息
		 */
		ClusterCacheMessage ccm = new ClusterCacheMessage(nameSpace, key, IPortalCacheClusterHandle.PID, null);
		/**
		 * 发送消息
		 */
		ClusterCacheInvocationProxy.fire(ccm);
	}
	
	/**
	 * 获得所有用户的页面缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static   Map<String, Page> getUserPageCache(){
		ILfwCache cache = LfwCacheManager.getSessionCache();
		Map<String, Page> userPortalPages = ( Map<String, Page>) cache.get(CacheKeys.USER_PAGES_CACHE);
		if(userPortalPages == null){
			userPortalPages = new ConcurrentHashMap<String, Page>();
			cache.put(CacheKeys.USER_PAGES_CACHE,userPortalPages);
		}
		return userPortalPages;
	}	
	/**
	 * 获得用户Portlet配置
	 * @param window
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Preferences getPreferences(PortletWindow window){
		LfwSessionBean sb = LfwRuntimeEnvironment.getLfwSessionBean();
		if(sb == null)
			return null;
		ILfwCache cache = LfwCacheManager.getSessionCache();
		Map<String,Preferences> pm = (Map<String,Preferences>) cache.get(CacheKeys.USER_PREFERENCES_CACHE);
		/**
		 * 初始化缓存
		 */
		if(pm == null){
			pm = new ConcurrentHashMap<String, Preferences>();
			cache.put(CacheKeys.USER_PREFERENCES_CACHE , pm);
		}
		PortletWindowID win = window.getId();
		String key = win.getStringId();
		Preferences pf = pm.get(key);
		/**
		 * 初始化Portlet配置缓存
		 */
		if(pf == null){
			String pk_user = sb.getPk_user();
			String pk_group = sb.getPk_unit();
			String portletname = PortalPageDataWrap.modModuleName(win.getModule(), win.getPortletName());
			String pagename = PortalPageDataWrap.modModuleName(win.getPageModule(), win.getPageName());
			IPtPortletQryService pq = PortalServiceUtil.getPortletQryService();
			try {
				/**
				 * 取用户自定义配置
				 */
				PtPreferenceVO vo = pq.getUserPortletPreference(pk_user, pk_group, portletname, pagename);
				if(vo == null)
					vo = pq.getGroupPortletPreference(pk_group, portletname, pagename);
				/**
				 * 取集团配置
				 */
				if(vo != null){
					pf = JaxbMarshalFactory.newIns().encodeXML(Preferences.class, vo.doGetPreferences());
				}else
				{
					/**
					 * 取全局配置
					 */
					pf =window.getPortletDefinition().getPortletPreferences();
				}
				pm.put(key, pf);
			} catch (PortalServiceException e) {
				LfwLogger.error("===PortalCacheManager#getPreferences===获得用户Portlet配置错误:" + e.getMessage() ,e );
			}
		}
		return pf;
	}
	

	/**
	 * 获得Portlet分类信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, PortletDisplayCategory> getPortletDisplays(){
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTLET_DISPLAY_CATEGORY, getDs());
		Map<String, PortletDisplayCategory> displays = (Map<String, PortletDisplayCategory>)cache.get(CacheKeys.PORTLET_DISPLAY_CATEGORY);
		if(displays == null){
			displays = new LinkedHashMap<String, PortletDisplayCategory>();
			try {
				SuperVO[] cates = CRUDHelper.getCRUDService().queryVOs(new PtDisplaycateVO(),null,null);
				/**
				 * 加入分组到缓存中
				 */
				if(cates != null && cates.length > 0){
					for(SuperVO vo : cates){
						PtDisplaycateVO cate = (PtDisplaycateVO) vo;
						PortletDisplayCategory pdc = new PortletDisplayCategory(cate);
						displays.put(cate.getId(), pdc);
					}
				}
				SuperVO[] displayvos = CRUDHelper.getCRUDService().queryVOs(new PtDisplayVO(),null,null);
				/**
				 * 加入portlet显示对象到分组中
				 */
				if(displayvos != null && displayvos.length > 0){
					for(SuperVO vo : displayvos){
						PtDisplayVO display = (PtDisplayVO) vo;
						PortletDisplay pd = new  PortletDisplay(display);
						String cateid = display.getCateid();
						PortletDisplayCategory pdc = displays.get(cateid);
						if(pdc != null)
							pdc.addPortletDisplayList(pd);
						else
							LfwLogger.warn("初始化Portlet分类信息的时候未找到"+pd.getTitle()+"的分类:"+cateid );
					}
				}
				cache.put(CacheKeys.PORTLET_DISPLAY_CATEGORY, displays);
			} catch (Exception e) {
				LfwLogger.error("获得Portlet分类信息缓存初始化失败!" + e);
			}
		}
		return displays;
	}
	/**
	 * 获得Portal设置缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getPortalConfig(){
		String dsName = getDs();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_CONFIG_CACHE, dsName);
		Map<String, String> config = (Map<String, String>)cache.get(CacheKeys.PORTAL_CONFIG_CACHE_ITEM);
		if(config == null){
			try {
				config = new ConcurrentHashMap<String, String>();
				PtProtalConfigVO[] cfgvos = PortalServiceUtil.getConfigQryService().getAllPortalConfig();
				if(!PtUtil.isNull(cfgvos)){
					for(PtProtalConfigVO cfgvo : cfgvos){
						config.put(cfgvo.getConfig_key(), cfgvo.getConfig_value());
					}
				}
				cache.put(CacheKeys.PORTAL_CONFIG_CACHE_ITEM,config);
			} catch (PortalServiceException e) {
				LfwLogger.error("Portal配置信息初始化失败",e);
			}
		}
		return config ;
	}

	/**
	 * 获得信息中心缓存
	 * @param cacheKey
	 * @return
	 */
	public static Object getMessageCache(String cacheKey){
		String dsName = getDs();
 		ILfwCache cache = LfwCacheManager.getSoftCache(MessageCenter.MESSAGE_CENTER_KEY, dsName);
 		Object o = cache.get(cacheKey);
 		if(o == null)
 			initMCP();
 		return cache.get(cacheKey);
	}
	/**
 	 * 初始化消息类型与插件对照表
 	 * @return
 	 */
 	private static void initMCP(){
 		String dsName = getDs();
 		ILfwCache cache = LfwCacheManager.getSoftCache(MessageCenter.MESSAGE_CENTER_KEY, dsName);
 		/**
 		 * 消息分类对应的插件字典
 		 */
 		Map<String,String> pcp = new LinkedHashMap<String, String>();
 		/**
 		 * 消息编码和消息名称对应字典
 		 */
 		Map<String,String> icp = new LinkedHashMap<String, String>();
 		/**
 		 * 消息分类对应字典
 		 */
 		Map<String,PtMessagecategoryVO> ncp = new LinkedHashMap<String, PtMessagecategoryVO>();
 		/**
 		 * 消息分类对应的系统编码字典
 		 */
 		Map<String,String> mcp = new LinkedHashMap<String, String>();
 		/**
 		 * 系统对应的消息分类编码
 		 */
 		Map<String,List<String>> ccp = new LinkedHashMap<String,List<String>>();
 		List<PtMessagecategoryVO> mcv = new ArrayList<PtMessagecategoryVO>();
		List<PtExtension> exs = new ArrayList<PtExtension>();
		PluginManager pm = PluginManager.newIns();
		List<PtExtension> messagePlugin = pm.getExtensions(IPortalNoticeMessageExchange.PLUGIN_ID);
		if(messagePlugin != null)
			exs.addAll(messagePlugin);
		messagePlugin = pm.getExtensions(IPortalTaskMessageExchange.PLUGIN_ID);
		if(messagePlugin != null)
			exs.addAll(messagePlugin);
		messagePlugin = pm.getExtensions(IPortalWaringMessageExchange.PLUGIN_ID);
		if(messagePlugin != null)
			exs.addAll(messagePlugin);
		if (CollectionUtils.isNotEmpty(exs)) {
			for (PtExtension ex : exs) {
				try {
					IPortalMessage me = (IPortalMessage) ex.newInstance();
					
					/**
					 * 是否放弃集成此系统
					 */
					boolean giveUp = false;
					if(me instanceof IBaseIntegrate){
						giveUp = ((IBaseIntegrate) me).isGiveUp();
					}
					
					if(me != null && !giveUp && me.getCategory() != null){
						List<PtMessagecategoryVO>  vos = me.getCategory();
						if(!vos.isEmpty()){
							for(PtMessagecategoryVO vo : vos){
								ncp.put(vo.getSyscode(), vo);
								pcp.put(vo.getSyscode(), vo.getPluginid());
								mcp.put(vo.getSyscode(), vo.getParentid());
								icp.put(vo.getSyscode(), vo.getTitle());
								if(ccp.containsKey(vo.getParentid())){
									List<String> cp = ccp.get(vo.getParentid());
									cp.add(vo.getSyscode());
									ccp.put(vo.getParentid(), cp);
								}else{
									List<String> cp = new ArrayList<String>();
									cp.add(vo.getSyscode());
									ccp.put(vo.getParentid(), cp);
								}
								mcv.add(vo);
							}
						}
					}
				} catch (Exception e) {
					LfwLogger.error("获得信息导航数据时获取插件失败", e);
				}
			}
		}
 		cache.put(MessageCenter.PLUGIN_CATEGORY_KEY,pcp);
 		cache.put(MessageCenter.MESSAGE_CATEGORY_KEY,mcp);
 		cache.put(MessageCenter.CATEGORY_SYSTEMCODE_KEY,ccp);
 		cache.put(MessageCenter.MESSAGE_NAV_CATEGORY_KEY,mcv);
 		cache.put(MessageCenter.MESSAGE_NAV_DIC_KEY,ncp);
 		cache.put(MessageCenter.MESSAGE_MAPING_DIC_KEY,icp);
 	}
 	
	public static <T> T getCacheDispache(Class<T> tx){
		PortalCacheInvocationHandler proxy = new PortalCacheInvocationHandler();
		T txt = (T) proxy.bind(tx, NCLocator.getInstance().lookup(tx));
		return txt;
	}
}
class PortalCacheInvocationHandler implements InvocationHandler{
	
	private Object target;
	private Class tx;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		result = method.invoke(target, args);
		Method imd = tx.getMethod(method.getName(), method.getParameterTypes());
		return result;
	}
	public Object bind(Class tx, Object target){
		this.target = target;
		this.tx = tx;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
}