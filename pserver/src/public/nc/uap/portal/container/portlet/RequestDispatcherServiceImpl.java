
package nc.uap.portal.container.portlet;

import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.service.itf.RequestDispatcherService;


/**
 * @version $Id: RequestDispatcherServiceImpl.java 771319 2009-05-04 14:39:49Z ate $
 *
 */
public class RequestDispatcherServiceImpl implements RequestDispatcherService
{   
    public RequestDispatcherServiceImpl()
    {
        this(0);
    }
    
    public RequestDispatcherServiceImpl(int dispatchDetectionOrdinal)
    {        
        if (dispatchDetectionOrdinal == HttpServletPortletRequestWrapper.DispatchDetection.CHECK_STATE.ordinal())
        {
            HttpServletPortletRequestWrapper.dispatchDetection = HttpServletPortletRequestWrapper.DispatchDetection.CHECK_STATE;
        }
        else if (dispatchDetectionOrdinal == HttpServletPortletRequestWrapper.DispatchDetection.CHECK_REQUEST_WRAPPER_STACK.ordinal())
        {
            HttpServletPortletRequestWrapper.dispatchDetection = HttpServletPortletRequestWrapper.DispatchDetection.CHECK_STATE;
        }
        else
        {
            HttpServletPortletRequestWrapper.dispatchDetection = HttpServletPortletRequestWrapper.DispatchDetection.EVALUATE;
        }
    }
    
    public PortletRequestDispatcher getNamedDispatcher(ServletContext servletContext, PortletApplicationDefinition app,
                                                       String name)
    {
        
        RequestDispatcher dispatcher = servletContext.getNamedDispatcher(name);
        if (dispatcher != null)
        {
            return new PortletRequestDispatcherImpl(dispatcher, true);
        }
        if (LfwLogger.isInfoEnabled())
        {
            LfwLogger.info("No matching request dispatcher found for name: "+ name);
        }
        return null;
    }

    public PortletRequestDispatcher getRequestDispatcher(ServletContext servletContext,
                                                         PortletApplicationDefinition app, String path)
    {
        // Check if the path name is valid. A valid path name must not be null
        //   and must start with a slash '/' as defined by the portlet spec.
        if (path == null || !path.startsWith("/"))
        {
            if (LfwLogger.isInfoEnabled())
            {
                LfwLogger.info("Failed to retrieve PortletRequestDispatcher: path name must begin with a slash '/'.");
            }
            return null;
        }
        
        // Construct PortletRequestDispatcher.
        PortletRequestDispatcher portletRequestDispatcher = null;
        try 
        {
            RequestDispatcher servletRequestDispatcher = servletContext.getRequestDispatcher(path);
            if (servletRequestDispatcher != null) 
            {
                portletRequestDispatcher = new PortletRequestDispatcherImpl(servletRequestDispatcher, false);
            } 
            else 
            {
                if (LfwLogger.isInfoEnabled()) 
                {
                    LfwLogger.info("No matching request dispatcher found for: " + path);
                }
            }
        } 
        catch (Exception ex) 
        {
            // We need to catch exception because of a Tomcat 4.x bug.
            //   Tomcat throws an exception instead of return null if the path
            //   was not found.
            if (LfwLogger.isInfoEnabled())
            {
                LfwLogger.info("Failed to retrieve PortletRequestDispatcher: "+ ex.getMessage());
            }
            portletRequestDispatcher = null;
        }
        return portletRequestDispatcher;
    }

    public HttpServletRequestWrapper getRequestWrapper(ServletContext servletContext,
                                                       HttpServletRequest servletRequest,
                                                       PortletRequest portletRequest, HttpSession session,
                                                       boolean included, boolean named)
    {
        return new HttpServletPortletRequestWrapper(servletRequest, servletContext, session, portletRequest, included, named);
    }

    public HttpServletResponseWrapper getResponseWraper(ServletContext servletContext,
                                                        HttpServletResponse servletResponse,
                                                        PortletRequest portletRequest, PortletResponse portletResponse,
                                                        boolean included)
    {
        return new HttpServletPortletResponseWrapper(servletResponse, portletRequest, portletResponse, included);
    }
}
