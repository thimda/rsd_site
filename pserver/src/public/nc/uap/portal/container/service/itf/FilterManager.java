package nc.uap.portal.container.service.itf;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;

/**
 * Manage the initialization and doFilter for the filter which are
 * declareted in the deployment descriptor.
 * @since 05/29/2007
 * @version 2.0
 */
public interface FilterManager {

    void processFilter(ActionRequest req, ActionResponse res, Portlet portlet, PortletContext portletContext) throws PortletException, IOException;
    void processFilter(RenderRequest req, RenderResponse res, Portlet portlet, PortletContext portletContext) throws PortletException, IOException;
    void processFilter(ResourceRequest req, ResourceResponse res, ResourceServingPortlet resourceServingPortlet, PortletContext portletContext)throws PortletException, IOException;
    void processFilter(EventRequest req, EventResponse res, EventPortlet eventPortlet, PortletContext portletContext)throws PortletException, IOException;
}
