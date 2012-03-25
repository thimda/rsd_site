package nc.uap.portal.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServletBase;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.constant.AttributeKeys;
import nc.uap.portal.constant.ParameterKey;
import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.portlet.CacheControlImpl;
import nc.uap.portal.container.portlet.PortletContainer;
import nc.uap.portal.container.portlet.PortletResponseContext;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.portlet.PortletWindowImpl;
import nc.uap.portal.container.portlet.RenderRequestImpl;
import nc.uap.portal.container.portlet.StateAwareResponseContext;
import nc.uap.portal.container.service.ContainerServices;
import nc.uap.portal.container.service.itf.FilterManager;
import nc.uap.portal.container.service.itf.PortletEnvironmentService;
import nc.uap.portal.container.service.itf.PortletInvokerService;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortalRenderEnv;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * 处理Portlet请求  
 * @author licza
 *
 */
public class PortletActServlet extends LfwServletBase {
	private static final long serialVersionUID = 3056289918696585588L;

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		 doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String pageModule = request.getParameter(ParameterKey.PAGE_MODULE);
		String pageName = request.getParameter(ParameterKey.PAGE_NAME);
		String portletModule = request.getParameter(ParameterKey.PORTLET_MODULE);
		String portletName = request.getParameter(ParameterKey.PORTLET_NAME);
		String windowState = request.getParameter(ParameterKey.WINDOW_STATE);
		String portletMode = request.getParameter(ParameterKey.PORTLET_MODE);
		String action = request.getParameter(ActionRequest.ACTION_NAME);
		String resID=request.getParameter(ParameterKey.RESOURCE_ID);
		Page[] myPages = null;
		try {
			myPages = PortalPageDataWrap.getUserPages();
		} catch (UserAccessException e1) {
			LfwLogger.error(e1.getMessage());
		}
		/**
		 * 获得当前页面
		 */
		Page page = PortalPageDataWrap.getUserPage(myPages, pageModule, pageName);
		PortalRenderEnv.setCurrentPage(page);
		
		PortalRenderEnv.setPortletRenderType(PortalRenderEnv.RENDER_REQ_TYPE_AJAX);
		PortletWindowID winId = new PortletWindowID(pageModule, pageName,portletModule, portletName);
		try {
			PortletWindow portletWindow = new PortletWindowImpl(winId,	new WindowState(StringUtils.defaultIfEmpty(windowState, "normal")), new PortletMode(StringUtils.defaultIfEmpty(portletMode,"view")));
			if (action == null || "".equals(action)) {
				if(resID==null || "".equals(resID)){
					doRender(request, response, portletWindow);
				}else{
					doServeResource(request, response, portletWindow);
				}
				return;
			} else {
				doAction(request, response, portletWindow);
				return;
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获得JSON格式的Portlet视图
	 * @param portletWin
	 * @param content
	 * @return Portlet视图
	 */
	public static JSONObject getPortletView(PortletWindow portletWin, String content,String title,String protocol) {
		org.json.JSONObject node=new org.json.JSONObject();
		node.put(ParameterKey.RESPONSE_WIN_MODE, portletWin.getPortletMode().toString());
		node.put(ParameterKey.RESPONSE_NAME, portletWin.getId().getStringId());
		node.put(ParameterKey.RESPONSE_WINDOWSTATE, portletWin.getWindowState().toString());
		node.put(ParameterKey.RESPONSE_CONTENT, content);
		node.put(ParameterKey.RESPONSE_TITLE, title);
		node.put(ParameterKey.RESPONSE_PROTOCOL, protocol);
		return node;
	}
	/**
	 * 打印HTML
	 * @param request
	 * @param response
	 * @param content
	 */
	public static void print(HttpServletRequest request,HttpServletResponse response, String content) {
		BaseAction print = new BaseAction();
		print.setRequest(request);
		print.setResponse(response);
		response.setContentType("text/json");
		print.print(content);
		print.fush();
	}
	/**
	 * 处理渲染类型的请求
	 * @param request
	 * @param response
	 * @param portletWindow
	 * @throws PortletException
	 * @throws IOException
	 * @throws PortletContainerException
	 */
	public static void doRender(HttpServletRequest request,	HttpServletResponse response, PortletWindow portletWindow)throws PortletException, IOException, PortletContainerException {
		StringServletResponse stringResponse = new StringServletResponse(response);
		PortletContainer container = new PortletContainer();
		PortletResponseContext resCtx = container.doRender(portletWindow, request, stringResponse);
		String portletContent = stringResponse.getString(); 
		String title = resCtx.getTitle();
		JSONArray returnData = new JSONArray();
		JSONObject node = getPortletView(portletWindow,	portletContent,title,ParameterKey.RESPONSE_MODE_HTML);
		returnData.put(node);
		String returnJson =returnData.toString();
		print(request, response, returnJson);
	}
	/**
	 * 处理Resource请求
	 * @param request
	 * @param response
	 * @param portletWindow
	 */
	public static void doServeResource(HttpServletRequest request,	HttpServletResponse response, PortletWindow portletWindow){
		PortletContainer container = new PortletContainer();
		try {
			container.doServeResource(portletWindow, request, response);
		} catch ( Exception e) {
			LfwLogger.error(e.getMessage(), e);
		} 
 	}
	
	/**
	 * 处理事件类型的请求
	 * @param request
	 * @param response
	 * @param portletWindow
	 * @throws PortletException
	 * @throws IOException
	 * @throws PortletContainerException
	 */
	public static void doAction(HttpServletRequest request,	HttpServletResponse response, PortletWindow portletWindow)	throws PortletException, IOException, PortletContainerException {
		PortletContainer container = new PortletContainer();
		container.doAction(portletWindow, request, response);
		Map<PortletWindow, StateAwareResponseContext> eventPortlets = container.getEventResponse();
		Set<Map.Entry<PortletWindow, StateAwareResponseContext>> eventPortletSet = eventPortlets.entrySet();
		JSONArray returnData = new JSONArray();
		ContainerServices containerServices = ContainerServices	.getInstance();
		PortletEnvironmentService envService = containerServices.getPortletEnvironmentService();
		for (Map.Entry<PortletWindow, StateAwareResponseContext> eventPortlet : eventPortletSet) {
			PortletWindow window = eventPortlet.getKey();
			StateAwareResponseContext ctx = eventPortlet.getValue();
			/**
			 *  执行JS代码
			 */
			if(AttributeKeys.MODE_SCRIPT.equals(ctx.getProperty(AttributeKeys.RESPONSE_MODE)))
			{
				String content = (String) ctx.getProperty(AttributeKeys.CLIENT_EXEC_SCRIPT);
				JSONObject node = getPortletView(window,content,StringUtils.EMPTY,ParameterKey.RESPONSE_MODE_SCRIPT);
				returnData.put(node);
			}
			/**
			 * 渲染HTML
			 */
			EventResponse resp = envService.createEventResponse(ctx);
			StringServletResponse renderResponse = new StringServletResponse(response);
			try {
				PortletInvokerService invoker = containerServices.getPortletInvokerService();
				RenderRequestImpl portletRequest = (RenderRequestImpl) envService.createRenderRequest(request, window,new CacheControlImpl());
				portletRequest.setParameters(resp.getRenderParameterMap());
				PortletResponseContext prc = new PortletResponseContext(request,renderResponse,window);
				RenderResponse portletResponse = envService.createRenderResponse(prc);
				FilterManager filterManager = container.filterInitialisation(window, PortletRequest.RENDER_PHASE);
				invoker.render(window, portletRequest, portletResponse,filterManager);
				String content = null;
				JSONObject node = null;
				/**
				 *  执行JS代码
				 */
				if(AttributeKeys.MODE_SCRIPT.equals(prc.getProperty(AttributeKeys.RESPONSE_MODE)))
				{
					content = (String) prc.getProperty(AttributeKeys.CLIENT_EXEC_SCRIPT);
					node = getPortletView(window,content,StringUtils.EMPTY,ParameterKey.RESPONSE_MODE_SCRIPT);
				}else{
					content = renderResponse.getString();
					node = getPortletView(window,content,prc.getTitle(),ParameterKey.RESPONSE_MODE_HTML);
				}
				returnData.put(node);
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
			}
		}
		String returnJson = returnData.toString();
		print(request, response, returnJson);
	}
}
