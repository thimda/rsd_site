package nc.uap.portal.servlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;
import javax.portlet.UnavailableException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.driver.PortletInstanceUtil;
import nc.uap.portal.container.service.itf.FilterManager;
import nc.uap.portal.container.service.itf.PortletInvokerService;

public class PortletServlet extends HttpServlet{
    private static final long serialVersionUID = -5096339022539360365L;

    /**
     * Initialize the portlet invocation servlet.
     * @throws ServletException
     *             if an error occurs while loading portlet.
     */
    public void init(ServletConfig config) throws ServletException{
        // Call the super initialization method.
        super.init(config);
    }
  
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        dispatch(request, response);
    }


    // Private Methods ---------------------------------------------------------

    /**
     * Dispatch the request to the appropriate portlet methods. This method
     * assumes that the following attributes are set in the servlet request
     * scope:
     * <ul>
     * <li>METHOD_ID: indicating which method to dispatch.</li>
     * <li>PORTLET_REQUEST: the internal portlet request.</li>
     * <li>PORTLET_RESPONSE: the internal portlet response.</li>
     * </ul>
     *
     * @param request
     *            the servlet request.
     * @param response
     *            the servlet response.
     * @throws ServletException
     * @throws IOException
     */
    private void dispatch(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException{
    	PortletInstanceUtil.getInstance().initPortlets();
    	String portletName = (String) request.getAttribute(PortletInvokerService.TARGET_PORTLET);
    	String pk_org = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
    	String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
    	Portlet portlet = null;
    	
        try{
        	portlet = PortletInstanceUtil.getInstance().getPortlet(portletName, pk_org, pk_user);
        	PortletContext portletContext = PortletInstanceUtil.getInstance().getPortletContext(portletName);
        	Integer methodId = (Integer) request.getAttribute(PortletInvokerService.METHOD_ID);
        	
        	final PortletRequest portletRequest = (PortletRequest)request.getAttribute(PortletInvokerService.PORTLET_REQUEST);
        	
        	final PortletResponse portletResponse = (PortletResponse)request.getAttribute(PortletInvokerService.PORTLET_RESPONSE);
        	
        	final FilterManager filterManager = (FilterManager)request.getAttribute(PortletInvokerService.FILTER_MANAGER);
        	
   
        	
            // The requested method is RENDER: call Portlet.render(..)
            if (methodId == PortletInvokerService.METHOD_RENDER)
            {
                RenderRequest renderRequest = (RenderRequest) portletRequest;
                RenderResponse renderResponse = (RenderResponse) portletResponse;
                filterManager.processFilter(renderRequest, renderResponse,
                        portlet, portletContext);
            }

            // The requested method is RESOURCE: call
            // ResourceServingPortlet.serveResource(..)
            else if (methodId == PortletInvokerService.METHOD_RESOURCE)
            {
            	ResourceServingPortlet resourceServingPortlet = (ResourceServingPortlet) portlet;
                ResourceRequest resourceRequest = (ResourceRequest) portletRequest;
                ResourceResponse resourceResponse = (ResourceResponse) portletResponse;
                filterManager.processFilter(resourceRequest, resourceResponse, resourceServingPortlet, portletContext);
            }

            // The requested method is ACTION: call Portlet.processAction(..)
            else if (methodId == PortletInvokerService.METHOD_ACTION)
            {
                ActionRequest actionRequest = (ActionRequest) portletRequest;
                ActionResponse actionResponse = (ActionResponse) portletResponse;
                filterManager.processFilter(actionRequest, actionResponse,  portlet, portletContext);
            }

            // The request methode is Event: call Portlet.processEvent(..)
            else if (methodId == PortletInvokerService.METHOD_EVENT)
            {
            	EventPortlet eventPortlet = (EventPortlet) portlet;
                EventRequest eventRequest = (EventRequest) portletRequest;
                EventResponse eventResponse = (EventResponse) portletResponse;
                filterManager.processFilter(eventRequest, eventResponse,
                        eventPortlet, portletContext);
            }
            else if (methodId == PortletInvokerService.METHOD_LOAD)
            {
                // Do nothing.
            }

        }
        catch (UnavailableException ex)
        {
        	LfwLogger.error(ex.getMessage(), ex);
        	portlet.destroy();
        	dispatchToErrorPage(request, response);
        }
        catch (PortletException ex){
        	LfwLogger.error(ex.getMessage(), ex);
        	dispatchToErrorPage(request, response);
        } 
//        catch (PortletContainerException e) {
//        	dispatchToErrorPage(request, response);
//		}
        catch(Exception e){
        	LfwLogger.error(e.getMessage(),e);
		}
    }
    private void dispatchToErrorPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	RequestDispatcher disp = getServletContext().getRequestDispatcher("/error/error.html");
    	disp.forward(req, res);
    }

}
