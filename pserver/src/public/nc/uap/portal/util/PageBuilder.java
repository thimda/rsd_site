package nc.uap.portal.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.constant.ParameterKey;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PageMenu;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import freemarker.template.Configuration;

/**
 * 生成PortalPage.
 * 
 * @author licza
 * 
 */
public class PageBuilder {

	/**
	 * Page参数名
	 */
	public static final String PAGE_ARG = "page";
	/**
	 * 常量池
	 */
	public static final String CONSTANTS = "ptconstants";
	/**
	 * UserPages参数名
	 */
	public static final String USER_PAGES_ARG = "userPages";
	/**
	 * 菜单
	 */
	public static final String USER_PAGES_MENU = "USER_MENU";
	/**
	 * 是否保持状态
	 */
	public static final String IS_KEEP_STATE = "IS_KEEP_STATE";
	/**
	 * 渲染PortalPage
	 * 
	 * @param pml
	 * @param userPages
	 * @return
	 * @throws PortalServiceException
	 */
	public static String render(Page page, Page[] userPages,PageMenu menu)
			throws PortalServiceException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put(PAGE_ARG, page);
		root.put(USER_PAGES_ARG, userPages);
		PortalRenderEnv.setPortletRenderType(PortalRenderEnv.RENDER_REQ_TYPE_AJAX);
		root.put(USER_PAGES_MENU , menu);
		boolean isKeepState =  menu.isKeepState();
		root.put(IS_KEEP_STATE , isKeepState);
		Set<Map<String, String>> constValSet = new HashSet<Map<String, String>>();
		Map<String, String> ctxName = new HashMap<String, String>();
		ctxName.put("fieldName", "ROOT_PATH");
		ctxName.put("fieldVal", LfwRuntimeEnvironment.getRootPath());
		Map<String, String> pageName = new HashMap<String, String>();
		pageName.put("fieldName", "CUR_PAGE_NAME");
		pageName.put("fieldVal", page.getPagename());
		constValSet.add(pageName);
		Map<String, String> pageModule = new HashMap<String, String>();
		pageModule.put("fieldName", "CUR_PPAGE_MODULE");
		pageModule.put("fieldVal", page.getModule());
		constValSet.add(pageModule);

		Map<String, String> pageReadOnly = new HashMap<String, String>();
		pageReadOnly.put("fieldName", "CUR_PPAGE_READONLY");
		pageReadOnly.put("fieldVal", new Boolean(page.getReadonly() == null
				|| page.getReadonly()).toString());
		constValSet.add(pageReadOnly);
		constValSet.add(ctxName);
	 
		String resourcePath = PtUtil.getResourcePath(page);
		constValSet.addAll(ConstantUtil.shatter(ParameterKey.class));
		root.put(CONSTANTS, constValSet);
		root.put("USER_INFO", ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser());
		root.put("RES_PATH", resourcePath);
		String tmp = FreeMarkerTools.lookUpTheme(page.getModule(), page
				.getTemplate(), page.getSkin(), PortalEnv.PAGE_FOLDER);
		Configuration cfg = FreeMarkerTools.getFreeMarkerCfg();
		/**
		 * 设置页面信息到上下文中
		 */
		PortalRenderEnv.setCurrentPage(page);
		PortalRenderEnv.setPages(userPages);
		PortalRenderEnv.setPortletRenderType(PortalRenderEnv.RENDER_REQ_TYPE_GET);
		return FreeMarkerTools.render(tmp, root, cfg);
	}

}
