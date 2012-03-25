package nc.uap.portal.container.driver;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.portlet.PortletContextImpl;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortletDataWrap;
import nc.uap.portal.vo.PtPortletVO;

public final class PortletInstanceUtil {
	private static PortletInstanceUtil instance = new PortletInstanceUtil();
	//存放系统Portlet实例
	private Map<String, Portlet> sysPortletMap;
	//存放集团Portlet实例
	private Map<String, Map<String, Portlet>> grpPortletMap;
	//存放用户Portlet实例
	private Map<String, Map<String, Portlet>> userPortletMap;
	
 	private Map<String, PortletContext> portletCtxMap = new HashMap<String, PortletContext>();
 	private Map<String, PortletConfig> portletCfgMap = new HashMap<String, PortletConfig>();
	
	private boolean initialized = false;
	private PortletInstanceUtil() {
	}
	public static PortletInstanceUtil getInstance() {
		return instance;
	}
	
	
	public void initPortlets() {
		if(initialized)
			return;
		try{
			this.initSysPortlets(LfwRuntimeEnvironment.getServletContext());
			this.initGroupPortlets(LfwRuntimeEnvironment.getServletContext());
			this.initUserPortlets(LfwRuntimeEnvironment.getServletContext());
		}
		catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
		}
		finally{
			 initialized = true;
		}
	}
	
	public Portlet getPortlet(String portletId, String pk_org, String pk_user) throws PortletContainerException {
		if(sysPortletMap == null){
			
		}
		Portlet portlet = sysPortletMap.get(portletId);
		if(portlet == null){
				
			Map<String, Portlet> grpMap = grpPortletMap.get(pk_org);
			if(grpMap != null)
				portlet = grpMap.get(portletId);
			
			if(portlet == null){
				Map<String, Portlet> userMap = userPortletMap.get(pk_user);
				if(userMap != null)
					portlet = userMap.get(pk_user);
			}
			
			if(portlet == null){
				String error = "没有为集团:" + pk_org + ",用户:" + pk_user + "找到对应的Portlet实例:"+portletId;
				LfwLogger.error(error);
				throw new PortletContainerException(error);
			}
		}
		return portlet;
	}
	
	public PortletContext getPortletContext(String PortletName){
		return portletCtxMap.get(PortletName);
	}
	
	
	public PortletConfig getPortletConfig(String PortletName){
		return portletCfgMap.get(PortletName);
	}
	
	
	
	private void initGroupPortlets(ServletContext ctx) throws PortalServiceException {
		grpPortletMap = new HashMap<String, Map<String, Portlet>>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		IPtPortletQryService portletRegistry = PortalServiceUtil.getPortletQryService();
		PtPortletVO[] portletVos = portletRegistry.getGroupPortlets();
		if(portletVos != null){
			for (int i = 0; i < portletVos.length; i++) {
				PtPortletVO ptvo = portletVos[i];
				try {
					PortletDefinition portletDD = PortletDataWrap.warpDefinition(ptvo);
					Class<?> clazz = classLoader.loadClass((portletDD.getPortletClass()));
					String module = ptvo.getModule();
					Portlet portlet = (Portlet) clazz.newInstance();
					PortletContextImpl portletContext = new PortletContextImpl(ctx, module);
					PortletConfig portletConfig = new PortletConfigImpl(portletContext, portletDD);
					portlet.init(portletConfig);
					
					Map<String, Portlet> grpMap = grpPortletMap.get(ptvo.getPk_group());
					if(grpMap == null){
						grpMap = new HashMap<String, Portlet>();
						grpPortletMap.put(ptvo.getPk_group(), grpMap);
					}
					String portletName=PortalPageDataWrap.modModuleName(module, portletDD.getPortletName());
					grpMap.put(portletName, portlet);
					portletCtxMap.put(portletName, portletContext);
					portletCfgMap.put(portletName, portletConfig);
				} 
				catch (Throwable e) {
					LfwLogger.error("初始化Portlet出错,portlet id:" + ptvo.getPortletid(), e);
				}
			}
		}
	}
	
	private void initUserPortlets(ServletContext ctx) throws PortalServiceException {
		userPortletMap = new HashMap<String, Map<String, Portlet>>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		IPtPortletQryService portletRegistry = PortalServiceUtil.getPortletQryService();
		PtPortletVO[] portletVos = portletRegistry.getUserDiyPortlets();
		if(portletVos != null){
			for (int i = 0; i < portletVos.length; i++) {
				PtPortletVO ptvo = portletVos[i];
				try {
					PortletDefinition portletDD = PortletDataWrap.warpDefinition(ptvo);
					Class<?> clazz = classLoader.loadClass((portletDD.getPortletClass()));
					String module = ptvo.getModule();
					Portlet portlet = (Portlet) clazz.newInstance();
					PortletContextImpl portletContext = new PortletContextImpl(ctx, module);
					PortletConfig portletConfig = new PortletConfigImpl(portletContext, portletDD);
					portlet.init(portletConfig);
					
					Map<String, Portlet> grpMap = grpPortletMap.get(ptvo.getPk_group());
					if(grpMap == null){
						grpMap = new HashMap<String, Portlet>();
						grpPortletMap.put(ptvo.getPk_group(), grpMap);
					}
					String portletName=PortalPageDataWrap.modModuleName(module, portletDD.getPortletName());
					grpMap.put(portletName, portlet);
					portletCtxMap.put(portletName, portletContext);
					portletCfgMap.put(portletName, portletConfig);
				} 
				catch (Throwable e) {
					LfwLogger.error("初始化Portlet出错,portlet id:" + ptvo.getPortletid(), e);
				}
			}
		}
	}
	
	private void initSysPortlets(ServletContext ctx) throws PortalServiceException {
		sysPortletMap = new HashMap<String, Portlet>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		IPtPortletQryService portletRegistry = PortalServiceUtil.getPortletQryService();
		PtPortletVO[] portletVos = portletRegistry.getSystemPortlet();
		for (int i = 0; i < portletVos.length; i++) {
			PtPortletVO ptvo = portletVos[i];
			try {
				PortletDefinition portletDD = PortletDataWrap.warpDefinition(ptvo);
				Class<?> clazz = classLoader.loadClass((portletDD.getPortletClass()));
				String module = ptvo.getModule();
				Portlet portlet = (Portlet) clazz.newInstance();
				PortletContextImpl portletContext = new PortletContextImpl(ctx, module);
				PortletConfig portletConfig = new PortletConfigImpl(portletContext, portletDD);
				portlet.init(portletConfig);
				String portletName=PortalPageDataWrap.modModuleName(module, portletDD.getPortletName());
				sysPortletMap.put(portletName, portlet);
				portletCtxMap.put(portletName, portletContext);
				portletCfgMap.put(portletName, portletConfig);
			} 
			catch (Throwable e) {
				LfwLogger.error("初始化Portlet出错,portlet id:" + ptvo.getPortletid(), e);
			}
		}
	}
	public PortletConfig getPortletConfig(String module, String portletName) {
		// TODO Auto-generated method stub
		return null;
	}
}