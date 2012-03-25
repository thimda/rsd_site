
package nc.uap.portal.container.portlet;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.CacheControl;
import javax.portlet.MimeResponse;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.om.CustomPortletMode;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.om.SecurityRoleRef;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.container.service.ContainerServices;
import nc.uap.portal.container.service.itf.PortletEnvironmentService;
import nc.uap.portal.util.ArgumentUtility;
import nc.uap.portal.util.PortletDataWrap;



/**
 * Abstract <code>javax.portlet.PortletRequest</code> implementation.
 * This class also implements InternalPortletRequest.
 *
 */
public abstract class PortletRequestImpl implements PortletRequest
{
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    // Private Member Variables ------------------------------------------------

    /** The PortalContext within which this request is occuring. */
    protected PortalContext portalContext;

    public void setPortletWindow(PortletWindow portletWindow) {
		this.portletWindow = portletWindow;
	}

	/** The portlet session. */
    protected PortletSession portletSession;

    /** Response content types. */
    protected ArrayList<String> contentTypes;

    protected PortletPreferences portletPreferences;

    protected Map<String, String[]> parameters;
    protected Map<String, String[]> publicParameters;
    protected Map<String, String[]> privateParameters;
    protected Map<String, Object> attributes;
    protected Map<String, String[]> requestProperties;
    protected List<String> requestPropertyNames;

    protected Cookie[] requestCookies;

    protected Map<String, String> userInfo;

    private final String lifecyclePhase;
    
    protected HttpServletRequest request;
    
    private PortletWindow portletWindow;
    
    public PortletRequestImpl(HttpServletRequest request, PortletWindow portletWindow, String lifecyclePhase)
    {
    	this.request = request;
    	this.portletWindow = portletWindow;
        this.lifecyclePhase = lifecyclePhase;
    }
    
    @SuppressWarnings("unchecked")
	private void retrieveRequestProperties()
    {
 
        HashMap<String, String[]> properties = new HashMap<String, String[]>();
        for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements(); )
        {
            String name = names.nextElement();
            ArrayList<String> values = new ArrayList<String>();
            for (Enumeration<String> headers = request.getHeaders(name); headers.hasMoreElements(); )
            {
                values.add(headers.nextElement());
            }
            int size = values.size();
            if (size > 0)
            {
            	  requestPropertyNames = new ArrayList<String>(properties.size());
                  requestProperties = new HashMap<String, String[]>(properties.size());
                  for (Map.Entry<String, String[]> entry : properties.entrySet())
                  {
                      requestPropertyNames.add(entry.getKey());
                      requestProperties.put(entry.getKey().toLowerCase(), entry.getValue());
                  }
            }
        }
    }

    private void checkInitParameterMap()
    {
        if (parameters == null)
        {
            parameters = initParameterMap();
        }
    }

    protected static Map<String, String[]> cloneParameterMap(Map<String, String[]> map)
    {
        if (!map.isEmpty())
        {
            Map<String, String[]> result = new HashMap<String, String[]>(map.size());
            for (Map.Entry<String,String[]> entry : map.entrySet())
            {
                if (entry.getValue() != null)
                {
                    result.put(entry.getKey(), entry.getValue().clone());
                }
            }
            return Collections.unmodifiableMap(result);
        }
        return Collections.emptyMap();
    }

    public Map<String, String[]> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String[]> parameters) {
		if(this.parameters==null){
			this.parameters=initParameterMap();
		}
		this.parameters.putAll( parameters);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String[]> initParameterMap()
    {
        String[] values  = null;
        Map<String, String[]> parameters = new HashMap<String, String[]> () ;
        Map<String, String[]> publicParameters = request.getParameterMap();
        if (!publicParameters.isEmpty())
        {
            parameters = new HashMap<String, String[]>(parameters);
            for (Map.Entry<String,String[]> entry : publicParameters.entrySet())
            {
                values = parameters.get(entry.getKey());
                if (values == null)
                {
                    parameters.put(entry.getKey(), entry.getValue().clone());
                }
                else
                {
                    String[] copy = new String[values.length+entry.getValue().length];
                    System.arraycopy(values, 0, copy, 0, values.length);
                    System.arraycopy(entry.getValue(), 0, copy, values.length, entry.getValue().length);
                    parameters.put(entry.getKey(), copy);
                }
            }
        }
        return parameters;
    }

//    protected PortletContext getPortletContext()
//    {
//        return requestContext.getPortletConfig().getPortletContext();
//    }

	   protected boolean isPublicRenderParameter(String name)
	    {
	        List<String> publicRenderParameterNames = portletWindow.getPortletDefinition().getSupportedPublicRenderParameters();
	        return publicRenderParameterNames.isEmpty() ? false : publicRenderParameterNames.contains(name);
	    }
    protected PortletWindow getPortletWindow()
    {
        return portletWindow;
    }

    public HttpServletRequest getServletRequest()
    {
        return request;
    }

    protected String getMimeRequestProperty(String name, CacheControl cacheControl)
    {
        if (MimeResponse.ETAG.equals(name))
        {
            return cacheControl.getETag();
        }
        else if (MimeResponse.CACHE_SCOPE.equals(name))
        {
            return cacheControl.isPublicScope() ? MimeResponse.PUBLIC_SCOPE : MimeResponse.PRIVATE_SCOPE;
        }
        else if (MimeResponse.USE_CACHED_CONTENT.equals(name))
        {
            return cacheControl.useCachedContent() ? "true" : null;
        }
        else if (MimeResponse.EXPIRATION_CACHE.equals(name))
        {
            return Integer.toString(cacheControl.getExpirationTime());
        }
        return null;
    }

    // PortletRequest Impl -----------------------------------------------------

    public Object getAttribute(String name)
    {
        ArgumentUtility.validateNotNull("attributeName", name);
        if (name.equals(PortletRequest.LIFECYCLE_PHASE))
        {
            return lifecyclePhase;
        }
        else if (name.equals(PortletRequest.USER_INFO))
        {
            if (userInfo == null)
            {
                try
                {
                    userInfo = ContainerServices.getInstance().getUserInfoService().getUserInfo(this, getPortletWindow());
                }
                catch (Exception e)
                {
                    userInfo = Collections.emptyMap();
                }
            }
            return userInfo;
        }

        else if (name.equals(PortletRequest.CCPP_PROFILE))
        {
//            if (ccppProfile == null)
//            {
//                ccppProfile = getPortletContainer().getContainerServices().getCCPPProfileService().getCCPPProfile(getServletRequest());
//            }
//            return ccppProfile;
        }else if(getAttributes().containsKey(name)){
        	return getAttributes().get(name);
        }
        
        
        return request.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames()
    {
     	Enumeration<String> em=((Hashtable<String,Object>)getAttributes()).keys() ;	  
        return em;
    }

    public String getAuthType()
    {
        return getServletRequest().getAuthType();
    }

//    public String getContextPath()
//    {
//        if (contextPath == null)
//        {
//            contextPath = requestContext.getPortletWindow().getPortletDefinition().getApplication().getContextPath();
//        }
//    	return this.requestContext.getContainerRequest().getContextPath();
//    }

    public Cookie[] getCookies()
    {
        if (requestCookies == null)
        {
            requestCookies = request.getCookies();
            if (requestCookies == null)
            {
                requestCookies = new Cookie[0];
            }
        }
        return requestCookies.length > 0 ? requestCookies.clone() : null;
    }

    public Locale getLocale()
    {
        return request.getLocale();
    }

    @SuppressWarnings("unchecked")
    public Enumeration<Locale> getLocales()
    {
        Locale preferredLocale = getLocale();
        ArrayList<Locale> locales = new ArrayList<Locale>();
        locales.add(preferredLocale);
        for (Enumeration e = getServletRequest().getLocales(); e.hasMoreElements(); )
        {
            Locale locale = (Locale)e.nextElement();
            if (!locale.equals(preferredLocale))
            {
                locales.add(locale);
            }
        }
        return Collections.enumeration(locales);
    }

    public String getParameter(String name)
    {
        ArgumentUtility.validateNotNull("parameterName", name);
        checkInitParameterMap();
        String[] values = parameters.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

    public Map<String, String[]> getParameterMap()
    {
        checkInitParameterMap();
        return cloneParameterMap(parameters);
    }

    public Enumeration<String> getParameterNames()
    {
        checkInitParameterMap();
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String name)
    {
        ArgumentUtility.validateNotNull("parameterName", name);
        checkInitParameterMap();
        String[] values =  parameters.get(name);
        return values != null ? values.clone() : null;
    }

    public PortalContext getPortalContext()
    {
        return portalContext;
    }

    public PortletMode getPortletMode()
    {
        return getPortletWindow().getPortletMode();
    }

    public PortletSession getPortletSession()
    {
        return getPortletSession(true);
    }

    /**
     * Returns the portlet session.
     * <p>
     * Note that since portlet request instance is created everytime the portlet
     * container receives an incoming request, the portlet session instance held
     * by the request instance is also re-created for each incoming request.
     * </p>
     */
    public PortletSession getPortletSession(boolean create)
    {
        if (LfwLogger.isDebugEnabled())
        {
            LfwLogger.debug("Retreiving portlet session (create=" + create + ")");
        }

        HttpSession httpSession = getServletRequest().getSession(create);
        if (portletSession == null)
        {
            if (LfwLogger.isDebugEnabled()){
                LfwLogger.debug("Creating new portlet session...");
            }
            final PortletEnvironmentService portletEnvironmentService = ContainerServices.getInstance().getPortletEnvironmentService();

            portletSession = portletEnvironmentService.createPortletSession(null, getPortletWindow(), httpSession);
        }
        return portletSession;
    }

    public PortletPreferences getPreferences()
    {
    	if(portletPreferences == null){
    		try {
    			PortletWindow win = getPortletWindow();
    			Preferences pf =  PortalCacheManager.getPreferences(win);
    			portletPreferences = PortletDataWrap.convertPreferences(pf);;
			} 
    		catch (Exception e) {
				LfwLogger.error("获取Portlet Preferences时出现异常", e);
			}
    	}
        return portletPreferences;
    }

    @SuppressWarnings("unchecked")
	public Map<String, String[]> getPrivateParameterMap()
    {
        return cloneParameterMap(request.getParameterMap());
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getProperties(String name)
    {
        ArgumentUtility.validateNotNull("propertyName", name);

        if(ACCEPT_LANGUAGE.equalsIgnoreCase(name))
        {
            Locale preferredLocale = getLocale();
            ArrayList<String> locales = new ArrayList<String>();
            locales.add(preferredLocale.toString());
            for (Enumeration e = getServletRequest().getLocales(); e.hasMoreElements(); )
            {
                Locale locale = (Locale)e.nextElement();
                if (!locale.equals(preferredLocale))
                {
                    locales.add(locale.toString());
                }
            }
            return Collections.enumeration(locales);
        }

        if (requestProperties == null)
        {
            retrieveRequestProperties();
        }
        String[] properties = requestProperties.get(name.toLowerCase());
        if (properties == null)
        {
            return Collections.enumeration(new ArrayList<String>());
        }
        return Collections.enumeration(Arrays.asList(properties));
    }

    public String getProperty(String name)
    {
        ArgumentUtility.validateNotNull("name", name);

        if(ACCEPT_LANGUAGE.equalsIgnoreCase(name))
        {
            return getLocale().toString();
        }

        if (requestProperties == null)
        {
            retrieveRequestProperties();
        }
        String property = null;
        String[] properties = requestProperties.get(name.toLowerCase());
        if (properties != null && properties.length > 0)
        {
            property = properties[0];
        }
        return property;
    }

    public Enumeration<String> getPropertyNames()
    {
        if (requestProperties == null)
        {
            retrieveRequestProperties();
        }
        return Collections.enumeration(requestPropertyNames);
    }

    @SuppressWarnings("unchecked")
	public Map<String, String[]> getPublicParameterMap()
    {
        return cloneParameterMap(request.getParameterMap());
    }

    public String getRemoteUser()
    {
        return getServletRequest().getRemoteUser();
    }

    public String getRequestedSessionId()
    {
        return getServletRequest().getRequestedSessionId();
    }

    public String getResponseContentType()
    {
        return getResponseContentTypes().nextElement();
    }

    public Enumeration<String> getResponseContentTypes()
    {
        if (contentTypes == null)
        {
            contentTypes = new ArrayList<String>();
            PortletDefinition dd = getPortletWindow().getPortletDefinition();
            for (Supports sup : dd.getSupports())
            {
                contentTypes.add(sup.getMimeType());
            }
            if (contentTypes.isEmpty())
            {
                contentTypes.add("text/html");
            }
        }
        return Collections.enumeration(contentTypes);
    }

    public String getScheme()
    {
        return getServletRequest().getScheme();
    }

    public String getServerName()
    {
        return getServletRequest().getServerName();
    }

    public int getServerPort()
    {
        return getServletRequest().getServerPort();
    }

    public Principal getUserPrincipal()
    {
        return getServletRequest().getUserPrincipal();
    }

    public String getWindowID()
    {
        return getPortletWindow().getId().getStringId();
    }

    public WindowState getWindowState()
    {
        return getPortletWindow().getWindowState();
    }

    public boolean isPortletModeAllowed(PortletMode mode)
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
                    Enumeration<PortletMode> supportedModes = portalContext.getSupportedPortletModes();
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

    public boolean isRequestedSessionIdValid()
    {
        return getServletRequest().isRequestedSessionIdValid();
    }

    public boolean isSecure()
    {
        return getServletRequest().isSecure();
    }

    /**
     * Determines whether a user is mapped to the specified role.  As specified
     * in PLT-20-3, we must reference the &lt;security-role-ref&gt; mappings
     * within the deployment descriptor. If no mapping is available, then, and
     * only then, do we check use the actual role name specified against the web
     * application deployment descriptor.
     *
     * @param roleName the name of the role
     * @return true if it is determined the user has the given role.
     */
    public boolean isUserInRole(String roleName)
    {
        PortletDefinition def = getPortletWindow().getPortletDefinition();
        String link = roleName;

        for (SecurityRoleRef r : def.getSecurityRoleRefs())
        {
            if (r.getRoleName().equals(roleName))
            {
                if (r.getRoleLink() != null)
                {
                    link = r.getRoleLink();
                }
                break;
            }
        }
        return getServletRequest().isUserInRole(link);
    }

    /**
     * Determine whether or not the specified WindowState is allowed for this
     * portlet.
     *
     * @param state the state in question
     * @return true if the state is allowed.
     */
    public boolean isWindowStateAllowed(WindowState state)
    {
        for (Enumeration<WindowState> en = portalContext.getSupportedWindowStates();
        en.hasMoreElements(); )
        {
            if (en.nextElement().toString().equalsIgnoreCase(state.toString()))
            {
                return true;
            }
        }
        return false;
    }

    public void setAttribute(String name, Object value)
    {
        ArgumentUtility.validateNotEmpty("name", name);
        if(value != null)
        	getAttributes().put(name, value);
        //licza fix for wls清理Attribute时异常
        else
        	getAttributes().remove(name);
    }
    protected Map<String,Object> getAttributes(){
    	if(attributes==null){
    		attributes=new Hashtable<String,Object>();
    	}
    	return attributes;
    }
    public void removeAttribute(String name)
    {
        ArgumentUtility.validateNotEmpty("name", name);
        getAttributes().remove(name);
    }
}
