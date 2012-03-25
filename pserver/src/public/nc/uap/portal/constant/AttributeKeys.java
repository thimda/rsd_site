package nc.uap.portal.constant;

/**
 * Constants used as attribute keys to bind values to servlet context or servlet
 * request.
 *
 * @version 1.0
 */
public class AttributeKeys {
	
	public static final String RESPONSE_MODE = "nc.portlet.response.mode";
	/** 响应体为HTML **/
	public static final String MODE_HTML = "nc.portlet.response.mode.html";
	/** 响应体为SCRIPT **/
	public static final String MODE_SCRIPT = "nc.portlet.response.mode.script";
	public static final String CLIENT_EXEC_SCRIPT = "nc.portlet.response.script";
    /**
     * Attribute Key used to bind the application's driver config to the
     * ServletContext.
     */
    public static final String DRIVER_CONFIG = "driverConfig";

    /**
     * Attribute Key used to bind the application's driver admin config
     * to the ServletContext.
     */
    public static final String DRIVER_ADMIN_CONFIG = "driverAdminConfig";

    /**
     * Attribute Key used to bind the application's portlet container to the
     * ServletContext.
     */
    public static final String PORTLET_CONTAINER = "portletContainer";

    /** Attribute key used to bind the current page to servlet request. */
    public static final String CURRENT_PAGE = "currentPage";

    /** Attribute key used to bind the portlet title to servlet request. */
    public static final String PORTLET_TITLE =
    		"nc.portal.driver.DynamicPortletTitle";

    public static final String PORTAL_URL_PARSER = "PORTAL_URL_PARSER";

    // Constructor -------------------------------------------------------------

    /**
     * Private constructor that prevents external instantiation.
     */
    private AttributeKeys() {

    }
}

