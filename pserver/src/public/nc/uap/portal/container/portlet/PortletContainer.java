package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.CacheControl;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.service.ContainerServices;
import nc.uap.portal.container.service.itf.FilterManager;
import nc.uap.portal.container.service.itf.PortletEnvironmentService;
import nc.uap.portal.container.service.itf.PortletInvokerService;

/**
 * Portlet容器实现.
 * 
 * @version 2.0
 */
public class PortletContainer {
	private Map<PortletWindow, StateAwareResponseContext> eventResponse ;
	public void init() throws PortletContainerException {
	}

	public void destroy() {
	}

	/**
	 * 渲染指定的Portlet.
	 * 
	 * @param portletWindow the portlet window.
	 * @param request the servlet request.
	 * @param response the servlet response.
	 * @throws IllegalStateException if the container is not initialized.
	 * @throws PortletException
	 * @throws IOException
	 * @throws PortletContainerException
	 * 
	 * @see javax.portlet.Portlet#render(RenderRequest, RenderResponse)
	 */
	public PortletResponseContext doRender(PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response) throws PortletException, IOException,
			PortletContainerException {
		ContainerServices containerServices = ContainerServices.getInstance();
		PortletEnvironmentService envService = containerServices.getPortletEnvironmentService();
		PortletInvokerService invoker = containerServices.getPortletInvokerService();
		RenderRequest portletRequest = envService.createRenderRequest(request, portletWindow, new CacheControlImpl());
		PortletResponseContext renderCtx = new PortletResponseContext(request,response,portletWindow);
		RenderResponse portletResponse = envService.createRenderResponse(renderCtx);
		FilterManager filterManager = filterInitialisation(portletWindow, PortletRequest.RENDER_PHASE);
		invoker.render(portletWindow, portletRequest, portletResponse, filterManager);
		return renderCtx;
	}

	/**
	 * Indicates that a portlet resource Serving occured in the current request
	 * and calls the processServeResource method of this portlet.
	 * 
	 * @param portletWindow the portlet Window
	 * @param request the servlet request
	 * @param response the servlet response
	 * @throws PortletException if one portlet has trouble fulfilling the
	 *             request
	 * @throws PortletContainerException if the portlet container implementation
	 *             has trouble fulfilling the request
	 */
	public void doServeResource(PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response) throws PortletException, IOException,
			PortletContainerException {
		ContainerServices containerServices = ContainerServices.getInstance();
		PortletEnvironmentService envService = containerServices.getPortletEnvironmentService();
		PortletInvokerService invoker = containerServices.getPortletInvokerService();
		CacheControl cc = new CacheControlImpl();
		ResourceRequest portletRequest = envService.createResourceRequest(request, portletWindow, cc);
		PortletResponseContext renderCtx = new PortletResponseContext(request,response,portletWindow);
		ResourceResponse portletResponse = envService.createResourceResponse(renderCtx, portletRequest.getCacheability());
		FilterManager filterManager = filterInitialisation(portletWindow, PortletRequest.RESOURCE_PHASE);
		invoker.serveResource(portletWindow, portletRequest, portletResponse, filterManager);
	}

	/**
	 * Process action for the portlet associated with the given portlet window.
	 * 
	 * @param portletWindow the portlet window.
	 * @param request the servlet request.
	 * @param response the servlet response.
	 * @throws PortletException
	 * @throws IOException
	 * @throws PortletContainerException
	 * 
	 * @see javax.portlet.Portlet#processAction(ActionRequest, ActionResponse)
	 */
	public void doAction(PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response) throws PortletException, IOException,
			PortletContainerException {
		ContainerServices containerServices = ContainerServices.getInstance();
		PortletEnvironmentService envService = containerServices.getPortletEnvironmentService();
		PortletInvokerService invoker = containerServices.getPortletInvokerService();
		ActionRequest portletRequest = envService.createActionRequest(request, portletWindow);
		StateAwareResponseContext sarc = new StateAwareResponseContext(request,response,portletWindow);
		ActionResponse portletResponse = envService.createActionResponse(sarc);
		FilterManager filterManager = filterInitialisation(portletWindow, PortletRequest.ACTION_PHASE);
		invoker.action(portletWindow, portletRequest, portletResponse, filterManager);
			List<Event> events = sarc.getEvents();
			if (!events.isEmpty()) {
				containerServices.getEventCoordinationService().processEvents(this, portletWindow, request, response, events);
			}
	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String location) throws IOException {
		response.sendRedirect(location);
	}

	/**
	 * Fire Event for the portlet associated with the given portlet window and
	 * eventName
	 * 
	 * @param portletWindow the portlet window.
	 * @param request the servlet request.
	 * @param response the servlet response.
	 * @param event the event
	 * @throws PortletException
	 * @throws IOException
	 * @throws PortletContainerException
	 * 
	 * @see javax.portlet.EventPortlet#processEvent(javax.portlet.EventRequest,
	 *      javax.portlet.EventResponse)
	 */
	public void doEvent(PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response, Event event) throws PortletException,
			IOException, PortletContainerException {
		ContainerServices containerServices = ContainerServices.getInstance();
		PortletEnvironmentService envService = containerServices.getPortletEnvironmentService();
		PortletInvokerService invoker = containerServices.getPortletInvokerService();
		EventRequest portletRequest = envService.createEventRequest(request, portletWindow, event);
		StateAwareResponseContext sarc = new StateAwareResponseContext(request,response,portletWindow);
		EventResponse portletResponse = envService.createEventResponse(sarc);
		FilterManager filterManager = filterInitialisation(portletWindow, PortletRequest.EVENT_PHASE);
		List<Event> events = null;
		try {
			invoker.event(portletWindow, portletRequest, portletResponse, filterManager);
			events = sarc.getEvents();
			/**
			 * 添加事件
			 */
			getEventResponse().put(portletWindow, sarc);
			
		} finally {
			// portletResponse.release();
		}
		if (events != null && !events.isEmpty()) {
			containerServices.getEventCoordinationService().processEvents(this, portletWindow, request, response, events);
		}
	}

	/**
	 * 初始化过滤器
	 * 
	 * @param portletWindow the PortletWindow
	 * @param lifeCycle like ACTION_PHASE, RENDER_PHASE,...
	 * @return FilterManager
	 * @throws PortletContainerException
	 */
	public FilterManager filterInitialisation(PortletWindow portletWindow, String lifeCycle) throws PortletContainerException {
		return ContainerServices.getInstance().getFilterManagerService().getFilterManager(portletWindow, lifeCycle);
	}
	/**
	 * 获得响应队列
	 * @return
	 */
	public Map<PortletWindow, StateAwareResponseContext> getEventResponse() {
		if(eventResponse == null)
			eventResponse = new HashMap<PortletWindow, StateAwareResponseContext>();
		return eventResponse;
	}
	  
 
}
