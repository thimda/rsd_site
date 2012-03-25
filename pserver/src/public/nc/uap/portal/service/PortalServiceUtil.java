package nc.uap.portal.service;

import nc.bs.framework.cluster.itf.ClusterSender;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.portal.service.itf.IEncodeService;
import nc.uap.portal.service.itf.IPortalDeployService;
import nc.uap.portal.service.itf.IPortalSpService;
import nc.uap.portal.service.itf.IPortalSpecService;
import nc.uap.portal.service.itf.IPtConfigQryService;
import nc.uap.portal.service.itf.IPtConfigService;
import nc.uap.portal.service.itf.IPtMessageCenterQryService;
import nc.uap.portal.service.itf.IPtMessageCenterService;
import nc.uap.portal.service.itf.IPtPageQryService;
import nc.uap.portal.service.itf.IPtPageService;
import nc.uap.portal.service.itf.IPtPluginQryService;
import nc.uap.portal.service.itf.IPtPluginService;
import nc.uap.portal.service.itf.IPtPortalConfigRegistryService;
import nc.uap.portal.service.itf.IPtPortalModuleQryService;
import nc.uap.portal.service.itf.IPtPortalModuleService;
import nc.uap.portal.service.itf.IPtPortalPageRegistryService;
import nc.uap.portal.service.itf.IPtPortalSkinQryService;
import nc.uap.portal.service.itf.IPtPortalSkinService;
import nc.uap.portal.service.itf.IPtPortalStatusService;
import nc.uap.portal.service.itf.IPtPortalThemeQryService;
import nc.uap.portal.service.itf.IPtPortalThemeService;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.service.itf.IPtPortletRegistryService;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.service.itf.IPtWorkDayQryService;
import nc.uap.portal.service.itf.IPtWorkDayService;
import nc.uap.portal.service.itf.PortletAppDescriptorService;
import nc.uap.portal.user.itf.IPortalServiceProvider;
/**
 * Portal服务工具类
 * 
 * @author licza
 * 
 */
public final class PortalServiceUtil {
	/**
	 * 工作日设置服务接口
	 */
	public static IPtWorkDayService getWorkDayService(){
		return NCLocator.getInstance().lookup(IPtWorkDayService.class);
	}

	
	
	public static IPortalSpService getPortalSpService() {
		return NCLocator.getInstance().lookup(IPortalSpService.class);
	}
	/**
	 * 获得Portal配置注册服务
	 * @return
	 */
	public static IPtPortalConfigRegistryService getConfigRegistryService(){
		return NCLocator.getInstance().lookup(IPtPortalConfigRegistryService.class);
	}
	
	/**
	 * 获得Portal配置服务
	 * @return
	 */
	public static IPtConfigService getConfigService(){
		return NCLocator.getInstance().lookup(IPtConfigService.class);
	}
	
	/**
	 * 获得Portal配置查询服务
	 * @return
	 */
	public static IPtConfigQryService getConfigQryService(){
		return NCLocator.getInstance().lookup(IPtConfigQryService.class);
	}
	
	/**
	 * 获得Portal部署服务
	 * @return
	 */
	public static IPortalDeployService getPortalDeployService() {
		return NCLocator.getInstance().lookup(IPortalDeployService.class);
	}
	
	/**
	 * 获得Portal扫描服务
	 * @return
	 */
	public static IPortalSpecService getPortalSpecService() {
		return NCLocator.getInstance().lookup(IPortalSpecService.class);
	}
	
	/**
	 * 获得Portlet处理服务
	 * @return
	 */
	public static PortletAppDescriptorService getPortletAppDescriptorService() {
		return NCLocator.getInstance().lookup(PortletAppDescriptorService.class);
	}
	
	/**
	 * 获得Portal解码服务
	 * @return
	 */
	public static IEncodeService getEncodeService() {
		return NCLocator.getInstance().lookup(IEncodeService.class);
	}

	/**
	 * 获得Portal页面查询服务
	 * 
	 * @return IPtPortalPageQryService
	 */
	public static IPtPageQryService getPageQryService() {
		return NCLocator.getInstance().lookup(IPtPageQryService.class);
	}

	/**
	 * 获得Portal页面服务
	 * 
	 * @return IPtPortalPageService
	 */
	public static IPtPageService getPageService() {
		return NCLocator.getInstance().lookup(IPtPageService.class);
	}
	
	
	/**
	 * 获得查询功能节点服务
	 * @return
	 */
//	public static IPtPortalManagerAppsService getPortalManagerAppsService() {
//		return NCLocator.getInstance().lookup(IPtPortalManagerAppsService.class);
//	}

	/**
	 * 获得Portal页面缓存注册服务
	 * 
	 * @return IPtPortalPageRegistryService
	 */
	public static IPtPortalPageRegistryService getRegistryService() {
		return NCLocator.getInstance().lookup(IPtPortalPageRegistryService.class);
	}

	/**
	 * 获得Portlet查询服务
	 * @return IPtPortletQryService
	 */
	public static IPtPortletQryService getPortletQryService(){
		return NCLocator.getInstance().lookup(IPtPortletQryService.class);
	}

	/**
	 * 获得Portlet服务
	 * @return IPtPortletService
	 */
	public static IPtPortletService getPortletService(){
		return NCLocator.getInstance().lookup(IPtPortletService.class);
	}

	/**
	 * 获得Portlet缓存注册服务
	 * @return IPtPortletRegistryService
	 */
	public static IPtPortletRegistryService getPortletRegistryService(){
		return NCLocator.getInstance().lookup(IPtPortletRegistryService.class);
	}

	/**
	 * 得到消息服务
	 * 
	 * @return IPtMessageCenterService
	 */
	public  static IPtMessageCenterService getMessageService() {
		return NCLocator.getInstance().lookup(IPtMessageCenterService.class);
	}

	/**
	 * 得到信息查询服务
	 * 
	 * @return IPtMessageCenterQryService
	 */
	public static IPtMessageCenterQryService getMessageQryService() {
		return NCLocator.getInstance().lookup(IPtMessageCenterQryService.class);
	}

	/**
	 * 获得Portal资源查询服务
	 * @return IPtResourceQryService
	
	public static IPtResourceQryService getResourceQryService(){
		return NCLocator.getInstance().lookup(IPtResourceQryService.class);
	}

	/**
	 * 获得Portal资源服务
	 * @return IPtResourceService
	
	public static IPtResourceService getResourceService(){
		return NCLocator.getInstance().lookup(IPtResourceService.class);
	}
	 */
	/**
	 * 得到主题服务
	 * @return
	 */
	public static IPtPortalThemeService getThemeService(){
		return NCLocator.getInstance().lookup(IPtPortalThemeService.class);
	}
	/**
	 * 得到主题查询服务
	 * @return
	 */
	public static IPtPortalThemeQryService getThemeQryService(){
		return NCLocator.getInstance().lookup(IPtPortalThemeQryService.class);
	}
	
	/**
	 * 得到样式服务
	 * @return
	 */
	public static IPtPortalSkinService getSkinService(){
		return NCLocator.getInstance().lookup(IPtPortalSkinService.class);
	}
	/**
	 * 得到样式查询服务
	 * @return
	 */
	public static IPtPortalSkinQryService getSkinQryService(){
		return NCLocator.getInstance().lookup(IPtPortalSkinQryService.class);
	}
	
	/**
	 * 获得工作日查询服务
	 * @return
	 */
	public static IPtWorkDayQryService getWorkDayQryService(){
		return NCLocator.getInstance().lookup(IPtWorkDayQryService.class);
	}
	/**
	 * 获得插件查询服务
	 * @return
	 */
	public static IPtPluginQryService getPluginQryService(){
		return NCLocator.getInstance().lookup(IPtPluginQryService.class);
	}
	/**
	 * 获得插件服务
	 * @return
	 */
	public static IPtPluginService getPluginService(){
		return NCLocator.getInstance().lookup(IPtPluginService.class);
	}
 
	/**
	 * 获得模块查询服务
	 * @return
	 */
	public static IPtPortalModuleQryService getModuleQryService(){
		return NCLocator.getInstance().lookup(IPtPortalModuleQryService.class);
	}
	/**
	 * 获得模块服务
	 * @return
	 */
	public static IPtPortalModuleService getModuleService(){
		return NCLocator.getInstance().lookup(IPtPortalModuleService.class);
	}
	/**
	 * 获得集群消息发送者
	 * @return
	 */
	public static ClusterSender getClusterSender(){
		return NCLocator.getInstance().lookup(ClusterSender.class);
	}
	
 
	public static IPortalServiceProvider getServiceProvider(){
		return (IPortalServiceProvider)LfwClassUtil.newInstance("nc.uap.portal.user.impl.PortalServiceProviderImpl");
	}
	
	/**
	 * 获得Portal状态服务
	 * @return
	 */
	public static IPtPortalStatusService getPortalStatusService(){
		return NCLocator.getInstance().lookup(nc.uap.portal.service.itf.IPtPortalStatusService.class);
	}

	
}
