package nc.uap.portal.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import nc.uap.cpb.log.LoginLogHelper;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalPassWordException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PageMenu;
import nc.uap.portal.om.Portlet;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PageBuilder;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortletDataWrap;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;


/**
 * 显示Portal页面
 * 
 * @author licza
 * 
 */
@Servlet(path = "/home")
public class PortalHomeAction extends BaseAction {
	
	/**
	 * 显示用户主页
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Action
	public void index() throws IOException  {
		try {
			Page[] myPages = PortalPageDataWrap.getUserPages();
			/**
			 * 获得主页
			 */
			Page page = PortalPageDataWrap.getUserDefaultPage(myPages);
			/**
			 * 菜单
			 */
			PageMenu menu = PortalPageDataWrap.getUserMenu(myPages);
			if(menu.isKeepState()){
				//TODO 
			}
			/**
			 * 打印生成的页面
			 */
			print(PageBuilder.render(page, myPages,menu));
			HttpSession ses = request.getSession();
			
			/**
			 * 首次登陆判断
			 */
			String firstForce = (String)ses.getAttribute("firstForce");
			if(firstForce != null && firstForce.equals("1")) 
				throw new PortalPassWordException("首次登陆,请修改密码!");
 			
			/**
			 * 密码策略
			 */
			String sercutityForce = (String)ses.getAttribute("sercutityForce");
			if(sercutityForce != null && sercutityForce.equals("1")) 
				throw new PortalPassWordException("请立即修改密码");
			
			/**
			 * 密码过期
			 */
			String outdateForce = (String)ses.getAttribute("outdateForce");
			if(outdateForce != null && outdateForce.equals("1")) 
				throw new PortalPassWordException("密码已过期,请立即修改密码!");
	 
		} 
		catch (PortalServiceException e) {
			throw new LfwRuntimeException(e.getMessage(), "模板渲染异常", e);
		} 
		catch (UserAccessException e) {
			logout();
		}
		catch (LfwRuntimeException e) {
			print("<h1>"+e.getMessage()+"</h1><a href='/portal/core/login.jsp?pageId=login'>BACK</a>");
		}
		catch(PortalPassWordException e){
			String chagePassWdURL = request.getContextPath()+"/pages/changePasswd.jsp";
			addExecScript("showFrameDailog('"+e.getMessage()+"',420,250,'"+chagePassWdURL+"',false);");
		}
	}
	
	/**
	 * 新增一个Portlet
	 * @param pageName
	 * @param pageModule
	 */
	@Action
	public void insertNewPortlet(@Param(name = "pageName") String pageName,
			   @Param(name = "pageModule") String pageModule){
		Map<String, PortletDisplayCategory>  cates = PortalServiceUtil.getPortletRegistryService().getPortletDisplayCache();
		if(cates == null || cates.isEmpty())
			return;
		String ftlName = "nc/portal/ftl/insertPortlet.ftl";
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("pageName", pageName);
		root.put("pageModule", pageModule);
		root.put("PortletDisplayCategory", cates.values());
		try {
			print(FreeMarkerTools.contextTemplatRender(ftlName, root));
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage());
		}
	}
	
	/**
	 * 显示用户的一个Portal页面
	 * 
	 * @param model 所在模块
	 * @param pageName 页面名称
	 * @throws IOException
	 */
	@Action
	public void view(@Param(name = "pageModule") String model,
					 @Param(name = "pageName") String pageName) throws  IOException,ServletException {
		try {
			Page[] myPages = PortalPageDataWrap.getUserPages();
			/**
			 * 获得当前页面
			 */
			Page page = PortalPageDataWrap.getUserPage(myPages, model, pageName);
			/**
			 * 菜单
			 */ 
			PageMenu menu = PortalPageDataWrap.getUserMenu(myPages);
			/**
			 * 打印生成的页面
			 */
			print(PageBuilder.render(page, myPages,menu));
		} catch (PortalServiceException e) {
			throw new LfwRuntimeException(e.getMessage(), "模板渲染异常", e);
		} catch (UserAccessException e) {
			logout();
		}catch (NullPointerException e) {
			print("<h1>"+e.getMessage()+"</h1><a href='/portal/core/login.jsp?pageId=login'>BACK</a>");
		}
	}

	/**
	 * 更新Porltet布局
	 * @param pageName 页面名称
	 * @param pageModule 页面模块
	 * @param portletId PortletID
	 * @param destinationId 目标ID
	 * @param isAfter 是否在前面(如果容器内部为空 则为NULL)
	 * @throws PortalServiceException 
	 * @throws IOException 
	 */
	@Action
	public void layout(@Param(name = "pageName") String pageName,
					   @Param(name = "pageModule") String pageModule,
					   @Param(name = "portletId") String portletId, 
					   @Param(name = "destinationId") String destinationId,
					   @Param(name = "isAfter") Boolean isAfter) throws  IOException {
			try {
				
				/**
				 * 获得用户所有页面
				 */
				Page[] myPages = PortalPageDataWrap.getUserPages();
				
				/**
				 * 获得用户主键
				 */
				PtSessionBean sbean = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
				String userId = sbean.getPk_user();
				 
				/**
				 * 获得当前页面
				 */
				Page page = PortalPageDataWrap.getUserPage(myPages, pageModule, pageName);
				
				/**
				 * 判断页面是否只读
				 */
				if(page.getReadonly() == null || page.getReadonly()){
					return;
				}
				
				/**
				 * 建立坐标系
				 */
				page.plantCoordTrees();
				if (isAfter == null) {
					
					/**
					 * 空Layout 
					 * 直接将PortLet加入Layout
					 */
					page.addPortletToBlankLayout(portletId, destinationId);
				} else {
					
					/**
					 * 非空Layout
					 * 移动PortLet
					 */
					page.movePortlet(portletId, destinationId, isAfter);
				}
				
				/**
				 * 更新缓存
				 */
				PortalServiceUtil.getRegistryService().registryUserPageCache(page);
				
				/**
				 * 更新数据库
				 */
				PtPageVO portalPageVO=new PtPageVO();
				portalPageVO.setFk_pageuser(userId);
				portalPageVO=PortalPageDataWrap.copyPage2PageVO(page, portalPageVO);
				PortalServiceUtil.getPageService().updateLayout(portalPageVO);
			} catch (PortalServiceException e) {
				throw new LfwRuntimeException(e.getMessage(), "模板渲染异常", e);
			} catch (UserAccessException e) {
				logout();
			}
	}
	
	/**
	 * 插入新Portlet
	 * @param pageName 页面ID
	 * @param pageModule
	 * @param portletModule
	 * @param portletId
	 * @param skin
	 * @throws IOException
	 */
	@Action
	public void doDelPortlet(@Param(name = "PageName") String pageName,
			   @Param(name = "PageModule") String pageModule,
			   @Param(name = "PortletModule") String portletModule,
			   @Param(name = "PortletName") String portletId,
			   @Param(name = "pid") String pid) throws IOException{
		JSONArray returnData = new JSONArray();
		try {
			org.json.JSONObject node=new org.json.JSONObject();
			node.put("err", 1);
 
			/**
			 * 获得用户所有页面
			 */
			Page[] myPages = PortalPageDataWrap.getUserPages();
			
			/**
			 * 获得用户主键
			 */
			PtSessionBean sbean = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
			String userId = sbean.getPk_user();
			 
			/**
			 * 获得当前页面
			 */
			Page page = PortalPageDataWrap.getUserPage(myPages, pageModule, pageName);
			
			if(page.getReadonly() == null || page.getReadonly()){
				node.put("msg", "此页面禁止删除Portlet!");
				returnData.put(node);
				return;
			}
 			/**
			 * 查看当前页面里是否已经有要添加的Portlet
			 */
			boolean b = PortletDataWrap.hasPortlet(page, portletModule, portletId);
			
			if(!b){
				node.put("msg", "页面中不存在此Portlet!");
				returnData.put(node);
				return;
			}
			/**
			 * 建立坐标系
			 */
			page.plantCoordTrees();
			
			/**
			 * 删除Portlet
			 * !!传入的参数是pid  非PortletId
			 */
			page.removePortletElement(pid);
			
 			/**
			 * 更新缓存
			 */
			PortalServiceUtil.getRegistryService().registryUserPageCache(page);
			
			/**
			 * 更新数据库
			 */
			PtPageVO portalPageVO=new PtPageVO();
			portalPageVO.setFk_pageuser(userId);
			portalPageVO=PortalPageDataWrap.copyPage2PageVO(page, portalPageVO);
			PortalServiceUtil.getPageService().updateLayout(portalPageVO);
			node.put("msg", "Portlet删除成功!");
			node.put("err", 0);
			returnData.put(node);
		} catch (UserAccessException e) {
			logout();
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			print(returnData);
		}
	}
	
	/**
	 * 插入新Portlet
	 * @param pageName 页面ID
	 * @param pageModule
	 * @param portletModule
	 * @param portletId
	 * @param skin
	 * @throws IOException
	 */
	@Action
	public void doInsertNewPortlet(@Param(name = "pageName") String pageName,
			   @Param(name = "pageModule") String pageModule,
			   @Param(name = "portletModule") String portletModule,
			   @Param(name = "portletId") String portletId,
			   @Param(name = "skin") String skin) throws IOException{
		try {
			/**
			 * 获得用户所有页面
			 */
			Page[] myPages = PortalPageDataWrap.getUserPages();
			
			/**
			 * 获得用户主键
			 */
			PtSessionBean sbean = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
			String userId = sbean.getPk_user();
			 
			/**
			 * 获得当前页面
			 */
			Page page = PortalPageDataWrap.getUserPage(myPages, pageModule, pageName);
			
			if(page.getReadonly() == null || page.getReadonly()){
				alert("此页面禁止添加Portlet!");
				return;
			}
			
			/**
			 * 查看当前页面里是否已经有要添加的Portlet
			 */
			boolean b = PortletDataWrap.hasPortlet(page, portletModule, portletId);
			
			if(b){
				alert("页面中已存在此Portlet!");
				return;
			}
			/**
			 * 建立坐标系
			 */
			page.plantCoordTrees();
			
			Portlet portlet = PortalPageDataWrap.creatPortlet(portletId,portletModule,skin);
			page.insertNewPortlet(portlet);
			/**
			 * 更新缓存
			 */
			String key = PortalPageDataWrap.modModuleName(pageModule, pageName);
			PortalCacheManager.getUserPageCache().put(key, page);
			
			/**
			 * 更新数据库
			 */
			PtPageVO portalPageVO=new PtPageVO();
			portalPageVO.setFk_pageuser(userId);
			portalPageVO=PortalPageDataWrap.copyPage2PageVO(page, portalPageVO);
			PortalServiceUtil.getPageService().updateLayout(portalPageVO);
			alert("Portlet新增成功!");
			addExecScript("parent.insertOK();");
		} catch (UserAccessException e) {
			logout();
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 注销登陆
	 * 
	 * @throws IOException
	 */
	@Action
	public void logout() throws IOException {
		Object osl = LfwClassUtil.newInstance("nc.uap.portal.integrate.system.OtherSystemLogout");
		String logoutScript = StringUtils.EMPTY;
		
		try {
			logoutScript = (String)MethodUtils.invokeMethod(osl, "getOtherSysLogoutScript", null);
		} catch (Exception e) {
			LfwLogger.error("获得第三方注销脚本出现异常：" + e.getMessage(),e);
		} 
		
		StringBuffer output = new StringBuffer(logoutScript);
		String loginUrl = request.getContextPath() +"/core/login.jsp?pageId=login";
		output.append("<script>window.location.href='"+loginUrl+"';</script>");
		print(output);
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		try {
			LoginLogHelper.logout(session.getId(), new UFDateTime());
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
		/**
		 * 销毁session
		 */
		request.getSession().invalidate();
	}

} 