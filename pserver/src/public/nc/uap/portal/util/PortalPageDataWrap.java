package nc.uap.portal.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.comm.drivers.DriverPhase;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PageMenu;
import nc.uap.portal.om.PageMenuItem;
import nc.uap.portal.om.Portlet;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

/**
 * PortalPage数据包装类
 * 
 * @author licza
 * 
 */
public class PortalPageDataWrap {
	/**
	 * 获得用户的默认Portal页面
	 * 
	 * @param userPages 页面列表
	 * @return 默认页面
	 * @throws PortalServiceException
	 */
	public static Page getUserDefaultPage(Page[] userPages) throws PortalServiceException {
		if (!ArrayUtils.isEmpty(userPages)) {
			for (Page page : userPages) {
				if (page.getIsdefault()) {
					return page;
				}
			}
			return userPages[0];
		} else {
			return null;
		}
	}
	/**
	 * 获得页签 
	 * @param pages
	 * @return
	 */
	public static PageMenu getUserMenu(Page[] pages){
		PageMenu cm = new PageMenu();
		if(pages != null && pages.length > 0){
			for(Page page : pages){
				cm.addItems(getCard(page));
			}
		}
		return cm;
	}

	/**
	 * 获得页签项
	 * 
	 * @param page
	 * @return
	 */
	public static PageMenuItem getCard(Page page){
		if(page == null)
			return null;
		PageMenuItem cmi = new PageMenuItem();
		cmi.setForceshow(page.getForceshow());
		cmi.setIcon(page.getIcon());
		cmi.setId(page.getPagename());
		cmi.setIsdefault(page.getIsdefault());
		cmi.setKeepstate(page.getKeepstate());
		cmi.setLable(page.getLable());
		cmi.setModule(page.getModule());
		cmi.setOrdernum(page.getOrdernum());
		cmi.setTitle(page.getTitle());
		cmi.setUndercontrol(page.getUndercontrol());
		return cmi;
	}
	
	/**
	 * 根据页面名称获得用户的页面
	 * 
	 * @param userPages 页面列表
	 * @param pageModule 页面模块
	 * @param pageName 页面名称
	 * @return 选择的页面
	 * @throws PortalServiceException
	 */
	public static Page getUserPage(Page[] userPages, String pageModule, String pageName) {
			for (Page page : userPages) {
				if (page.getPagename().equals(pageName) && page.getModule().equals(pageModule)) {
					return page;
				}
			}
			return userPages[0];
	}

	/**
	 * 根据页面名称获得用户的页面
	 * 
	 * @param userPages 页面列表
	 * @param pageModule 页面模块
	 * @param pageName 页面名称
	 * @return 选择的页面
	 * @throws PortalServiceException
	 */
	public static Page getUserPage(Page[] userPages, String originalid) {
			for (Page page : userPages) {
				if(PtUtil.isNull(page.getModule())){
					if( page.getPagename().equals(originalid))
						return page;
				}else{
					if (modModuleName(page.getModule(), page.getPagename()).equals(originalid) ) {
						return page;
					}
				}
				
			}
			return null;
	}
	
	/**
	 * 检查是否有新的页面
	 * 
	 * @param userPages 用户当前页面
	 * @param groupPages 获授权页面
	 * @return 用户新获授权的页面
	 */
	public static PtPageVO[] checkNews(PtPageVO[] userPages, PtPageVO[] groupPages) {
		List<PtPageVO> pageList = new ArrayList<PtPageVO>();
		if (!ArrayUtils.isEmpty(groupPages)) {
			for (PtPageVO groupPage : groupPages) {
				if (!containsPage(userPages, groupPage)) {
					pageList.add(groupPage);
				}
			}
		}
		return pageList.toArray(new PtPageVO[0]);
	}

	/**
	 * 是包含集团PortalPage
	 * 
	 * @param userPages
	 * @param groupPage
	 * @return
	 */
	public static Boolean containsPage(PtPageVO[] userPages, PtPageVO groupPage) {
		Boolean contains = false;
		String groupPageKey = PortalPageDataWrap.modModuleName(groupPage.getModule(), groupPage.getPagename());
		if (!ArrayUtils.isEmpty(userPages)) {
			for (PtPageVO page : userPages) {
				String pageKey = PortalPageDataWrap.modModuleName(page.getModule(), page.getPagename());
				if (groupPageKey.equals(pageKey)) {
					contains = true;
				}
			}
		}
		return contains;
	}

	/**
	 * 将PML解析为Page对象
	 * 
	 * @param pageVO
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Page parsePage(PtPageVO pageVO) throws IOException, SAXException {
		Reader pml = new StringReader(pageVO.doGetSettingsStr());
		Page page = (Page) PmlUtil.getPortletDigester().parse(pml);
		page.setPagename(pageVO.getPagename());
		page.setModule(pageVO.getModule());
		page.setUndercontrol(pageVO.getUndercontrol() != null && pageVO.getUndercontrol().booleanValue());
		page.setLevel(pageVO.getLevela());
		return page;
	}

	/**
	 * 将page内容粘贴到pageVO中
	 * 
	 * @param page
	 * @param pageVO
	 * @return
	 */
	public static PtPageVO copyPage2PageVO(Page page, PtPageVO portalPageVO) {
		PtPageVO pageVO;
		if (portalPageVO == null) {
			pageVO = new PtPageVO();
		} else {
			pageVO = (PtPageVO) portalPageVO.clone();
		}
		pageVO.setTitle(page.getTitle());
		pageVO.setVersion(page.getVersion());
		pageVO.setPagename(page.getPagename());
		pageVO.setLevela(page.getLevel());
		pageVO.setUndercontrol(UFBoolean.valueOf(page.getUndercontrol()));
		pageVO.setOrdernum(page.getOrdernum());
		pageVO.setModule(StringUtils.defaultIfEmpty(page.getModule(), pageVO.getModule()));
		pageVO.setFk_pageuser(StringUtils.defaultIfEmpty(pageVO.getFk_pageuser(), "*"));
		pageVO.doSetSettingsStr(page.toXml());
		if (page.getIsdefault() != null) {
			pageVO.setIsdefault(UFBoolean.valueOf(page.getIsdefault()));
		}
		pageVO.setCreatedate(UFDate.getDate(new Date()));
		return pageVO;
	}




	/**
	 * 批量处理单个用户的PortalPage
	 * 
	 * @param portalPageVOs
	 * @throws PortalServiceException
	 */
	public static List<Page> praseUserPages(PtPageVO[] portalPageVOs) throws PortalServiceException {
		List<Page> userPages = new ArrayList<Page>();
		if (ArrayUtils.isEmpty(portalPageVOs))
			return userPages;
		for (PtPageVO portalPageVO : portalPageVOs) {
			Page pd;
			try {
				pd = parsePage(portalPageVO);
				userPages.add(pd);
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e.getCause());
				throw new PortalServiceException(e);
			}
		}
		return userPages;
	}

	/**
	 * 批量处理单个用户的Page
	 * 
	 * @param portalPageVOs
	 * @throws PortalServiceException
	 */
	public static Map<String, Page> praseUserPages(Page[] portalPages) throws PortalServiceException {
		Map<String, Page> systemPortlets = new ConcurrentHashMap<String, Page>();
		if (!ArrayUtils.isEmpty(portalPages)) {
			for (Page portalPage : portalPages) {
				try {
					systemPortlets.put(modModuleName(portalPage.getModule(), portalPage.getPagename()), portalPage);
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e.getCause());
					throw new PortalServiceException(e);
				}
			}
		}
		return systemPortlets;
	}

	 
 
	/**
	 * 合并模块名与ID
	 * 
	 * @param module
	 * @param id
	 * @return
	 */
	public static String modModuleName(String module, String id) {
		StringBuffer sb = new StringBuffer(module);
		sb.append(":");
		sb.append(id);
		return sb.toString();
	}

	/**
	 * 根据用户类型过滤Portal页面
	 * 
	 * @param pages 页面列表
	 * @param userType 用户类型
	 * @return Pages 过滤后页面列表
	 */
	public static PtPageVO[] filterPagesByUserType(PtPageVO[] pages, Integer userType) {
		Set<PtPageVO> userOwerPages = new HashSet<PtPageVO>();
		if (!ArrayUtils.isEmpty(pages)) {
			for (PtPageVO page : pages) {
				if (page.getLevela().equals(userType)) {
					userOwerPages.add(page);
				}
			}
		}
		return userOwerPages.toArray(new PtPageVO[0]);
	}

	
	
 
	/**
	 * 获得用户的页面
	 * @return 用户有权限的页面
	 * @throws UserAccessException 用户未登录
	 */
	public static Page[] getUserPages() throws UserAccessException{
		
		LfwSessionBean sbean = LfwRuntimeEnvironment.getLfwSessionBean();
		/**
		 * 未登陆,重定向到登陆页面
		 */
		if (sbean == null) 
			throw new UserAccessException();
		
		Map<String, Page> userPages = PortalCacheManager.getUserPageCache();
		
		List<Page> pageList = new ArrayList<Page>();
		/**
		 *  过滤掉非用户资源Page
		 */
		try {
			List<Page> userPageCollection = PortalServiceUtil.getServiceProvider().getPortalPowerService().filterPagesByUserResource(userPages.values().toArray(new Page[0]));
			
			DriverPhase dr = DriverPhase.PC;
			if(LfwRuntimeEnvironment.getBrowserInfo().isIpad()){
				dr = DriverPhase.Ipad;
			}
			if(LfwRuntimeEnvironment.getBrowserInfo().isIphone()){
				dr = DriverPhase.Iphone;
			}
			pageList.addAll(filterPageByDriver(userPageCollection, dr));
		} catch (Exception e) {
			LfwLogger.error("过滤掉非用户资源Portal页面发生异常",e);
		}
		// 排序
		Collections.sort(pageList);
		/**
		 * 用户无权限,重定向到登陆页面
		 */
		if (pageList.isEmpty()) 
			throw new LfwRuntimeException("没有Portal页面权限!");
		
		/**
		 * 获得所有页面
		 */
		Page[] pages = pageList.toArray(new Page[0]);
		return pages;
	}
	
	/**
	 * 根据设备过滤用户页面
	 * @param userPageCollection
	 * @param driver
	 * @return
	 */
	public static List<Page> filterPageByDriver(List<Page> userPageCollection, DriverPhase driver){
		List<Page> result = new ArrayList<Page>();
		if(ToolKit.notNull(userPageCollection)){
			for(Page userPage : userPageCollection){
				String pageDriver = StringUtils.defaultIfEmpty(userPage.getDevices(), "PC");
				if(pageDriver.indexOf(driver.toString()) != -1)
					result.add(userPage);
			}
		}
		return result;
	}
	/**
	 * 在页面中插入一个Portlet
	 * @param portletModule
	 * @param portletName
	 * @param page
	 */
	public static Portlet creatPortlet(String portletId , String module,String skin){
		PortletDefinition pd =PortalServiceUtil.getPortletQryService().findPortletFromSystemCache(portletId, module);
		String title = pd.getPortletName();
		Portlet portlet = new Portlet();
		portlet.setId("p"+(new Random()).nextLong());
		portlet.setTitle(title);
		portlet.setName(modModuleName(module, portletId));
		if(PtUtil.isNull(skin))
			portlet.setTheme("blue");
		else
			portlet.setTheme(skin);
		return portlet;
	}
	/**
	 * 获得页面标题
	 * @param page
	 * @return
	 */
	public static String getTitle(Page page){
		if(PtUtil.isNull(page.getTitle())){
			return PortalServiceUtil.getConfigRegistryService().get(PortalEnv.DEFAULT_SYSTEM_TITLE);
		}
		return page.getTitle();
	}
}
