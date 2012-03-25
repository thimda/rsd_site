package nc.uap.portal.container.portlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import nc.uap.portal.container.driver.Configuration;
import nc.uap.portal.container.driver.NCPortalContainerInfo;
import nc.uap.portal.container.service.itf.ContainerInfo;


/**
 * Default Portlet Context Implementation.
 * 
 * @version $Id: PortletContextImpl.java 771319 2009-05-04 14:39:49Z ate $
 */
public class PortletContextImpl implements PortletContext
{
    private static final String APP_PRE = "/apps/";
	// Private Member Variables ------------------------------------------------
    
    protected ServletContext servletContext;
    protected ContainerInfo containerInfo;
    protected List<String> supportedContainerRuntimeOptions;
    private String module;

    // Constructor -------------------------------------------------------------
    
    /**
     * Constructs an instance.
     * @param servletContext  the servlet context in which we are contained.
     * @param module 
     * @param portletApp  the portlet application descriptor.
     */
    public PortletContextImpl(ServletContext servletContext, String module)
    {
        this.servletContext = servletContext;
        this.containerInfo = NCPortalContainerInfo.getInfo();
        this.module = module;
        this.supportedContainerRuntimeOptions = Configuration.getSupportedContainerRuntimeOptions();
    }
    
    // PortletContext Impl -----------------------------------------------------
    
    /**
     * Retrieve the PortletContainer's server info.
     * @return the server info in the form of <i>Server/Version</i>
     */
    public String getServerInfo() {
        return containerInfo.getServerInfo();
    }
    
    public PortletRequestDispatcher getRequestDispatcher(String path){
   	
    	RequestDispatcher reqDisp = servletContext.getRequestDispatcher(getDispatcherURL(path));
    	PortletRequestDispatcherImpl disp = new PortletRequestDispatcherImpl(reqDisp, false);
        return disp;
    }
 
    public PortletRequestDispatcher getNamedDispatcher(String name){
    	RequestDispatcher reqDisp = servletContext.getNamedDispatcher(getDispatcherURL(name));
    	PortletRequestDispatcherImpl disp = new PortletRequestDispatcherImpl(reqDisp, false);
    	return disp;
    }

    private String getDispatcherURL(String path){
    	
    	String dispatcherURL=null;
    	if(path.startsWith("/")){
    		dispatcherURL=path;
    	}else{
    		dispatcherURL=APP_PRE + module + "/" + path;
    	}
    	return dispatcherURL;
    }
    
    public InputStream getResourceAsStream(String path) {
        return servletContext.getResourceAsStream(path);
    }

    public int getMajorVersion() {
        return containerInfo.getMajorSpecificationVersion();
    }

    public int getMinorVersion() {
        return containerInfo.getMinorSpecificationVersion();
    }

    public String getMimeType(String file) {
        return servletContext.getMimeType(file);
    }

    public String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getResourcePaths(String path) {
        return servletContext.getResourcePaths(path);
    }

    public URL getResource(String path)
        throws java.net.MalformedURLException {
        if (path == null || !path.startsWith("/")) {
            throw new MalformedURLException("path must start with a '/'");
        }
        return servletContext.getResource(path);
    }

    public Object getAttribute(java.lang.String name) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        return servletContext.getAttribute(name);
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getAttributeNames() {
        return servletContext.getAttributeNames();
    }

    public String getInitParameter(java.lang.String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name == null");
        }

        return servletContext.getInitParameter(name);
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    public void log(java.lang.String msg) {
        servletContext.log(msg);
    }

    public void log(java.lang.String message, Throwable throwable) {
        servletContext.log(message, throwable);
    }

    public void removeAttribute(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.removeAttribute(name);
    }

    public void setAttribute(String name, Object object) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.setAttribute(name, object);
    }

    public String getPortletContextName() {
        return servletContext.getServletContextName();
    }
    
    
    public ServletContext getServletContext() {
        return servletContext;
    }

	public Enumeration<String> getContainerRuntimeOptions() {
		Enumeration<String> e = Collections.enumeration(supportedContainerRuntimeOptions);
		return e;
	}
}

