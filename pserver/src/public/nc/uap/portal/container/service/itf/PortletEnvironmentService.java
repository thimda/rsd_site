package nc.uap.portal.container.service.itf;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.CacheControl;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nc.uap.portal.container.portlet.ActionRequestImpl;
import nc.uap.portal.container.portlet.ActionResponseImpl;
import nc.uap.portal.container.portlet.EventRequestImpl;
import nc.uap.portal.container.portlet.EventResponseImpl;
import nc.uap.portal.container.portlet.PortletResponseContext;
import nc.uap.portal.container.portlet.PortletSessionImpl;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.RenderRequestImpl;
import nc.uap.portal.container.portlet.RenderResponseImpl;
import nc.uap.portal.container.portlet.ResourceRequestImpl;
import nc.uap.portal.container.portlet.ResourceResponseImpl;
import nc.uap.portal.container.portlet.StateAwareResponseContext;


public class PortletEnvironmentService
{
    public ActionRequest createActionRequest(HttpServletRequest request, PortletWindow portletWindow)
    {
        return new ActionRequestImpl(request, portletWindow);
    }

    public ActionResponse createActionResponse(StateAwareResponseContext ctx)
    {
        return new ActionResponseImpl(ctx);
    }

    public EventRequest createEventRequest(HttpServletRequest request, PortletWindow portletWindow, Event event)
    {
        return new EventRequestImpl(request, portletWindow, event);
    }

    public EventResponse createEventResponse(StateAwareResponseContext ctx)
    {
        return new EventResponseImpl(ctx);
    }

    public PortletSession createPortletSession(PortletContext portletContext, PortletWindow portletWindow,
                                               HttpSession session)
    {
        return new PortletSessionImpl(portletContext, portletWindow, session);
    }

    public RenderRequest createRenderRequest(HttpServletRequest request, PortletWindow portletWindow, CacheControl cacheControl)
    {
        return new RenderRequestImpl(request, portletWindow, cacheControl);
    }

    public RenderResponse createRenderResponse(PortletResponseContext ctx)
    {
        return new RenderResponseImpl(ctx);
    }

    public ResourceRequest createResourceRequest(HttpServletRequest request, PortletWindow portletWindow, CacheControl cacheControl)
    {
        return new ResourceRequestImpl(request, portletWindow, cacheControl);
    }

    public ResourceResponse createResourceResponse(PortletResponseContext ctx, String requestCacheLevel)
    {
        return new ResourceResponseImpl(ctx, requestCacheLevel);
    }
}