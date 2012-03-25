package nc.uap.portal.container.tags;

import java.lang.reflect.Field;
import java.util.Hashtable;

import javax.portlet.BaseURL;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.jsp.JspException;



/**
 * Abstract supporting class for the JSR168 actionURL 
 * and renderURL tag handlers.
 * 
 * @version 2.0
 */

public abstract class PortletURLTag168 extends BaseURLTag {
	
	private static final long serialVersionUID = 4167483154265467568L;

	protected String portletMode = null;
	
	protected String windowState = null;
	
	protected PortletURL portletURL = null;
	
	 
	@Override
    public int doStartTag() throws JspException {
    	    	    	
        PortletResponse portletResponse = 
        	(PortletResponse) pageContext.getRequest().getAttribute(Constants.PORTLET_RESPONSE);

        if (portletResponse != null) {
        	
        	PortletURL portletURL = createPortletUrl(portletResponse);             
 
            if (portletMode != null) {//set portlet mode
                try {
                    PortletMode mode = (PortletMode)
                        TEI.portletModes.get(portletMode.toUpperCase());
                    
                    if (mode == null) {
                        mode = new PortletMode(portletMode); 
                    }
                    
                    portletURL.setPortletMode(mode);
                    
                } catch (PortletModeException e) {                	
                    throw new JspException(e);                    
                }
            }
            
            if (windowState != null) {//set window state
                try {                	
                    WindowState state = (WindowState)
                        TEI.definedWindowStates.get(windowState.toUpperCase());                  
                    
                    if (state == null) {
                        state = new WindowState(windowState); 
                    }
                    
                    portletURL.setWindowState(state);
                    
                } catch (WindowStateException e) {                	
                    throw new JspException(e);
                }
            }else{
            	try {
					portletURL.setWindowState( WindowState.NORMAL);
				} catch (WindowStateException e) {
				}
            }
            
            setUrl(portletURL);
            
        }
        
        return super.doStartTag();
    }
	
    
	/**
     * Returns the portletMode property.
     * @return String
     */
    public String getPortletMode() {
        return portletMode;
    }
    
    
    /**
     * Returns the windowState property.
     * @return String
     */
    public String getWindowState() {
        return windowState;
    }
    
        
    /**
     * Sets the portletMode property.
     * @param portletMode - the portlet mode to set
     * @return void
     */
    public void setPortletMode(String portletMode) {
        this.portletMode = portletMode;
    }
    
    
    /**
     * Sets the windowState property.
     * @param windowState - the portlet window state to set
     * @return void
     */
    public void setWindowState(String windowState) {
        this.windowState = windowState;
    }
    
   
	@Override
	protected BaseURL getUrl() {
		return portletURL;
	}
 
	@Override
	protected void setUrl(BaseURL url) {
		this.portletURL = (PortletURL)url;
	}


	/**
     * Creates an actionURL or a renderURL
     * @param portletResponse PortletResponse
     * @return PortletURL
     */
    protected abstract PortletURL createPortletUrl(PortletResponse portletResponse);   
    
    
  
	/**
	 * TagExtraInfo class for PortletURLTag.
	 */
	public static class TEI extends BaseURLTag.TEI {
        public final static Hashtable<String,Object> definedWindowStates = 
        	getDefinedWindowStates();
        
        public final static Hashtable<String,Object> portletModes = 
        	getDefinedPortletModes();

        /**
         * Provides a list of all static PortletMode available in the
         * specifications by using introspection
         * @return Hashtable
         */
        private static Hashtable<String,Object> getDefinedPortletModes() {
            Hashtable<String,Object> portletModes = new Hashtable<String,Object>();
            Field[] f = PortletMode.class.getDeclaredFields();

            for (int i = 0; i < f.length; i++) {
                if (f[i].getType().isAssignableFrom(
                    javax.portlet.PortletMode.class)) {
                    try {
                        portletModes.put(
                            f[i].get(f[i]).toString().toUpperCase(),
                            f[i].get(f[i]));
                    } catch (IllegalAccessException e) {
                    }
                }
            }

            return portletModes;
        }

        /**
         * Provides a list of all static WindowsStates available in the
         * specifications by using introspection
         * @return Hashtable
         */
        private static Hashtable<String,Object> getDefinedWindowStates() {
            Hashtable<String,Object> definedWindowStates = new Hashtable<String,Object>();
            Field[] f = WindowState.class.getDeclaredFields();

            for (int i = 0; i < f.length; i++) {
                if (f[i].getType().isAssignableFrom(
                    javax.portlet.WindowState.class)) {
                    try {
                        definedWindowStates.put(
                            f[i].get(f[i]).toString().toUpperCase(),
                            f[i].get(f[i]));
                    } catch (IllegalAccessException e) {

                    }
                }
            }
            return definedWindowStates;
        }
    }
	
}
