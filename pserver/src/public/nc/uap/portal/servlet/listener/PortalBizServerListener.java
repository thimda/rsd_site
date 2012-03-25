package nc.uap.portal.servlet.listener;

import javax.servlet.ServletContext;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServerListener;
import nc.uap.lfw.login.itf.ILfwSsoService;
import nc.uap.portal.deploy.PortalDeployer;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PortalDsnameFetcher;
import nc.vo.pub.BusinessException;
/**
 * Portal启动回调类，用来初始化portal单点信息
 * Portal启动过程中，需完成
 * 1.运行过程中所需常量的设置(目前一般迁移到请求listener中，即PortalRequestListener).
 * 2.读取并缓存Portlet配置信息。
 * 3.读取并缓存theme信息
 * 4.读取并缓存单点信息
 * 5.资源控制同步
 * 
 * @author dengjt
 * @version NC5.6
 * @since Portal502
 */
public class PortalBizServerListener extends LfwServerListener {
//	private final static String ncHome = LfwRuntimeEnvironment.getRootPath();
	public static final String APP_DOMAIN = "portal.appdomain";
	public PortalBizServerListener(ServletContext ctx) {
		super(ctx);
	}

	protected void doAfterStarted() {
		doInit();
	}

	private void doInit() {
		try {
			System.out.println("\nInitializing master portal server......");
			if (Logger.isDebugEnabled())
				LfwLogger.debug("===PortalBizServerListener类:Begin to initialize Portal!");
		
			String portalDs = PortalDsnameFetcher.getPortalDsName();
			if(portalDs == null || portalDs.equals("")){
				LfwLogger.error("没有获取到NC数据源，不做初始化");
				return;
			}
			String oldDs = null;
			if(portalDs != null){
				oldDs = InvocationInfoProxy.getInstance().getUserDataSource();
				LfwRuntimeEnvironment.setDatasource(portalDs);
			}
			/* 初始化配置信息*/
//
			try {
				initConfig();
			} catch (Throwable e) {
				LfwLogger.error("===PortalBizServerListener类:initConfig error!" + e.getMessage(),e);
			}
			
			try {
				initCtx();
			} catch (Throwable e) {
				LfwLogger.error("===PortalBizServerListener类:initCtx error!" + e.getMessage(),e);
			}

			/* 部署Portal */
			initDeploy();
			
			/**吊销登陆令牌**/
			destoryToken();
			/**
			 * Portal定时任务调度器初始化
			 */
			//new Executor(new PtTimer(LfwRuntimeEnvironment.getDatasource())).start();

			if(oldDs != null)
				LfwRuntimeEnvironment.setDatasource(oldDs);
			System.out.println("master portal server is successfully initialized");
		} catch (Exception e) {
			System.out.println("master portal server is not successfully initialized");
			Logger.error("error occurred while initializing portal container!", e);
		}  
	}
	/**
	 * 初始化部署Portal
	 */
	private void initDeploy(){
		new PortalDeployer().doIt();
	}

//	private void initPlugins() throws Exception {
//		if (Logger.isDebugEnabled())
//			Logger.debug("===PortalBizServerListener类initPlugins方法:begin to read plugins");
//		InputStream stream = null;
//		try{
//			stream = getCtx().getResourceAsStream("/WEB-INF/conf/plugins.xml");
//			Digester digester = PortalDescriptorParseUtil.getPluginsDigester();
//			PluginManager manager = (PluginManager) digester.parse(stream);
//			PortalServiceFacility.getClusterCatheService().setPluginManager(manager);
//		}
//		finally{
//			if(stream != null)
//				stream.close();
//		}
//		//ctx.getResource("/WEB-INF/conf/look-and-feel.xml")
//	}

	private void initConfig() {
//		PortalServiceUtil.getClusterConfigService().initConfigurations();
//		// 创建需要的文件目录
//		String path = PortalProperties.get(PortalPropertiesConstant.NEWS_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.COMPDOC_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.ALBUM_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.USERPHOTO_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.INDEX_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.DOCMANAGE_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.WFTASK_SAVE_PATH);
//		PortalUtil.createDir(path);
//		path = PortalProperties.get(PortalPropertiesConstant.NEWEVENTS_SAVE_PATH);
//		PortalUtil.createDir(path);
	}

//	/**
//	 * 创建内存型软引用内存。region为 uapportal
//	 * TODO 内存大小来自于Portal配置
//	 */
//	private void initNCCache() {
//		CacheConfig config = new CacheConfig("uapportal", true, -1, -1, CacheConfig.CacheType.MEMORY, CacheConfig.MemoryCacheLevel.SOFT);
//		CacheManager.getInstance().getCache(config);
//	}
//
	/**
	 * 初始化相关环境信息
	 * @throws BusinessException 
	 */
	private void initCtx() throws BusinessException {
		ServletContext ctx = getCtx();
		// Initialize
//		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
//		SpringUtil.setContext(springContext);
		
		
//		PortalContextImpl portalCtx = new PortalContextImpl();
//		ctx.setAttribute(WebKeys.PORTALCTX, portalCtx);
		// 设置产品多语资源目录
		ctx.setAttribute(WebConstant.MULTILANGE_PRODUCT_CODE, "portal");
		
		String domain = PortalServiceUtil.getConfigRegistryService().get(APP_DOMAIN);
		if(domain != null && !domain.trim().equals(""))
			ctx.setAttribute(WebConstant.DOMAIN_KEY, domain);
		
		// 设置system.properties中配置的服务地址地址,ca登陆等需要 gd 2007-11-29
//		LfwRuntimeEnvironment.setServerAddr(PortalServiceUtil.getConfigRegistryService().get(SERVER_ADDR));
	}
//
//	/**
//	 * 初始化Portlet的相关信息
//	 * 
//	 * @throws BusinessException
//	 * @throws Exception
//	 */
//	private void initPortlet() throws BusinessException, Exception {
//		if(Logger.isDebugEnabled()) 
//			Logger.debug("===PortalBizServerListener类initPortlet方法:开始从库中查询各个集团下的共有动态portlet和私有动态portlet!");
//		// 从库中查出所有的动态Portlet,根据用户ID分为公有和私有,注册到Portlet运行时缓存中
//		Map<String, List<PortletVO>> shareListMap = new HashMap<String, List<PortletVO>>();
//		Map<String, List<PortletVO>> privateListMap = new HashMap<String, List<PortletVO>>();
//		PortletVO[] portlets = PortalServiceFacility.getPortalQryService().getDynamicPortlets();
//		for (int i = 0; i < portlets.length; i++) {
//			PortletVO portlet = portlets[i];
//			String pk_corp = portlet.getPk_org();
//			if(portlet.getUserId().equals("*")){
//				List<PortletVO> shareList = shareListMap.get(pk_corp);
//				if(shareList == null){
//					shareList = new ArrayList<PortletVO>();
//					shareListMap.put(pk_corp, shareList);
//				}
//				shareList.add(portlet);
//			}
//			else{
//				List<PortletVO> privateList = privateListMap.get(pk_corp);
//				if(privateList == null){
//					privateList = new ArrayList<PortletVO>();
//					privateListMap.put(pk_corp, privateList);
//				}
//				privateList.add(portlet);
//			}
//		}
//		
//		PtCorpVO[] corps = PortalServiceFacility.getPtCorpQryService().getAllCorpsExcepSealed();
//		Iterator<Entry<String, List<PortletVO>>> shareIt = shareListMap.entrySet().iterator();
//		while(shareIt.hasNext()){
//			Entry<String, List<PortletVO>> entry = shareIt.next();
//			String pk_corp = entry.getKey();
//			if(corpValid(pk_corp, corps)){
//				List<PortletVO> shareList = entry.getValue();
//				PortalServiceFacility.getPortletService().addPublicPortlets(shareList.toArray(new PortletVO[shareList.size()]), pk_corp);
//			}
//		}
//		
//		Iterator<Entry<String, List<PortletVO>>> privateIt = privateListMap.entrySet().iterator();
//		while(privateIt.hasNext()){
//			Entry<String, List<PortletVO>> entry = privateIt.next();
//			String pk_corp = entry.getKey();
//			if(corpValid(pk_corp, corps)){
//				List<PortletVO> privateList = entry.getValue();
//				PortalServiceFacility.getPortletService().addPrivatePortlets(privateList.toArray(new PortletVO[privateList.size()]));
//			}
//		}
//		
////		if(corps != null && corps.length > 0)
////		{
////			ArrayList<PortletVO> privateList = new ArrayList<PortletVO>();
////			for(int i = 0; i < corps.length; i++)
////			{
////				ArrayList<PortletVO> shareList = new ArrayList<PortletVO>();
////				PortletVO[] portlets = PortalServiceFacility.getPortalQryService().getDynamicPortlets(corps[i].getPk_corp());
////				if (portlets != null && portlets.length > 0) 
////				{
////					for (int j = 0; j < portlets.length; j++) {
////						if (portlets[j].getUserId().equals("*") || portlets[j].getUserId().equals("#"))
////							shareList.add(portlets[j]);
////						else
////							privateList.add(portlets[j]);
////					}
////				}
////				
////				if(shareList.size() > 0)
////				{
////					Logger.debug("===PortalBizServerListener类initPortlet方法:" + corps[i].getCorpname() + "(pk=" + corps[i].getPk_corp() + ")中获取公共portlet" + shareList.size() + "个!");
////					PortalServiceFacility.getPortletService().addPublicPortlets(shareList.toArray(new PortletVO[shareList.size()]), corps[i].getPk_corp());
////				}
////			}
////			PortalServiceFacility.getPortletService().addPrivatePortlets(privateList.toArray(new PortletVO[privateList.size()]));
////		}
//	}
//
//	private boolean corpValid(String pk_corp, PtCorpVO[] corps) {
//		for (int i = 0; i < corps.length; i++) {
//			if(corps[i].getPk_corp().equals(pk_corp)){
//				return corps[i].getIssealed() == null? true : !corps[i].getIssealed().booleanValue();
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 初始化主题信息
//	 * 
//	 * @throws Exception
//	 */
//	private void initTheme() throws Exception {
//
//		if (Logger.isDebugEnabled())
//			Logger.debug("===PortalBizServerListener类initTheme方法:begin to read portlet theme configuration");
//
//		PortalServiceFacility.getThemeService().initThemeConfig(new URL[] { getCtx().getResource("/WEB-INF/conf/look-and-feel.xml") });
//	}
//
//	/**
//	 * 初始化SSO相关配置信息
//	 * 
//	 * @throws Exception
//	 */
//	private void initSSO() throws Exception {
//		if (Logger.isDebugEnabled()) 
//			Logger.debug("===PortalBizServerListener类initSSO方法:begin to read single-sign-on configuration");
//		ISSOProviderService ssoProviderService = NCLocator.getInstance().lookup(ISSOProviderService.class);
//		ssoProviderService.initProviders();
//
//	}
//
//	/**
//	 * 对其他辅助信息在此处设置
//	 * 
//	 * @throws Exception
//	 */
//	private void initOthers() throws Exception {
//		// 同步portal系统资源(portlet + layout)
//		PortalServiceFacility.getSecurityService().resourceSynchronize(null, getCtx());
//		// 删除可能存在的用户在线记录信息
//		PortalServiceFacility.getSecurityService().deleteExecTimeUser(0);
//	}
//	
//	private void initImport() throws Exception{
//		PtGroupVO[] ptgroupVos = PortalServiceFacility.getPtGroupQryService().getAllGroupsExSealed();
//		for (int i = 0; i < ptgroupVos.length; i++) {
//			GeneratorTaskId generatorTaskId = new GeneratorTaskId();
//			generatorTaskId.generatorTaskId(ptgroupVos[i].getPk_group());
//		}
//	}
	/**
	 * 吊销登陆令牌
	 */
	private void destoryToken(){
		try {
			if(!LfwRuntimeEnvironment.isDevelopMode()){
				NCLocator.getInstance().lookup(ILfwSsoService.class).destoryAllToken();
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error("吊销登陆令牌时出现异常",e);
		}
	}
	
}
