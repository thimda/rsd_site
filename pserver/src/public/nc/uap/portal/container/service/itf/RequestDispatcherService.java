package nc.uap.portal.container.service.itf;

import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import nc.uap.portal.container.om.PortletApplicationDefinition;

/**
 * Service to retrieve a RequestDispatcher and corresponding HttpServletRequest and HttpServletResponse wrappers
 * for usage by the PortletContext and to support custom Servlet Request dispatching.
 * 
 * @version $Id: RequestDispatcherService.java 771319 2009-05-04 14:39:49Z ate $
 */
public interface RequestDispatcherService
{
    PortletRequestDispatcher getRequestDispatcher(ServletContext servletContext, PortletApplicationDefinition app,
                                                  String path);

    PortletRequestDispatcher getNamedDispatcher(ServletContext servletContext, PortletApplicationDefinition app,
                                                String name);

    HttpServletRequestWrapper getRequestWrapper(ServletContext servletContext, HttpServletRequest servletRequest,
                                                PortletRequest portletRequest, HttpSession session, boolean included,
                                                boolean named);

    HttpServletResponseWrapper getResponseWraper(ServletContext servletContext, HttpServletResponse servletResponse,
                                                 PortletRequest portletRequest, PortletResponse portletResponse,
                                                 boolean included);
}
