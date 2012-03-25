package nc.uap.portal.container.service.itf;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.portlet.PortletWindow;



/**
 * Service used to invoke portlets.
 */
public interface PortletInvokerService {

    /**
     * The key used to bind the <code>PortletRequest</code> to the underlying
     * <code>HttpServletRequest</code>.
     */
    String PORTLET_REQUEST = "javax.portlet.request";

    /**
     * The key used to bind the <code>PortletResponse</code> to the underlying
     * <code>HttpServletRequest</code>.
     */
    String PORTLET_RESPONSE = "javax.portlet.response";

    /**
     * The key used to bind the <code>PortletConfig</code> to the underlying
     * PortletConfig.
     */
    String PORTLET_CONFIG = "javax.portlet.config";

//    /**
//     * The request attribute key used to retrieve the <code>PortletRequestContext</code> instance
//     */
//    String REQUEST_CONTEXT = PortletRequestContext.class.getName();
//
//    /**
//     * The request attribute key used to retrieve the <code>PortletRequestContext</code> instance
//     */
//    String RESPONSE_CONTEXT = PortletResponseContext.class.getName();

    /**
     * The key used to bind the method of processing being requested by the
     * container to the underlying <code>PortletRquest</code>.
     */
    String METHOD_ID = "nc.portal.core.method";

    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doRender(nc.uap.portal.container.portlet.PortletWindow,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    Integer METHOD_RENDER = Integer.valueOf(1);

    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doAction(nc.uap.portal.container.portlet.PortletWindow,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    Integer METHOD_ACTION = Integer.valueOf(3);

    /**
     * The unique method identifier for load requests.  Load requests are
     * requested through a call to the {@link PortletContainer#doLoad(nc.uap.portal.container.portlet.PortletWindow,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    Integer METHOD_LOAD = Integer.valueOf(5);

    /**
     * The unique method identifier for resource Serving requests.  Resource requests are
     * requested through a call to the {@link PortletContainer#doServeResource(PortletWindow,
     *  javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    Integer METHOD_RESOURCE = Integer.valueOf(7);

    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doEvent(nc.uap.portal.container.portlet.PortletWindow,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.portlet.Event)}
     * method.
     */
    Integer METHOD_EVENT = Integer.valueOf(9);

    String FILTER_MANAGER = "FilterManager";
    

    String TARGET_PORTLET = "TargetPortlet";
    void action(PortletWindow portletWindow, ActionRequest req, ActionResponse res, FilterManager filterManager)
    throws IOException, PortletException, PortletContainerException;

    void event(PortletWindow portletWindow, EventRequest request, EventResponse response, FilterManager filterManager)
    throws IOException, PortletException, PortletContainerException;

    void render(PortletWindow portletWindow, RenderRequest req, RenderResponse res, FilterManager filterManager)
    throws IOException, PortletException, PortletContainerException;

    void serveResource(PortletWindow portletWindow, ResourceRequest req, ResourceResponse res, FilterManager filterManager)
    throws IOException, PortletException, PortletContainerException;

    void load(PortletWindow portletWindow, PortletRequest req, PortletResponse res)
    throws IOException, PortletException, PortletContainerException;

}
