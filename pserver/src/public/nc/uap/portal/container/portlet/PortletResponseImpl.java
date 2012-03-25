
package nc.uap.portal.container.portlet;

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.driver.ResourceURLProvider;
import nc.uap.portal.container.om.CustomPortletMode;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.container.service.ContainerServices;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * Abstract <code>javax.portlet.PortletResponse</code> implementation.
 */
public  class PortletResponseImpl implements PortletResponse
{
	
	protected String namespace;   
    protected HttpServletResponse response;
    protected PortletWindow portletWindow;
    protected PortletResponseContext context;
    
    // Constructor -------------------------------------------------------------
    
    public PortletResponseImpl(PortletResponseContext context) {
    	this.context = context;
        this.response = context.getContainerResponse();
        this.portletWindow = context.getPortletWindow();
    }
    
    protected PortletWindow getPortletWindow() 
    {
        return portletWindow;
    }


    protected PortalContext getPortalContext()
    {
        return PortalContextImpl.getInstance();
    }

//    protected HttpServletRequest getServletRequest()
//    {
//       
//    }
    
    public HttpServletResponse getServletResponse()
    {
       return response;
    }

    protected boolean isPortletModeAllowed(PortletMode mode)
    {
        if(PortletMode.VIEW.equals(mode))
        {
            return true;
        }
        
        String modeName = mode.toString();

        PortletDefinition dd = getPortletWindow().getPortletDefinition();

        for (Supports sup : dd.getSupports())
        {
            for (String m : sup.getPortletModes())
            {
                if (m.equalsIgnoreCase(modeName)) 
                {
            		String module = portletWindow.getPortletDefinition().getModule();
            		ModuleApplication app = PortalCacheManager.getModuleAppByModuleName(module);

                    // check if a portlet managed mode which is always allowed.
                    CustomPortletMode cpm = app.getCustomPortletMode(modeName);
                    if (cpm != null && !cpm.isPortalManaged())
                    {
                        return true;
                    }
                    Enumeration<PortletMode> supportedModes = getPortalContext().getSupportedPortletModes();
                    while (supportedModes.hasMoreElements()) 
                    {
                        if (supportedModes.nextElement().equals(mode)) 
                        {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    // PortletResponse Impl ----------------------------------------------------
    
    public void addProperty(Cookie cookie)
    {
    	this.context.addProperty(cookie);
    }

    public void addProperty(String key, Element element)
    {
        this.context.addProperty(key,element);
    }


    public void addProperty(String key, String value)
    {
    	this.context.addProperty(key, value);
    }
    
    public Element createElement(String tagName) throws DOMException
    {
        return this.context.createElement(tagName);
    }
    
    public String encodeURL(String path)
    {
        if (path.indexOf("://") == -1 && !path.startsWith("/"))
        {
            throw new IllegalArgumentException("only absolute URLs or full path URIs are allowed");
        }
                
        ResourceURLProvider provider = new ResourceURLProvider();
        if (path.indexOf("://") != -1) {
            provider.setAbsoluteURL(path);
        } else {
            provider.setAbsoluteURL(path);
        }
        return getServletResponse().encodeURL(provider.toString());
    }
    
    public String getNamespace()
    {
        if (namespace == null)
        {
             namespace = ContainerServices.getInstance().getNamespaceMapper().encode(getPortletWindow().getId(), "");
             StringBuffer validNamespace = new StringBuffer();
             for (int i = 0; i < namespace.length(); i++)
             {
                char ch = namespace.charAt(i);
                if (Character.isJavaIdentifierPart(ch))
                {
                    validNamespace.append(ch);
                } 
                else
                {
                    validNamespace.append('_');
                }
             }
             namespace = validNamespace.toString();
        }
        return namespace;
    }
    
    public void setProperty(String key, String value)
    {
       this.context.setProperty(key, value);
    }

}
