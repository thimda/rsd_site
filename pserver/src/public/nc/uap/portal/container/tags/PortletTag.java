package nc.uap.portal.container.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.tags.el.ExpressionEvaluatorProxy;
import nc.uap.portal.servlet.PortalServletResponse;
 

/**
 * The portlet tag is used to render a portlet specified by the portlet ID.
 *
 * @see javax.portlet.Portlet#render(javax.portlet.RenderRequest,javax.portlet.RenderResponse)
  *
 */
public class PortletTag extends BodyTagSupport {
	
	/** Logger. */
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 6744651855812642818L;

	/**
	 * 
	 */

	/** Status constant for failed rendering. */
    public static final int FAILED = 0;
    
    /** Status constant for successful rendering. */
    public static final int SUCCESS = 1;
    
    
    // Private Member Variables ------------------------------------------------
    
    /** The portlet ID attribute passed into this tag. */
    private String portletId;

    /** The evaluated value of the portlet ID attribute. */
    private String evaluatedPortletId;

    /** The cached portal servlet response holding rendering result. */
    private PortalServletResponse response;

    /** The cached rendering status: SUCCESS or FAILED. */
    private int status;
    
    /** The cached Throwable instance when fail to render the portlet. */
    private Throwable throwable;


    // Tag Attribute Accessors -------------------------------------------------
    
    /**
     * Returns the portlet ID attribute.
     * @return the portlet ID attribute.
     */
    public String getPortletId() {
        return portletId;
    }
    
    /**
     * Sets the portlet ID attribute.
     * @param portletId  the portlet ID attribute.
     */
    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }
    
    
    // BodyTagSupport Impl -----------------------------------------------------
    
    /**
     * Method invoked when the start tag is encountered.
     * @throws JspException  if an error occurs.
     */
    public int doStartTag() throws JspException {
        
//    	// Evaluate portlet ID attribute.
//    	evaluatePortletId();
//        
//    	// Retrieve the portlet window config for the evaluated portlet ID.
//        ServletContext servletContext = pageContext.getServletContext();
//
//        PortletWindowConfig windowConfig =
//            PortletWindowConfig.fromId(evaluatedPortletId);
//
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("Rendering Portlet Window: " + windowConfig);
//        }
//        
//        // Retrieve the current portal URL.
//        PortalRequestContext portalEnv = PortalRequestContext.getContext(
//                (HttpServletRequest) pageContext.getRequest());
//        PortalURL portalURL = portalEnv.getRequestedPortalURL();
//        
//        // Retrieve the portlet container from servlet context.
//        PortletContainer container = (PortletContainer)
//                servletContext.getAttribute(AttributeKeys.PORTLET_CONTAINER);
//        
//        // Create the portlet window to render.
//        PortletWindow window = null;
//
//
//        try {
//        	window = new PortletWindowImpl(container, windowConfig, portalURL);
//        } catch(RuntimeException e) // FIXME: Prose a change to anything else, handle it.
//        {
//      	  if (LOG.isDebugEnabled()) {
//              LOG.debug("The portlet " + windowConfig.getPortletName() + " is not available. Is already deployed?");
//          }
//        }
//
//    	// Create portal servlet response to wrap the original
//    	// HTTP servlet response.
//    	PortalServletResponse portalResponse = new PortalServletResponse(
//                (HttpServletResponse) pageContext.getResponse());
//    	
//    	
//        if(window!=null)
//        {
//        	// Check if someone else is maximized. If yes, don't show content.
//        	Map windowStates = portalURL.getWindowStates();
//        	for (Iterator it = windowStates.keySet().iterator(); it.hasNext(); ) {
//        		String windowId = (String) it.next();
//        		WindowState windowState = (WindowState) windowStates.get(windowId);
//        		if (WindowState.MAXIMIZED.equals(windowState)
//        				&& !window.getId().getStringId().equals(windowId)) {
//        			return SKIP_BODY;
//        		}
//        	}
//
//        
//        }
//        
//        // Render the portlet and cache the response.
//        try {
//            container.doRender(window, (HttpServletRequest)pageContext.getRequest(), portalResponse);
//            response = portalResponse;
//            status = SUCCESS;
//        } catch (Throwable th) {
//            status = FAILED;
//            throwable = th;
//        }
        
        // Continue to evaluate the tag body.
        return EVAL_BODY_INCLUDE;
    }
    
    
    // Package Methods ---------------------------------------------------------
    
    /**
     * Returns the rendering status.
     * @return the rendering status.
     */
    int getStatus() {
        return status;
    }
    
    /**
     * Returns the portal servlet response holding rendering result. 
     * @return the portal servlet response holding rendering result.
     */
    PortalServletResponse getPortalServletResponse() {
        return response;
    }
    
    /**
     * Returns the error that has occurred when rendering portlet.
     * @return the error that has occurred when rendering portlet.
     */
    Throwable getThrowable() {
        return throwable;
    }
    
    /**
     * Returns the evaluated portlet ID.
     * @return the evaluated portlet ID.
     */
    String getEvaluatedPortletId() {
        return evaluatedPortletId;
    }

    
    
    // Private Methods ---------------------------------------------------------
    
    /**
     * Evaluates the portlet ID attribute passed into this tag. This method
     * evaluates the member variable <code>portletId</code> and saves the
     * evaluated result to <code>evaluatedPortletId</code>
     * @throws JspException  if an error occurs.
     */
    @SuppressWarnings("unused")
	private void evaluatePortletId() throws JspException {
        ExpressionEvaluatorProxy proxy = ExpressionEvaluatorProxy.getProxy();
        Object obj = proxy.evaluate(portletId, pageContext);
        if (LfwLogger.isDebugEnabled()) {
        	LfwLogger.debug("Evaluated portletId to: " + obj);
        }
        evaluatedPortletId = (String) obj;
    }
    
}
