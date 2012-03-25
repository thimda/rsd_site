/**
 * 
 */
package nc.uap.portal.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.constant.ParameterKey;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PageMenu;
import nc.uap.portal.util.ConstantUtil;
import nc.uap.portal.util.PageBuilder;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import freemarker.template.Configuration;

/**
 * @author chouhl
 *
 */
@Servlet(path = "/login")
public class ThirdPartyLoginAction extends BaseAction {
	
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

	@Action
	public void index(@Param(name = "url") String url) throws IOException {
		try {
			Page[] myPages = PortalPageDataWrap.getUserPages();
			/**
			 * 获得主页
			 */
			Page page = PortalPageDataWrap.getUserDefaultPage(myPages);
			page.setTemplate("thirdparty");
			PortalRenderEnv.setCurrentPage(page);
			/**
			 * 菜单
			 */
			PageMenu menu = PortalPageDataWrap.getUserMenu(myPages);
			
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("RENDERTYPE", "content");
			root.put("url", request.getAttribute("thirdPartyUrl"));
			root.put(PageBuilder.USER_PAGES_ARG, myPages);
			root.put(PageBuilder.PAGE_ARG, page);

			root.put(PageBuilder.USER_PAGES_MENU , menu);
			boolean isKeepState =  menu.isKeepState();
			root.put(PageBuilder.IS_KEEP_STATE , isKeepState);
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

			constValSet.addAll(ConstantUtil.shatter(ParameterKey.class));
			root.put(PageBuilder.CONSTANTS, constValSet);			
			root.put("RES_PATH", LfwRuntimeEnvironment.getRootPath());
			root.put("USER_INFO", ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser());
			String tmp = FreeMarkerTools.lookUpTheme(page.getModule(), page.getTemplate(), page.getSkin(), PortalEnv.PAGE_FOLDER);
			Configuration cfg = FreeMarkerTools.getFreeMarkerCfg();
			/**
			 * 设置页面信息到上下文中
			 */
			PortalRenderEnv.setPages(myPages);
			String html = FreeMarkerTools.render(tmp, root, cfg);
			print(html);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		} 
	}
	
}
